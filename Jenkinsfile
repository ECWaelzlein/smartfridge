pipeline {
    agent {
        kubernetes {
            //cloud 'kubernetes'
            containerTemplate {
                name 'maven'
                image 'maven:3.6.3-openjdk-15-slim'
                ttyEnabled true
                command 'cat'
            }
            containerTemplate {
                name 'docker'
                image 'docker:20.10.3'
                ttyEnabled true
                command 'cat'
            }
        }
    }
    stages{
        stage('Build Project') {
            container('maven') {
                steps {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        stage('Test Project') {
            container('maven') {
                steps {
                    /*withSonarQubeEnv(installationName: 'sonarqube-server') {
                        sh 'mvn verify sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=$SONAR_HOST_URL -Ptest'
                    }*/
                    sh 'mvn verify'
                }
            }
        }
        stage('Build Image') {
            container('docker') {
                steps {
                    echo '=== Building Docker Image ==='
                    script {
                        app = docker.build("smart-fridge-backend")
                    }
                }
            }
        }
        stage('Push Image') {
            container('docker') {
                steps {
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
