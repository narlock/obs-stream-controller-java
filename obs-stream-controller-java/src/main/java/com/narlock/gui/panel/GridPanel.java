package com.narlock.gui.panel;

import com.narlock.Main;
import com.narlock.gui.Window;
import com.narlock.model.Button;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    System.out.println("gridRows: " + gridRows + ", gridCols: " + gridCols);
    setLayout(new GridLayout(gridRows, gridCols));

    for(int x = 0; x < gridRows; x++) {
      for(int y = 0; y < gridCols; y++) {
        boolean buttonAdded = false;

        // check to see if a button in our list is in the current grid position
        for(Button configButton : Main.settings.getButtonConfig().getButtons()) {
          if(configButton.getGrid().get(0) == x && configButton.getGrid().get(1) == y) {
            add(getActionButton(configButton));
            buttonAdded = true;
            break;
          }
        }

        // if not,found, add a blank panel to the grid
        if(!buttonAdded) {
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
    if(configButton.getType().equals("switchScene")) {
      JButton actionButton = new JButton("Switch Scene to " + configButton.getScene());
      actionButton.addActionListener(e -> Window.obsController.switchScene(configButton.getScene()));
      actionButton.setBorder(BORDER);
      return actionButton;
    }

    throw new RuntimeException("Invalid configButton type received: " + configButton.getType());
  }

  public void toggleEditMode() {
    this.removeAll();

    JButton button = new JButton("test");
    button.setBorder(BORDER);
    button.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.out.println("test");
          }
        });
    JButton button1 = new JButton("test");
    button1.setBorder(BORDER);
    JButton button2 = new JButton("test");
    button2.setBorder(BORDER);
    JButton button3 = new JButton("test");
    button3.setBorder(BORDER);

    this.add(button);
    this.add(button1);
    this.add(button2);
    this.add(button3);

    this.validate();
    this.repaint();
  }
}
