def call(Map config=[:]){

    buildStage(
            label: "${config.label}",
            useDockerAgent: "${config.useDockerAgent}",
            agentImage: "${config.agentImage}",
            dockerRegistryUrl: "${config.dockerRegistryUrl}",
            registryCredentialsId: "${config.registryCredentialsId}"
    ) {
        opensearchLoaderProperties(
                modelRepoUrl: config.modelRepoUrl,
                backendRepoUrl: config.backendRepoUrl,
                frontendRepoUrl: config.frontendRepoUrl,
                customBranch: config.customBranch,
                useCustomBranch: config.useCustomBranch
        )
        gitCheckout checkoutDirectory: "workspace", gitUrl: "https://github.com/CBIIT/icdc-dataloader",  gitBranch: params["LoaderTag"]
        gitCheckout checkoutDirectory: "${WORKSPACE}/${params.ProjectName}-model", gitUrl: config.modelRepoUrl, gitBranch: params["ModelTag"]
        gitCheckout checkoutDirectory: "${WORKSPACE}/${params.ProjectName}-frontend", gitUrl: config.frontendRepoUrl, gitBranch: params["FrontendTag"]
        gitCheckout checkoutDirectory: "${WORKSPACE}/${params.ProjectName}-backend", gitUrl: config.backendRepoUrl, gitBranch: params["BackendTag"]
        if (config.playbookRepoUrl){
            gitCheckout checkoutDirectory: "${WORKSPACE}/playbooks", gitUrl: config.playbookRepoUrl, gitBranch: config.playbookRepoBranch
        }else{
            gitCheckout checkoutDirectory: "${WORKSPACE}/playbooks", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        }
        sh "ls -l"
        sh "git submodule update --init"


        stage("loader"){
                runAnsible(
                        playbook: "${WORKSPACE}/playbooks/${config.playbook}",
                        inventory: "${WORKSPACE}/playbooks/${config.inventory}",
                        tier: "${config.tier}",
                        projectName: "${config.projectName}",
                        extraAnsibleVars: [
                                model_file_1: config.model_file1,
                                model_file_2: config.model_file2,
                                property_File: config.property_file,
                                indices_File: config.indices_file
                        ]
                )
        }
        notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
    }
}