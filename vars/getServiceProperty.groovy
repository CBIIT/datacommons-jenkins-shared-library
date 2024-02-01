def call(Map config = [:]){
    def deployment = readYaml file: config.deploymentFile
    def propertyValue = ""
    def propertyKey = config.property
    deployment.services.each {
        if (it.key == config.service) {
            propertyValue = it.value."${propertyKey}"
        }
    }
    return propertyValue
}