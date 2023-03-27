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
            gitCheckout checkoutDirectory: "playbooks", gitUrl: config.playbookRepoUrl, gitToken: config.githubToken, gitBranch: config.playbookRepoBranch
        }else{
            gitCheckout checkoutDirectory: "playbooks", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        }
        gitCheckout checkoutDirectory: config.deploymentCheckoutDirectory, gitUrl: config.deploymentRepoUrl, gitBranch: params["Environment"]
        setEnvValues(){
            if (config.service == "frontend"){
                env.BE_VERSION  = getVersion(service: "backend",deploymentFile: config.deploymentFile)
            }
        }
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