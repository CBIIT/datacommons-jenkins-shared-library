def call(Map config = [:]){
    def deployment = readYaml file: config.deploymentFile
    def propertyValue = ""
    def propertyKey = config.property
    deployment.services.each {
        if (it.key == config.service) {
            property = it.value."${propertyKey}"
            // property = it.value.version
        }
    }
    return property
}