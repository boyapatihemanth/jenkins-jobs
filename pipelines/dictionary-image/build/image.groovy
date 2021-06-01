node {

    stage('Checkout') {
      deleteDir()
      git_checkout()
    }



} //END of node(...)

def git_checkout() {
  checkout([$class: 'GitSCM',
            branches: [[name: gitBranch]],
            doGenerateSubmoduleConfigurations: false,
            extensions: [[$class: 'CleanCheckout']],
            submoduleCfg: [],
            userRemoteConfigs: [[credentialsId: 'git-cred', url: gitUrl]]])
}
