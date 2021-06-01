dockerAccount = "boyapatihemanth"

node {
    environment {
        PATH = "/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Users/hemanthboyapati/toolsdir"
    }

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
    sh "/usr/local/bin/docker build -t ${dockerAccount}/${imageName} ."

}

def scanImage(imageName) {
    echo "Scanning Image ${dockerAccount}/${imageName} using trivy"
    sh "/Users/hemanthboyapati/toolsdir/trivy ${dockerAccount}/${imageName}"
}