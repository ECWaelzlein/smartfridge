data "aws_eks_cluster" "cluster" {
  name = module.eks.cluster_id
}

data "aws_eks_cluster_auth" "cluster" {
  name = module.eks.cluster_id
}

data "aws_subnet_ids" "vpc" {
  vpc_id = module.vpc.vpc_id
  filter {
    name   = "tag:Name"
    values = ["subnet-private-gruppe2"]
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
  depends_on = [null_resource.curl_policy]
  filename = "iam-policy.json"
}

terraform {
  backend "s3" {
    bucket         = "terraform-state-gruppe2"
    key            = "global/s3/terraform.tfstate"
    region         = "eu-central-1"

    dynamodb_table = "terraform-locks"
    encrypt        = true
  }

  required_providers {
    kubernetes = {
      version = ">= 1.11.1"
    }
  }
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
  token                  = data.aws_eks_cluster_auth.cluster.token
  load_config_file       = false
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
  source  = "terraform-aws-modules/vpc/aws"
  version = "2.66.0"

  name = "smartfridge-vpc-gruppe2"

  cidr = "10.0.0.0/16"

  azs             = ["eu-west-1a"]
  private_subnets = ["10.0.1.0/24"]
  public_subnets  = ["10.0.101.0/24"]

  enable_dns_hostnames = true
  enable_nat_gateway = true
  single_nat_gateway = true

  public_subnet_tags = {
    Name = "subnet-public-gruppe2"
    "kubernetes.io/cluster/${var.eks-name-dev}"   = "shared"
    "kubernetes.io/role/elb"                      = "1"
  }

  private_subnet_tags = {
    Name = "subnet-private-gruppe2"
    "kubernetes.io/cluster/${var.eks-name-dev}"   = "shared"
    "kubernetes.io/role/internal-elb"             = "1"
  }

  tags = {
    Owner       = var.owner
    Environment = var.environment
    "kubernetes.io/cluster/${var.eks-name-dev}" = "shared"
  }

  vpc_tags = {
    Name = "smartfridge-vpc-gruppe2"
  }
}

resource "null_resource" "kubectl-apply-target-bindings" {
  depends_on = [module.eks, data.aws_eks_cluster.cluster]
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
  depends_on = [null_resource.curl_policy]
  name        = "AWSLoadBalancerControllerIAMPolicy"
  description = "Worker policy for the ALB Ingress"

  policy = data.local_file.iam-policy.content
}

module "eks" {
  depends_on = [
    module.vpc,
    module.rds-smartfridge-dev,
    aws_iam_policy.worker_policy,
    aws_security_group.worker_group_reinhard,
    data.aws_subnet_ids.vpc
  ]
  source  = "terraform-aws-modules/eks/aws"
  version = "13.2.1"

  workers_additional_policies = [aws_iam_policy.worker_policy.arn]

  cluster_name = var.eks-name-dev
  cluster_version = "1.18"
  subnets = data.aws_subnet_ids.vpc.ids
  vpc_id = data.aws_vpc.vpc.id

  map_users = [
    {
      groups = ["system:masters"],
      userarn = data.aws_iam_user.user_thilo.arn,
      username = data.aws_iam_user.user_thilo.user_name
    },
    {
      groups = ["system:masters"],
      userarn = data.aws_iam_user.user_thorben.arn,
      username = data.aws_iam_user.user_thorben.user_name
    },
    {
      groups = ["system:masters"],
      userarn = data.aws_iam_user.user_ElisabethWaelzlein.arn,
      username = data.aws_iam_user.user_ElisabethWaelzlein.user_name
    }
  ]

  worker_groups = [
    {
      instance_type = "t2.micro"
      asg_max_size  = 5
      asg_desired_capacity = 5
      asg_min_size = 5
      additional_security_group_ids = [aws_security_group.worker_group_reinhard.id]
      name = "reinhard"
    },
    {
      instance_type = "t2.medium"
      asg_max_size  = 1
      asg_desired_capacity = 1
      asg_min_size = 1
      additional_security_group_ids = [aws_security_group.worker_group_reinhard.id]
      name = "reinhard-medium"
    }
  ]
}

resource "helm_release" "ingress-alb" {
  depends_on = [module.eks, data.aws_eks_cluster.cluster, null_resource.kubectl-apply-target-bindings]
  name       = "ingress"
  chart      = "aws-load-balancer-controller"
  repository = "https://aws.github.io/eks-charts"
  create_namespace = true
  namespace = "tools"

  set {
    name  = "clusterName"
    value = data.aws_eks_cluster.cluster.name
  }
}

resource "helm_release" "sonarqube" {
  depends_on = [module.eks, data.aws_eks_cluster.cluster]
  name       = "sonarqube"
  chart      = "../helm/kubernetes-tools"
  create_namespace = true
  namespace = "tools"

  set {
    name  = "dbSonarqubePassword"
    value = var.dbDevAdminPassword
  }
  set {
    name  = "dbSonarqubeUsername"
    value = var.dbDevAdminUsername
  }
  set {
    name  = "dbSonarqubeServerName"
    value = module.rds-smartfridge-dev.this_db_instance_name
  }
  set {
    name  = "dbSonarqubeHostName"
    value = module.rds-smartfridge-dev.this_db_instance_endpoint
  }
}

resource "aws_security_group" "worker_group_reinhard" {
  depends_on = [module.vpc, data.aws_vpc.vpc]
  name_prefix = "worker_group_reinhard"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port = 22
    to_port   = 22
    protocol  = "tcp"

    cidr_blocks = [
      "10.0.0.0/8",
    ]
  }
}

module "rds-smartfridge-dev" {
  depends_on = [module.vpc, data.aws_subnet_ids.vpc]
  source  = "terraform-aws-modules/rds/aws"
  version = "2.20.0"

  identifier = "smartfridge-db-dev"

  engine            = "postgres"
  engine_version    = "12.4"
  instance_class    = "db.t2.micro"
  allocated_storage = 5
  storage_encrypted = false

  name = "smartfridgeDbDev"

  username = var.dbDevAdminUsername

  password = var.dbDevAdminPassword
  port     = "5432"

  maintenance_window = "Mon:00:00-Mon:03:00"
  backup_window      = "03:00-06:00"
  apply_immediately = true

  # disable backups to create DB faster
  backup_retention_period = 0

  tags = {
    Owner       = var.owner
    Environment = var.environment
  }

  enabled_cloudwatch_logs_exports = ["postgresql", "upgrade"]

  # DB subnet group
  subnet_ids = data.aws_subnet_ids.vpc.ids

  # DB parameter group
  family = "postgres12"

  # DB option group
  major_engine_version = "12"

  # Snapshot name upon DB deletion
  final_snapshot_identifier = "smartfridge-db-dev"

  # Database Deletion Protection
  deletion_protection = false
}

module "jenkins" {
  source  = "./jenkins"

  jenkins_context_path = "/jenkins"
  create_namespace = true
  namespace = var.namespace
  jenkins_image = "jenkins/jenkins:2.60.3"
  depends_on = [module.vpc, module.eks, data.aws_eks_cluster.cluster]
}

module "tools-ingress" {
  source = "./kubernetes-ingress"
  depends_on = [helm_release.sonarqube, helm_release.ingress-alb, module.jenkins]
  namespace = var.namespace
}

data "aws_route53_zone" "g2-fridge" {
  name = "g2.myvirtualfridge.net"
}

data "aws_elb" "g2-fridge-elb" {
  name = "k8s-tools-toolsing-82c694fda4"
}

resource "aws_route53_record" "www" {
  depends_on = [module.tools-ingress, data.aws_route53_zone.g2-fridge]

  zone_id = data.aws_route53_zone.g2-fridge.zone_id
  name    = "dev.g2.myvirtualfridge.net"
  type    = "A"
  allow_overwrite = true

  alias {
    name                   = data.aws_elb.g2-fridge-elb.dns_name
    zone_id                = data.aws_elb.g2-fridge-elb.zone_id
    evaluate_target_health = true
  }
}