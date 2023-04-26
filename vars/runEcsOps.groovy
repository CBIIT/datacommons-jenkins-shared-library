def call(Map config=[:]){

    buildStage(
            label: "${config.label}",
            useDockerAgent: "${config.useDockerAgent}",
            agentImage: "${config.agentImage}",
            dockerRegistryUrl: "${config.dockerRegistryUrl}",
            registryCredentialsId: "${config.registryCredentialsId}"
    ) {
        ecsOpsProperties  etlRepoUrl: "${config.etlRepoUrl}"
        gitCheckout checkoutDirectory: "workspace", gitUrl: "${config.etlRepoUrl}",  gitBranch: params["ETLTag"]

        if (config.playbookRepoUrl){
            gitCheckout checkoutDirectory: "playbooks", gitUrl: config.playbookRepoUrl, gitBranch: config.playbookRepoBranch
        }else{
            gitCheckout checkoutDirectory: "playbooks", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        }

        sh "git submodule update --init"
        sh "ls -la ${WORKSPACE}"
        stage("UpdateEcsService"){
                runAnsible(
                        playbook: "${WORKSPACE}/playbooks/${config.playbook}",
                        inventory: "${WORKSPACE}/playbooks/${config.inventory}",
                        tier: "${config.tier}",
                        projectName: "${config.projectName}",
                        extraAnsibleVars: [
                                service_name: config.service_name
                        ]
                )
        }
    }
}