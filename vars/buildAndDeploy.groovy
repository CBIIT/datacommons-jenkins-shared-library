def call(Map config=[:]){
    build(label: "${config.label}") {
        buildProperties(
                name: "${config.parameterName}",
                remoteRepoUrl: "${config.codeRepoUrl}"
        )
        def checkoutBranch = "params." + "${config.parameterName}"
        echo params[config.parameterName]
        //gitCheckout checkoutDirectory: "${config.checkoutDirectory}", gitUrl: "${config.codeRepoUrl}", gitBranch: "${checkoutBranch}"
        gitCheckout checkoutDirectory: "icdc-devops", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        setEnvValues(){}
        stage("build"){
            runAnsible playbook: "${WORKSPACE}/icdc-devops/ansible/${config.buildPlaybook}", inventory: "${WORKSPACE}/icdc-devops/ansible/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}"
        }
        stage("deploy"){
            setEnvValues(){
                env."${config.appVersionName}" = "${checkoutBranch}-${BUILD_NUMBER}"
            }
            runAnsible playbook: "${WORKSPACE}/icdc-devops/ansible/${config.deployPlaybook}", inventory: "${WORKSPACE}/icdc-devops/ansible/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}"
        }
        stage("tag repos"){
            tagRepo gitTag: "${checkoutBranch}", gitUrl: "${config.codeRepoUrl}", checkoutDirectory: "${config.checkoutDirectory}"
        }
        notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
    }
}