package io.appetizer.intellij.remotecall.filenavigator;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import com.intellij.openapi.diagnostic.Logger;
import io.appetizer.intellij.ProjectAll;
import io.appetizer.intellij.ProjectInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TargetProject {
  private static final Logger log = Logger.getInstance(TargetProject.class);
  private static final String pathConstraint = "src";
  private static final String androidManifestName = "AndroidManifest.xml";

  public static Project getTargetProject(String fileName, boolean isId) {
    Map<Project, Collection<VirtualFile>> foundFilesInAllProjects = ProjectAll.setFoundFilesInAllProjects(fileName, isId);
    for (Project project : foundFilesInAllProjects.keySet()) {
      for (VirtualFile directFile : foundFilesInAllProjects.get(project)) {
        log.info("Check project package name:" + directFile.getName());
        if (isId ) {
          ProjectInfo.setApplicationId(fileName);
          if (!(directFile.getPath().contains(pathConstraint) && directFile.getPath().endsWith(androidManifestName) && isValidProject(directFile.getPath(), fileName))) {
            continue;
          }
        } else {
          if (!(directFile.getPath().contains(pathConstraint) && directFile.getPath().endsWith(fileName))) {
            continue;
          }
        }
        log.info("Check project package name:" + directFile.getName());
        if (directFile.getPath().contains("/src/")) {
          ProjectInfo.setBaseProjectPath(directFile.getPath().substring(0, directFile.getPath().indexOf("/src/")));
        }
        log.info("project:" + project.getName());
        ProjectInfo.setProject(project);
        ProjectInfo.setProjectpath(project.getBasePath());
        ProjectInfo.setProjectName(project.getName());
        return project;
      }
    }
    log.info("what?");
    return ProjectInfo.getProject();
  }

  private static boolean isValidProject(String path, String applicationId) {
    Element element;
    File f = new File(path);
    log.info("file path: " + path);
    DocumentBuilder db;
    DocumentBuilderFactory dbf;
    try {
      dbf = DocumentBuilderFactory.newInstance();
      db = dbf.newDocumentBuilder();
      Document dt = db.parse(f);
      element = dt.getDocumentElement();
      String packageName = element.getAttributes().getNamedItem("package").getNodeValue();
      log.info("packageName:" + packageName);
      if (packageName.equals(applicationId)) {
        return true;
      }
      else {
        return false;
      }
    } catch (Exception e) {
      log.error("Error", e);
    }
    return false;
  }
}
