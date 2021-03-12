pipelineJob('job-name') {
  definition {
    parameters {    
      stringParam("TestParm","","")
      stringParam("TestParm2","","")
    }    
    cps {
          script(readFileFromWorkspace('pipelines/first-job/main.groovy'))
          sandbox()
    }
  }
}
