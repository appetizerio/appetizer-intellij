package io.appetizer.intellij.remotecall.highlight;

import java.util.ArrayList;

public class HighLight {
  private static ArrayList<GroupHighLight> highlines = new ArrayList<GroupHighLight>();

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
}

