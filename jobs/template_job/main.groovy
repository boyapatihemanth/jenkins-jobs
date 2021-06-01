/*pipelineJob('my-deploy-job'){
  displayName('SG Deploy Job')
  description('...')
  logRotator(2, 30)
  parameters {
    stringParam('gitBranch', 'master', 'Description of var')
    choiceParam('terraformEnv', ['select one ...', 'dev', 'sys'], 'Select the req env')
    nonStoredPasswordParam('run_ansible_vault_password', 'Enter Ansible Vault Password')
    choiceParam('deploy_sg', ['false','true'], '...')
    choiceParam('terraform_apply', ['false','true'], '...')
  }
  definition {
    cps {
      script(readFileFromWorkspace('pipelines/template_job/deploy/main.groovy'))
      sandbox()
    }
  }
}*/