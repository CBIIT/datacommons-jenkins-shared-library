def call(String secretName){
    echo "In secret"
    env.SECRET = sh(script: "aws secretsmanager get-secret-value  --secret-id ${secretName} --region us-east-1 | jq --raw-output .SecretString | jq -r .token | tr -d '\n'",returnStdout: true)
    echo "${SECRET}"
    return env.SECRET
}