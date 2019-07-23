pipelineJob('job-name') {
  definition {
    cps {
          script(readFileFromWorkspace('pipelines/first-job/main.groovy'))
          sandbox()
    }
  }
}
