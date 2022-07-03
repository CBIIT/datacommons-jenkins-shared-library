import groovy.json.JsonOutput

def call(Map slackParams) {
    long epoch = System.currentTimeMillis()/1000
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
                                    color: slackParams.color
                            ]
                    ]
            ]
    )
    try {
        process = [ 'sh', '-c', "curl -v -k -X POST -H \"Content-Type: application/json\" -d '${slackPayload}' '${slackParams.slackUrl}'" ].execute()
        process.waitFor()
        println process.err.text
        println process.text
        //sh "curl -X POST -H 'Content-type: application/json' --data '${slackPayload}'  '${slackParams.slackUrl}'"
    }catch(err){
        println "${err} Slack notify failed"
    }
}
