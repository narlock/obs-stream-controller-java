package com.narlock.controller;

import com.narlock.Main;
import io.obswebsocket.community.client.OBSRemoteController;
import io.obswebsocket.community.client.message.request.scenes.GetCurrentProgramSceneRequest;
import io.obswebsocket.community.client.message.request.scenes.GetSceneListRequest;
import io.obswebsocket.community.client.message.request.scenes.SetCurrentProgramSceneRequest;
import io.obswebsocket.community.client.message.response.scenes.GetCurrentProgramSceneResponse;
import io.obswebsocket.community.client.message.response.scenes.GetSceneListResponse;
import io.obswebsocket.community.client.message.response.scenes.SetCurrentProgramSceneResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  /** Connects to the OBS web socket */
  public void connect() {
    obsRemoteController.connect();
  }

  /** Disconnects from the OBS web socket */
  public void disconnect() {
    obsRemoteController.disconnect();
  }

  //  public void getScenes() {
  //    obsRemoteController.sendRequest(
  //        GetSceneListRequest.builder().build(),
  //        (GetSceneListResponse response) -> {
  //          if (response.isSuccessful()) {
  //            response
  //                .getScenes()
  //                .forEach(
  //                    scene -> {
  //                      System.out.println("Scene: " + scene.getSceneName());
  //                    });
  //          } else {
  //            System.err.println("Failed to get scene list");
  //          }
  //        });
  //  }

  public List<String> getScenes() {
    List<String> scenes = new ArrayList<>();
    obsRemoteController.sendRequest(
        GetSceneListRequest.builder().build(),
        (GetSceneListResponse response) -> {
          if (response.isSuccessful()) {
            response.getScenes().forEach(scene -> scenes.add(scene.getSceneName()));
          } else {
            System.err.println("Failed to get scene list");
          }
        });
    return scenes;
  }

  public void switchScene(String sceneName) {
    if (!Main.connectedToOBS) {
      JOptionPane.showMessageDialog(
          Main.window,
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

  /** Gets the currently active OBS scene */
  public CompletableFuture<String> getCurrentScene() {
    CompletableFuture<String> futureSceneName = new CompletableFuture<>();

    if (!Main.connectedToOBS) {
      JOptionPane.showMessageDialog(
          Main.window,
          "Operation failed. Currently not connected to OBS.",
          "Connection Error",
          JOptionPane.ERROR_MESSAGE);
      futureSceneName.completeExceptionally(new RuntimeException("Not connected to OBS"));
      return futureSceneName;
    }

    obsRemoteController.sendRequest(
        GetCurrentProgramSceneRequest.builder().build(),
        (GetCurrentProgramSceneResponse response) -> {
          if (response.isSuccessful()) {
            futureSceneName.complete(response.getCurrentProgramSceneName());
          } else {
            futureSceneName.completeExceptionally(
                new RuntimeException("Failed to retrieve current active OBS scene"));
          }
        });

    return futureSceneName;
  }
}
