def call(Map config=[:],Closure body) {
    node("${config.label}") {
        projectProperties()
        timestamps {
            body()
        }
    }
}