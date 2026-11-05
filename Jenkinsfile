pipeline {
  agent any
  options {
    timestamps()
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '20'))
  }
  triggers { pollSCM('H/2 * * * *') }
  environment {
    REGISTRY    = 'localhost:5001'
    IMAGE       = "${REGISTRY}/demo-app"
    GITOPS_REPO = 'github.com/joeltalabi10/cicd-lab-gitops.git'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
        script {
          env.SHORT_SHA = sh(returnStdout: true, script: 'git rev-parse --short=7 HEAD').trim()
        }
        echo "Building commit ${env.SHORT_SHA}"
      }
    }
    stage('Build') {
      steps { sh './mvnw -B -ntp clean package -DskipTests' }
    }
    stage('Unit Tests') {
      steps { sh './mvnw -B -ntp test' }
      post { always { junit 'target/surefire-reports/*.xml' } }
    }
    stage('SonarQube Analysis') {
      steps {
        withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
          sh '''
            ./mvnw -B -ntp org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
              -Dsonar.projectKey=demo-app \
              -Dsonar.projectName=demo-app \
              -Dsonar.host.url=http://sonarqube:9000 \
              -Dsonar.token=$SONAR_TOKEN \
              -Dsonar.qualitygate.wait=true
          '''
        }
      }
    }
    stage('Docker Build') {
      steps {
        sh 'docker build --build-arg APP_VERSION=${SHORT_SHA} -t ${IMAGE}:${SHORT_SHA} .'
      }
    }
    stage('Trivy Scan') {
      steps {
        sh 'trivy image --no-progress --exit-code 1 --severity HIGH,CRITICAL --ignore-unfixed ${IMAGE}:${SHORT_SHA}'
      }
    }
    stage('Push Image') {
      steps { sh 'docker push ${IMAGE}:${SHORT_SHA}' }
    }
    stage('Update GitOps Repo') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'github-pat',
                                          usernameVariable: 'GH_USER',
                                          passwordVariable: 'GH_TOKEN')]) {
          sh '''
            set -e
            rm -rf gitops
            git clone https://${GH_USER}:${GH_TOKEN}@${GITOPS_REPO} gitops
            cd gitops/apps/demo-app/overlays/dev
            kustomize edit set image demo-app=${IMAGE}:${SHORT_SHA}
            cd ../../../..
            git config user.name  "jenkins"
            git config user.email "jenkins@lab.local"
            git add -A
            if git diff --cached --quiet; then
              echo "gitops already at ${SHORT_SHA}"
            else
              git commit -m "demo-app: deploy ${SHORT_SHA}"
              git push origin HEAD:main
            fi
          '''
        }
      }
    }
  }
}
