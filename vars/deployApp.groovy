def call(Map config=[:],Closure body){
    deploy(label: "${config.label}") {
        deployProperties parameterName: "$config.parameterName}", repoName: "${config.repoName}"
        gitCheckout checkoutDirectory: "icdc-devops", gitUrl: "https://github.com/CBIIT/icdc-devops", gitBranch: "master"
        setEnvValues(type: "deploy"){}
        stage("deploy"){
            runAnsible playbook: "${config.playbook}", inventory: "${config.inventory}", tier: "${config.tier}", projectName: "${config.projectName}"
        }
        stage("send slack"){
            notify secretPath: "${config.slackSecretPath}", secretName: "${config.slackSecretName}"
        }
    }
    body()
}