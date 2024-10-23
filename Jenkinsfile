pipeline {
    environment {
        DOCKERHUB_CREDENTIALS = credentials('docker') // DockerHub credentials
        GITHUB_TOKEN = credentials('github-token') // GitHub token
         SCANNER_HOME = tool 'sonar-scanner'
    }
    agent any
     triggers {
    githubPush()
  }
    stages {
        stage("Clean Up") {
            steps {
                deleteDir() // Clean the workspace
            }
        }
        stage('Checkout GIT') {
            steps {
                withCredentials([string(credentialsId: 'github-token', variable: 'GITHUB_TOKEN')]) {
                    sh '''
                        git clone --branch etudiant https://nourchawebi:${GITHUB_TOKEN}@github.com/nourchawebi/maven-devops-project.git
                    '''
                }
            }
        }
        stage('OWASP Scan') {
            steps {
                echo "Cleaning up OWASP Dependency Check database files..."
                
                // Delete the old database files if they exist
                sh '''
                    if [ -f /data/jenkins/nvid/dc.h2.db ]; then
                        rm /data/jenkins/nvid/dc.h2.db
                    fi
                    if [ -f /data/jenkins/nvid/dc.lock.db ]; then
                        rm /data/jenkins/nvid/dc.lock.db
                    fi
                    if [ -f /data/jenkins/nvid/dc.trace.db ]; then
                        rm /data/jenkins/nvid/dc.trace.db
                    fi
                '''
                echo "Running OWASP Dependency Check..."
                
                // Run the OWASP Dependency Check against the project
                dependencyCheck additionalArguments: '', odcInstallation: 'owasp-m'
                
                // Publish the Dependency Check report
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
        stage("Move Project Files") {
            steps {
                sh '''
                    mv maven-devops-project/* . || true  # Move all contents of the project directory to the current directory
                    rmdir maven-devops-project || true  # Remove the now-empty directory
                '''
            }
        }
        stage("Maven clean") {
            steps {
                script {
                    sh '''
                        ls -l /var/lib/jenkins/workspace/nour &&
                        mvn clean
                    '''
                }
            }
        }
        stage('Compile Artifact') {
            steps {
                echo "Compiling..."
                sh 'mvn compile' // Compile the project
            }
        }
        stage('Run Tests mockito') {
            steps {
                script {
                    // Run tests with Maven
                    sh 'mvn test'
                }
            }
        }
        
         
    
        stage('mvn package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
        
        stage('JaCoCo Report') {
            steps {
                // Generate the JaCoCo report
                sh 'mvn jacoco:report'
            }
        }
        stage('Copy JaCoCo Report') {
            steps {
               
                // Copy the JaCoCo report to the Jenkins workspace
                sh 'cp -R target/site/jacoco/* .'
            }
        }
        stage('mvn deploy') {
            steps {
                sh 'mvn deploy -DskipTests'
            }
        }
        stage('sonar docker compose up') {
steps {
    
     
    echo "Starting application with Docker Compose..."
    sh "docker compose -f sonar-docker-compose.yaml up -d" 
    echo "Waiting for 8 minutes to ensure application starts properly..."
    sleep(time: 1, unit: 'MINUTES') 
}}
         stage('SonarQube Analysis') {
            steps {
              withCredentials([ string(credentialsId: 'sonarqube-url', variable: 'SONARQUBE_HOST_URL'), 
string(credentialsId: 'sonarqube-login', variable: 'SONARQUBE_LOGIN') ]) { 
                    sh '''$SCANNER_HOME/bin/sonar-scanner \
                        -Dsonar.host.url=${SONARQUBE_HOST_URL} \
                        -Dsonar.token=${SONARQUBE_LOGIN} \
                        -Dsonar.projectName=tpfoyerJenkins \
                        -Dsonar.java.binaries=. \
                       -Dsonar.projectKey=tpfoyerJenkins '''   }
                
            }
        }

 

        stage("Generate Docker Image") {
            steps {
                echo "Building Docker image..."
                sh "docker build -t nourchawebi/foyer-app:1.1.${env.BUILD_NUMBER} ."
            }
        }
        stage('Docker Image Scan') {
            steps {
                echo "Scanning Docker image with Trivy..."
                sh "trivy clean --java-db"
                sh "trivy image --timeout 20m --format table --scanners vuln --debug --ignore-unfixed -o trivy-imageesprit-report.html nourchawebi/foyer-app:1.1.${env.BUILD_NUMBER}"
            }
        }
        stage("Docker Login") {
            steps {
                script {
                    sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                }
            }
        }
        stage("Push Docker Image") {
            steps {
                echo "Pushing Docker image to DockerHub..."
                sh "docker push nourchawebi/foyer-app:1.1.${env.BUILD_NUMBER}"
            }
        }
        stage("Clean Up again") {
            steps {
                deleteDir() // Clean the workspace
            }
        }

stage("Clone git for deployment") {
  steps {
                withCredentials([string(credentialsId: 'github-token', variable: 'GITHUB_TOKEN')]) {
                    sh '''
                        git clone --branch etudiant https://nourchawebi:${GITHUB_TOKEN}@github.com/nourchawebi/maven-devops-project.git
                    '''
                }
            }
        }


           stage("Change Image Tag") {
            steps {dir('maven-devops-project'){
                script {
                    def deployment = readFile('docker-compose.yaml')
                    // Escape the forward slash in the Docker image name
                    def updatedDeployment = deployment.replaceAll(/nourchawebi\/foyer-app:\s*[\w.-]+/, "nourchawebi/foyer-app:1.1.${env.BUILD_NUMBER}")
                    writeFile file: 'docker-compose.yaml', text: updatedDeployment
                }
            } }
        }

        stage("Commit Changes") {
            steps {
                dir('maven-devops-project'){
                script {
                     sh 'git config user.email "jenkins@agent.com"'
                      sh 'git config user.name "jenkins-admin"'
                    sh 'git add docker-compose.yaml'
                    sh 'git commit -m "updated image tag"'
                    sh 'git push https://nourchawebi:${GITHUB_TOKEN}@github.com/nourchawebi/maven-devops-project.git etudiant'
                }
            }}
        }
        stage('Docker Compose Up') {
            steps {dir('maven-devops-project'){
                echo "Starting application with Docker Compose..."
                sh "docker compose up -d" // Add -d for detached mode
            }}
        }
    }
}
