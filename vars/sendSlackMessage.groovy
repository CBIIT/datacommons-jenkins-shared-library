import groovy.json.JsonOutput

def call(Map slackParams) {
    long epoch = System.currentTimeMillis()/1000
    def BUILD_COLORS = ['SUCCESS': 'good', 'FAILURE': 'danger', 'UNSTABLE': 'danger', 'ABORTED': 'danger']
    def slackPayload = JsonOutput.toJson(
            [
                    icon_emoji: slackParams.emojiIcon,
                    attachments: [
                            [
                                    title: slackParams.title,
                                    text: slackParams.text,
                                    fallback: slackParams.fallbackMessage,
                                    footer: slackParams.footerText,
                                    ts: epoch,
                                    mrkdwn_in: ["footer", "title"],
                                    color: "${BUILD_COLORS[currentBuild.currentResult]}"
                            ]
                    ]
            ]
    )
    try {
        sh "curl -X POST -H 'Content-type: application/json' --data '${slackPayload}'  '${slackParams.slackUrl}'"
    }catch(err){
        println "${err} Slack notify failed"
    }
}
