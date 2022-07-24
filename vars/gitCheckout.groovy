def call(Map config) {
        dir("${config.checkoutDirectory}"){
            git branch: "${config.gitBranch}", changelog: false, poll: false, url: "${config.gitUrl}"
        }
}