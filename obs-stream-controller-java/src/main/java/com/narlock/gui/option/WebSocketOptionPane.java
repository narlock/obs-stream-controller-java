package com.narlock.gui.option;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.narlock.Main;
import com.narlock.gui.Window;
import com.narlock.model.Settings;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;
import org.apache.commons.lang3.StringUtils;

public class WebSocketOptionPane {
  // io configurations
  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  public static Settings configureWebSocketSettings() throws IOException {
    // retrieve defaults
    InputStream defaultSettingsStream = Settings.class.getResourceAsStream("/defaultSettings.json");
    Settings settings = objectMapper.readValue(defaultSettingsStream, Settings.class);

    // show option pane
    showWebSocketOptionPane(settings);

    // return settings
    return settings;
  }

  public static void configureExistingWebSocketSettings() {
    // Disconnect if connected to OBS
    if(Main.connectedToOBS) {
      Window.obsController.disconnect();
    }

    // Show option pane
    showWebSocketOptionPane(Main.settings);
  }

  public static void showWebSocketOptionPane(Settings settings) {
    System.out.println("Settings " + settings);

    // Create a JPanel with a label
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    JLabel messageLabel =
        new JLabel("To find your settings, open OBS, go to Tools → WebSocket Server Settings");
    panel.add(messageLabel);

    JPanel connectionHostPanel = new JPanel();
    JLabel connectionHostLabel = new JLabel("OBS WebSocket IP");
    JTextField connectionHostTextField = new JTextField(10);
    connectionHostTextField.setText(settings.getConnection().getHost());
    connectionHostPanel.add(connectionHostLabel);
    connectionHostPanel.add(connectionHostTextField);
    panel.add(connectionHostPanel);

    JPanel connectionPortPanel = new JPanel();
    JLabel connectionPortLabel = new JLabel("OBS WebSocket Port");
    JTextField connectionPortTextField = new JTextField(10);
    connectionPortTextField.setText(String.valueOf(settings.getConnection().getPort()));
    connectionPortPanel.add(connectionPortLabel);
    connectionPortPanel.add(connectionPortTextField);
    panel.add(connectionPortPanel);

    JPanel connectionPasswordPanel = new JPanel();
    JLabel connectionPasswordLabel =
        new JLabel(
            "<html><span style='color:black'>OBS WebSocket Password</span><span style='color:red'>*</span></html>");
    JPasswordField connectionPasswordField = new JPasswordField(10);
    connectionPasswordField.setText(settings.getConnection().getPassword());
    JCheckBox showPasswordCheckBox = new JCheckBox("Show");
    char defaultEchoChar = connectionPasswordField.getEchoChar();
    showPasswordCheckBox.addActionListener(
        e -> {
          if (showPasswordCheckBox.isSelected()) {
            connectionPasswordField.setEchoChar((char) 0); // Show password
          } else {
            connectionPasswordField.setEchoChar(defaultEchoChar); // Hide password
          }
        });
    connectionPasswordPanel.add(connectionPasswordLabel);
    connectionPasswordPanel.add(connectionPasswordField);
    connectionPasswordPanel.add(showPasswordCheckBox);
    panel.add(connectionPasswordPanel);

    JPanel connectOnStartUpPanel = new JPanel();
    JCheckBox connectOnStartUpCheckBox = new JCheckBox("Connect to OBS on start");
    if(settings.getConnection().getConnectOnStartUp()) {
      connectOnStartUpCheckBox.setSelected(true);
    }
    connectOnStartUpPanel.add(connectOnStartUpCheckBox);
    panel.add(connectOnStartUpPanel);

    // Show the panel in a JOptionPane with an OK button
    JOptionPane.showMessageDialog(
        null, panel, "Setup OBS Stream Controller", JOptionPane.INFORMATION_MESSAGE);

    // set connection host if not empty
    if (!StringUtils.isEmpty(connectionHostTextField.getText())) {
      settings.getConnection().setHost(connectionHostTextField.getText());
    }

    // set connection port if not empty
    if (!StringUtils.isEmpty(connectionPortTextField.getText())) {
      try {
        settings.getConnection().setPort(Integer.parseInt(connectionPortTextField.getText()));
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    }

    // set password
    settings.getConnection().setPassword(new String(connectionPasswordField.getPassword()));

    // set to true if checked
    if (connectOnStartUpCheckBox.isSelected()) {
      settings.getConnection().setConnectOnStartUp(true);
    } else {
      settings.getConnection().setConnectOnStartUp(false);
    }

    settings.save();
  }
}
