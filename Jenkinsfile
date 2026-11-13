pipeline {
    agent any

    options {
        disableConcurrentBuilds()
        timestamps()
    }

    environment {
        IMAGE_NAME = 'ramsai333/banking-account-service'
        IMAGE_TAG = "build-${BUILD_NUMBER}"

        K8S_SERVER = 'https://172.31.11.222:6443'
        K8S_NAMESPACE = 'banking'
        K8S_CA = '/etc/jenkins/kubernetes/ca.crt'
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
                sh '''
                    docker build \
                        -t "${IMAGE_NAME}:${IMAGE_TAG}" .
                '''
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
                            -u "$DOCKER_USER" \
                            --password-stdin

                        docker push "${IMAGE_NAME}:${IMAGE_TAG}"

                        docker logout
                    '''
                }
            }
        }

        
stage('Deploy to Kubernetes') {
    steps {
        withCredentials([
            string(
                credentialsId: 'k8s-jenkins-token',
                variable: 'K8S_TOKEN'
            )
        ]) {
            sh '''
                set +x
                set -eu

                export KUBECONFIG
                KUBECONFIG="$(mktemp)"
                chmod 600 "$KUBECONFIG"

                trap 'rm -f "$KUBECONFIG"' EXIT

                kubectl config set-cluster banking-k3s \
                    --server="$K8S_SERVER" \
                    --certificate-authority="$K8S_CA" \
                    --embed-certs=true

                kubectl config set-credentials jenkins-deployer \
                    --token="$K8S_TOKEN"

                kubectl config set-context banking-context \
                    --cluster=banking-k3s \
                    --user=jenkins-deployer \
                    --namespace="$K8S_NAMESPACE"

                kubectl config use-context banking-context

                sed "s|ramsai333/banking-account-service:1.0|${IMAGE_NAME}:${IMAGE_TAG}|g" \
                    k8s/deployment.yaml |
                    kubectl apply -n "$K8S_NAMESPACE" -f -

                kubectl apply \
                    -n "$K8S_NAMESPACE" \
                    -f k8s/service.yaml

                kubectl rollout status \
                    deployment/banking-account-service \
                    -n "$K8S_NAMESPACE" \
                    --timeout=180s
            '''
        }
    }
}
}

    post {
        success {
            echo 'Banking Account Service CI/CD and Kubernetes deployment succeeded!'
        }

        failure {
            echo 'Pipeline failed. Check Jenkins Console Output.'
        }

        always {
            echo "Pipeline finished for build ${BUILD_NUMBER}"
        }
    }
}
