def call(Map config = [:]){
    build(
            propagate: false,
            job: config.jobName,
            parameters: config.parameters
    )
}