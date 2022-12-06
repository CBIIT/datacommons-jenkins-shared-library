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
                                args: "--net=host -u root -v /var/run/docker.sock:/var/run/docker.sock -e NODE_OPTIONS='--max-old-space-size=${nodeMemory}'",
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
                    try {
					    
						body()
						
					} catch (e) {
					
					    echo "build failed"
					
					} finally {
					
					    post
						
					}
                }
            }
        }
    }
}