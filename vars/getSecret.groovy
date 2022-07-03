def call(Map config){
    env.SECRET = sh(script: "aws secretsmanager get-secret-value  --secret-id ${config.secretName} --region us-east-1 | jq --raw-output .SecretString | jq -r .token | tr -d '\n'", returnStdout: true)
    return env.SECRET
}