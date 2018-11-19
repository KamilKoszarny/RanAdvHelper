package model.items.armor;

import javafx.scene.image.Image;
import model.items.ItemsLoader;

public enum Gloves implements Armor{

    NOTHING(0, 0),
    RAG_GLOVES(0, 3),
    LEATHER_GLOVES(1, 6),
    CHAIN_GLOVES(2, 9),
    STEEL_GLOVES(4, 16),
    PLATE_GLOVES(3, 13);

    private int armor, durability;
    private Image image;
    private String name;

    Gloves(int armor, int durability) {
        this.armor = armor;
        this.durability = durability;

        image = ItemsLoader.loadItemImage("/items/gloves/" + this.name() + ".png");
        name = ItemsLoader.setItemName(this.name());
    }

    public int getArmor() {
        return armor;
    }

    public int getDurability() {
        return durability;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}