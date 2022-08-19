def call(Map config){
    println  config.extraAnsibleVars

    def extraVars = [tier: config.tier, project_name: config.projectName]

    if (config.extraAnsibleVars){
        println extraVars
        extraVars = extraVars.plus(config.extraAnsibleVars)

    }
    wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
        ansiblePlaybook(
                playbook: config.playbook,
                inventory: config.inventory,
                extraVars: extraVars,
                colorized: true)
    }
}