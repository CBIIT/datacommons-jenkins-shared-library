def call(Map config){
    stage("run post-build jobs"){

		echo "Running post-build job:  ${config.jobPath}"
		
		config.jobParams.each { echo "${it}" }
		
		build job: "${config.jobPath}", parameters: [config.jobParams]

    }
}