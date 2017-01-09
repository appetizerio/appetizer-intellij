package io.appetizer.intellij.remotecall.highlight;

import com.intellij.openapi.diagnostic.Logger;

import java.util.ArrayList;

public class HighLight {
  private static ArrayList<GroupHighLight> highlines = new ArrayList<GroupHighLight>();
  private static final Logger log = Logger.getInstance(HighLight.class);

  public static void addHighToGroup(int groupid, FileHighLight fileHighLight) {
    for (GroupHighLight ghl : highlines) {
      if (ghl.getGroupid() == groupid) {
        ghl.addHighlines(fileHighLight);
        return;
      }
    }
    GroupHighLight groupHighLight = new GroupHighLight();
    groupHighLight.setGroupid(groupid);
    groupHighLight.addHighlines(fileHighLight);
    highlines.add(groupHighLight);
  }

  public static ArrayList<FileHighLight> getFileLines(int groupid) {
    if (highlines == null) {
      return null;
    }
    for (GroupHighLight ghl : highlines) {
      if (ghl.getGroupid() == groupid) {
        return ghl.getHighlines();
      }
    }
    return null;
  }

  public static String getFileLinesJson(int groupid) {
    String json = "{";
    if (highlines == null) {
      return null;
    }
    log.info("luoluyao:groupid" + groupid);
    for (GroupHighLight ghl : highlines) {
      if (ghl.getGroupid() == groupid) {
        log.info("luoluyao:ghl.getGroupid()" + ghl.getGroupid());
        for (FileHighLight fhl: ghl.getHighlines()) {
          json += fhl.getJson() + ",";
        }
      }
    }
    if (json.contains(",")) {
      json = json.substring(0, json.length() - 1);
    }
    json += "}";
    return json;
  }

  public static void removeByFile(String fileName) {
    int indexh = 0;
    boolean isMatch = false;
    for (GroupHighLight ghl : highlines) {
      int index = 0;
      if (ghl.myFileHighLights != null) {
        for (FileHighLight fhl: ghl.myFileHighLights) {
          if (fileName.endsWith(fhl.getFileName())) {
            isMatch = true;
            break;
          }
          index ++;
        }
        if (isMatch) {
          highlines.get(indexh).myFileHighLights.remove(index);
          return;
        }
      }
      indexh ++;
    }
  }

  public static void removeAll() {
    highlines.clear();
  }
}

