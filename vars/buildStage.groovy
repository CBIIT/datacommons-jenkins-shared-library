def call(Map config=[:],Closure body) {
    if (config.useDockerAgent == true){
        println
        node("${config.label}") {
            ansiColor('xterm') {
                timestamps {
                    docker.withRegistry( config.dockerRegistryUrl, config.registryCredentialsId) {
                        def buildAgent = docker.image(config.agentImage)
                        buildAgent.pull()
                        buildAgent.inside("--net=host -u root -v /var/run/docker.sock:/var/run/docker.sock"){
                            body()
                        }
                    }
                }
            }
        }
    }else{
        println "No agent"
        node("${config.label}") {
            ansiColor('xterm') {
                timestamps {
                    body()
                }
            }
        }
    }
}