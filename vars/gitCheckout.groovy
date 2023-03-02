def call(Map config) {
    stage("Checkout"){
        script{
            if ("${config.checkoutDirectory}" == "workspace"){
                checkout([
                        $class: 'GitSCM', branches: [[name: "${config.gitBranch}"]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions: [],
                        submoduleCfg: [],
                        userRemoteConfigs: [[credentialsId: "${config.gitToken}",url: "${config.gitUrl}"]]
                ])

            }else {
                checkout([
                        $class: 'GitSCM', branches: [[name: "${config.gitBranch}"]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: "${config.checkoutDirectory}"]],
                        submoduleCfg: [],
                        userRemoteConfigs: [[credentialsId: "${config.gitToken}",url: "${config.gitUrl}"]]
                ])
            }
        }
    }
}

