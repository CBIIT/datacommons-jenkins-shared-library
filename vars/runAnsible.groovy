def call(Map config = [:]){
    def extraVars = [tier: config.tier, project_name: config.projectName]

    if (config.extraVars){
        extraVars = extraVars.plus(config.extraVars[0])
    }
    println "Here are the vars"
    println extraVars
    wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
        ansiblePlaybook(
                playbook: config.playbook,
                inventory: config.inventory,
                extraVars: extraVars,
                colorized: true)
    }
}