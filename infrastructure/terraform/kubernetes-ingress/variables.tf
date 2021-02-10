variable "name" {
  type = string
  default = "tools-ingress"
}

variable "jenkinsName" {
  type = string
  default = "jenkins"
}

variable "jenkinsPort" {
  default = 8080
  type = number
}

variable "jenkinsPath1" {
  type = string
  default = "/jenkins/*"
}

variable "jenkinsPath2" {
  type = string
  default = "/jenkins"
}

variable "sonarName" {
  type = string
  default = "sonarqube"
}

variable "sonarPort" {
  default = 9000
  type = number
}

variable "sonarURL" {
  type = string
}

variable "jenkinsURL" {
  type = string
  default = "dev.g2.myvirtualfridge.net"
}

variable "devURL" {
  type = string
  default = "develop.g2.myvirtualfridge.net"
}


variable "namespace" {
  type = string
}

variable "httpsCertificateArn" {
  type = string
}