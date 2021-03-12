pipelineJob('job-name') {
  definition {
    parameters {    
      stringParam("TestParm","","")
    }    
    cps {
          script(readFileFromWorkspace('pipelines/first-job/main.groovy'))
          sandbox()
    }
  }
}
