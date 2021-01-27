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

        path {
          backend {
            service_name = var.sonarName
            service_port = var.sonarPort
          }

          path = var.sonarPath1
        }

        path {
          backend {
            service_name = var.sonarName
            service_port = var.sonarPort
          }

          path = var.sonarPath2
        }
      }
    }
  }
}