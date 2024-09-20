pipeline {
    agent any

    environment {
        timestamp = "${System.currentTimeMillis() / 1000L}"
    }

    stages {
        stage('Prepare') {
            steps {
                script {
                    // Get the ID of the be_taelim:latest image
                    def oldImageId = sh(script: "docker images be_taelim:latest -q", returnStdout: true).trim()
                    env.oldImageId = oldImageId
                }

                git branch: 'distribution',
                    url: 'https://github.com/ckd0523/taelim-back.git'
            }

            post {
                success {
                    sh 'echo "Successfully Cloned Repository"'
                }
                failure {
                    sh 'echo "Fail Cloned Repository"'
                }
            }
        }

        stage('Build Gradle') {
            steps {
                dir('.') {
                    sh """
                    chmod +x gradlew
                    """
                }

                dir('.') {
                    sh """
                    ./gradlew clean build
                    """
                }
            }

            post {
                success {
                    sh 'echo "Successfully Build Gradle Test"'
                }
                 failure {
                    sh 'echo "Fail Build Gradle Test"'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t be_taelim:${timestamp} ."
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    // Check if the container is already running
                    def isRunning = sh(script: "docker ps -q -f name=be_taelim", returnStdout: true).trim()

                    if (isRunning) {
                        sh "docker rm -f be_taelim"
                    }

                    // Run the new container
                    try {
                        sh """
                        docker run \
                          --name=be_taelim \
                          -v /docker_projects/be_taelim/volumes/gen:/gen \
                          --restart unless-stopped \
                          --network app \
                          -e TZ=Asia/Seoul \
                          -d \
                          be_taelim:${timestamp}
                        """
                    } catch (Exception e) {
                        // If the container failed to run, remove it and the image
                        isRunning = sh(script: "docker ps -q -f name=be_taelim", returnStdout: true).trim()

                        if (isRunning) {
                            sh "docker rm -f be_taelim"
                        }

                        def imageExists = sh(script: "docker images -q be_taelim:${timestamp}", returnStdout: true).trim()

                        if (imageExists) {
                            sh "docker rmi be_taelim:${timestamp}"
                        }

                        error("Failed to run the Docker container.")
                    }

                    // If there's an existing 'latest' image, remove it
                    def latestExists = sh(script: "docker images -q be_taelim:latest", returnStdout: true).trim()

                    if (latestExists) {
                        sh "docker rmi be_taelim:latest"

                        if(!oldImageId.isEmpty()) {
                        	sh "docker rmi ${oldImageId}"
                        }
                    }

                    // Tag the new image as 'latest'
                    sh "docker tag be_taelim:${env.timestamp} be_taelim:latest"
                }
            }
        }
    }
}