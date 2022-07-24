def call(Map config=[:]){
    build(label: "${config.label}") {
        buildProperties(name: "${config.parameterName}", remoteRepoUrl: "${config.codeRepoUrl}") {
        }
        gitCheckout checkoutDirectory: "${config.checkoutDirectory}", gitUrl: "${config.codeRepoUrl}", gitBranch: "${params.${config.parameterNam}}"
        gitCheckout checkoutDirectory: "icdc-devops", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        setEnvValues(type: "build"){}
        stage("build"){
            runAnsible playbook: "${WORKSPACE}/icdc-devops/ansible/${buildPlaybook}", inventory: "${WORKSPACE}/icdc-devops/ansible/${inventory}", tier: "${config.tier}", projectName: "${config.projectName}"
        }
        stage("deploy"){
            runAnsible playbook: "${WORKSPACE}/icdc-devops/ansible/${deployPlaybook}", inventory: "${WORKSPACE}/icdc-devops/ansible/${inventory}", tier: "${config.tier}", projectName: "${config.projectName}"
        }
        stage("tag repos"){
            tagRepo gitTag: "${params.${config.parameterNam}}", gitUrl: "${config.codeRepoUrl}", checkoutDirectory: "${config.checkoutDirectory}"
        }
        notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
    }
}