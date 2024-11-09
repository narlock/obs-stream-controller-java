package com.narlock.gui.panel;

import com.narlock.Main;
import com.narlock.gui.Window;
import com.narlock.gui.option.SetTileOptionPane;
import com.narlock.model.Button;
import com.narlock.model.Tile;
import com.narlock.model.TileType;
import com.narlock.util.ImageUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.border.Border;

public class GridPanel extends JPanel {
  private static final Border BORDER = BorderFactory.createLineBorder(Color.BLACK, 1);

  public GridPanel() {
    // Default to Stream Mode
    toggleStreamMode();
  }

  public void toggleStreamMode() {
    // Remove all components and set layout
    this.removeAll();
    int gridRows = Main.settings.getButtonConfig().getGridSize().get(0);
    int gridCols = Main.settings.getButtonConfig().getGridSize().get(1);
    setLayout(new GridLayout(gridRows, gridCols));

    for (int x = 0; x < gridRows; x++) {
      for (int y = 0; y < gridCols; y++) {
        boolean buttonAdded = false;

        // check to see if a button in our list is in the current grid position
        for (Button configButton : Main.settings.getButtonConfig().getButtons()) {
          if (configButton.getGrid().get(0) == x && configButton.getGrid().get(1) == y) {
            try {
              add(getActionButton(configButton));
              buttonAdded = true;
              break;
            } catch (RuntimeException e) {
              System.out.println("Warning: button config removed since the tile does not exist");
            }
          }
        }

        // if not,found, add a blank panel to the grid
        if (!buttonAdded) {
          JPanel spacePanel = new JPanel();
          spacePanel.setBorder(BORDER);
          add(spacePanel);
        }
      }
    }

    this.validate();
    this.repaint();
  }

  public JButton getActionButton(Button configButton) {
    Tile tile;
    try {
      tile = Main.settings.getTileByName(configButton.getTileName());
    } catch (RuntimeException e) {
      Main.settings.getButtonConfig().getButtons().remove(configButton);
      Main.settings.save();
      throw new RuntimeException(e);
    }

    JButton actionButton = new JButton();

    // Create the initial image and set the icon
    Image originalImage = ImageUtils.readImage(tile.getImagePath());
    ImageIcon icon = new ImageIcon(originalImage);
    actionButton.setIcon(icon);

    // Add a ComponentListener to resize the icon when the button size changes
    actionButton.addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            // Get the new button dimensions
            int width = actionButton.getWidth();
            int height = actionButton.getHeight();

            // Scale the original image to fit the button size
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            // Set the new icon with the scaled image
            actionButton.setIcon(new ImageIcon(scaledImage));
          }
        });

    if (tile.getType().equals(TileType.SWITCH_SCENE)) {
      actionButton.addActionListener(e -> Window.obsController.switchScene(tile.getScene()));
      actionButton.setBorder(BORDER);
      return actionButton;
    }

    throw new RuntimeException(
        "Invalid configButton tile type received: " + configButton.getTileName());
  }

  public void toggleEditMode() {
    this.removeAll();
    int gridRows = Main.settings.getButtonConfig().getGridSize().get(0);
    int gridCols = Main.settings.getButtonConfig().getGridSize().get(1);
    setLayout(new GridLayout(gridRows, gridCols));

    for (int x = 0; x < gridRows; x++) {
      for (int y = 0; y < gridCols; y++) {
        boolean buttonAdded = false;

        // check to see if a button in our list is in the current grid position
        for (Button configButton : Main.settings.getButtonConfig().getButtons()) {
          if (configButton.getGrid().get(0) == x && configButton.getGrid().get(1) == y) {
            try {
              add(getActionEditButton(configButton.getTileName(), x, y));
              buttonAdded = true;
              break;
            } catch (RuntimeException e) {
              System.out.println("Warning: button config removed since the tile does not exist");
            }
          }
        }

        // if not,found, add a blank panel to the grid
        if (!buttonAdded) {
          add(getActionEditButton("No Button", x, y));
        }
      }
    }

    this.validate();
    this.repaint();
  }

  public JButton getActionEditButton(String name, int x, int y) {
    JButton actionButton = new JButton(name);
    actionButton.setBorder(BORDER);

    actionButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            SetTileOptionPane.show(x, y);
            toggleEditMode();
          }
        });

    return actionButton;
  }
}
