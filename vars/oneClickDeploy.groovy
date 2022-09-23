def call(Map config=[:]) {
    buildStage(label: config.label) {
        oneClickProperties()
        gitCheckout checkoutDirectory: config.checkoutDirectory, gitUrl: config.codeRepoUrl, gitBranch: params["Environment"]
        def deployment = readYaml file: config.deploymentFile
        deployment.services.each {
            buildJob(
                    jobName:  config.jobPath + "Deploy" + "${it.key}".capitalize(),
                    parameters:[
                            string(name: 'Environment', value: params["Environment"]),
                            string(name: 'ProjectName', value: deployment.project),
                            string(name: "${it.key}".capitalize() + "Tag" , value: it.value.image)
                    ]
            )
        }
        stage("send slack"){
            notifySummary secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
        }

    }
}