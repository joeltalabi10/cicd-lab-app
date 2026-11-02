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
  }
}
