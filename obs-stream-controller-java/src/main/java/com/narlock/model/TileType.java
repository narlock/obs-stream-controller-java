package com.narlock.model;

public enum TileType {
    SWITCH_SCENE("switchScene"),
    TOGGLE_MICROPHONE("toggleMicrophone");

    private final String typeName;

    // Constructor to initialize the typeName
    TileType(String typeName) {
        this.typeName = typeName;
    }

    // Getter for the typeName
    public String getTypeName() {
        return typeName;
    }

    // Static method to construct TileType from a tileName
    public static TileType fromTileName(String tileName) {
        for (TileType type : TileType.values()) {
            if (type.getTypeName().equalsIgnoreCase(tileName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid tileName: " + tileName);
    }
}

