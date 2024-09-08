package com.narlock;

import com.narlock.gui.Window;
import com.narlock.model.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Main {
  private static final Logger log = LoggerFactory.getLogger(Main.class);
  public static Settings settings;
  public static Window window;
  public static boolean connectedToOBS;

  public static void main(String[] args) throws InterruptedException {
    // Load settings
    settings = Settings.load();
    log.info("Settings loaded {}", settings);

    // Instantiate the OBS Controller window
    window = new Window();
    log.info("Window successfully initialized");

    // establish connection if setting is enabled
    if (Main.settings.getConnection().getConnectOnStartUp()) {
      Window.obsController.connect();
      Thread.sleep(1000);

      // Show error message if not connected
      if(!Main.connectedToOBS) {
        JOptionPane.showMessageDialog(null,
                "Failed to connect to OBS. Please check configurations and that OBS is open.",
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}
