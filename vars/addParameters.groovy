//This solution comes from https://stackoverflow.com/questions/48952478/use-groovy-to-add-an-additional-parameter-to-a-jenkins-job
def call(List<Map> parameter){
    existing = currentBuild.rawBuild.parent.properties
            .findAll { it.value instanceof hudson.model.ParametersDefinitionProperty }
            .collectMany { it.value.parameterDefinitions }
    jobParams = parameter + existing
    properties([
            parameters(jobParams)
    ])
}