
pipeline {
    agent any

    options {
        disableConcurrentBuilds()
        timestamps()
    }

    environment {
        IMAGE_NAME = 'ramsai333/banking-account-service'
        IMAGE_TAG = "build-${BUILD_NUMBER}"
    }

    stages {
        stage('GitHub Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Build and Test') {
            steps {
                sh 'mvn -B clean verify'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
            }
        }

        stage('Docker Hub Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_TOKEN'
                    )
                ]) {
                    sh '''
                        set +x
                        echo "$DOCKER_TOKEN" | docker login \
                            -u "$DOCKER_USER" --password-stdin

                        docker push "${IMAGE_NAME}:${IMAGE_TAG}"

                        docker logout
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Banking Account Service CI/CD pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed. Check the Jenkins console output.'
        }
    }
}

