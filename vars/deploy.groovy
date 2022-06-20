def call(Closure body) {
    node {
        checkout scm
        stage('Install') {
            sh 'npm install'
        }
        stage('Test') {
            sh 'npm test'
        }
        stage('Deploy') {
            if (deploy == true) {
                sh 'npm publish'
            }
        }
        body()
    }
}