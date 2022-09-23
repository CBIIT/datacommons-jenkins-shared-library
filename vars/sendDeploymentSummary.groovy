import groovy.json.JsonOutput

def call(Map config) {
    def deployment = readYaml file: config.deploymentFile
    def header = [type: "header",text: [type: "plain_text",text: "One Click Deployment Summary", emoji: true]]
    def title = [type: "section",fields:[[type: "mrkdwn",text: "*Microservice Name*"],[type: "mrkdwn",text: "*Version*"]]]
    def services = []
    deployment.services.each {
        services.add([type: "mrkdwn",text: it.key])
        services.add([type: "mrkdwn", text: it.value.version])
    }
    def fields = [type: "section",fields: services]
    def blocks = [blocks: [header,title,fields ]]

    long epoch = System.currentTimeMillis()/1000
    def BUILD_COLORS = ['SUCCESS': 'good', 'FAILURE': 'danger', 'UNSTABLE': 'danger', 'ABORTED': 'danger']
    def slackPayload = JsonOutput.toJson(
            [
                    icon_emoji: config.emojiIcon,
                    attachments: [
                            [
                                    title: config.title,
                                    text: config.text,
                                    fallback: config.fallbackMessage,
                                    footer: config.footerText,
                                    ts: epoch,
                                    mrkdwn_in: ["footer", "title"],
                                    color: "${BUILD_COLORS[currentBuild.currentResult]}",
                                    blocks: blocks
                            ]
                    ]
            ]
    )
    try {
        sh "curl -X POST -H 'Content-type: application/json' --data '${slackPayload}'  '${config.slackUrl}'"
    }catch(err){
        println "${err} Slack notify failed"
    }
}
