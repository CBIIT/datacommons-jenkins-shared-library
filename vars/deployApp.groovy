def call(Map config=[:]){
    deploy(label: "${config.label}") {
        deployProperties parameterName: "${config.parameterName}", repoName: "${config.repoName}"
        gitCheckout checkoutDirectory: "icdc-devops", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        setEnvValues(type: "deploy"){}
        stage("deploy"){
            runAnsible playbook: "${WORKSPACE}/icdc-devops/ansible/${config.playbook}", inventory: "${WORKSPACE}/icdc-devops/ansible/${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}"
        }
        stage("send slack"){
            notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
        }
    }
}