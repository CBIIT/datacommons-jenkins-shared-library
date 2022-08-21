def call(Map config) {
//    if ("${config.checkoutDirectory}" == "workspace"){
//            git branch: "${config.gitBranch}", changelog: false, poll: false, url: "${config.gitUrl}"
//    } else {
//        dir("${config.checkoutDirectory}") {
//            git branch: "${config.gitBranch}", changelog: false, poll: false, url: "${config.gitUrl}"
//        }
//    }
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

            }else {
                checkout([
                        $class: 'GitSCM', branches: [[name: "${config.gitBranch}"]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: "${config.checkoutDirectory}"]],
                        submoduleCfg: [],
                        userRemoteConfigs: [[url: "${config.gitBranch}"]]
                ])
            }
        }
    }
}

