def call(Map config=[:]){
    build(label: "${config.label}") {
        buildProperties(name: "${config.parameterName}", remoteRepoUrl: "${config.codeRepoUrl}") {
        }
        echo config.parameterName
        gitCheckout checkoutDirectory: "${config.checkoutDirectory}", gitUrl: "${config.codeRepoUrl}", gitBranch: "${params.config.parameterName}"
        gitCheckout checkoutDirectory: "icdc-devops", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        setEnvValues(type: "build"){}
        stage("build"){
            runAnsible playbook: "${WORKSPACE}/icdc-devops/ansible/${config.buildPlaybook}", inventory: "${WORKSPACE}/icdc-devops/ansible/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}"
        }
        stage("deploy"){
            runAnsible playbook: "${WORKSPACE}/icdc-devops/ansible/${config.deployPlaybook}", inventory: "${WORKSPACE}/icdc-devops/ansible/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}"
        }
        stage("tag repos"){
            tagRepo gitTag: "${params.config.parameterName}", gitUrl: "${config.codeRepoUrl}", checkoutDirectory: "${config.checkoutDirectory}"
        }
        notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
    }
}