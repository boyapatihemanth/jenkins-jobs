dockerAccount = "boyapatihemanth"
node {
    stage('Checkout') {
      deleteDir()
      gitCheckout()
    }

    stage('Build Image') {
        buildImage("dictionary")
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
    sh "ls -lart"
    //sh "docker build -t ${dockerAccount}/${imageName} ."

}