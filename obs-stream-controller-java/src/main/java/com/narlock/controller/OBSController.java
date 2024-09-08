package com.narlock.controller;

import com.narlock.Main;
import io.obswebsocket.community.client.OBSRemoteController;
import io.obswebsocket.community.client.message.request.scenes.GetSceneListRequest;
import io.obswebsocket.community.client.message.request.scenes.SetCurrentProgramSceneRequest;
import io.obswebsocket.community.client.message.response.scenes.GetSceneListResponse;
import io.obswebsocket.community.client.message.response.scenes.SetCurrentProgramSceneResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class OBSController {
  private static final Logger log = LoggerFactory.getLogger(OBSController.class);
  private OBSRemoteController obsRemoteController;

  public OBSController() {
    obsRemoteController =
        OBSRemoteController.builder()
            .host(Main.settings.getConnection().getHost())
            .port(Main.settings.getConnection().getPort())
            .password(Main.settings.getConnection().getPassword())
            .lifecycle()
            .onConnect(session -> Main.window.infoPanel.setConnectedToOBS(true))
            .onDisconnect(() -> Main.window.infoPanel.setConnectedToOBS(false))
            .onReady(() -> System.out.println("OBS WebSocket is ready to accept requests"))
            .and()
            .build();
  }

  public void resetObsRemoteController() {
      obsRemoteController = OBSRemoteController.builder()
              .host(Main.settings.getConnection().getHost())
              .port(Main.settings.getConnection().getPort())
              .password(Main.settings.getConnection().getPassword())
              .lifecycle()
              .onConnect(session -> Main.window.infoPanel.setConnectedToOBS(true))
              .onDisconnect(() -> Main.window.infoPanel.setConnectedToOBS(false))
              .onReady(() -> System.out.println("OBS WebSocket is ready to accept requests"))
              .and()
              .build();
  }

  /** Connects to the OBS web socket */
  public void connect() {
    obsRemoteController.connect();
  }

  /** Disconnects from the OBS web socket */
  public void disconnect() {
    obsRemoteController.disconnect();
  }

  public void getScenes() {
    obsRemoteController.sendRequest(
        GetSceneListRequest.builder().build(),
        (GetSceneListResponse response) -> {
          if (response.isSuccessful()) {
            response
                .getScenes()
                .forEach(
                    scene -> {
                      System.out.println("Scene: " + scene.getSceneName());
                    });
          } else {
            System.err.println("Failed to get scene list");
          }
        });
  }

  public void switchScene(String sceneName) {
      if(!Main.connectedToOBS) {
          JOptionPane.showMessageDialog(null,
                  "Operation failed. Currently not connected to OBS.",
                  "Connection Error",
                  JOptionPane.ERROR_MESSAGE);
          return;
      }

    obsRemoteController.sendRequest(
        SetCurrentProgramSceneRequest.builder().sceneName(sceneName).build(),
        (SetCurrentProgramSceneResponse response) -> {
          if (response.isSuccessful()) {
            System.out.println("Successful switched OBS scene to " + sceneName);
          } else {
            System.err.println("Failed to switch OBS scene to " + sceneName);
          }
        });
  }
}
