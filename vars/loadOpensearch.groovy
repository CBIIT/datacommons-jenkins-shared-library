def call(Map config=[:]){

    buildStage(
            label: "${config.label}",
            useDockerAgent: "${config.useDockerAgent}",
            agentImage: "${config.agentImage}",
            dockerRegistryUrl: "${config.dockerRegistryUrl}",
            registryCredentialsId: "${config.registryCredentialsId}"
    ) {
        opensearchLoaderProperties  modelRepoUrl: "${config.modelRepoUrl}"
        gitCheckout checkoutDirectory: "icdc-devops", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        gitCheckout checkoutDirectory: "${params.ProjectName}-model", gitUrl: config.modelRepoUrl, gitBranch: params["ModelTag"]
        gitCheckout checkoutDirectory: "workspace", gitUrl: "https://github.com/CBIIT/icdc-dataloader",  gitBranch: params["LoaderTag"]
        gitCheckout checkoutDirectory: "${params.ProjectName}-frontend", gitUrl: config.frontendRepoUrl, gitBranch: params["FrontendTag"]
        gitCheckout checkoutDirectory: "${params.ProjectName}-backend", gitUrl: config.backendRepoUrl, gitBranch: params["BackendTag"]

        sh "git submodule update --init"

        stage("loader"){
                runAnsible(
                        playbook: "${WORKSPACE}/icdc-devops/ansible/${config.playbook}",
                        inventory: "${WORKSPACE}/icdc-devops/ansible/${config.inventory}",
                        tier: "${config.tier}",
                        projectName: "${config.projectName}",
                        extraAnsibleVars: [
                                model_file1: config.model_file1,
                                model_file2: config.model_file2,
                                property_file: config.property_file,
                                index_file: config.index_file
                        ]
                )
        }
        notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
    }
}