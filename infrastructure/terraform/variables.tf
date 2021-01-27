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