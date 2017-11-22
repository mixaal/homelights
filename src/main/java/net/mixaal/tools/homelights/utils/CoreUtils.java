package net.mixaal.tools.homelights.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.mixaal.tools.homelights.IConfig;
import net.mixaal.tools.homelights.IConfig.SystemProperties;

public class CoreUtils {

  enum Variables { ACCESS_KEY, SERVICE_LOCATION }

  public static String getServiceLocation() {
    final String serviceLocation = System.getProperty(SystemProperties.BRIDGE_IP);
    return (serviceLocation!=null && !serviceLocation.isEmpty()) ? serviceLocation : getVariable(Variables.SERVICE_LOCATION.toString());
  }

  public static String getAccessKey() {
    final String accessKey = System.getProperty(SystemProperties.HUE_ACCESS_KEY);
    return (accessKey!=null && !accessKey.isEmpty()) ? accessKey : getVariable(Variables.ACCESS_KEY.toString());
  }

  private static String getVariable(final String varName) {
    final File configFile = new File(System.getProperty("user.home") + "/.homelights/config");
    try {
      final String[] content = Files.toString(configFile, Charsets.UTF_8).split("\\n");
      for(final String line: content) {
        if (line.startsWith(varName+"=")) {
          final String accessKey = line.replaceFirst(varName+"=", "").replaceAll("\\\"", "");
          return accessKey;
        }
      }
      System.out.println("Please make sure your config file starts with:");
      System.out.println(varName+"=<your access to hue>");
      System.exit(-1);
    }
    catch (FileNotFoundException ex) {
      System.out.println("Please create file: "+configFile.getAbsolutePath()+" with the content:");
      System.out.println(varName+"=<your access to hue>");
      System.exit(-1);
    }
    catch (IOException ex) {
      ex.printStackTrace();
      System.exit(-1);
    }
    return null;
  }

  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    }
    catch (InterruptedException ex) {}
  }


}
