def call(Map config=[:],Closure body) {
    node("${config.label}") {
        projectProperties()
        tools {
            maven 'maven-3.6.1'
            jdk 'Default'
        }
        ansiColor('xterm') {
            timestamps {

                body()
            }
        }
    }
}