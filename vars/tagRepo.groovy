def call(Map config) {

        sh "cd ${WORKSPACE}/${config.checkoutDirectory}"
        sh "gitToken=$(aws secretsmanager get-secret-value  --secret-id github/pat --region us-east-1 | jq --raw-output .SecretString | jq -r .token)"
		echo "Applying tag ${config.gitTag} to ${config.gitUrl}"
        echo "$gitToken"
//		git config user.email "jenkins@bento-tools.org"
//		git config user.name "Bento Jenkins"
//		git tag --no-sign -a "${config.gitTag}-${BUILD_NUMBER}" -m "Jenkins tag: ${config.gitTag}-${BUILD_NUMBER}"
//		git push "https://${gitToken}:x-oauth-basic@${config.gitUrl}" --tags
    }