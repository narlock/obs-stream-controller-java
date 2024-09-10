package com.narlock.model;

import java.util.List;

public class Button {
    private String tileName;
    private List<Integer> grid;

    public String getTileName() {
        return tileName;
    }

    public void setTileName(String tileName) {
        this.tileName = tileName;
    }

    public List<Integer> getGrid() {
        return this.grid;
    }

    public void setGrid(List<Integer> grid) {
        this.grid = grid;
    }
}
