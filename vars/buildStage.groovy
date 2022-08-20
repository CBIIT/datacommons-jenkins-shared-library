def call(Map config=[:],Closure body) {
    if (config.useDockerAgent){
        node("${config.label}") {
            ansiColor('xterm') {
                timestamps {
                    docker.withRegistry( config.dockerRegistryUrl, config.registryCredentialsId) {
                        def buildAgent = docker.image(config.agentImage)
                        buildAgent.inside("--net=host -u root"){
                            body()
                        }
                    }
                }
            }
        }
    }else{
        node("${config.label}") {
            ansiColor('xterm') {
                timestamps {
                    body()
                }
            }
        }
    }
}