def call(Map config=[:]) {
    buildStage(label: config.label) {
        oneClickProperties()
        gitCheckout checkoutDirectory: config.checkoutDirectory, gitUrl: config.codeRepoUrl, gitBranch: params["Environment"]
        def deployment = readYaml file: config.deploymentFile
        def jobPath = "One-Click-Deployment/Bento/_jobs/Deploy"
        deployment.services.each {
            buildJob(
                    jobName:  jobPath + "${it.key}".capitalize(),
                    parameters:[
                            string(name: 'Environment', value: params["Environment"]),
                            string(name: 'ProjectName', value: deployment.project),
                            string(name: "${it.key}".capitalize() + "Tag" , value: it.value.image)
                    ]
            )
        }

    }
}