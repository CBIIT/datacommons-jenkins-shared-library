def call(Map config=[:],Closure body) {
    node("${config.node}") {
        projectProperties()
        timestamps {
            body()
        }
    }
}