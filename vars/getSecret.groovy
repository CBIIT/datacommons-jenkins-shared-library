def call(Map config){
    echo "In secret"
    env.SECRET_NAME="${config.secretName}"
    env.SECRET = sh(script: "aws secretsmanager get-secret-value  --secret-id $SECRET_NAME --region us-east-1 | jq --raw-output .SecretString | jq -r .token | tr -d '\n'", returnStdout: true)
    echo "${SECRET}"
    return env.SECRET
}