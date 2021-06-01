node {

    stage('Checkout') {
      deleteDir()
      git_checkout()
    }



} //END of node(...)

def git_checkout() {
  checkout([$class: 'GitSCM', branches: [[name: gitBranch]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'SubmoduleOptions', disableSubmodules: false, parentCredentials: true, reference: '', trackingSubmodules: false]], submoduleCfg: [], userRemoteConfigs: [[url: gitUrl]]])
}
