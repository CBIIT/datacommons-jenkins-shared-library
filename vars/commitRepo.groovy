def call(Map config) {
    env.GIT_TOKEN = sh(script: 'aws secretsmanager get-secret-value  --secret-id github/pat --region us-east-1 | jq --raw-output .SecretString | jq -r .token | tr -d \'\\n\'', returnStdout: true)
    env.GIT_REPO_URL = "${config.gitUrl}".replace('https://','')
    def targetDirectory = ""
    if ("${config.checkoutDirectory}" == "workspace"){
        targetDirectory = "."
    }else{
        targetDirectory = "${WORKSPACE}/${config.checkoutDirectory}"
    }
    sh """
        cd "${targetDirectory}"
        echo "Applying tag ${config.gitTag} to ${config.gitUrl}"
        git config --global push.followTags true
        git config user.email "jenkins@bento-tools.org"
        git config user.name "Bento Jenkins"
        git add --all .
        git commit -am "tagging latest deployment.yaml"
        git push "https://${GIT_TOKEN}:x-oauth-basic@${GIT_REPO_URL}" HEAD:${params.Environment}
        git tag --no-sign -a "${config.service}.${BUILD_NUMBER}" -m "Jenkins tag: ${config.service}.${BUILD_NUMBER}"
        git push "https://${GIT_TOKEN}:x-oauth-basic@${GIT_REPO_URL}" --tags
"""
}
