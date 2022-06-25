def call(Map config){
    wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
        ansiblePlaybook(
                playbook: config.playbook,
                inventory: config.inventory,
                extraVars: [
                        tier: config.tier,
                        project_name: config.projectName
                ],
                colorized: true)
    }
}