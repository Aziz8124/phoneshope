pipeline {
    agent any

    tools {
        maven 'Maven_3.8.8' // تأكد أن اسم الأدوات في Jenkins مطابق لما هو معرف
        jdk 'Java_17'       // تأكد أن Java 17 معرف مسبقًا في Jenkins
    }

    stage('Checkout Code') {
        steps {
            git branch: 'main', url: 'https://github.com/Aziz8124/phoneshope.git'
        }
    }


        stage('Build with Maven') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        // اختياري: يمكنك تشغيل التطبيق
        stage('Run App') {
            steps {
                sh 'java -jar target/*.jar &'
            }
        }
    }
}
