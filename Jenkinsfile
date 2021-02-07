pipeline {
    agent {
        kubernetes {
            yamlFile 'KubernetesPod.yaml'
        }
    }
    triggers {
        gitlab(triggerOnPush: true, triggerOnMergeRequest: true, branchFilterType: 'All')
    }
    environment {
        GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
        SHORT_COMMIT = "${GIT_COMMIT_HASH[0..7]}"
    }
    stages{
        stage('Build Project') {
            steps {
                container('maven') {
                    updateGitlabCommitStatus name: 'build', state: 'pending'
                    sh 'mvn clean package -DskipTests'
                    updateGitlabCommitStatus name: 'build', state: 'success'
                }
            }
        }
        stage('Test Project') {
            steps {
                container('maven') {
                   withSonarQubeEnv('sonarqube') {
                       updateGitlabCommitStatus name: 'test', state: 'pending'
                       sh 'mvn verify sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=$SONAR_HOST_URL'
                       updateGitlabCommitStatus name: 'test', state: 'success'
                   }
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
                    updateGitlabCommitStatus name: 'build docker', state: 'pending'
                    sh 'docker build -t "registry.gitlab.com/master-intelligente-systeme/ise/smartfridge/smart-fridge-backend:$SHORT_COMMIT" .'
                    sh 'docker tag "registry.gitlab.com/master-intelligente-systeme/ise/smartfridge/smart-fridge-backend:$SHORT_COMMIT" "registry.gitlab.com/master-intelligente-systeme/ise/smartfridge/smart-fridge-backend:latest"'
                    updateGitlabCommitStatus name: 'build docker', state: 'success'
                }
            }
        }
        stage('Push Image') {
            steps {
                container('docker') {
                    echo '=== Pushing Docker Image ==='
                    updateGitlabCommitStatus name: 'pushing docker', state: 'pending'
                    sh 'docker push "registry.gitlab.com/master-intelligente-systeme/ise/smartfridge/smart-fridge-backend"'
                    updateGitlabCommitStatus name: 'pushing docker', state: 'success'
                }
            }
        }
    }
}
