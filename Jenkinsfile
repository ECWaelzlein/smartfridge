podTemplate(
    containers: [
        containerTemplate(name: 'docker', image: 'maven:3.6.3-openjdk-15-slim', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'docker', image: 'docker:20.10.3', ttyEnabled: true, command: 'cat')
    ],
    volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]
)
{
    node(POD_LABEL) {
        stage('Build Project') {
            container('maven') {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Test Project') {
            container('maven') {
                /*withSonarQubeEnv(installationName: 'sonarqube-server') {
                    sh 'mvn verify sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=$SONAR_HOST_URL -Ptest'
                }*/
                sh 'mvn verify'
            }
        }
        stage('Build Image') {
            container('docker') {
                echo '=== Building Docker Image ==='
                script {
                    app = docker.build("smart-fridge-backend")
                }
            }
        }
        stage('Push Image') {
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
