def call(Map config=[:]){

    def parsedVars = [:]
    if (config.extraAnsibleVars != null){
        parsedVars = config.extraAnsibleVars
    }
    deployStage(
            label: "${config.label}",
            useDockerAgent: "${config.useDockerAgent}",
            agentImage: "${config.agentImage}",
            dockerRegistryUrl: "${config.dockerRegistryUrl}",
            registryCredentialsId: "${config.registryCredentialsId}"
    ) {

        deployProperties parameterName: "${config.parameterName}", repoName: "${config.repoName}"
        if (config.playbookRepoUrl){
            gitCheckout checkoutDirectory: "playbooks", gitUrl: config.playbookRepoUrl, gitBranch: config.playbookRepoBranch
        }else{
            gitCheckout checkoutDirectory: "playbooks", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        }
        setEnvValues()
        stage("deploy"){
            if(parsedVars){
                runAnsible playbook: "${WORKSPACE}/playbooks/${config.playbook}", inventory: "${WORKSPACE}/playbooks/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}", extraAnsibleVars: parsedVars
            }else{
                runAnsible playbook: "${WORKSPACE}/playbooks/${config.playbook}", inventory: "${WORKSPACE}/playbooks/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}"
            }
        }
        notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
    }
}