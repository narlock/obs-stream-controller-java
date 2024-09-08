package com.narlock.model;

import java.util.List;

public class Button {
    private String type;
    private String image;
    private List<Integer> grid;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Integer> getGrid() {
        return this.grid;
    }

    public void setGrid(List<Integer> grid) {
        this.grid = grid;
    }

    public Button() {}

    // type = "switchScene" only component
    private String scene;

    public String getScene() {
        return this.scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }
}
