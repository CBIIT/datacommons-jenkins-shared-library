def call(String secretPath,String secretName ){
    env.SECRET = sh(script: "aws secretsmanager get-secret-value  --secret-id ${secretPath} --region us-east-1 | jq --raw-output .SecretString | jq -r .${secretName} | tr -d '\n'",returnStdout: true)
    return env.SECRET
}