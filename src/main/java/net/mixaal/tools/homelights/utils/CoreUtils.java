package net.mixaal.tools.homelights.utils;

import static net.mixaal.tools.homelights.IConfig.SystemProperties.CONF_PROPERTY;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import net.mixaal.tools.homelights.IConfig;
import net.mixaal.tools.homelights.IConfig.SystemProperties;

/**
 * Basic utility for configuration reading.
 */
public class CoreUtils {

  enum Variables { ACCESS_KEY, SERVICE_LOCATION }

  public static boolean isMovieMode() {
    return getBoolean(SystemProperties.MOVIE_MODE);
  }

  public static String getServiceLocation() {
    return getStringValue(
        SystemProperties.BRIDGE_IP,
        getVariable(Variables.SERVICE_LOCATION.toString())
    );
  }

  public static String getAccessKey() {
    return getStringValue(
        SystemProperties.HUE_ACCESS_KEY,
        getVariable(Variables.ACCESS_KEY.toString())
    );
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
      System.err.println("Please make sure your config file starts with:");
      System.err.println(varName+"=<your access to hue>");
      System.exit(-1);
    }
    catch (FileNotFoundException ex) {
      System.err.println("Please create file: "+configFile.getAbsolutePath()+" with the content:");
      System.err.println(varName+"=<your access to hue>");
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


  private static final Properties properties = readProperties();

  private static String getValueFromConfigOrSystem(String propertyName) {
    final String valueFromConfig = properties.getProperty(propertyName);
    final String valueFromSystem = System.getProperty(propertyName);
    return valueFromSystem == null ? valueFromConfig : valueFromSystem;
  }

  private static String getStringValue(String propertyName, String defaultValue) {
    final String strValue = getValueFromConfigOrSystem(propertyName);
    if(strValue==null || strValue.isEmpty()) return defaultValue;
    return strValue;
  }

  private static double getDouble(String propertyName, double defaultValue) {
    final String strValue = getValueFromConfigOrSystem(propertyName);
    if(strValue==null || strValue.isEmpty()) return defaultValue;
    return Double.valueOf(strValue);
  }

  private static long getLong(String propertyName, long defaultValue) {
    final String strValue = getValueFromConfigOrSystem(propertyName);
    if(strValue==null || strValue.isEmpty()) return defaultValue;
    return Long.valueOf(strValue);
  }

  private static int getInteger(String propertyName, int defaultValue) {
    final String strValue = getValueFromConfigOrSystem(propertyName);
    if(strValue==null || strValue.isEmpty()) return defaultValue;
    return Integer.valueOf(strValue);
  }

  private static boolean getBoolean(String propertyName) {
    final String value = getValueFromConfigOrSystem(propertyName);
    if(value==null || value.isEmpty()) {
      return false;
    }
    if(value.toLowerCase().equals("true")) {
      return true;
    }
    return false;
  }



  public static void printConfiguration() {
    System.out.println("Configured as:");
    System.out.println("  Movie mode  : "+isMovieMode());
    System.out.println("  Bridge IP   : "+getServiceLocation());
  }

  private static java.util.Properties readProperties() {
    java.util.Properties p = new java.util.Properties();
    File configFile = new File(System.getProperty("user.home") + "/.homelights/config.properties");
    if(!configFile.exists()) {
      final String confFileName = System.getProperty(CONF_PROPERTY);
      if (confFileName == null || confFileName.isEmpty()) {
        return p;
      }
      configFile = new File(confFileName);
      if(!configFile.exists()) {
        return p;
      }
    }
    try {
      InputStream in = new FileInputStream(configFile);
      p.load(in);
      return p;
    }
    catch (IOException ex) {
      return p;
    }

  }
}
