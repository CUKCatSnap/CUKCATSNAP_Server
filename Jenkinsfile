pipeline {
    agent any

    stages {
        stage('main branch clone') {
            steps {
                git branch: 'main', credentialsId: 'github_rsa_key',
                        url: 'https://github.com/CUKCatSnap/CUKCATSNAP_Server'
            }
        }
        stage('copy application.yml file'){
            steps {
                withCredentials([file(credentialsId: 'CATSNAP_SPRING_APPLICATION_FILE', variable: 'application')]) {
                    script {
                        sh '''
                            mkdir -p ./src/main/resources
                            cp $application ./src/main/resources/application.yml
                        '''
                    }
                }
            }
        }

        stage('project build'){
            steps{
                sh './gradlew build'
            }

        }

        stage('send jar file'){
            steps {
                // Publish over SSH를 통해 원격 서버로 JAR 파일 전송
                sshPublisher(
                        publishers: [
                                sshPublisherDesc(
                                        configName: 'catsnap_was_server',
                                        transfers: [
                                                sshTransfer(
                                                        sourceFiles: 'build/libs/catsnap-0.0.1-SNAPSHOT.jar',
                                                        removePrefix: 'build/libs/',
                                                        execCommand: 'sudo kill -9 $(sudo lsof -i :8080 -t) ; sleep 5 ;nohup java -jar ./spring_jar/catsnap-0.0.1-SNAPSHOT.jar > ./spring_jar/log.txt 2>&1 &'
                                                )
                                        ]
                                )
                        ]
                )
            }
        }
    }
}

