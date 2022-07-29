def call(Map config=[:]) {
    buildStage(label: config.label) {
        buildProperties(
                name: config.parameterName,
                remoteRepoUrl: config.deploymentRepoUrl
        )
        gitCheckout checkoutDirectory: config.checkoutDirectory, gitUrl: config.deploymentRepoUrl, gitBranch: params[config.parameterName]
        def deployment = readYaml file: config.deploymentFile
        deployment.services.each {
            if (it.key == config.service) {
                it.value.version = config.version
                it.value.image = config.image
                it.value.buildNumber = env.BUILD_NUMBER
                writeYaml file: config.deploymentFile, data: deployment, overwrite: true
            }
        }
}