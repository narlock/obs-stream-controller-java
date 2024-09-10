package com.narlock.model;

import static com.narlock.util.Constants.SETTINGS_PATH;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.narlock.gui.option.WebSocketOptionPane;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.*;

public class Settings {
  // io configurations
  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  // attributes
  private List<Integer> screenSize;
  private Boolean lockScreenSize;
  private Boolean askAreYouSureOnWindowExit;
  private Integer controllerSide; // Side to put the controller: 0 for WEST, 1 for EAST
  private Connection connection;
  private ButtonConfig buttonConfig;
  private List<Tile> tiles;

  // mutators and accessors
  public List<Integer> getScreenSize() {
    return this.screenSize;
  }

  public void setScreenSize(List<Integer> screenSize) {
    this.screenSize = screenSize;
  }

  public Boolean getLockScreenSize() {
    return lockScreenSize;
  }

  public void setLockScreenSize(Boolean lockScreenSize) {
    this.lockScreenSize = lockScreenSize;
  }

  public Boolean getAskAreYouSureOnWindowExit() {
    return this.askAreYouSureOnWindowExit;
  }

  public void setAskAreYouSureOnWindowExit(Boolean askAreYouSureOnWindowExit) {
    this.askAreYouSureOnWindowExit = askAreYouSureOnWindowExit;
  }

  public Integer getControllerSide() {
    return this.controllerSide;
  }

  public void setControllerSide(Integer controllerSide) {
    this.controllerSide = controllerSide;
  }

  public Connection getConnection() {
    return this.connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public ButtonConfig getButtonConfig() {
    return this.buttonConfig;
  }

  public void setButtonConfig(ButtonConfig buttonConfig) {
    this.buttonConfig = buttonConfig;
  }

  public List<Tile> getTiles() {
    return tiles;
  }

  public void setTiles(List<Tile> tiles) {
    this.tiles = tiles;
  }

  /**
   * Retrieves a Tile from tiles matching tileName parameter.
   * @param tileName
   * @return tile matching tileName
   */
  public Tile getTileByName(String tileName) {
    for(Tile tile : tiles) {
      if(tile.getName().equals(tileName)) {
        return tile;
      }
    }

    throw new RuntimeException("No tile was found with name " + tileName);
  }

  /**
   * Loads settings from the settings.json file. Will create a new settings file if the directory or
   * file(s) are not present.
   *
   * @return settings
   */
  public static Settings load() {
    // Check if there is a settings file
    File file = new File(SETTINGS_PATH);
    try {
      // Ensure the parent directory exists
      File parentDir = file.getParentFile();
      if (!parentDir.exists()) {
        parentDir.mkdirs(); // Create the directory if it does not exist
      }

      // If the file exists, map to Settings
      if (file.exists()) {
        System.out.println("Settings file exists");
        return objectMapper.readValue(file, Settings.class);
      }
      // If the file does not exist, lets create it based on resources/defaultSettings.json
      else {
        System.out.println("Settings file does not exist");

        // Initiate configure web socket settings
        return WebSocketOptionPane.configureWebSocketSettings();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Should not reach this branch
    throw new RuntimeException("An unexpected error occurred");
  }

  /** Saves the settings object to the settings path. */
  public void save() {
    File file = new File(SETTINGS_PATH);
    try {
      objectMapper.writeValue(file, this);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public class Connection {
    private String host;
    private Integer port;
    private String password;
    private Boolean connectOnStartUp;

    public Connection() {}

    public Connection(String host, Integer port, String password, Boolean connectOnStartUp) {
      this.host = host;
      this.port = port;
      this.password = password;
      this.connectOnStartUp = connectOnStartUp;
    }

    public String getHost() {
      return this.host;
    }

    public void setHost(String host) {
      this.host = host;
    }

    public Integer getPort() {
      return this.port;
    }

    public void setPort(Integer port) {
      this.port = port;
    }

    public String getPassword() {
      return this.password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public Boolean getConnectOnStartUp() {
      return this.connectOnStartUp;
    }

    public void setConnectOnStartUp(Boolean connectOnStartUp) {
      this.connectOnStartUp = connectOnStartUp;
    }

    @Override
    public String toString() {
      return "Connection{"
          + "host='"
          + host
          + '\''
          + ", port="
          + port
          + ", password='"
          + password
          + '\''
          + ", connectOnStartUp="
          + connectOnStartUp
          + '}';
    }
  }

  @Override
  public String toString() {
    return "Settings["
        + "screenSize="
        + screenSize
        + ", "
        + "askAreYouSureOnWindowExit="
        + askAreYouSureOnWindowExit
        + ", "
        + "connection="
        + connection;
  }
}
