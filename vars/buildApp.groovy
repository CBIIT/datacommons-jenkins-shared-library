def call(Map config=[:],Closure body) {
    node("${config.label}") {
        ansiColor('xterm') {
            timestamps {
                body()
            }
        }
    }
}