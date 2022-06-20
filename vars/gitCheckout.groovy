def call(Map gitParams) {
    dir("${gitParams.checkoutDirectory}"){
        git branch: "${gitParams.gitBranch}", changelog: false, poll: false, url: "${gitParams.gitUrl}"
    }
}