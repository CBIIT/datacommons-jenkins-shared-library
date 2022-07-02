def call(Map config) {
        env.GIT_TOKEN = sh(script: 'aws secretsmanager get-secret-value  --secret-id github/pat --region us-east-1 | jq --raw-output .SecretString | jq -r .token | tr -d \'\\n\'', returnStdout: true)
        env.GIT_URL = "${config.gitUrl}".replace('https://','')
        sh """
        cd "${WORKSPACE}/${config.checkoutDirectory}"
        echo "Applying tag ${config.gitTag} to ${config.gitUrl}"
        git config user.email "jenkins@bento-tools.org"
        git config user.name "Bento Jenkins"
        git tag --no-sign -a "${config.gitTag}-${BUILD_NUMBER}" -m "Jenkins tag: ${config.gitTag}-${BUILD_NUMBER}"
        git push "https://${GIT_TOKEN}:x-oauth-basic@${GIT_URL}" --tags
"""
}