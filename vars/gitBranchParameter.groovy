def call(Map config){
    gitParameter(branch: "", branchFilter: "origin/(.*)", defaultValue: "master", description: "Select Branch or Tag to build", name: "${config.name}", quickFilterEnabled: false, selectedValue: "NONE", sortMode: "NONE", tagFilter: "*", type: "GitParameterDefinition",useRepository: "${config.remoteRepoUrl}")
}