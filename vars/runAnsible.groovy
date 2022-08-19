def call(Map config = [:]){
    def extraAnsibleVars = [:]
    def extraVars = [tier: config.tier, project_name: config.projectName]
    if (config.extraAnsibleVars){
        extraVars = extraVars.plus(config.extraAnsibleVars)
        config.extraAnsibleVars.each{
            println "looping" + "${it}"
        }
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