pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9.6'
        jdk 'JDK 17'
    }
    
    environment {
        MAVEN_OPTS = '-Xmx1024m'
        APP_NAME = 'cdc-gen-backend-api'
        VERSION = '${BUILD_NUMBER}'
        
        JWT_SECRET = credentials('cdc-gen-jwt-secret')
        
        GOOGLE_APPLICATION_CREDENTIALS = credentials('google-vertex-ai-credentials')
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo 'Code checkout complete'
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean compile'
                echo 'Build complete'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
                echo 'Tests complete'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
                echo 'Packaging complete'
            }
        }
        
        stage('Code Quality') {
            steps {
                echo 'Running code quality checks'
            }
        }
        
        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
        
        stage('Docker Build') {
            when {
                branch 'main'
            }
            steps {
                echo 'Building Docker image'
            }
        }
        
        stage('Deploy to Dev') {
            when {
                branch 'develop'
            }
            steps {
                echo 'Deploying to Development environment'
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                timeout(time: 2, unit: 'DAYS') {
                    input message: 'Approve deployment to production?'
                }
                echo 'Deploying to Production environment'
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed'
            cleanWs()
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
