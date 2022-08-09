def call(Map config = [:]){
    def deployment = readYaml file: config.deploymentFile
    def version = ""
    deployment.services.each {
        if (it.key == config.service) {
            version = it.value.version
        }
    }
    return version
}