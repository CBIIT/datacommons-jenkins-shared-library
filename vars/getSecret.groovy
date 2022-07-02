def call(Map config){
    return sh(script: 'aws secretsmanager get-secret-value  --secret-id "${config.secretName}" --region us-east-1 | jq --raw-output .SecretString | jq -r .token | tr -d \'\\n\'', returnStdout: true)
}