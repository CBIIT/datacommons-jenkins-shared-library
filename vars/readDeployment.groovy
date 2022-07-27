//import groovy.yaml.YamlBuilder
def call(Map config = [:]){
    stage('read deployments.yaml') {
                def deployment = new File(config.deploymentFile)
                    deployment.withReader { reader ->
                        def detail = new YamlSlurper().parse(reader)
                        detail.services.each {
                            val -> println val
                        }
                    }
            }

    }

