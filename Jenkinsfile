pipeline {
    agent {
        kubernetes {
            yamlFile 'KubernetesPod.yaml'
        }
    }
    environment {
        GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
        SHORT_COMMIT = "${GIT_COMMIT_HASH[0..7]}"
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
                    withCredentials([usernamePassword(credentialsId: 'gitlab-jenkins', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh 'docker login -u $USERNAME -p $PASSWORD registry.gitlab.com'
                    }
                    echo '=== Building Docker Image ==='
                    sh 'docker build -t "registry.gitlab.com/master-intelligente-systeme/ise/smartfridge/smart-fridge-backend:$SHORT_COMMIT" .'
                    sh 'docker tag "registry.gitlab.com/master-intelligente-systeme/ise/smartfridge/smart-fridge-backend:$SHORT_COMMIT" "registry.gitlab.com/master-intelligente-systeme/ise/smartfridge/smart-fridge-backend:latest'
                }
            }
        }
        stage('Push Image') {
            steps {
                container('docker') {
                    echo '=== Pushing Docker Image ==='
                    sh 'docker push "registry.gitlab.com/master-intelligente-systeme/ise/smartfridge/smart-fridge-backend"'
                }
            }
        }
    }
}
