pipeline {
    agent any

    environment {
        // 定义一些环境变量
        FRONTEND_DIR = 'front-end'
        BACKEND_DIR = 'BackEnd'
        REMOTE_HOST = '123.56.102.167'
        REMOTE_USER = 'root'
        DOCKER_IMAGE_FRONTEND = 'my-frontend'
        DOCKER_IMAGE_BACKEND = 'my-backend'
        CI = 'false'  // 禁用CI环境变量
        JAR_NAME = 'backend.jar'
    }

    stages {
        stage('Checkout') {
            steps {
                // 从Git仓库检出代码
                git branch: 'main', url: 'https://github.com/sustech-cs304/team-project-24spring-20.git'
            }
        }

        stage('Build Frontend') {
            steps {
                script {
                    dir("${env.FRONTEND_DIR}") {
                        // 安装Node.js依赖并构建前端项目
                        sh 'npm install'
                        sh 'npm run build'
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                script {
                    dir("${env.BACKEND_DIR}") {
                        // 使用Maven构建后端项目
                        sh 'mvn clean package -Dmaven.test.skip=true'
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    // 构建前端Docker镜像
                    dir("${env.FRONTEND_DIR}") {
                        sh "docker build -t ${env.DOCKER_IMAGE_FRONTEND} ."
                    }

                    // 构建后端Docker镜像
                    dir("${env.BACKEND_DIR}") {
                        sh "docker build --platform linux/amd64 -t ${env.DOCKER_IMAGE_BACKEND} --build-arg JAR_FILE=target/${env.JAR_NAME} ."

                    }
                }
            }
        }

        stage('Deploy to Remote Server') {
            steps {
                script {
                    // 将Docker镜像推送到远程服务器
                    withCredentials([sshUserPrivateKey(credentialsId: '5659fa11-2a13-4a8b-a435-3ce671f9ad45', keyFileVariable: 'SSH_KEY')]) {
                        // 将Docker镜像保存到文件
                        sh "docker save -o ${env.DOCKER_IMAGE_FRONTEND}.tar ${env.DOCKER_IMAGE_FRONTEND}"
                        sh "docker save -o ${env.DOCKER_IMAGE_BACKEND}.tar ${env.DOCKER_IMAGE_BACKEND}"

                        // 将Docker镜像文件传输到远程服务器
                        sh "scp -i ${SSH_KEY} ${env.DOCKER_IMAGE_FRONTEND}.tar ${env.REMOTE_USER}@${env.REMOTE_HOST}:~/"
                        sh "scp -i ${SSH_KEY} ${env.DOCKER_IMAGE_BACKEND}.tar ${env.REMOTE_USER}@${env.REMOTE_HOST}:~/"

                        // 在远程服务器上加载并运行Docker镜像
                        sh """
                            ssh -i ${SSH_KEY} ${env.REMOTE_USER}@${env.REMOTE_HOST} '
                            docker load -i ${env.DOCKER_IMAGE_FRONTEND}.tar &&
                            docker load -i ${env.DOCKER_IMAGE_BACKEND}.tar &&
                            docker run -d -p 8088:80 ${env.DOCKER_IMAGE_FRONTEND} &&
                            docker run -d -p 8083:8080 ${env.DOCKER_IMAGE_BACKEND}
                            '
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            // 清理工作
            cleanWs()
        }
        success {
            // 在构建成功后通知
            echo '构建成功！'
        }
        failure {
            // 在构建失败后通知
            echo '构建失败！'
        }
    }
}

