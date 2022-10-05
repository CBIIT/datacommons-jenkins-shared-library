def call(Map config) {
    stage("Checkout"){
        script{
            if ("${config.checkoutDirectory}" == "workspace"){
                checkout([
                        $class: 'GitSCM', branches: [[name: "${config.gitBranch}"]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions: [],
                        submoduleCfg: [],
                        userRemoteConfigs: [[url: "${config.gitUrl}"]]
                ])
            }else if(config.checkoutSubmodule == "true"){
                echo "Here"
                sh "mkdir -p ~/.ssh && touch ~/.ssh/config"
                sh '''echo "StrictHostKeyChecking no" > ~/.ssh/config'''
                git credentialsId: 'git-ssh-cred', url: config.gitUrl,branch: config.gitBranch
            }else {
                checkout([
                        $class: 'GitSCM', branches: [[name: "${config.gitBranch}"]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: "${config.checkoutDirectory}"]],
                        submoduleCfg: [],
                        userRemoteConfigs: [[url: "${config.gitUrl}"]]
                ])
            }
        }
    }
}

