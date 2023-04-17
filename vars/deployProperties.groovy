def call(Map config){
    properties(
            [
                    [$class: 'JiraProjectProperty'],
                    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '15')),
                    [$class: 'RebuildSettings', autoRebuild: false, rebuildDisabled: false],
                    parameters(
                            [
                                    [$class: 'ChoiceParameter', choiceType: 'PT_SINGLE_SELECT', description: 'Choose the environment that this deployment should apply', filterLength: 1, filterable: false, name: 'Environment', randomName: 'choice-parameter-5032484503854306', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: true, script: 'return ["dev"]'], script: [classpath: [], sandbox: true, script: 'return ["dev","qa","stage","prod"]']]],
                                    [$class: 'ChoiceParameter', choiceType: 'PT_SINGLE_SELECT', filterLength: 1, filterable: false, name: 'ProjectName', randomName: 'choice-parameter-5032484505435771', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: true, script: 'return ["bento"]'], script: [classpath: [], sandbox: true, script: 'return ["hub"]']]],
                                    [$class: 'CascadeChoiceParameter', choiceType: 'PT_SINGLE_SELECT', filterLength: 1, filterable: false, name: "${config.parameterName}", randomName: 'choice-parameter-8744093374048404', referencedParameters: '', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: true, script: 'return ["latest"]'], script: [classpath: [], sandbox: true, script: """def cmd = "/usr/local/bin/aws ecr list-images --repository-name ${config.repoName} --filter tagStatus=TAGGED --region us-east-1 --output text"
def ecr_images_json = cmd.execute()
ecr_images_json.waitFor()
def ecr_images_list = ecr_images_json.text.readLines().collect { it.substring(it.lastIndexOf("\\t") + 1) }
ecr_images_list.removeAll(['latest'] as Object[])
ecr_images_list.removeAll { it.contains('internal') }
List tagnames = ecr_images_list.sort()
return tagnames"""]]],
                            ]

                    )
            ]
    )
}