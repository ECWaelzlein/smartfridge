variable "dbDevAdminPassword" {
  type = string
  description = "Admin user password of the development postgres database."
}

variable "owner" {
  type = string
  description = "The owner of the corresponding resources."
  default = "gruppe2"
}

variable "environment" {
  type = string
  description = "The stage of the environment."
  default = "dev"
}

variable "eks-name-dev" {
  type = string
  default = "smartfridge-eks-dev-gruppe2"
}

variable "namespace" {
  type = string
  default = "tools"
}

variable "dbDevAdminUsername" {
  default = "smartfridge_db_admin"
}

variable "dbSonarqubeUserPassword" {
  type = string
}

variable "dbSonarqubeUsername" {
  type = string
  default = "sonarqube"
}

variable "sonarURL" {
  type = string
  default = "sonar.dev.g2.myvirtualfridge.net"
}

variable "jenkinsURL" {
  type = string
  default = "dev.g2.myvirtualfridge.net"
}

variable "jenkinsServicePath1" {
  type = string
  default = "/jenkins/*"
}

variable "jenkinsServicePath2" {
  type = string
  default = "/jenkins"

}

variable "sonarServicePath1" {
  type = string
  default = "/sonarqube/*"
}

variable "sonarServicePath2" {
  type = string
  default = "/*"

}

variable "sonarPort" {
  default = 9000
  type = number
}

variable "jenkinsPort" {
  default = 8080
  type = number
}