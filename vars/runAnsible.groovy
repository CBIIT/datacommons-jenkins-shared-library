def call(Map config = [:]){
    def extraVars = [tier: config.tier, project_name: config.projectName]
    if (config.extraAnsibleVars){
        extraVars = extraVars.plus(extraAnsibleVars)
        config.extraAnsibleVars.each{
            println "looping" + "${it}"
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