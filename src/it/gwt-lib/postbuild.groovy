import java.io.*;
import java.util.*;
import java.util.jar.*;
import org.codehaus.plexus.util.*;

try {
  File target = new File(basedir, "target");
  if (!target.exists() || !target.isDirectory()) {
    System.err.println("target file is missing or not a directory.");
    return false;
  }

  File moduleFile = new File(target, "classes/it/testlib/TestLib.gwt.xml");
  if (!moduleFile.isFile()) {
    System.err.println("it/testlib/TestLib.gwt.xml module is missing or is not a file.");
    return false;
  }
  String module = FileUtils.fileRead(moduleFile);
  if (!module.contains("<super-source")) {
    System.err.println("GWT module does not have generated sources.");
    return false;
  }

  File jar = new File(target, "gwt-library-1.0.jar");
  if (!jar.exists() || jar.isDirectory()) {
    System.err.println("jar file is missing or a directory.");
    return false;
  }

  JarFile jarFile = new JarFile(jar);
  Enumeration entries = jarFile.entries();

  boolean seenMainModule = false;
  boolean seenGwtXml = false;
  boolean seenJava = false;
  boolean seenClass = false;
  boolean seenSuperJava = false;
  boolean seenSuperClass = false;
  boolean seenGeneratedJava = false;
  boolean seenGeneratedClass = false;
  while (entries.hasMoreElements()) {
    JarEntry entry = entries.nextElement();
    String name = entry.getName();
    if (name.equals("META-INF/gwt/mainModule")) {
      seenMainModule = true;
    } else if (name.equals("it/testlib/TestLib.gwt.xml")) {
      seenGwtXml = true;
    } else if (name.equals("it/testlib/client/TestLib.java")) {
      seenJava = true;
    } else if (name.equals("it/testlib/client/TestLib.class")) {
      seenClass = true;
    } else if (name.equals("it/testlib/super/it/testlib/client/Super.java")) {
      seenSuperJava = true;
    } else if (name.equals("it/testlib/super/it/testlib/client/Super.class")) {
      seenSuperClass = true;
    } else if (name.equals("it/testlib/client/AutoValue_Processed.java")) {
      seenGeneratedJava = true;
    } else if (name.equals("it/testlib/client/AutoValue_Processed.class")) {
      seenGeneratedClass = true;
    }
  }
  boolean result = true;
  if (!seenMainModule) {
    System.err.println("META-INF/gwt/mainModule missing from jar");
    result = false;
  }
  if (!seenGwtXml) {
    System.err.println("gwt.xml missing from jar");
    result = false;
  }
  if (!seenJava) {
    System.err.println("Java source missing from jar");
    result = false;
  }
  if (!seenClass) {
    System.err.println("Compiled Java class missing from jar");
    result = false;
  }
  if (!seenSuperJava) {
    System.err.println("Java super-source missing from jar");
    result = false;
  }
  if (seenSuperClass) {
    System.err.println("jar erroneously contains compiled Java super-source");
    result = false;
  }
  if (!seenGeneratedJava) {
    System.err.println("Generated java source missing from jar");
    result = false;
  }
  if (!seenGeneratedClass) {
    System.err.println("Compiled generated Java class missing from jar");
    result = false;
  }

  File buildLogFile = new File(basedir, "build.log");
  if (!buildLogFile.exists() || buildLogFile.isDirectory()) {
    System.err.println("build.log file is missing or a directory.");
    result = false;
  } else {
    String buildLog = FileUtils.fileRead(buildLogFile);
    if (buildLog.contains("build is platform dependent")) {
      System.err.println("Encoding is not set.");
      result = false;
    }
    if (!buildLog.contains("Tests run: 3, Failures: 0, Errors: 0, Skipped: 0")) {
      System.err.println("build.log does not talk about running tests");
      result = false;
    }
  }

  return result;
} catch (Throwable t) {
  t.printStackTrace();
  return false;
}

return true;