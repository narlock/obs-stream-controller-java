package com.narlock;

import static org.slf4j.LoggerFactory.getLogger;

import io.obswebsocket.community.client.OBSRemoteController;
import io.obswebsocket.community.client.message.event.ui.StudioModeStateChangedEvent;
import io.obswebsocket.community.client.message.request.ui.GetStudioModeEnabledRequest;
import io.obswebsocket.community.client.message.response.ui.GetStudioModeEnabledResponse;
import org.slf4j.Logger;

public class Example {

  private static final Logger log = getLogger(Example.class);
  private OBSRemoteController obsRemoteController;
  private boolean isReconnecting = false;

  public static void main(String[] args) {
    new Example();
  }

  private Example() {
    this.createOBSRemoteController();

    // Connect
    this.obsRemoteController.connect();
  }

  private void createOBSRemoteController() {
    // Create OBSRemoteController through its builder
    this.obsRemoteController =
        OBSRemoteController.builder()
            .autoConnect(false) // Do not connect automatically
            .host("127.0.0.1") // Set the host
            .port(4455) // Set the port
            .password("6bJgCRfOani4YFdA") // Set the password
            .lifecycle() // Add some lifecycle callbacks
            .onReady(this::onReady) // Add onReady callback
            .and() // Build the LifecycleListenerBuilder
            .registerEventListener(
                StudioModeStateChangedEvent.class,
                this::onStudioModeStateChanged) // Register a StudioModeStateChangedEvent
            .build(); // Build the OBSRemoteController
  }

  private void onReady() {
    if (!this.isReconnecting) {
      // First connection
      this.onFirstConnection();
    } else {
      // Second connection
      this.onSecondConnection();
    }
  }

  private void onFirstConnection() {
    // Send a blocking call (last parameter is a timeout instead of a callback)
    this.obsRemoteController.setStudioModeEnabled(true, 1000);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException ignored) {
    }

    // Send a request through a convenience method
    this.obsRemoteController.getSceneList(
        getSceneListResponse -> {
          if (getSceneListResponse.isSuccessful()) {
            // Print each Scene
            getSceneListResponse.getScenes().forEach(scene -> log.info("Scene: {}", scene));
          }

          try {
            Thread.sleep(1000);
          } catch (InterruptedException ignored) {
          }

          this.obsRemoteController.setStudioModeEnabled(false, 1000);

          try {
            Thread.sleep(1000);
          } catch (InterruptedException ignored) {
          }

          this.disconnectAndReconnect();
        });
  }

  private void onSecondConnection() {
    // Send a Request through specific Request builder
    this.obsRemoteController.sendRequest(
        GetStudioModeEnabledRequest.builder().build(),
        (GetStudioModeEnabledResponse requestResponse) -> {
          if (requestResponse.isSuccessful()) {
            if (requestResponse.getStudioModeEnabled()) {
              log.info("Studio mode enabled");
            } else {
              log.info("Studio mode not enabled");
            }
          }

          try {
            Thread.sleep(1000);
          } catch (InterruptedException ignored) {
          }

          // Disconnect
          this.obsRemoteController.disconnect();
        });
  }

  private void disconnectAndReconnect() {
    // Disconnect
    this.obsRemoteController.disconnect();

    // Set a flag
    this.isReconnecting = true;

    //    // Recreate instance
    this.createOBSRemoteController();

    // Reconnect
    this.obsRemoteController.connect(); // onReady will be called another time
  }

  private void onStudioModeStateChanged(StudioModeStateChangedEvent studioModeStateChangedEvent) {
    log.info(
        "Studio Mode State Changed to: {}", studioModeStateChangedEvent.getStudioModeEnabled());
  }
}
