package com.narlock.gui.option;

import com.narlock.Main;
import com.narlock.gui.Window;
import com.narlock.model.Tile;
import com.narlock.model.TileType;
import com.narlock.util.ImageUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

public class CreateTileOptionPane {
  public static final String SWITCH_SCENE = "Switch Scene";

  public static JPanel rootPanel = new JPanel();
  public static JLabel actionLabel = new JLabel("Action");
  public static JComboBox<String> typeComboBox;
  public static JPanel optionPanel = new JPanel();
  public static String imagePath = null;

  public static void show() {
    // Reset rootPane on show
    rootPanel.setPreferredSize(new Dimension(400, 300));
    rootPanel.setLayout(new GridLayout(2, 1));
    rootPanel.removeAll();

    // Initialize rootPane components
    JPanel topPanel = new JPanel();
    typeComboBox = new JComboBox<>();
    typeComboBox.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            try {
              addComponentsByTileType();
            } catch (InterruptedException ex) {
              throw new RuntimeException(ex);
            }
          }
        });

    // add tile options
    typeComboBox.addItem("Select Action");
    typeComboBox.addItem(SWITCH_SCENE);

    // add top panel components
    topPanel.add(actionLabel);
    topPanel.add(typeComboBox);

    // create option panel
    JPanel centerPanel = new JPanel();
    centerPanel.add(optionPanel);

    // show the scene
    rootPanel.add(topPanel);
    rootPanel.add(centerPanel);

    int result =
        JOptionPane.showConfirmDialog(null, rootPanel, "Create Tile", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
      // Handle OK option

    }
  }

  public static void addComponentsByTileType() throws InterruptedException {
    Object selectedItem = typeComboBox.getSelectedItem();
    imagePath = null;

    if (selectedItem instanceof String && selectedItem.equals(SWITCH_SCENE)) {
      // open dialog for switching scene
      System.out.println("Switch Scene");

      JLabel label = new JLabel("Switch to scene");
      JButton saveButton = new JButton("Save to disk");
      List<String> scenes = Window.obsController.getScenes();
      Thread.sleep(1000);

      JButton imageButton = new JButton();

      Image originalImage = ImageUtils.readImage(null);
      ImageIcon icon = new ImageIcon(originalImage);
      imageButton.setIcon(icon);

      imageButton.addActionListener(
          new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              // Open file menu dialog that will store path of exercise
              JFileChooser fileChooser = new JFileChooser();
              int returnValue = fileChooser.showOpenDialog(Main.window);

              if (returnValue == JFileChooser.APPROVE_OPTION) {
                // Input validation
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (filePath.endsWith(".png")
                    || filePath.endsWith(".jpg")
                    || filePath.endsWith(".jpeg")) {
                  System.out.println(
                      "[ManageExercisesPanel.editExercise.imageButton] Valid file chosen: "
                          + filePath);
                  // Set the image path
                  imagePath = filePath;
                  imageButton.setIcon(
                      new ImageIcon(ImageUtils.scaleImage(ImageUtils.readImage(filePath), 64)));
                } else {
                  // Print an error dialog
                  JOptionPane.showMessageDialog(
                      Main.window, "Invalid file type provided. Supports: .png, .jpg, .jpeg");
                  throw new RuntimeException("Invalid file type");
                }
              }
            }
          });

      JComboBox<String> sceneComboBox = new JComboBox<>(scenes.toArray(new String[0]));
      scenes.sort(String::compareTo);
      saveButton.addActionListener(
          e -> {
            Tile tile = new Tile();
            tile.setName("Switch to " + sceneComboBox.getSelectedItem());
            tile.setType(TileType.SWITCH_SCENE);
            tile.setScene((String) sceneComboBox.getSelectedItem());
            tile.setImagePath(imagePath);
            Main.settings.getTiles().add(tile);
            Main.settings.save();
          });
      optionPanel.add(label);
      optionPanel.add(sceneComboBox);
      optionPanel.add(imageButton);
      optionPanel.add(saveButton);

      rootPanel.repaint();
      rootPanel.revalidate();

    } else {
      System.out.println("Other type received " + selectedItem);
      optionPanel.removeAll();
    }
  }
}
