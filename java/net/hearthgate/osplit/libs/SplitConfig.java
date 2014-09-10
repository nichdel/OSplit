package net.hearthgate.osplit.libs;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SplitConfig {

  // Timer controls
  public static final String TIMER_START = "timer.start";
  public static final String TIMER_ADVANCE = "timer.advance";
  public static final String TIMER_STOP = "timer.stop";
  public static final String TIMER_PAUSE = "timer.pause";

  // Menu controls
  public static final String MENU_HIDE = "menu.hide";

  private static Properties properties;

  /**
   * Set the defaults, then overwrite them with the settings in the config file.
   */
  public static void readConfig(String fileName) throws IOException {
    properties = new Properties();

    setDefaults();

    properties.load(new FileInputStream(fileName));
  }

  public static void setDefaults() {
    // Timer controls
    properties.setProperty(TIMER_START,"control shift S");
    properties.setProperty(TIMER_ADVANCE,"control shift A");
    properties.setProperty(TIMER_STOP,"control shift X");
    properties.setProperty(TIMER_PAUSE,"control shift D");

    // Menu controls
    properties.setProperty(MENU_HIDE,"alt 0");
  }

  public static String getProperty(String property_name) {
    return properties.getProperty(property_name);
  }
}
