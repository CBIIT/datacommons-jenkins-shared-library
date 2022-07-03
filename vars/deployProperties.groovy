def call(Map config,Closure body){
    properties(
            [
                    [$class: 'JiraProjectProperty'],
                    [$class: 'RebuildSettings', autoRebuild: false,rebuildDisabled: false],
                    parameters(
                            [
                                    [$class: 'ChoiceParameter', choiceType: 'PT_SINGLE_SELECT', description: 'Project Name', filterLength: 1, filterable: false, name: 'ProjectName', randomName: 'choice-parameter-819708392080066', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: true, script: 'return ["bento"]'], script: [classpath: [], sandbox: true, script: 'return ["bento","cds","gmb","icdc","vote"]']]],
                                    [$class: 'ChoiceParameter', choiceType: 'PT_SINGLE_SELECT', description: 'Environment', filterLength: 1, filterable: false, name: 'Environment', randomName: 'choice-parameter-819708393885594', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: true, script: 'return ["dev"]'], script: [classpath: [], sandbox: true, script: 'return ["dev","qa","stage","prod"]']]],
                                    [$class: 'CascadeChoiceParameter', choiceType: 'PT_SINGLE_SELECT', description: 'Select Tag', filterLength: 1, filterable: false, name: "${config.parameterName}", randomName: 'choice-parameter-819708396775522', referencedParameters: '', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: false, script: ''], script: [classpath: [], sandbox: true, script: '''def cmd = "/usr/local/bin/aws ecr list-images --repository-name \${config.ecrRepoName} --filter tagStatus=TAGGED --region us-east-1 --output text"
def ecr_images_json = cmd.execute()
ecr_images_json.waitFor()
def ecr_images_list = ecr_images_json.text.readLines().collect { it.substring(it.lastIndexOf("\\t") + 1) }
ecr_images_list.removeAll([\'latest\'] as Object[])
ecr_images_list.removeAll { it.contains(\'production\') }
List tagnames = ecr_images_list.sort()
return tagnames''']]],
                                    body(),
                            ]
                    )
            ]
    )
}