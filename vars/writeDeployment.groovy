def call(Map config = [:]){
    stage('write deployments.yaml') {
        deployment = readYaml file: config.deploymentFile
        deployment.services.each {
            if (it.key == config.service){
                it.value.version =  config.version
                it.value.image = config.image
                it.value.buildNumber = env.BUILD_NUMBER
            }
        }
        writeYaml file: config.deploymentFile, datas: deployement, overwrite: "true"
    }
    stage("tag deployment repo"){
        tagRepo gitTag: params["DeployRepoTag"], gitUrl: config.deploymentRepoUrl, checkoutDirectory: config.deploymentCheckoutDirectory
    }
}