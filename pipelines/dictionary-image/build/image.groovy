dockerAccount = "boyapatihemanth"

node {

    stage('Checkout') {
      deleteDir()
      gitCheckout()
    }

    stage('Build Image') {
        buildImage("dictionary")
    }

    stage('scan Image') {
        scanImage("dictionary")
    }

} //END of node(...)

def gitCheckout() {
  checkout([$class: 'GitSCM',
            branches: [[name: gitBranch]],
            doGenerateSubmoduleConfigurations: false,
            extensions: [[$class: 'CleanCheckout']],
            submoduleCfg: [],
            userRemoteConfigs: [[credentialsId: 'git-cred', url: gitUrl]]])
}

def buildImage(imageName) {

    echo "Building Image ${dockerAccount}/${imageName}"
    sh "docker build -t ${dockerAccount}/${imageName} ."

}

def scanImage(imageName) {
    echo "Scanning Image ${dockerAccount}/${imageName} using trivy"
    sh "/usr/local/bin/trivy ${dockerAccount}/${imageName}"
}