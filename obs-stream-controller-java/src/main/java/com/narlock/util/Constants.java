package com.narlock.util;

import java.io.File;

public class Constants {
  public static final String WINDOW_TITLE = "OBS Stream Controller";
  public static final String VERSION = "v1.0.0";
  public static final String SETTINGS_PATH =
      System.getProperty("user.home")
          + File.separatorChar
          + "Documents"
          + File.separatorChar
          + "narlock"
          + File.separatorChar
          + "obs-controller"
          + File.separatorChar
          + "settings.json";
}
