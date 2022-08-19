def call(Map config){
    println  config.extraAnsibleVars
    println "Hello ${config.extraAnsibleVars[0]}"
    def extraVars = [tier: config.tier, project_name: config.projectName]

    if (config.extraAnsibleVars){
        println "Hello ${config.extraAnsibleVar}"
        println "Hello ${config.extraAnsibleVar}"

    }
    wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
        ansiblePlaybook(
                playbook: config.playbook,
                inventory: config.inventory,
                extraVars: extraVars,
                colorized: true)
    }
}