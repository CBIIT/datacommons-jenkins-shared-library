def call(Map config,Closure body) {
    if (config.useDockerAgent == "true"){
        node("${config.label}") {
            ansiColor('xterm') {
                timestamps {
                    docker.withRegistry( config.dockerRegistryUrl, config.registryCredentialsId) {
                        def buildAgent = docker.image(config.agentImage)
                        buildAgent.pull()
                        withDockerContainer(
                                args: "--net=host -u root -v /var/run/docker.sock:/var/run/docker.sock",
                                image: config.agentImage
                        ){
                            try {

                                body()

					        } finally {

								cleanWs()
							}
                        }
                    }
                }
            }
        }
    }else{
        node("${config.label}") {
            ansiColor('xterm') {
                timestamps {
                    try {
					    
						body()
						
					} finally {
					
					    cleanWs()
						
					}
                }
            }
        }
    }
}