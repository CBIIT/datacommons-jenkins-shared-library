def call(Map config=[:],Closure body) {
    if ("${config.type}" =="build"){
        env.FE_VERSION = "${params.FrontendTag}-${BUILD_NUMBER}"
        env.BE_VERSION = "${params.BackendTag}-${BUILD_NUMBER}"
        env.AUTH_VERSION = "${params.AuthTag}-${BUILD_NUMBER}"
        env.PROJECT_NAME = "${params.ProjectName}-${BUILD_NUMBER}"
        env.FILES_VERSION = "${params.FilesTag}-${BUILD_NUMBER}"
        env.USERS_VERSION = "${params.UsersTag}-${BUILD_NUMBER}"
    } else {
        env.FE_VERSION = "${params.FrontendTag}"
        env.BE_VERSION = "${params.BackendTag}"
        env.AUTH_VERSION = "${params.AuthTag}"
        env.PROJECT_NAME = "${params.ProjectName}"
        env.FILES_VERSION = "${params.FilesTag}"
        env.USERS_VERSION = "${params.UsersTag}"
    }
    body()
}
