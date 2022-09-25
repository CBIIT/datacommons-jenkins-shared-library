def call(Map config){
        def slackUrl = getSecret("${config.secretPath}","${config.secretName}")
        sendDeploymentSummary([
                emojiIcon: ":jenkins:",
                slackUrl: slackUrl,
                title: "Jenkins Job Alert - ${currentBuild.currentResult}",
                fallback: "Bento Jenkins Build",
                footer: "bento devops",
                text: "Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} :beer:\n Details at: ${env.BUILD_URL}console",
                deploymentFile: config.deploymentFile,
                projectName: config.projectName,
                tier: config.tier
        ])

}