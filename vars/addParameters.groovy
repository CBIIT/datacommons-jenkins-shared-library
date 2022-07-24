def call(List config){
    existing = currentBuild.rawBuild.parent.properties
            .findAll { it.value instanceof hudson.model.ParametersDefinitionProperty }
            .collectMany { it.value.parameterDefinitions }
    jobParams = config.additionalParams + existing
    properties([
            parameters(jobParams)
    ])
}