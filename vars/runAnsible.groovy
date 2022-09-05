
def call(Map config){

    def extraVars = [:]
    def defaultVars = [tier: config.tier, project_name: config.projectName]

    if (params.AuthEnabled == true || params.AuthEnabled == false){
        extraVars = [tier: config.tier, project_name: config.projectName, auth_enabled: params.AuthEnabled ]
    }else {
        extraVars = defaultVars
    }

    if (config.extraAnsibleVars){
        println config.extraAnsibleVars
//        def passedVars = config.extraAnsibleVars.tokenize(",").collectEntries {
//            it.tokenize(":").with {
//                [(it[0]):it[1]]
//            }
//        }

        extraVars = extraVars.plus(config.extraAnsibleVars)
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