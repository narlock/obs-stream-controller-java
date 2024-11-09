package com.narlock.gui.option;

import com.narlock.Main;
import com.narlock.model.Button;
import com.narlock.model.Tile;
import java.util.List;
import java.util.Objects;
import javax.swing.*;

public class SetTileOptionPane {
  public static JPanel rootPanel = new JPanel();

  public static JComboBox<Tile> refreshTile() {
    List<Tile> tiles = Main.settings.getTiles();
    JComboBox<Tile> tileComboBox = new JComboBox<>();
    for (Tile tile : tiles) {
      tileComboBox.addItem(tile);
    }
    return tileComboBox;
  }

  public static void show(int x, int y) {
    // Remove all to ensure integrity of panel
    rootPanel.removeAll();

    JComboBox<Tile> tileComboBox = refreshTile();
    rootPanel.add(tileComboBox);

    int result =
        JOptionPane.showConfirmDialog(
            null, rootPanel, "Set Tile at " + x + ", " + y, JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
      Tile tile = (Tile) tileComboBox.getSelectedItem();

      Button button = new Button();
      button.setTileName(tile.getName());
      button.setGrid(List.of(x, y));

      // need to overwrite existing if something exists with both of those indices
      Main.settings.getButtonConfig().getButtons().stream()
          .filter(Objects::nonNull)
          .filter(
              existingButton ->
                  existingButton.getGrid() != null
                      && existingButton.getGrid().size() > 1
                      && existingButton.getGrid().get(0) == x
                      && existingButton.getGrid().get(1) == y)
          .findFirst()
          .ifPresent(Main.settings.getButtonConfig().getButtons()::remove);

      Main.settings.getButtonConfig().getButtons().add(button);
      Main.settings.save();
    }
  }
}
