def call(Map config){
    existing = currentBuild.rawBuild.parent.properties
            .findAll { it.value instanceof hudson.model.ParametersDefinitionProperty }
            .collectMany { it.value.parameterDefinitions }
    jobParams = existing + config.additionalParams
    properties([
            parameters(jobParams)
    ])
}