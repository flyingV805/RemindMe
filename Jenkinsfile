#!groovy
pipeline {
    agent {
        docker {
            image "android-sdk"
            args '-it --memory=12g --cpus="4"'
        }
    }
    parameters {
        string(
            name: "BRANCH",
            defaultValue: "master",
            description: "Какой бренч собрать?"
        )
    }
    stages {
        stage("init") {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew'
            }
        }
        stage("build") {
            steps {
                sh "./gradlew assembleDebug"
            }
        }
        stage("deploy") {
            steps {
                echo "deploy stage"
            }
        }
    }
    post {
        always {
            archiveArtifacts(artifacts: '**/build/reports/**', allowEmptyArchive: true)
        }
    }
}
