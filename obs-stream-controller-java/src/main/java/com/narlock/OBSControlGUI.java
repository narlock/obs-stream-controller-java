package com.narlock;

import io.obswebsocket.community.client.OBSRemoteController;
import io.obswebsocket.community.client.message.request.scenes.SetCurrentProgramSceneRequest;
import io.obswebsocket.community.client.message.response.scenes.SetCurrentProgramSceneResponse;
import javax.swing.*;

public class OBSControlGUI extends JFrame {

  private OBSRemoteController controller;

  public OBSControlGUI() {
    // Set up WebSocket connection to OBS
    connectToOBS();

    // Create GUI
    setTitle("OBS Controller");
    setSize(300, 100);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel panel = new JPanel();

    // Button to switch to Scene 1
    JButton button1 = new JButton("Switch to Scene");
    button1.addActionListener(e -> switchScene("Scene"));
    panel.add(button1);

    // Button to switch to Scene 2
    JButton button2 = new JButton("Switch to MyScene");
    button2.addActionListener(e -> switchScene("MyScene"));
    panel.add(button2);

    add(panel);
  }

  private void connectToOBS() {
    controller =
        OBSRemoteController.builder()
            .host("192.168.0.35") // Default host
            .port(4455) // Default port
            .password("6bJgCRfOani4YFdA") // Set OBS password here
            .lifecycle()
            .onReady(() -> System.out.println("OBS WebSocket is ready to accept requests"))
            .and()
            .build();

    controller.connect(); // Connect to OBS WebSocket
  }

  private void switchScene(String sceneName) {
    controller.sendRequest(
        SetCurrentProgramSceneRequest.builder().sceneName(sceneName).build(),
        (SetCurrentProgramSceneResponse response) -> {
          if (response.isSuccessful()) {
            System.out.println("successful");
          } else {
            System.out.println("Not successful");
          }
        });
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          OBSControlGUI gui = new OBSControlGUI();
          gui.setVisible(true);
        });
  }
}
