def call(Map config){
    config.extraAnsibleVars.getClass()
    def extraVars = [tier: config.tier, project_name: config.projectName]
    if (config.extraAnsibleVars){
        extraVars = extraVars + config.extraAnsibleVars

    }
    println "Can't read config"
    wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
        ansiblePlaybook(
                playbook: config.playbook,
                inventory: config.inventory,
                extraVars: extraVars,
                colorized: true)
    }
}