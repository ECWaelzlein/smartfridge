data "aws_eks_cluster" "cluster" {
  name = module.eks.cluster_id
}

data "aws_eks_cluster_auth" "cluster" {
  name = module.eks.cluster_id
}

data "aws_subnet_ids" "vpc" {
  vpc_id = module.vpc.vpc_id
  filter {
    name = "tag:Name"
    values = [
      "subnet-private-gruppe2"]
  }
}

data "aws_vpc" "vpc" {
  id = module.vpc.vpc_id
}

data "aws_iam_user" "user_thilo" {
  user_name = "ThiloGraffe"
}

data "aws_iam_user" "user_thorben" {
  user_name = "ThorbenHorn"
}

data "aws_iam_user" "user_ElisabethWaelzlein" {
  user_name = "ElisabethWaelzlein"
}

data "local_file" "iam-policy" {
  depends_on = [
    null_resource.curl_policy]
  filename = "iam-policy.json"
}

terraform {
  backend "s3" {
    bucket = "terraform-state-gruppe2"
    key = "global/s3/terraform.tfstate"
    region = "eu-central-1"

    dynamodb_table = "terraform-locks"
    encrypt = true
  }

  required_providers {
    kubernetes = {
      version = ">= 1.11.1"
    }
  }
}

provider "kubernetes" {
  host = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
  token = data.aws_eks_cluster_auth.cluster.token
  //load_config_file = false
}

provider "aws" {
  region = "eu-west-1"
}

provider "helm" {
  kubernetes {
    config_path = "kubeconfig_smartfridge-eks-dev-gruppe2"
  }
}

module "vpc" {
  source = "terraform-aws-modules/vpc/aws"
  version = "2.66.0"

  name = "smartfridge-vpc-gruppe2"

  cidr = "10.0.0.0/16"

  azs = [
    "eu-west-1a",
    "eu-west-1b"]
  private_subnets = [
    "10.0.1.0/24",
    "10.0.2.0/24"]
  public_subnets = [
    "10.0.101.0/24",
    "10.0.102.0/24"]

  enable_dns_hostnames = true
  enable_nat_gateway = true
  single_nat_gateway = true

  public_subnet_tags = {
    Name = "subnet-public-gruppe2"
    "kubernetes.io/cluster/${var.eks-name-dev}" = "shared"
    "kubernetes.io/role/elb" = "1"
  }

  private_subnet_tags = {
    Name = "subnet-private-gruppe2"
    "kubernetes.io/cluster/${var.eks-name-dev}" = "shared"
    "kubernetes.io/role/internal-elb" = "1"
  }

  tags = {
    Owner = var.owner
    Environment = var.environment
    "kubernetes.io/cluster/${var.eks-name-dev}" = "shared"
  }

  vpc_tags = {
    Name = "smartfridge-vpc-gruppe2"
  }
}

resource "null_resource" "kubectl-apply-target-bindings" {
  depends_on = [
    module.eks,
    data.aws_eks_cluster.cluster]
  provisioner "local-exec" {
    command = "kubectl apply -k \"github.com/aws/eks-charts/stable/aws-load-balancer-controller//crds?ref=master\" --kubeconfig kubeconfig_smartfridge-eks-dev-gruppe2"
  }
}

resource "null_resource" "curl_policy" {
  provisioner "local-exec" {
    command = "curl -o iam-policy.json https://raw.githubusercontent.com/kubernetes-sigs/aws-load-balancer-controller/main/docs/install/iam_policy.json"
  }
}

resource "aws_iam_policy" "worker_policy" {
  depends_on = [
    null_resource.curl_policy]
  name = "AWSLoadBalancerControllerIAMPolicy"
  description = "Worker policy for the ALB Ingress"

  policy = data.local_file.iam-policy.content
}

module "eks" {
  depends_on = [
    module.vpc,
    module.rds-smartfridge-dev,
    module.rds-sonarqube,
    aws_iam_policy.worker_policy,
    aws_security_group.worker_group_reinhard,
    data.aws_subnet_ids.vpc
  ]
  source = "terraform-aws-modules/eks/aws"
  version = "13.2.1"

  workers_additional_policies = [
    aws_iam_policy.worker_policy.arn]

  cluster_name = var.eks-name-dev
  cluster_version = "1.18"
  subnets = data.aws_subnet_ids.vpc.ids
  vpc_id = data.aws_vpc.vpc.id

  map_users = [
    {
      groups = [
        "system:masters"],
      userarn = data.aws_iam_user.user_thilo.arn,
      username = data.aws_iam_user.user_thilo.user_name
    },
    {
      groups = [
        "system:masters"],
      userarn = data.aws_iam_user.user_thorben.arn,
      username = data.aws_iam_user.user_thorben.user_name
    },
    {
      groups = [
        "system:masters"],
      userarn = data.aws_iam_user.user_ElisabethWaelzlein.arn,
      username = data.aws_iam_user.user_ElisabethWaelzlein.user_name
    }
  ]

  worker_groups = [
    {
      instance_type = "t2.small"
      asg_max_size = 6
      asg_desired_capacity = 4
      asg_min_size = 4
      additional_security_group_ids = [
        aws_security_group.worker_group_reinhard.id]
      name = "reinhard-small"
    },
    {
      instance_type = "t2.medium"
      asg_max_size = 3
      asg_desired_capacity = 2
      asg_min_size = 2
      additional_security_group_ids = [
        aws_security_group.worker_group_reinhard.id]
      name = "reinhard-medium"
    }
  ]
}

resource "helm_release" "ingress-alb" {
  depends_on = [
    module.eks,
    data.aws_eks_cluster.cluster,
    null_resource.kubectl-apply-target-bindings]
  name = "ingress"
  chart = "aws-load-balancer-controller"
  repository = "https://aws.github.io/eks-charts"
  create_namespace = true
  namespace = "tools"

  set {
    name = "clusterName"
    value = data.aws_eks_cluster.cluster.name
  }
  set {
    name = "vpcId"
    value = data.aws_vpc.vpc.id
  }
}

resource "helm_release" "sonarqube" {
  depends_on = [
    module.eks,
    data.aws_eks_cluster.cluster
  ]
  name = "sonarqube"
  chart = "../helm/kubernetes-tools"
  create_namespace = true
  namespace = "tools"

  set {
    name = "dbSonarqubePassword"
    value = var.dbSonarqubeUserPassword
  }
  set {
    name = "dbSonarqubeUsername"
    value = var.dbSonarqubeUsername
  }
  set {
    name = "dbSonarqubeServerName"
    value = module.rds-sonarqube.this_db_instance_name
  }
  set {
    name = "dbSonarqubeHostName"
    value = module.rds-sonarqube.this_db_instance_endpoint
  }
}

resource "random_string" "password" {
  length = 16
  special = true
  override_special = "!@#$%&*()-_=+[]:?"
}

resource "null_resource" "setup-sonarqube-server" {
  depends_on = [helm_release.sonarqube,
                aws_route53_record.sonarRecord]

  provisioner "local-exec" {
    command = "./sonarqube/setupSonarqubeServer.sh '${random_string.password.result}'"
  }
}

resource "aws_security_group" "worker_group_reinhard" {
  depends_on = [
    module.vpc,
    data.aws_vpc.vpc,
    data.aws_subnet_ids.vpc]
  name_prefix = "worker_group_reinhard"
  vpc_id = module.vpc.vpc_id

  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"

    cidr_blocks = [
      "10.0.0.0/8",
    ]
  }
}

resource "aws_security_group" "rds_accept_all_from_vpc" {
  description = "Security group to allow worker groups of the eks the access to the rds."
  depends_on = [
    module.vpc,
    data.aws_vpc.vpc,
    data.aws_subnet_ids.vpc,
    aws_security_group.worker_group_reinhard]
  name_prefix = "rds_accept_all_from_vpc"
  vpc_id = module.vpc.vpc_id

  ingress {
    from_port = 0
    to_port = 0
    protocol = "-1"

    security_groups = [
      aws_security_group.worker_group_reinhard.id
    ]
  }

  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = [
      "0.0.0.0/0"
    ]
  }
}

module "rds-smartfridge-dev" {
  depends_on = [
    module.vpc,
    data.aws_subnet_ids.vpc,
    aws_security_group.rds_accept_all_from_vpc]
  source = "terraform-aws-modules/rds/aws"
  version = "2.20.0"

  identifier = "smartfridge-db-dev"

  engine = "postgres"
  engine_version = "12.4"
  instance_class = "db.t2.micro"
  allocated_storage = 5
  storage_encrypted = false

  name = "smartfridgeDbDev"

  username = var.dbDevAdminUsername

  password = var.dbDevAdminPassword
  port = "5432"

  maintenance_window = "Mon:00:00-Mon:03:00"
  backup_window = "03:00-06:00"
  apply_immediately = true

  # disable backups to create DB faster
  backup_retention_period = 0

  tags = {
    Owner = var.owner
    Environment = var.environment
  }

  ca_cert_identifier = ""

  enabled_cloudwatch_logs_exports = [
    "postgresql",
    "upgrade"]

  # DB subnet group
  subnet_ids = data.aws_subnet_ids.vpc.ids

  vpc_security_group_ids = [
    aws_security_group.rds_accept_all_from_vpc.id]

  # DB parameter group
  family = "postgres12"

  # DB option group
  major_engine_version = "12"

  # Snapshot name upon DB deletion
  final_snapshot_identifier = "smartfridge-db-dev"

  # Database Deletion Protection
  deletion_protection = false
}

module "rds-sonarqube" {
  depends_on = [
    module.vpc,
    data.aws_subnet_ids.vpc,
    aws_security_group.rds_accept_all_from_vpc]
  source = "terraform-aws-modules/rds/aws"
  version = "2.20.0"

  identifier = "sonarqube-db"

  engine = "postgres"
  engine_version = "12.4"
  instance_class = "db.t2.micro"
  allocated_storage = 5
  storage_encrypted = false

  name = "sonarqube"

  username = var.dbSonarqubeUsername

  password = var.dbSonarqubeUserPassword
  port = "5432"

  maintenance_window = "Mon:00:00-Mon:03:00"
  backup_window = "03:00-06:00"
  apply_immediately = true

  # disable backups to create DB faster
  backup_retention_period = 0

  tags = {
    Owner = var.owner
    Environment = var.environment
  }

  ca_cert_identifier = ""

  enabled_cloudwatch_logs_exports = [
    "postgresql",
    "upgrade"]

  # DB subnet group
  subnet_ids = data.aws_subnet_ids.vpc.ids

  vpc_security_group_ids = [
    aws_security_group.rds_accept_all_from_vpc.id]

  # DB parameter group
  family = "postgres12"

  # DB option group
  major_engine_version = "12"

  # Snapshot name upon DB deletion
  final_snapshot_identifier = "sonarqube"

  # Database Deletion Protection
  deletion_protection = false
}

module "rds-smartfridge-prod" {
  depends_on = [
    module.vpc,
    data.aws_subnet_ids.vpc,
    aws_security_group.rds_accept_all_from_vpc]
  source = "terraform-aws-modules/rds/aws"
  version = "2.20.0"

  identifier = "smartfridge-prod"

  engine = "postgres"
  engine_version = "12.4"
  instance_class = "db.t2.micro"
  allocated_storage = 5
  storage_encrypted = false

  name = "smartfridgeDbProd"

  username = var.dbProdAdminUsername

  password = var.dbProdAdminPassword
  port = "5432"

  maintenance_window = "Mon:00:00-Mon:03:00"
  backup_window = "03:00-06:00"
  apply_immediately = true

  # disable backups to create DB faster
  backup_retention_period = 0

  tags = {
    Owner = var.owner
    Environment = var.environment
  }

  ca_cert_identifier = ""

  enabled_cloudwatch_logs_exports = [
    "postgresql",
    "upgrade"]

  # DB subnet group
  subnet_ids = data.aws_subnet_ids.vpc.ids

  vpc_security_group_ids = [
    aws_security_group.rds_accept_all_from_vpc.id]

  # DB parameter group
  family = "postgres12"

  # DB option group
  major_engine_version = "12"

  # Snapshot name upon DB deletion
  final_snapshot_identifier = "smartfridgeDbProd"

  # Database Deletion Protection
  deletion_protection = false
}

resource "null_resource" "helm-release-jenkins" {
  depends_on = [
    module.eks,
    data.aws_eks_cluster.cluster]
  provisioner "local-exec" {
    command = "helm repo add jenkins https://charts.jenkins.io;helm repo update;helm upgrade -i -f ./jenkins/jenkins-values.yaml jenkins jenkins/jenkins -n tools --kubeconfig kubeconfig_smartfridge-eks-dev-gruppe2"
  }
}

module "tools-ingress" {
  source = "./kubernetes-ingress"
  depends_on = [
    helm_release.sonarqube,
    helm_release.ingress-alb,
    null_resource.helm-release-jenkins]
  namespace = var.namespace
  httpsCertificateArn = "arn:aws:acm:eu-west-1:484755436758:certificate/9b50d339-f993-4ed2-9eab-72e8fddc9dad"
  sonarURL = var.sonarURL
  devURL = var.devURL
}

data "aws_route53_zone" "g2-fridge" {
  name = "g2.myvirtualfridge.net"
}


resource "time_sleep" "wait_30_seconds_for_ingress" {
  depends_on = [module.tools-ingress]

  create_duration = "30s"
}

data "aws_alb" "g2-fridge-alb-tools" {
  depends_on = [
    module.tools-ingress]
  name = "k8s-tools-toolsing-82c694fda4"
}

data "aws_alb" "g2-fridge-alb-dev" {
  depends_on = [
    module.tools-ingress]
  name = "k8s-dev-devingre-5c76d5163b"
}

data "aws_alb" "g2-fridge-alb-prod" {
  depends_on = [
    module.tools-ingress]
  name = "k8s-prod-prodingr-79b4cd940f"
}
resource "aws_route53_record" "www" {
  depends_on = [
    time_sleep.wait_30_seconds_for_ingress,
    data.aws_route53_zone.g2-fridge]

  zone_id = data.aws_route53_zone.g2-fridge.zone_id
  name = "dev.g2.myvirtualfridge.net"
  type = "A"
  allow_overwrite = true

  alias {
    name = data.aws_alb.g2-fridge-alb-tools.dns_name
    zone_id = data.aws_alb.g2-fridge-alb-tools.zone_id
    evaluate_target_health = true
  }
}

resource "aws_route53_record" "sonarRecord" {
  depends_on = [
    time_sleep.wait_30_seconds_for_ingress,
    data.aws_route53_zone.g2-fridge]

  zone_id = data.aws_route53_zone.g2-fridge.zone_id
  name = var.sonarURL
  type = "A"
  allow_overwrite = true

  alias {
    name = data.aws_alb.g2-fridge-alb-tools.dns_name
    zone_id = data.aws_alb.g2-fridge-alb-tools.zone_id
    evaluate_target_health = true
  }
}

resource "aws_route53_record" "devRecord" {
  depends_on = [
    time_sleep.wait_30_seconds_for_ingress,
    data.aws_route53_zone.g2-fridge]

  zone_id = data.aws_route53_zone.g2-fridge.zone_id
  name = var.devURL
  type = "A"
  allow_overwrite = true

  alias {
    name = data.aws_alb.g2-fridge-alb-dev.dns_name
    zone_id = data.aws_alb.g2-fridge-alb-dev.zone_id
    evaluate_target_health = true
  }
}

resource "aws_route53_record" "prodRecord" {
  depends_on = [
    time_sleep.wait_30_seconds_for_ingress,
    data.aws_route53_zone.g2-fridge]

  zone_id = data.aws_route53_zone.g2-fridge.zone_id
  name = var.prodURL
  type = "A"
  allow_overwrite = true

  alias {
    name = data.aws_alb.g2-fridge-alb-prod.dns_name
    zone_id = data.aws_alb.g2-fridge-alb-prod.zone_id
    evaluate_target_health = true
  }
}