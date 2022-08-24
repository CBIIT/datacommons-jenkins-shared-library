def call(Map config){

    def extraVars = [tier: config.tier, project_name: config.projectName]

    if (config.extraAnsibleVars != null){
        def passedVars = config.extraAnsibleVars.tokenize(",").collectEntries {
            it.tokenize(":").with {
                [(it[0]):it[1]]
            }
        }
        extraVars = extraVars.plus(passedVars)
    }
    wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
        ansiblePlaybook(
                playbook: config.playbook,
                inventory: config.inventory,
                extraVars: extraVars,
                colorized: true)
    }
}