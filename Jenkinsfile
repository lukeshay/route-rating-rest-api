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
    stage('Setup') {
      steps {
        setBuildStatus('Starting build', 'PENDING')
        sh 's3cmd get s3://route-rating-data-backup/secrets/secrets.sh'
        sh 'secrets.sh'
      }
    }
    stage('Build') {
      steps {
        echo 'Building...'
        setBuildStatus('Starting build', 'PENDING')
        sh 'scripts/build.sh'
      }
    }
    stage('Lint') {
      steps {
        echo 'Linting...'
        sh './gradlew verifyGoogleJavaFormat'
      }
    }
    stage('Coverage') {
      steps {
        echo 'Getting coverage...'
        sh './gradlew jacocoTestCoverageVerification'
      }
    }
    stage('Build image') {
      when {
        branch 'master'
      }
      steps {
        echo 'Building image...'
        sh 'make'
        sh 'make push'
        sh 'make push-latest'
      }
    }
    stage('Deploy') {
      when {
        branch 'master'
      }
      steps {
        echo 'Deploying...'
        // build job: '', propagate: true, wait: true
        // Pass in the repository to get proper deploy files
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
