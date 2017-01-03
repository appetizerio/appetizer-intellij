package io.appetizer.intellij;

import com.intellij.openapi.project.Project;

public class ProjectInfo {
  private static String applicationId;
  private static String projectPath;
  private static String projectName;
  private static String apkPath;
  private static String baseProjectPath;
  private static Project project;

  public static String getBaseProjectPath() {
    return baseProjectPath;
  }

  public static void setBaseProjectPath(String baseProjectPath) {
    ProjectInfo.baseProjectPath = baseProjectPath;
  }

  public static Project getProject() {
    return project;
  }

  public static void setProject(Project project) {
    ProjectInfo.project = project;
  }

  public String getProjectName() {
    return projectName;
  }

  public static void setProjectName(String projectname) {
    projectName = projectname;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public static void setApplicationId(String applicationid) { applicationId = applicationid; }

  public String getProjectpath() {
    return projectPath;
  }

  public static void setProjectpath(String projectpath) {
    projectPath = projectpath;
  }

  public String getApkpath() {
    return apkPath;
  }

  private static void setApkpath(String apkpath) {
    apkPath = apkpath;
  }

  public static String getProjectInfo() {
    setApkpath(baseProjectPath + "/build/outputs/apk/");
    String projectinfo = "{";
    projectinfo += "\"projectname\":\"" + projectName + "\",";
    projectinfo += "\"projectpath\":\"" + projectPath + "\",";
    projectinfo += "\"projectapkpath\":\"" + apkPath + "\",";
    projectinfo += "\"baseprojectpath\":\"" + baseProjectPath + "\"";
    projectinfo += "}";
    return projectinfo;
  }
}
