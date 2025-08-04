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
                // استخدم هنا الأمر المناسب للبناء، مثلا Maven أو Gradle
                sh './mvnw clean package'  // لو تستخدم Maven Wrapper
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
                // ضع هنا أوامر النشر أو التحزيم حسب ما تريد
            }
        }
    }
}
