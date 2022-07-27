//import groovy.yaml.YamlBuilder
def call(Map config = [:]){
    stage('read deployments.yaml') {
        step {
            script{
                deployments = readYaml file: config.deploymentFile
                data.services.each {
                    val -> println val
                }
            }
        }
    }
}
