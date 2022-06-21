def call(Map gitParams){
    gitParameter(branch: "", branchFilter: "origin/(.*)", defaultValue: "master", description: "Select Branch or Tag to build", name: "${gitParams.name}", quickFilterEnabled: false, selectedValue: "NONE", sortMode: "NONE", tagFilter: "*", type: "GitParameterDefinition",useRepository: "${gitParams.remoteRepoUrl}")
}