def call(Map config=[:]){

    buildStage(
            label: "${config.label}",
            useDockerAgent: "${config.useDockerAgent}",
            agentImage: "${config.agentImage}",
            dockerRegistryUrl: "${config.dockerRegistryUrl}",
            registryCredentialsId: "${config.registryCredentialsId}"
    ) {
        dataLoaderProperties  modelRepoUrl: "${config.modelRepoUrl}"
        gitCheckout checkoutDirectory: "workspace", gitUrl: "https://github.com/CBIIT/icdc-dataloader",  gitBranch: params["LoaderTag"]
        gitCheckout checkoutDirectory: "${WORKSPACE}/${config.modelCheckoutDirectory}", gitUrl: config.modelRepoUrl, gitBranch: params["ModelTag"]
        sh "cd ${WORKSPACE} && git submodule update --init"
        if (config.playbookRepoUrl){
            gitCheckout checkoutDirectory: "${WORKSPACE}/playbooks", gitUrl: config.playbookRepoUrl, gitBranch: config.playbookRepoBranch
        }else{
            gitCheckout checkoutDirectory: "${WORKSPACE}/playbooks", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        }
        sh "ls -l"
        stage("loader"){
                runAnsible(
                        playbook: "${WORKSPACE}/playbooks/${config.playbook}",
                        inventory: "${WORKSPACE}/playbooks/${config.inventory}",
                        tier: "${config.tier}",
                        projectName: "${config.projectName}",
                        extraAnsibleVars: [
                                s3_folder: config.s3_folder,
                                wipe_db: config.wipe_db,
                                cheat_mode: config.cheat_mode,
                                data_bucket: config.data_bucket,
                                split_transactions: config.split_transactions,
                                model_file_1: config.model_file1,
                                model_file_2: config.model_file2,
                                property_File: config.property_file,
                                load_neo4j_container: "${params.LoadNeo4jContainer}"
                        ]
                )
        }
        notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
    }
}