def call(Map config = [:]){
    stage('read deployments.yaml') {

        deployement = readYaml file: config.deploymentFile
        deployement.services.each {
            val -> println val
        }
    }
}

