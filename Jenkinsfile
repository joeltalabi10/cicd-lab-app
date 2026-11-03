pipeline {
  agent any
  options {
    timestamps()
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '20'))
  }
  triggers { pollSCM('H/2 * * * *') }

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
  }
}
