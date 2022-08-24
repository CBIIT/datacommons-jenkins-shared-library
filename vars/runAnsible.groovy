def call(Map config){

    def extraVars = [tier: config.tier, project_name: config.projectName]

    if (config.extraAnsibleVars != null){
        def passedVars = config.extraAnsibleVars.tokenize(",").collectEntries {
            it.tokenize(":").with {
                if(it[0] == null || it [1] == null){
                    return
                }
                [(it[0]):it[1]]
            }
        }
        println passedVars
        extraVars = extraVars.plus(passedVars)
    }
    println extraVars
    wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
        ansiblePlaybook(
                playbook: config.playbook,
                inventory: config.inventory,
                extraVars: extraVars,
                colorized: true)
    }
}