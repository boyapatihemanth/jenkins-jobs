gitCreds = 'git_hub_cred'
terraformBucket = 'localmacuser'
terraformPrefix = 'tfstate/jenkins'

node('jenkins-deploy-client') {
  env.http_proxy = ""
  env.https_proxy = ""
  env.no_proxy = ""
  env.TEMP_ADMIN_PASS = sh(script: '/usr/bin/openssl rand -base64 7', returnStdout: true).trim()

  dir_sg = "stacks/network/security-group"

  writeFile(file: "git-askpass-${BUILD_TAG}", text: "#!/bin/bash\ncase \"\$1\" in\nUsername*) echo \"\${STASH_USERNAME}\" ;;\nPassword*) echo \"\${STASH_PASSWORD}\" ;;\nesac")
  sh "chmod a+x git-askpass-${BUILD_TAG}"

  dir(terraformenv) {
    stage('Checkout') {
      deleteDir()
      git_checkout()
    }

    if (run_ansible_release != 'na') {
      stage('validate_artifacts') {
        env.RELEASE_ID = "${run_ansible_release}"
        sh '''
          status_code=($(curl --write-out "%{http_code}" --silent --output /dev/null https://artifactory_domain/path/to/jar/abc.jar))
          if [[ "$status_code" -ne 200 ]] ; then
            echo "jar doesnt exist or domain is not rechable"
            exit 1
          fi
        '''
      }
    }

    if(deploy_sg == 'true') {
      dir(dir_sg) {
        stage("tf init") {
          terraformKey = "sg.tfstate"
          terraform_init(terraformBucket, terraformPrefix, terraformKey)
        }
        stage("tf plan") {
          global_tfvars = "../../../env/global.tfvars"
          env_tfvars = "../../../env/${terraformEnv}.tfvars"
          terraform_plan(terraformEnv, global_tfvars, env_tfvars)
        }
        if(terraformApply == 'true') {
          stage('tf apply') {
            terraform_apply()
          }
        }
      }
    }

  } //END of dir(terraformenv)

} //END of node(...)

def git_checkout() {
  checkout([$class: 'GitSCM', branches: [[name: gitBranch]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'SubmoduleOptions', disableSubmodules: false, parentCredentials: true, reference: '', trackingSubmodules: false]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: gitCreds, url: gitUrl]]])
}

def terraform_init(terraformBucket, terraformPrefix, terraformKey) {
  withEnv(["GIT_ASKPASS=${WORKSPACE}/git-askpass-${BUILD_TAG}"]){
    withCredentials([usernamePassword(credentialsId: gitCreds, passwordVariable: 'STASH_PASSWORD', usernameVariable: 'STASH_USERNAME')]){
      sh "terraform init -no-color -force-copy -input=false -upgrade=true -backend=true -backend-config='bucket=${terraformBucket}' -backend-config='workspace_key_prefix=${terraformPrefix}' -backend-config='key=${terraformKey}'"
      sh "terraform get -no-color -update=true"
    }
  }
}

def terraform_plan(workspace, global_tfvars, env_tfvars) {
  sh "terraform workspace select ${workspace} || terraform workspace new ${workspace}"
  withEnv(["TF_VAR_run_password=${run_password}"]) {
    sh "terraform plan -no-color -input=false -out=tfplan -var-file=${global_tfvars} -var-file=${env_tfvars} -var="run_key=value"
  }
}

def terraform_plan_destroy(workspace, global_tfvars, env_tfvars) {
  sh "terraform workspace select ${workspace} || terraform workspace new ${workspace}"
  withEnv(["TF_VAR_run_password=${run_password}"]) {
    withCredentials([usernamePassword(credentialsId: 'win-domain-join', passwordVariable: 'TF_VAR_domain_join_password', usernameVariable: 'TF_VAR_domain_join_user')]){
      sh "terraform plan -destroy -no-color -input=false -out=tfplan -var-file=${global_tfvars} -var-file=${env_tfvars} -var="run_key=value"
    }
  }
}

def terraform_apply() {
  sh "terraform apply -input=false -no-color tfplan"
}