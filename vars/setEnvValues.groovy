def call(Closure body) {
    withEnv([
            "FE_VERSION=${params.FrontendTag}",
            "BE_VERSION=${params.BackendTag}",
            "AUTH_VERSION=${params.AuthTag}",
            "PROJECT_NAME=${params.ProjectName}",
            "FILES_VERSION=${params.FilesTag}",
    ]) {
        body()
    }
}