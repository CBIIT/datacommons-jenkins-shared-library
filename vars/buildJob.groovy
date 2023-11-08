def call(Map config = [:]){
    build(
            propagate: true,
            job: config.jobName,
            parameters: config.parameters
    )
}