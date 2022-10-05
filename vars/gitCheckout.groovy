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
                checkout([
                        $class: 'GitSCM', branches: [[name: "${config.gitBranch}"]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions: [],
                        submoduleCfg: [],
                        userRemoteConfigs: [[url: "${config.gitUrl}", credentialsId: "git-ssh-cred"]]
                ])
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

