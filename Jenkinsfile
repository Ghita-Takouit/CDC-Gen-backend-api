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
        
        stage('Code Analysis') {
            steps {
                sh 'mvn verify sonar:sonar -Dsonar.projectKey=CDC-Gen-backend-api -Dsonar.host.url=http://your-sonarqube-server:9000 -Dsonar.login=$SONAR_TOKEN'
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
                    sh '''
                        echo $DOCKER_CREDENTIALS_PSW | docker login -u $DOCKER_CREDENTIALS_USR --password-stdin $DOCKER_REGISTRY
                        docker push ${DOCKER_IMAGE}
                        docker tag ${DOCKER_IMAGE} ${DOCKER_REPOSITORY}:latest
                        docker push ${DOCKER_REPOSITORY}:latest
                    '''
                }
            }
        }
        
        stage('Deploy to Development') {
            when {
                branch 'main'
            }
            steps {
                script {
                    // Update the application in the development environment
                    sh '''
                        ssh user@dev-server "cd /opt/cdc-gen && \
                        docker-compose down && \
                        docker pull ${DOCKER_IMAGE} && \
                        export VERSION=${VERSION} && \
                        docker-compose up -d"
                    '''
                }
            }
        }
        
        stage('Integration Tests') {
            when {
                branch 'main'
            }
            steps {
                sh 'mvn verify -Pintegration-tests'
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            input {
                message "Deploy to production?"
                ok "Yes, let's deploy"
            }
            steps {
                script {
                    // Update the application in production environment
                    sh '''
                        ssh user@prod-server "cd /opt/cdc-gen && \
                        docker-compose down && \
                        docker pull ${DOCKER_IMAGE} && \
                        export VERSION=${VERSION} && \
                        docker-compose up -d"
                    '''
                }
            }
        }
    }
    
    post {
        success {
            echo 'Build and deployment successful!'
            // Send notification about successful build
            mail to: 'team@example.com',
                 subject: "Success: ${currentBuild.fullDisplayName}",
                 body: "The build was successful. Check: ${env.BUILD_URL}"
        }
        failure {
            echo 'Build or deployment failed!'
            // Send notification about build failure
            mail to: 'team@example.com',
                 subject: "Failed: ${currentBuild.fullDisplayName}",
                 body: "The build failed. Check: ${env.BUILD_URL}"
        }
        always {
            // Clean up workspace
            cleanWs()
        }
    }
}
