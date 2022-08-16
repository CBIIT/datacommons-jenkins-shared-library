def call(Map config = [:]){
    def extraVars = [tier: config.tier, project_name: config.projectName]

    if (config.powerUser){
        extraVars = [tier: config.tier, project_name: config.projectName,iam_prefix: "power-user"]
    }
    wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
        ansiblePlaybook(
                playbook: config.playbook,
                inventory: config.inventory,
                extraVars: extraVars,
                colorized: true)
    }
}