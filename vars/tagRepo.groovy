def call(Map config = [:]) {
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
        git config user.email "tructruong@yahoo.com"
        git config user.name "Tracy Truong"
        git tag --no-sign -a "${config.gitTag}.${BUILD_NUMBER}" -m "Jenkins tag: ${config.gitTag}.${BUILD_NUMBER}"
        git push "https://${GIT_TOKEN}:x-oauth-basic@${GIT_REPO_URL}" --tags
"""
}
