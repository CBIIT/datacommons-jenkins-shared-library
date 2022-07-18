def call(Closure body) {
    env.FE_VERSION = "${params.FrontendTag}"
    env.BE_VERSION = "${params.BackendTag}"
    env.AUTH_VERSION = "${params.AuthTag}-${BUI"
    env.PROJECT_NAME = "${params.ProjectName}-${BUILD_NUMBER}"
    env.FILES_VERSION = "${params.FilesTag}-${BUILD_NUMBER}"
    body()
}