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
        gitCheckout checkoutDirectory: config.checkoutDirectory, gitUrl: config.codeRepoUrl, gitToken: config.githubToken, gitBranch: params[config.parameterName]
        gitCheckout checkoutDirectory: config.deploymentCheckoutDirectory, gitUrl: config.deploymentRepoUrl, gitToken: config.githubToken, gitBranch: params["Environment"]
        if (config.service == "backend" || config.service == "files"){
            additionalParameters values: [
                    booleanParam(defaultValue: false, name: 'AuthEnabled')
            ]
        }
        if (config.includeFrontendRepo == true){
            additionalParameters values: [
                    gitParameter(branch: "", branchFilter: "origin/(.*)", defaultValue: "main", description: "Select Branch or Tag to build", name: "FrontendTag", quickFilterEnabled: false, selectedValue: "NONE", sortMode: "NONE", tagFilter: "*", type: "GitParameterDefinition",useRepository: config.frontendRepoUrl),
                    //booleanParam(defaultValue: false, name: 'AuthEnabled')
            ]
            gitCheckout checkoutDirectory: config.frontendCheckoutDirectory, gitUrl: config.frontendRepoUrl, gitToken: config.githubToken, gitBranch: params["FrontendTag"]
        }

        if (config.playbookRepoUrl){
            gitCheckout checkoutDirectory: "playbooks", gitUrl: config.playbookRepoUrl, gitToken: config.githubToken, gitBranch: config.playbookRepoBranch
        }else{
            gitCheckout checkoutDirectory: "playbooks", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        }

        //update submodule for bento - experimental only
        if( params["ProjectName"] == "bento" && config.service == "backend"){
            sh "git submodule update --init --recursive"
        }

        //checkout submodules for auth,files and users

        if (config.service in ["auth","files","users"]){
            sh "cd ${config.checkoutDirectory} && ls -l && git submodule update --init --recursive"
        }

       
        //adding submodule for auth
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
        notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
    }
}