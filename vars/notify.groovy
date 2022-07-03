def call(Map config){
    stage("send slack"){
        def slackUrl = getSecret("${config.secretPath}","${config.secretName}")
        sendSlackMessage([
                emojiIcon: ":jenkins:",
                slackUrl: slackUrl,
                title: "Jenkins Job Alert - ${currentBuild.currentResult}",
                fallback: "Bento Jenkins Build",
                footer: "bento devops",
                text: "Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} :beer:\n Details at: ${env.BUILD_URL}console",
        ])
    }
}