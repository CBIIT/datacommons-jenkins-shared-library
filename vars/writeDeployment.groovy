def call(Map config = [:]){
    stage('update deployments.yaml') {
        def deployment = readYaml file: config.deploymentFile
        deployment.services.each {
            if (it.key == config.service){
                it.value.version =  config.version
                it.value.image = config.image
                it.value.buildNumber = env.BUILD_NUMBER
                writeYaml file: config.deploymentFile, data: deployment, overwrite: true
            }else{
                def map = [version: config.version,image: config.image,buildNumber: env.BUILD_NUMBER ]
                deployment.services[config.service] = map
                writeYaml file: config.deploymentFile, data: deployment, overwrite: true
            }
        }
    }
    stage("update deployment repo"){
        commitRepo service: config.service, gitTag: params["Environment"], gitUrl: config.deploymentRepoUrl, checkoutDirectory: config.deploymentCheckoutDirectory
    }
}