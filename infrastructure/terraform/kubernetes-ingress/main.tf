resource "kubernetes_ingress" "tools-ingress" {
  metadata {
    name = var.name
    namespace = var.namespace
    annotations = {
      "kubernetes.io/ingress.class": "alb"
      "alb.ingress.kubernetes.io/scheme": "internet-facing"
      "alb.ingress.kubernetes.io/target-type": "ip"
      #"alb.ingress.kubernetes.io/listen-ports": "[{\"HTTP\": 80}, {\"HTTPS\":443}]"
      "alb.ingress.kubernetes.io/listen-ports": "[{\"HTTP\": 80}]"
      "alb.ingress.kubernetes.io/group": "myapp"
      #"alb.ingress.kubernetes.io/certificate-arn": "arn:aws:acm:us-east-2:484755436758:certificate/2ea87eaa-a826-4df8-9400-80f17c6fb77f"
    }
  }

  spec {
    rule {
      http {
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