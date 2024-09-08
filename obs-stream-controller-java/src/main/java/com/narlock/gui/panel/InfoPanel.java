package com.narlock.gui.panel;

import com.narlock.Main;

import javax.swing.*;

public class InfoPanel extends JPanel {


  public String connectedString;
  public JLabel connectedToOBSLabel;

  public InfoPanel() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    add(new JLabel("Information"));

    // Add connection information
    connectedToOBSLabel =
        new JLabel(
            "<html><span style='color:black;'>OBS: </span><span style='color:red;'>Not connected</span></html>");
    add(connectedToOBSLabel);
  }

  public void setConnectedToOBS(boolean connected) {
    Main.connectedToOBS = connected;
    connectedString =
        connected
            ? "<span style='color:green;'>Connected</span>"
            : "<span style='color:red;'>Not connected</span>";
    connectedToOBSLabel.setText(
        "<html><span style='color:black;'>OBS: </span>" + connectedString + "</html>");

    if(connected) {
      // If we are connected, disable connect command
      Main.window.connectToOBSMenuItem.setEnabled(false);
      Main.window.disconnectToOBSMenuItem.setEnabled(true);
    } else {
      Main.window.connectToOBSMenuItem.setEnabled(true);
      Main.window.disconnectToOBSMenuItem.setEnabled(false);
    }
  }
}
