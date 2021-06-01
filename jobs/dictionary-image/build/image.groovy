pipelineJob('docker-image-build') {
  definition {
    parameters {
      stringParam("gitUrl","https://github.com/boyapatihemanth/docker.git","Enter Docker code git repo")
      stringParam("gitBranch","master","Enter Docker code git branch")
    }
    cps {
          script(readFileFromWorkspace('pipelines/dictionary-image/build/image.groovy'))
          sandbox()
    }
  }
}