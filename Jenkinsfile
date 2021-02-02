pipeline {
    agent any
    tools {
        maven 'maven'
        dockerTool 'docker'
        jdk 'jdk15'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        /*stage('Test with Sonarqube') {
            steps {
                withSonarQubeEnv(installationName: 'sonarqube-server') {
                    sh 'mvn verify sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=$SONAR_HOST_URL -Ptest'
                }
            }
        }*/
        stage('Test') {
            steps {
                sh 'mvn verify'
            }
        }
        stage('Build Docker Image') {
            steps {
                echo '=== Building Docker Image ==='
                script {
                    app = docker.build("smart-fridge-backend")
                }
            }
        }
        stage('Push Docker Image') {
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