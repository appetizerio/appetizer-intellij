package io.appetizer.intellij.remotecall.handler;

import io.appetizer.intellij.remotecall.filenavigator.FileNavigator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import io.appetizer.intellij.remotecall.filenavigator.ProcessType;
import io.appetizer.intellij.remotecall.highlight.FileHighLight;
import io.appetizer.intellij.remotecall.highlight.HighLight;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.URLDecoder.decode;
import static java.util.regex.Pattern.compile;

public class OpenFileMessageHandler implements MessageHandler {
  private static final Pattern COLUMN_PATTERN = compile("[:#](\\d+)[:#]?(.*)$");
  private final FileNavigator fileNavigator;
  private static final Logger log = Logger.getInstance(OpenFileMessageHandler.class);

  public OpenFileMessageHandler(FileNavigator fileNavigator) {
    this.fileNavigator = fileNavigator;
  }

  public void handleMessage(String message, boolean isAdd) {
    Matcher matcher = COLUMN_PATTERN.matcher(message);
    int groupid ;
    String lines;
    ArrayList<Integer> linesArrayList = new ArrayList<Integer>();
    if (matcher.find()) {
      if (isAdd) {
        groupid = StringUtil.parseInt(StringUtil.notNullize(matcher.group(1)), 1);
        if (matcher.groupCount() >= 1) {
          lines = StringUtil.notNullize(matcher.group(2));
          String[] strs = lines.split("\\-");
          for (String s : strs) {
            linesArrayList.add(Integer.parseInt(s));
            log.info("Line:" + s);
          }
          //GroupHighlighter.addGroup(new GroupHighlighter(groupid, linesArrayList));
          FileHighLight fileHighLight = new FileHighLight(matcher.replaceAll(""), linesArrayList);
          HighLight.addHighToGroup(groupid, fileHighLight);
          log.info("FileHighLight: fileName" + matcher.replaceAll(""));
          fileNavigator.findAndNavigate(matcher.replaceAll(""), linesArrayList, ProcessType.TYPE.NAVIGATEANDHIGHLIGHT);
        }
      }else {
        groupid = StringUtil.parseInt(StringUtil.notNullize(matcher.group(1)), 1);
        if (groupid == HighLight.MAXGROUPID) {
          linesArrayList.add(-1);
          log.info("-1 REMOVEHIGHLIGHT");
          fileNavigator.findAndNavigate(matcher.replaceAll(""), linesArrayList, ProcessType.TYPE.REMOVEHIGHLIGHT);
        }else {
          log.info("groupId : " + groupid);
          ArrayList<FileHighLight> als;
          als = HighLight.getFileLines(groupid);
          if (als == null) {
            return;
          }
          for (FileHighLight al : als) {
            log.info(al.getFileName());
            fileNavigator.findAndNavigate(al.getFileName(), al.getLines(), ProcessType.TYPE.REMOVEHIGHLIGHT);
          }
        }
      }
    }
  }
}
