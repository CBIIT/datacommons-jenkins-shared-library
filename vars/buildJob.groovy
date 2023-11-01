def call(Map config = [:]){
    build(
            //propagate: false,
            propagate: true,
            job: config.jobName,
            parameters: config.parameters
    )
}