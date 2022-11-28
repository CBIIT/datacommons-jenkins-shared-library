def call(Map config=[:],Closure body) {
    if (config.useDockerAgent == "true"){
        node("${config.label}") {
            ansiColor('xterm') {
                timestamps {
                    docker.withRegistry( config.dockerRegistryUrl, config.registryCredentialsId) {
                        def buildAgent = docker.image(config.agentImage)
						def nodeMemory = config.nodeMemory ?: "8192"
                        buildAgent.pull()
                        withDockerContainer(
                                args: "--net=host --dns='8.8.8.8' -u root -v /var/run/docker.sock:/var/run/docker.sock -e NODE_OPTIONS='--max-old-space-size=${nodeMemory}'",
                                image: config.agentImage
                        ){
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