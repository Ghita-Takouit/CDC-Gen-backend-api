pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9.6'
        jdk 'JDK 17'
    }
    
    environment {
        MAVEN_OPTS = '-Xmx1024m'
        APP_NAME = 'cdc-gen-backend-api'
        VERSION = "${BUILD_NUMBER}"
        
        JWT_SECRET = credentials('cdc-gen-jwt-secret')
        
        DOCKER_REGISTRY = 'docker.io'
        DOCKER_REPOSITORY = 'aeztic/cdc-gen-backend-api'
        DOCKER_IMAGE = "${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}:${VERSION}"
        
        DOCKER_CREDENTIALS = credentials('docker-hub-credentials')
    }
    
    stages {
        stage('Git Checkout') {
            steps {
                deleteDir()
                git branch: 'main', changelog: false, credentialsId: 'GithubToken',
                poll: false, url: 'https://github.com/Ghita-Takouit/CDC-Gen-backend-api.git'

            }
        }
    
        stage('Build') {
                steps {
                    sh 'mvn clean package -DskipTests'
                }
                post {
                    success {
                        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    }
                }
            }
    
        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Create env file with secrets for Docker
                    sh '''
                        echo "JWT_SECRET=${JWT_SECRET}" > .env
                    '''
                    
                    // Build Docker image
                    sh "docker build -t ${DOCKER_IMAGE} ."
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    withEnv(['DOCKER_CLIENT_TIMEOUT=300', 'COMPOSE_HTTP_TIMEOUT=300']) {
                        retry(3) {
                            sh '''
                                echo $DOCKER_CREDENTIALS_PSW | docker login -u $DOCKER_CREDENTIALS_USR --password-stdin $DOCKER_REGISTRY
                                docker push ${DOCKER_IMAGE}
                                docker tag ${DOCKER_IMAGE} ${DOCKER_REPOSITORY}:latest
                                docker push ${DOCKER_REPOSITORY}:latest
                            '''
                        }
                    }
                }
            }
        }

        stage('Install Docker Compose') {
            steps {
                sh '''
                    curl -SL https://github.com/docker/compose/releases/download/v2.27.0/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
                    chmod +x /usr/local/bin/docker-compose
                    [ -L /usr/bin/docker-compose ] && rm /usr/bin/docker-compose || true
                    ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
                '''
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // Write the VERSION to a .env file for docker-compose to use
                    sh '''
                        echo "VERSION=${VERSION}" > .env
                        docker-compose down
                        docker-compose pull
                        docker-compose up -d
                    '''
                }
            }
        }
        

    }
}