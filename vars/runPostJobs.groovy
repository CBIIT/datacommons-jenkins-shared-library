def call(Map config){
    stage("run post-build jobs"){

		echo "Running post-build job:  ${config.jobPath}"
		
		def paramList = []
		config.jobParams.each { paramList.add( it ) }
		echo "${paramList}"
		echo "${config.jobParams}"
		
		build job: "${config.jobPath}", parameters: paramList

    }
}