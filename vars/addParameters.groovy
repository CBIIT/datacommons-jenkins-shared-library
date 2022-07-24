def call(config=[]){
    existing = currentBuild.rawBuild.parent.properties
            .findAll { it.value instanceof hudson.model.ParametersDefinitionProperty }
            .collectMany { it.value.parameterDefinitions }
    jobParams = config + existing
    properties([
            parameters(jobParams)
    ])
}