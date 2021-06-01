dockerAccount = "boyapatihemanth"
node {
    $env.PATH="/Users/hemanthboyapati/toolsdir/google-cloud-sdk/bin://anaconda3/bin://anaconda3/condabin:/Library/Frameworks/Python.framework/Versions/3.7/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Users/hemanthboyapati/toolsdir:/Users/hemanthboyapati/toolsdir/maven/apache-maven-3.6.3/bin:/usr/local/go/bin:/Library/Frameworks/Mono.framework/Versions/Current/Commands"
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
    sh "trivy ${dockerAccount}/${imageName}"
}