def call(Map config = [:]){
    def deployment = readYaml file: config.deploymentFile
    def property = ""
    deployment.services.each {
        if (it.key == config.service) {
            property = it.value.${config.property}
        }
    }
    return property
}