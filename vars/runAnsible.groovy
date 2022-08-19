def call(Map config){
    println  config.extraAnsibleVars

    def extraVars = [tier: config.tier, project_name: config.projectName]

    if (config.extraAnsibleVars){
        config.extraAnsibleVars.each {
            println it
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