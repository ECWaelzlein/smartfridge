resource "kubernetes_namespace" "prod" {
  metadata {
    name = "prod"
  }
}

resource "kubernetes_namespace" "dev" {
  metadata {
    name = "dev"
  }
}

resource "kubernetes_service" "smart-fridge-backend-service-dev" {
  depends_on = [kubernetes_namespace.dev]
  metadata {
    name= "smart-fridge-backend-service-dev"
    namespace= "dev"
  }
  spec {
    type= "ClusterIP"
    port{
      port= 8080
      protocol= "TCP"
    }
    selector= {
      app= "smart-fridge-backend"
    }
  }
}

resource "kubernetes_service" "smart-fridge-backend-service-prod" {
  depends_on = [kubernetes_namespace.prod]
  metadata {
    name= "smart-fridge-backend-service-prod"
    namespace= "prod"
  }
  spec {
    type= "ClusterIP"
    port{
      port= 8080
      protocol= "TCP"
    }
    selector= {
      app= "smart-fridge-backend"
    }
  }
}

resource "kubernetes_service" "smart-fridge-frontend-service-dev" {
  depends_on = [kubernetes_namespace.dev]
  metadata {
    name= "smart-fridge-frontend-service-dev"
    namespace= "dev"
  }
  spec {
    type= "ClusterIP"
    port{
      port= 80
      protocol= "TCP"
    }
    selector= {
      app= "smart-fridge-frontend"
    }
  }
}

resource "kubernetes_service" "smart-fridge-frontend-service-prod" {
  depends_on = [kubernetes_namespace.prod]
  metadata {
    name= "smart-fridge-frontend-service-prod"
    namespace= "prod"
  }
  spec {
    type= "ClusterIP"

    port {
      port= 80
      protocol= "TCP"
    }
    selector= {
      app= "smart-fridge-frontend"
    }
  }
}

resource "kubernetes_ingress" "dev-ingress" {
  depends_on = [kubernetes_service.smart-fridge-backend-service-dev,
    kubernetes_service.smart-fridge-frontend-service-dev]
  metadata {
    name = "dev-ingress"
    namespace = "dev"
    annotations = {
      "kubernetes.io/ingress.class": "alb"
      "alb.ingress.kubernetes.io/scheme": "internet-facing"
      "alb.ingress.kubernetes.io/target-type": "ip"
      "alb.ingress.kubernetes.io/listen-ports": "[{\"HTTP\": 80}, {\"HTTPS\":443}]"
      "alb.ingress.kubernetes.io/group": "tools"
      "alb.ingress.kubernetes.io/certificate-arn": "arn:aws:acm:eu-west-1:484755436758:certificate/94360ae1-8613-44f3-b391-2ee37215775a"
      "alb.ingress.kubernetes.io/actions.ssl-redirect": "{\"Type\": \"redirect\", \"RedirectConfig\": { \"Protocol\": \"HTTPS\", \"Port\": \"443\", \"StatusCode\": \"HTTP_301\"}}"
    }
  }

  spec {
    rule {
      host = var.devURL
      http {

        path {
          backend {
            service_name = "ssl-redirect"
            service_port = "use-annotation"
          }

          path = "/*"
        }

        path {
          backend {
            service_name = "smart-fridge-backend-service-dev"
            service_port = 8080
          }

          path = "/api/*"
        }

        path {
          backend {
            service_name = "smart-fridge-frontend-service-dev"
            service_port = 80
          }

          path = "/*"
        }

      }
    }

  }
}

resource "kubernetes_ingress" "tools-ingress" {
  metadata {
    name = var.name
    namespace = var.namespace
    annotations = {
      "kubernetes.io/ingress.class": "alb"
      "alb.ingress.kubernetes.io/scheme": "internet-facing"
      "alb.ingress.kubernetes.io/target-type": "ip"
      "alb.ingress.kubernetes.io/listen-ports": "[{\"HTTP\": 80}, {\"HTTPS\":443}]"
      "alb.ingress.kubernetes.io/group": "tools"
      "alb.ingress.kubernetes.io/certificate-arn": var.httpsCertificateArn
      "alb.ingress.kubernetes.io/actions.ssl-redirect": "{\"Type\": \"redirect\", \"RedirectConfig\": { \"Protocol\": \"HTTPS\", \"Port\": \"443\", \"StatusCode\": \"HTTP_301\"}}"
    }
  }

  spec {
    rule {
      host = var.jenkinsURL
      http {

        path {
          backend {
            service_name = "ssl-redirect"
            service_port = "use-annotation"
          }

          path = "/*"
        }

        path {
          backend {
            service_name = var.jenkinsName
            service_port = var.jenkinsPort
          }

          path = var.jenkinsPath1
        }

        path {
          backend {
            service_name = var.jenkinsName
            service_port = var.jenkinsPort
          }

          path = var.jenkinsPath2
        }
      }
    }
    rule {
      host = var.sonarURL
      http {
        path {
          backend {
            service_name = "ssl-redirect"
            service_port = "use-annotation"
          }

          path = "/*"
        }
        path {
          backend {
            service_name = var.sonarName
            service_port = var.sonarPort
          }

          path ="/*"
        }
      }
    }
  }
}