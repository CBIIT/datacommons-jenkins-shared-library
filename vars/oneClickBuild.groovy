def call(Map config=[:]) {
    buildStage(label: config.label) {
        buildProperties(
                name: config.parameterName,
                remoteRepoUrl: config.codeRepoUrl
        )
        gitCheckout checkoutDirectory: config.checkoutDirectory, gitUrl: config.codeRepoUrl, gitBranch: params[config.parameterName]
        readDeployment deploymentFile: config.deploymentFile
    }
}