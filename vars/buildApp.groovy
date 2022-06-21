def call(Map config=[:],Closure body) {
    node("${config.label}") {
        projectProperties()
        ansiColor('xterm') {
            timestamps {

                body()
            }
        }
    }
}