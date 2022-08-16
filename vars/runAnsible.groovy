def call(Map config = [:]){
    def extraVars = [tier: config.tier, project_name: config.projectName]

    if (config.extraVars){
        config.extraVars.each{
            extraVars[it.key] = it.value
        }
    }
    wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
        ansiblePlaybook(
                playbook: config.playbook,
                inventory: config.inventory,
                extraVars: extraVars,
                colorized: true)
    }
}