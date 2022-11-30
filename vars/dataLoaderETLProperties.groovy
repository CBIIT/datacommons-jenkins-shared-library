def call(Map config){
    def defaultBranch = "master"
    if (config.useCustomBranch == "yes") {
        defaultBranch = config.customBranch
    }

    properties(
            [
                    [$class: 'JiraProjectProperty'],
                    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '15')),
                    [$class: 'RebuildSettings', autoRebuild: false, rebuildDisabled: false],
                    parameters(
                            [
                                    [$class: 'ChoiceParameter', choiceType: 'PT_SINGLE_SELECT', description: 'Choose the environment that this deployment should apply', filterLength: 1, filterable: false, name: 'Environment', randomName: 'choice-parameter-5032484503854306', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: true, script: 'return ["dev"]'], script: [classpath: [], sandbox: true, script: 'return ["dev","qa","stage","prod","perf","local"]']]],
                                    [$class: 'ChoiceParameter', choiceType: 'PT_SINGLE_SELECT', filterLength: 1, filterable: false, name: 'ProjectName', randomName: 'choice-parameter-5032484505435771', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: true, script: 'return ["bento"]'], script: [classpath: [], sandbox: true, script: 'return ["bento","cds","gmb","ctdc","c3dc","icdc"]']]],
                                    string(defaultValue: "",
                                            description: 'S3 Bucket',
                                            name: 'S3_BUCKET'),
                                    string(defaultValue: "",
                                            description: 'S3 Folder to load data from',
                                            name: 'S3_RAWDATA_SUBFOLDER'),
                                    string(defaultValue: "",
                                            description: 'Data Batch name',
                                            name: 'DATA_BATCH_NAME'),
                                    extendedChoice(
                                            defaultValue: '1.2',
                                            name: 'VERSION',
                                            description: 'Choose version to run',
                                            quoteValue: false,
                                            multiSelectDelimiter: ',',
                                            value: '1.2,1.3',
                                            type: 'PT_SINGLE_SELECT'),
                                    gitParameter(branchFilter: 'origin/(.*)',
                                            defaultValue: defaultBranch,
                                            name: 'ETLTag',
                                            type: 'PT_BRANCH_TAG',
                                            quickFilterEnabled: false,
                                            selectedValue: 'DEFAULT',
                                            sortMode: 'ASCENDING_SMART',
                                            tagFilter: '*',
                                            useRepository: "${config.etlRepoUrl}")
                            ]

                    )
            ]
    )
}