def call(Map config=[:]) {
    buildStage(label: config.label) {
        buildProperties(
                name: config.parameterName,
                remoteRepoUrl: config.codeRepoUrl
        )
        gitCheckout checkoutDirectory: config.checkoutDirectory, gitUrl: config.codeRepoUrl, gitBranch: params[config.parameterName]
        def deployment = readYaml file: config.deploymentFile
        deployment.services.each {
            buildJob(
                    jobName: "Deploy" + "${it.key}".capitalize(),
                    parameters:[
                            string(name: 'Environment', value: params["Environment"]),
                            string(name: 'ProjectName', value: deployment.project),
                            string(name: "${it.key}".capitalize() + "Tag" , value: it.value.image)
                    ]
            )
        }

    }
}