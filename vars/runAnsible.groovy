def call(Map config){

    def extraVars = [tier: config.tier, project_name: config.projectName]
    if (config.extraAnsibleVars){
        println "Hello ${config.extraAnsibleVar}"
        println "Hello,${config.extraAnsibleVar[0]})

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