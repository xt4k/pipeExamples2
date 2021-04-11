pipeline {
    agent any
    stages {
	    
	    stage('clean ws') { 
		    steps {
			    cleanWs()
		    }
	    }	   
	    
	    stage("01 git checkout"){
		    steps {
			    git 'https://github.com/xt4k/pipeExamples2'
		    }
	    }
	    
	    stage('02 run auto-test_in_Maven') {
		    steps {
			    withMaven( jdk: 'jdk-8', maven: 'maven') {
				    sh '${MAVEN_HOME}/bin/mvn -Dbrowser=chrome  -Dmaven.test.failure.ignore -Dsuite=common clean test'
				     //sh 'mvn -Dbrowser=chrome  -Dmaven.test.failure.ignore -Dsuite=common clean test'
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
