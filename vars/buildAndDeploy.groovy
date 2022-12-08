import groovy.json.JsonSlurper
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
            registryCredentialsId: "${config.registryCredentialsId}",
			nodeMemory: "${config.nodeMemory}"
    ) {
        buildProperties(
                name: config.parameterName,
                remoteRepoUrl: config.codeRepoUrl,
                deploymentRepoUrl: config.deploymentRepoUrl
        )
        gitCheckout checkoutDirectory: config.checkoutDirectory, gitUrl: config.codeRepoUrl, gitBranch: params[config.parameterName]
        gitCheckout checkoutDirectory: config.deploymentCheckoutDirectory, gitUrl: config.deploymentRepoUrl, gitBranch: params["Environment"]
        if (config.service == "backend"){
            additionalParameters values: [
                    booleanParam(defaultValue: false, name: 'AuthEnabled')
            ]
        }
        if (config.includeFrontendRepo == true){
            additionalParameters values: [
                    gitParameter(branch: "", branchFilter: "origin/(.*)", defaultValue: "main", description: "Select Branch or Tag to build", name: "FrontendTag", quickFilterEnabled: false, selectedValue: "NONE", sortMode: "NONE", tagFilter: "*", type: "GitParameterDefinition",useRepository: config.frontendRepoUrl),
                    //booleanParam(defaultValue: false, name: 'AuthEnabled')
            ]
            gitCheckout checkoutDirectory: config.frontendCheckoutDirectory, gitUrl: config.frontendRepoUrl, gitBranch: params["FrontendTag"]
        }

        if (config.playbookRepoUrl){
            gitCheckout checkoutDirectory: "playbooks", gitUrl: config.playbookRepoUrl, gitBranch: config.playbookRepoBranch
        }else{
            gitCheckout checkoutDirectory: "playbooks", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        }

        //update submodule for bento - experimental only
        if( params["ProjectName"] == "bento" && config.service == "backend"){
            sh "git submodule update --init --recursive"
        }
        setEnvValues(){}
        stage("build"){
            if(parsedVars) {
                runAnsible playbook: "${WORKSPACE}/playbooks/${config.buildPlaybook}", inventory: "${WORKSPACE}/playbooks/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}", extraAnsibleVars: parsedVars
            } else {
                runAnsible playbook: "${WORKSPACE}/playbooks/${config.buildPlaybook}", inventory: "${WORKSPACE}/playbooks/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}"
            }
        }
        stage("deploy"){
            setEnvValues(){
                def version = params["${config.parameterName}"] + "." + "${BUILD_NUMBER}"
                env."${config.appVersionName}" = version
                if (config.service == "frontend"){
                    env.BE_VERSION  = getVersion(service: "backend",deploymentFile: config.deploymentFile)
                }
            }
            if(parsedVars){
                runAnsible playbook: "${WORKSPACE}/playbooks/${config.deployPlaybook}", inventory: "${WORKSPACE}/playbooks/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}",extraAnsibleVars: parsedVars
            }else {
                runAnsible playbook: "${WORKSPACE}/playbooks/${config.deployPlaybook}", inventory: "${WORKSPACE}/playbooks/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}"
            }

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
        
		if (config.postBuildJobs){
            config.postBuildJobs.each { runPostJobs jobPath: "${it.jobPath}" jobParams: "${it.jobParams}" }
        }
		notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
    }
}