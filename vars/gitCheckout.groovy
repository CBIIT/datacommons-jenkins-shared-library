def call(Map config) {
    if ("${config.checkoutDirectory}" == "workspace"){
            git branch: "${config.gitBranch}", changelog: false, poll: false, url: "${config.gitUrl}"
    } else {
        dir("${config.checkoutDirectory}") {
            git branch: "${config.gitBranch}", changelog: false, poll: false, url: "${config.gitUrl}"
        }
    }
}