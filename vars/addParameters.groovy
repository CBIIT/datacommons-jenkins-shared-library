//This solution comes from https://stackoverflow.com/questions/48952478/use-groovy-to-add-an-additional-parameter-to-a-jenkins-job
def call(Closure body){
    existing = currentBuild.rawBuild.parent.properties
            .findAll { it.value instanceof hudson.model.ParametersDefinitionProperty }
            .collectMany { it.value.parameterDefinitions }
    jobParams = [
            body(),
    ] + existing
    properties([
            parameters(jobParams)
    ])
}