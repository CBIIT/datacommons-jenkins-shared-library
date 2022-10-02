def call(Map config=[:]){

    def parsedVars = [:]
    if (config.extraAnsibleVars != null){
        parsedVars = config.extraAnsibleVars
    }
    buildStage(
            label: "${config.label}",
            useDockerAgent: "${config.useDockerAgent}",
            agentImage: "${config.agentImage}",
            dockerRegistryUrl: "${config.dockerRegistryUrl}",
            registryCredentialsId: "${config.registryCredentialsId}"
    ) {
        dataLoaderProperties  modelRepoUrl: "${config.modelRepoUrl}"
        gitCheckout checkoutDirectory: "icdc-devops", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        gitCheckout checkoutDirectory: config.modelCheckoutDirectory, gitUrl: config.modelRepoUrl, gitBranch: params["ModelTag"]
        gitCheckout checkoutDirectory: "icdc-dataloader", gitUrl: "https://github.com/CBIIT/icdc-dataloader",  gitBranch: params["LoaderTag"]

        sh "cd icdc-dataloader && git submodule update --init"

        stage("loader"){
            if(parsedVars){
                runAnsible(
                        playbook: "${WORKSPACE}/icdc-devops/ansible/${config.playbook}",
                        inventory: "${WORKSPACE}/icdc-devops/ansible/${config.inventory}",
                        tier: "${config.tier}",
                        projectName: "${config.projectName}",
                        s3_folder: config.s3_folder,
                        wipe_db: config.wipe_db,
                        cheat_mode: config.cheat_mode,
                        data_bucket: config.data_bucket,
                        split_transactions: config.split_transactions,
                        extraAnsibleVars: parsedVars
                )
            }else{
                runAnsible (
                        playbook: "${WORKSPACE}/icdc-devops/ansible/${config.playbook}",
                        inventory: "${WORKSPACE}/icdc-devops/ansible/${config.inventory}",
                        tier: "${config.tier}",
                        projectName: "${config.projectName}"   ,
                        s3_folder: config.s3_folder,
                        wipe_db: config.wipe_db,
                        cheat_mode: config.cheat_mode,
                        data_bucket: config.data_bucket,
                        split_transactions: config.split_transactions
                )
            }
        }
        notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
    }
}