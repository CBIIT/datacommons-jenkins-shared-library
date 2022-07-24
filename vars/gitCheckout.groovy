def call(Map config) {
    if ("${config.checkoutDirectory}"){
        dir("${config.checkoutDirectory}"){
            git branch: "${config.gitBranch}", changelog: false, poll: false, url: "${config.gitUrl}"
        }
    }else{
        git branch: "${config.gitBranch}", changelog: false, poll: false, url: "${config.gitUrl}"
    }
}