pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Aziz8124/phoneshope.git'
            }
        }
        stage('Build') {
            steps {
                bat 'mvnw clean package'
            }
        }
        stage('Test') {
            steps {
                bat 'mvnw test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying (placeholder step)...'
            }
        }
    }
}
