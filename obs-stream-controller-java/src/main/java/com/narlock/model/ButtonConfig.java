package com.narlock.model;

import java.util.List;

public class ButtonConfig {
  private List<Integer> gridSize;
  private List<Button> buttons;

  public ButtonConfig() {}

  public ButtonConfig(List<Integer> gridSize, List<Button> buttons) {
    this.gridSize = gridSize;
    this.buttons = buttons;
  }

  public List<Integer> getGridSize() {
    return this.gridSize;
  }

  public void setGridSize(List<Integer> gridSize) {
    this.gridSize = gridSize;
  }

  public List<Button> getButtons() {
    return this.buttons;
  }

  public void setButtons(List<Button> buttons) {
    this.buttons = buttons;
  }
}
