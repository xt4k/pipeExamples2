pipeline {
    agent any
    stages {
        stage("git step"){
            steps {
                git 'https://github.com/xt4k/pipeExamples2'
            }
        }
        
       stage('Run_Auto-Test_in_Maven') {
			steps {
			    catchError(buildResult: 'SUCCESS', message: 'some tests are failed', stageResult: 'UNSTABLE') {
			        withMaven(globalMavenSettingsConfig: '2115a339-7bf0-4a98-8e0c-6e2742b17571', jdk: 'jdk-8', maven: 'maven') {
			            sh 'mvn -Dbrowser=chrome -Dsuite=common clean test'
			        }
			    }
			}
       }
                
        stage("allure report add") {
            steps{
                allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
                }
            }
            
        stage ("send notif to telegram") {
            steps {
            telegramSend(message: "BUILD_TAG: ${BUILD_TAG} completed", chatId: -1001424392176)
        }
    }
}
    
}
