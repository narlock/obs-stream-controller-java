package com.narlock.gui.option;

import com.narlock.Main;
import com.narlock.model.Tile;
import java.util.List;
import java.util.Objects;
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
    // Remove all components to ensure integrity
    rootPanel.removeAll();

    // add tiles to combo box
    JComboBox<Tile> tileComboBox = refreshTile();

    // create JPanel
    JPanel panel = new JPanel();
    if (tileComboBox.getItemCount() > 0) {
      panel.add(tileComboBox);
      JButton deleteButton = new JButton("Delete Tile");
      deleteButton.addActionListener(
          e -> {
            Tile tileToDelete = (Tile) tileComboBox.getSelectedItem();
            System.out.println("Inside of delete for: " + tileToDelete.getName());

            // Delete type from tiles list
            Main.settings.getTiles().remove((Tile) tileComboBox.getSelectedItem());

            // Remove button instances of tile
            Main.settings.getButtonConfig().getButtons().stream()
                .filter(Objects::nonNull)
                .filter(
                    existingButton ->
                        existingButton.getTileName() != null
                            && existingButton.getTileName().equals(tileToDelete.getName()))
                .findFirst()
                .ifPresent(Main.settings.getButtonConfig().getButtons()::remove);

            // Save
            Main.settings.save();

            // Remove from current panel
            rootPanel.remove(panel);
          });
      panel.add(deleteButton);
    } else {
      panel.add(new JLabel("No tiles"));
    }

    return panel;
  }
}
