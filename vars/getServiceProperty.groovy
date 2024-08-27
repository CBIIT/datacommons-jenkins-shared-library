def call(Map config = [:]){
    def deployment = readYaml file: config.deploymentFile
    def propertyValue = ""
    def propertyKey = config.property
    deployment.services.each {
        if (it.key == config.service) {
            // Check if the property exists in the main properties
            propertyValue = it.value."${propertyKey}"
            
            // If not found, check in additionalProperties
            if (propertyValue == null && it.value.additionalProperties?.containsKey(propertyKey)) {
                propertyValue = it.value.additionalProperties[propertyKey]
            }
        }
    }
    return propertyValue
}