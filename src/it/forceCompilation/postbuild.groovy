try {
  File buildLogFile = new File(basedir, "build.log")
  if (!buildLogFile.exists() || buildLogFile.isDirectory()) {
    System.err.println("build.log file is missing or a directory.")
    return false
  }

  String buildLog = buildLogFile.text
  int first = buildLog.indexOf("Compiling module it.test.Test")
  int last = buildLog.lastIndexOf("Compiling module it.test.Test")
  if (first == last) {
    System.err.println("build.log talks only once about compiling GWT module")
    return false
  }

  if (!new File(basedir, "target/gwt-application-1.0/test/test.nocache.js").exists()) {
    System.err.println("GWT module has not been compiled.")
    return false
  }

} catch (Throwable t) {
  t.printStackTrace()
  return false
}

return true
