def call(Map config=[:]){

    buildStage(
            label: "${config.label}",
            useDockerAgent: "${config.useDockerAgent}",
            agentImage: "${config.agentImage}",
            dockerRegistryUrl: "${config.dockerRegistryUrl}",
            registryCredentialsId: "${config.registryCredentialsId}"
    ) {
        dataLoaderETLProperties  etlRepoUrl: "${config.etlRepoUrl}"
        gitCheckout checkoutDirectory: "workspace", gitUrl: "${config.etlRepoUrl}",  gitBranch: params["ETLTag"]
        if (config.playbookRepoUrl){
            gitCheckout checkoutDirectory: "playbooks", gitUrl: config.playbookRepoUrl, gitBranch: config.playbookRepoBranch
        }else{
            gitCheckout checkoutDirectory: "$WORKSPACE}/playbooks", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        }


        sh "git submodule update --init"
        sh "ls -la ${WORKSPACE}"
        stage("ETL"){
                runAnsible(
                        playbook: "${WORKSPACE}/playbooks/${config.playbook}",
                        inventory: "${WORKSPACE}/playbooks/${config.inventory}",
                        tier: "${config.tier}",
                        projectName: "${config.projectName}",
                        extraAnsibleVars: [
                                data_batch_name: config.data_batch_name,
                                s3_bucket: config.s3_bucket,
                                s3_rawdata_subfolder: config.s3_rawdata_subfolder,
                                version: config.version
                        ]
                )
        }
        notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
    }
}