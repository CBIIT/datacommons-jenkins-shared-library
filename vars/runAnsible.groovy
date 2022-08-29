
def call(Map config){

    def extraVars = [:]
    def defaultVars = [tier: config.tier, project_name: config.projectName]

    if (config.extraAnsibleVars){
        extraVars = defaultVars + config.extraAnsibleVars
    }else{
        extraVars = defaultVars
    }

//    if (params.AuthEnabled){
//        extraVars = [tier: config.tier, project_name: config.projectName, auth_enabled: params.AuthEnabled ]
//    }else {
//        extraVars = extraVars
//    }
//    if (config.extraAnsibleVars){
//        def passedVars = config.extraAnsibleVars.tokenize(",").collectEntries {
//            it.tokenize(":").with {
//                [(it[0]):it[1]]
//            }
//        }
//
//        extraVars = extraVars.plus(passedVars)
//    }
    println extraVars
    wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
        ansiblePlaybook(
                playbook: config.playbook,
                inventory: config.inventory,
                extraVars: extraVars,
                colorized: true)
    }
}