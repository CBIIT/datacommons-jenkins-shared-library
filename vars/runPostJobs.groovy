def call(Map config){
    stage("run post-build jobs"){

		echo "Running post-build job:  ${config.jobPath}"

		build job: "${config.jobPath}", parameters: config.jobParams

    }
}