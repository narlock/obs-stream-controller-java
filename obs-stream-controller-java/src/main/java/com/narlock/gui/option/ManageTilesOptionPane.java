package com.narlock.gui.option;

import com.narlock.Main;
import com.narlock.model.Tile;
import java.util.List;
import javax.swing.*;

public class ManageTilesOptionPane {
  public static JPanel rootPanel = new JPanel();

  public static void show() {
    rootPanel.add(createManageInterface());

    JOptionPane.showMessageDialog(null, rootPanel, "Manage Tiles", JOptionPane.INFORMATION_MESSAGE);
  }

  public static JComboBox<Tile> refreshTile() {
    List<Tile> tiles = Main.settings.getTiles();
    JComboBox<Tile> tileComboBox = new JComboBox<>();
    for (Tile tile : tiles) {
      tileComboBox.addItem(tile);
    }
    return tileComboBox;
  }

  public static JPanel createManageInterface() {
    // add tiles to combo box
    JComboBox<Tile> tileComboBox = refreshTile();

    // create JPanel
    JPanel panel = new JPanel();
    if (tileComboBox.getItemCount() > 0) {
      panel.add(tileComboBox);
      JButton deleteButton = new JButton("Delete Tile");
      deleteButton.addActionListener(
          e -> {
            Main.settings.getTiles().remove((Tile) tileComboBox.getSelectedItem());
            Main.settings.save();
            rootPanel.remove(panel);

            rootPanel.validate();
            rootPanel.repaint();

            rootPanel.add(createManageInterface());

            rootPanel.validate();
            rootPanel.repaint();
          });
      panel.add(deleteButton);
    } else {
      panel.add(new JLabel("No tiles"));
    }

    return panel;
  }
}
