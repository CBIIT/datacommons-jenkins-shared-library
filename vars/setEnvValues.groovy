def call(Closure body) {
    env.FE_VERSION = "${params.FrontendTag}"
    env.BE_VERSION = "${params.BackendTag}"
    env.AUTH_VERSION = "${params.AuthTag}"
    env.PROJECT_NAME = "${params.ProjectName}"
    env.FILES_VERSION = "${params.FilesTag}"
	env.IO_VERSION = "${params.InteroperationTag}"
    env.USERS_VERSION = "${params.UsersTag}"
    env.NEO4J_VERSION = "${params.Neo4jTag}"
    body()
}
