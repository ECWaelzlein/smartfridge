pipeline {
    agent {
        kubernetes {
            yamlFile 'KubernetesPod.yaml'
        }
    }
    stages{
        stage('Build Project') {
            steps {
                container('maven') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        stage('Test Project') {
            steps {
                container('maven') {
                   /*withSonarQubeEnv(installationName: 'sonarqube-server') {
                       sh 'mvn verify sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=$SONAR_HOST_URL -Ptest'
                   }*/
                   sh 'mvn verify'
               }
           }
        }
        stage('Build Image') {
            steps {
                container('docker') {
                    echo '=== Building Docker Image ==='
                    script {
                        app = docker.build("smart-fridge-backend")
                    }
                }
            }
        }
        stage('Push Image') {
            steps {
                container('docker') {
                    echo '=== Pushing Docker Image ==='
                    script {
                        GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
                        SHORT_COMMIT = "${GIT_COMMIT_HASH[0..7]}"
                        docker.withRegistry('https://registry.gitlab.com/master-intelligente-systeme/ise/smartfridge', 'jenkins-deploy-token') {
                            app.push("$SHORT_COMMIT")
                            app.push("latest")
                        }
                    }
                }
            }
        }
    }
}
