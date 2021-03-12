pipelineJob('job-name') {
  definition {
    stringParam("TestParm","","")
    cps {
          script(readFileFromWorkspace('pipelines/first-job/main.groovy'))
          sandbox()
    }
  }
}
