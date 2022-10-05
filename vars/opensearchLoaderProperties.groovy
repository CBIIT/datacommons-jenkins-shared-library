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
//                                    string(defaultValue: "",
//                                            description: 'S3 Bucket',
//                                            name: 'DataBucket'),
//                                    string(defaultValue: "",
//                                            description: 'S3 Folder to load data from',
//                                            name: 'S3Folder'),
                                    extendedChoice(
                                            defaultValue: 'no',
                                            name: 'WipeDB',
                                            description: 'Choose yes to wipe DB',
                                            quoteValue: false,
                                            multiSelectDelimiter: ',',
                                            value: 'no,yes',
                                            type: 'PT_RADIO'),
                                    extendedChoice(
                                            defaultValue: 'no',
                                            name: 'CheatMode',
                                            description: 'Bypass Data Validation',
                                            quoteValue: false,
                                            multiSelectDelimiter: ',',
                                            value: 'no,yes',
                                            type: 'PT_RADIO'),
                                    extendedChoice(
                                            defaultValue: 'false',
                                            name: 'SplitTransactions',
                                            description: 'Choose true to the Split Transactions',
                                            quoteValue: false,
                                            multiSelectDelimiter: ',',
                                            value: 'false,true',
                                            type: 'PT_SINGLE_SELECT'),
                                    gitParameter(branchFilter: 'origin/(.*)',
                                            defaultValue: defaultBranch,
                                            name: 'LoaderTag',
                                            type: 'PT_BRANCH_TAG',
                                            quickFilterEnabled: false,
                                            selectedValue: 'DEFAULT',
                                            sortMode: 'ASCENDING_SMART',
                                            tagFilter: '*',
                                            useRepository: 'https://github.com/CBIIT/icdc-dataloader'),
                                    gitParameter(
                                            branch: "",
                                            branchFilter: "origin/(.*)",
                                            defaultValue: defaultBranch,
                                            description: "Filter Repo",
                                            name: "ModelTag",
                                            quickFilterEnabled: false,
                                            selectedValue: "NONE",
                                            sortMode: "NONE",
                                            tagFilter: "*",
                                            type: "PT_BRANCH_TAG",
                                            useRepository: "${config.modelRepoUrl}"),
                                    gitParameter(branchFilter: 'origin/(.*)',
                                            defaultValue: defaultBranch,
                                            name: 'BackendTag',
                                            type: 'PT_BRANCH_TAG',
                                            quickFilterEnabled: false,
                                            selectedValue: 'DEFAULT',
                                            sortMode: 'ASCENDING_SMART',
                                            tagFilter: '*',
                                            useRepository: "${config.backendRepoUrl}"),
                                    gitParameter(branchFilter: 'origin/(.*)',
                                            defaultValue: 'main',
                                            name: 'FrontendTag',
                                            type: 'PT_BRANCH_TAG',
                                            quickFilterEnabled: false,
                                            selectedValue: 'DEFAULT',
                                            sortMode: 'ASCENDING_SMART',
                                            tagFilter: '*',
                                            useRepository: "${config.frontendRepoUrl}"),
                            ]

                    )
            ]
    )
}