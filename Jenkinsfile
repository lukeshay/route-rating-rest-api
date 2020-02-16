void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: env.GIT_URL],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "Jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}

pipeline {
  agent any

  environment {
    SECRET_KEY = credentials('jenkins-aws-secret-key-id')
    ACCESS_KEY = credentials('jenkins-aws-secret-access-key')
    JWT_SECRET = credentials('jenkins-jwt-secret')
    REFRESH_SECRET = credentials('jenkins-refresh-secret')
    GOOGLE_RECAPTCHA_TOKEN = credentials('jenkins-google-recaptcha-token')
  }
  stages {
    stage('Build') {
      steps {
        echo 'Building...'
        setBuildStatus('Starting build', 'PENDING')
        sh 'make build'
      }
    }
    stage('Lint') {
      steps {
        echo 'Linting...'
        sh 'make lint'
      }
    }
    stage('Test') {
      steps {
        echo 'Testing...'
        sh 'make test'
      }
    }
    stage('Coverage') {
      steps {
        echo 'Getting coverage...'
        sh 'make coverage'
      }
    }
    stage('Deploy') {
      when {
        branch 'master'
      }
      steps {
        echo 'Deploying...'
//         build job: '', propagate: true, wait: true
      }
    }
  }
  post {
    success {
      setBuildStatus('Build succeeded', 'SUCCESS');
      sh 'make clean'
    }
    failure {
      setBuildStatus('Build failed', 'FAILURE');
      sh 'make clean'
    }
  }
}
