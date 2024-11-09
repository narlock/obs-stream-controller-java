package com.narlock.gui;

import static com.narlock.util.Constants.VERSION;
import static com.narlock.util.Constants.WINDOW_TITLE;

import com.narlock.Main;
import com.narlock.controller.OBSController;
import com.narlock.gui.option.CreateTileOptionPane;
import com.narlock.gui.option.ManageTilesOptionPane;
import com.narlock.gui.option.WebSocketOptionPane;
import com.narlock.gui.panel.GridPanel;
import com.narlock.gui.panel.InfoPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class Window extends JFrame {

  public static OBSController obsController;

  // panels
  public GridPanel gridPanel;
  public InfoPanel infoPanel = new InfoPanel();
  ;

  // menu options
  private JMenuItem exportSettingsMenuItem;
  private JMenuItem importSettingsMenuItem;
  private JMenuItem toggleLockScreenSizeMenuItem;
  private JMenuItem streamModeMenuItem;
  private JMenuItem editModeMenuItem;
  private JMenuItem switchInfoPanelSideMenuItem;
  private JMenuItem connectionSettingsMenuItem;
  public JMenuItem connectToOBSMenuItem;
  public JMenuItem disconnectToOBSMenuItem;
  private JMenuItem manageTilesMenuItem;
  private JMenuItem newTileMenuItem;

  public Window() {
    // initialize obs controller
    obsController = new OBSController();

    // initialize window
    configureWindow();

    // initialize window components
    initializeWindowMenu();
    initializeInfoPanel();
    initializeGridPanel();

    // display window
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public void configureWindow() {
    setLayout(new BorderLayout());
    setTitle(WINDOW_TITLE + " • " + VERSION);
    setSize(Main.settings.getScreenSize().get(0), Main.settings.getScreenSize().get(1));
    setResizable(!Main.settings.getLockScreenSize());
    if (Main.settings.getAskAreYouSureOnWindowExit()) {
      addWindowListener(makeExitWindowListener());
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    } else {
      setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Add a ComponentListener to listen for resize events
    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            // Get the size of the window
            int width = getWidth();
            int height = getHeight();

            // Save settings
            Main.settings.setScreenSize(List.of(width, height));
            Main.settings.save();
          }
        });
  }

  public void initializeWindowMenu() {
    JMenuBar menuBar = new JMenuBar();

    // file menu
    JMenu appMenu = new JMenu("App");
    exportSettingsMenuItem = new JMenuItem("Export Settings");
    // TODO
    exportSettingsMenuItem.setEnabled(false);
    importSettingsMenuItem = new JMenuItem("Import Settings");
    // TODO
    importSettingsMenuItem.setEnabled(false);
    JMenu resolutionMenu = new JMenu("Set Resolution");
    JMenuItem resolution640x480 = new JMenuItem("640 x 480");
    resolution640x480.addActionListener(e -> setSize(640, 480));
    JMenuItem resolution800x600 = new JMenuItem("800 x 600");
    resolution800x600.addActionListener(e -> setSize(800, 600));
    JMenuItem resolution1024x768 = new JMenuItem("1024 x 768");
    resolution1024x768.addActionListener(e -> setSize(1024, 768));
    JMenuItem resolution1280x720 = new JMenuItem("1280 x 720 (Default)");
    resolution1280x720.addActionListener(e -> setSize(1280, 720));
    resolutionMenu.add(resolution640x480);
    resolutionMenu.add(resolution800x600);
    resolutionMenu.add(resolution1024x768);
    resolutionMenu.add(resolution1280x720);
    toggleLockScreenSizeMenuItem =
        new JMenuItem(
            Main.settings.getLockScreenSize() ? "Unlock Screen Size" : "Lock Screen Size");
    configureFileMenuActions();
    appMenu.add(exportSettingsMenuItem);
    appMenu.add(importSettingsMenuItem);
    appMenu.add(resolutionMenu);
    appMenu.add(toggleLockScreenSizeMenuItem);
    menuBar.add(appMenu);

    // view menu
    JMenu viewMenu = new JMenu("View");
    streamModeMenuItem = new JMenuItem("Stream Mode");
    editModeMenuItem = new JMenuItem("Edit Mode");
    switchInfoPanelSideMenuItem = new JMenuItem("Switch Menu Item Side");
    configureViewMenuActions();
    viewMenu.add(streamModeMenuItem);
    viewMenu.add(editModeMenuItem);
    viewMenu.add(switchInfoPanelSideMenuItem);
    menuBar.add(viewMenu);

    // obs menu
    JMenu obsMenu = new JMenu("OBS");
    connectionSettingsMenuItem = new JMenuItem("OBS WebSocket Config");
    connectToOBSMenuItem = new JMenuItem("Connect to OBS");
    disconnectToOBSMenuItem = new JMenuItem("Disconnect from OBS");
    configureOBSMenuActions();
    obsMenu.add(connectionSettingsMenuItem);
    obsMenu.add(connectToOBSMenuItem);
    obsMenu.add(disconnectToOBSMenuItem);
    menuBar.add(obsMenu);

    // tile menu
    JMenu tileMenu = new JMenu("Tile");
    manageTilesMenuItem = new JMenuItem("Manage Tiles");
    newTileMenuItem = new JMenuItem("Create Tile");
    configureTileMenuActions();
    tileMenu.add(manageTilesMenuItem);
    tileMenu.add(newTileMenuItem);
    menuBar.add(tileMenu);

    this.add(menuBar, BorderLayout.NORTH);
  }

  public void configureFileMenuActions() {
    toggleLockScreenSizeMenuItem.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            if (!Main.settings.getLockScreenSize()) {
              Main.settings.setLockScreenSize(true);
              Main.window.setResizable(false);
              toggleLockScreenSizeMenuItem.setText("Unlock Screen Size");
              Main.settings.save();
            } else {
              Main.settings.setLockScreenSize(false);
              Main.window.setResizable(true);
              toggleLockScreenSizeMenuItem.setText("Lock Screen Size");
              Main.settings.save();
            }
          }
        });
  }

  public void configureViewMenuActions() {
    streamModeMenuItem.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            gridPanel.toggleStreamMode();
          }
        });

    editModeMenuItem.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            gridPanel.toggleEditMode();
          }
        });

    switchInfoPanelSideMenuItem.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            Main.settings.setControllerSide(Main.settings.getControllerSide() == 0 ? 1 : 0);
            Main.settings.save();

            remove(infoPanel);
            initializeInfoPanel();
            validate();
            repaint();
          }
        });
  }

  public void configureOBSMenuActions() {
    connectionSettingsMenuItem.addActionListener(
        e -> {
          WebSocketOptionPane.configureExistingWebSocketSettings();
          obsController.resetObsRemoteController();
        });
    connectToOBSMenuItem.addActionListener(
        e -> {
          obsController.connect();
          try {
            connectToOBSMenuItem.setEnabled(false);
            connectToOBSMenuItem.setText("Attempting to connect to OBS...");
            Thread.sleep(1000);
            if (!Main.connectedToOBS) {
              JOptionPane.showMessageDialog(
                  Main.window,
                  "Failed to connect to OBS. Please check configurations.",
                  "Connection Error",
                  JOptionPane.ERROR_MESSAGE);
              connectToOBSMenuItem.setEnabled(true);
            }
            connectToOBSMenuItem.setText("Connect to OBS");
          } catch (InterruptedException ex) {
            ex.printStackTrace();
          }
        });
    disconnectToOBSMenuItem.addActionListener(
        e -> {
          obsController.disconnect();
        });

    if (Main.connectedToOBS) {
      connectToOBSMenuItem.setEnabled(false);
      disconnectToOBSMenuItem.setEnabled(true);
    } else {
      connectToOBSMenuItem.setEnabled(true);
      disconnectToOBSMenuItem.setEnabled(false);
    }
  }

  public void configureTileMenuActions() {
    manageTilesMenuItem.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            ManageTilesOptionPane.show();
            gridPanel.toggleStreamMode();
          }
        });
    newTileMenuItem.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            CreateTileOptionPane.show();
          }
        });
  }

  public void initializeInfoPanel() {
    // set side
    if (Main.settings.getControllerSide() == 0) {
      add(infoPanel, BorderLayout.WEST);
    } else {
      add(infoPanel, BorderLayout.EAST);
    }
  }

  public void initializeGridPanel() {
    gridPanel = new GridPanel();
    add(gridPanel, BorderLayout.CENTER);
  }

  public WindowListener makeExitWindowListener() {
    return new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Are you sure you want to exit?");
        label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        JPanel confirmPanel = new JPanel();
        confirmPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        JCheckBox checkBox = new JCheckBox();
        JLabel confirmLabel = new JLabel("Don't show this option again");

        panel.add(label);
        confirmPanel.add(checkBox);
        confirmPanel.add(confirmLabel);
        panel.add(confirmPanel);

        // Ensure if it was changed in settings it closes
        if (!Main.settings.getAskAreYouSureOnWindowExit()) {
          System.exit(0);
        }

        int confirm =
            JOptionPane.showOptionDialog(
                rootPane,
                panel,
                "Exit?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if (confirm == JOptionPane.YES_OPTION) {
          if (checkBox.isSelected()) {
            Main.settings.setAskAreYouSureOnWindowExit(false);
            Main.settings.save();
          }
          System.exit(0);
        }
      }
    };
  }
}
