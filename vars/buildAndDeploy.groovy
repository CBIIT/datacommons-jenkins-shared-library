def call(Map config=[:]){
    buildStage(
            label: "${config.label}",
            useDockerAgent: "${config.useDockerAgent}",
            agentImage: "${config.agentImage}",
            dockerRegistryUrl: "${config.dockerRegistryUrl}",
            registryCredentialsId: "${config.registryCredentialsId}"
    ) {
        buildProperties(
                name: config.parameterName,
                remoteRepoUrl: config.codeRepoUrl,
                deploymentRepoUrl: config.deploymentRepoUrl
        )
        gitCheckout checkoutDirectory: config.checkoutDirectory, gitUrl: config.codeRepoUrl, gitBranch: params[config.parameterName]
        gitCheckout checkoutDirectory: config.deploymentCheckoutDirectory, gitUrl: config.deploymentRepoUrl, gitBranch: params["Environment"]
        if (config.includeFrontendRepo == true){
            additionalParameters values: [
                    gitParameter(branch: "", branchFilter: "origin/(.*)", defaultValue: "main", description: "Select Branch or Tag to build", name: "FrontendTag", quickFilterEnabled: false, selectedValue: "NONE", sortMode: "NONE", tagFilter: "origin/(.*)", type: "GitParameterDefinition",useRepository: config.frontendRepoUrl),
                    booleanParam(defaultValue: true, name: 'AuthEnabled')
            ]
            gitCheckout checkoutDirectory: config.frontendCheckoutDirectory, gitUrl: config.frontendRepoUrl, gitBranch: params["FrontendTag"]
        }
        gitCheckout checkoutDirectory: "icdc-devops", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        setEnvValues(){}
        stage("build"){
            runAnsible playbook: "${WORKSPACE}/icdc-devops/ansible/${config.buildPlaybook}", inventory: "${WORKSPACE}/icdc-devops/ansible/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}", extraAnsibleVars: "${config.extraAnsibleVars}"
        }
        stage("deploy"){
            setEnvValues(){
                def version = params["${config.parameterName}"] + "." + "${BUILD_NUMBER}"
                env."${config.appVersionName}" = version
                if (config.service == "frontend"){
                    env.BE_VERSION  = getVersion(service: "backend",deploymentFile: config.deploymentFile)
                }
            }
            runAnsible playbook: "${WORKSPACE}/icdc-devops/ansible/${config.deployPlaybook}", inventory: "${WORKSPACE}/icdc-devops/ansible/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}", extraAnsibleVars: "${config.extraAnsibleVars}"
        }
        if (params["UpdateDeploymentVersion"] == true){
            writeDeployment(
                    version: env[config.appVersionName],
                    image: env[config.appVersionName],
                    service: config.service,
                    deploymentFile: config.deploymentFile,
                    deploymentRepoUrl: config.deploymentRepoUrl,
                    deploymentCheckoutDirectory: config.deploymentCheckoutDirectory
            )
        }
        stage("tag repos"){
            tagRepo gitTag: params["${config.parameterName}"], gitUrl: "${config.codeRepoUrl}", checkoutDirectory: "${config.checkoutDirectory}"
        }
        notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
    }
}