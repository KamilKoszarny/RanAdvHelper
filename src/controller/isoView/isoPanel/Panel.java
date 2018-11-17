package controller.isoView.isoPanel;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Panel {

    private List<Label> charLabels;
    private List<ProgressBar> charBars;
    private Rectangle portraitRect;
    private Rectangle helmetRect, weaponRect, armorRect, shieldRect, glovesRect, bootsRect, amuletRect, ring1Rect, beltRect, ring2Rect, spareWeaponRect, spareShieldRect;

    public Panel(List<Label> charLabels, List<ProgressBar> charBars, Rectangle portraitRect, Rectangle helmetRect, Rectangle weaponRect, Rectangle armorRect, Rectangle shieldRect, Rectangle glovesRect, Rectangle bootsRect, Rectangle amuletRect, Rectangle ring1Rect, Rectangle beltRect, Rectangle ring2Rect, Rectangle spareWeaponRect, Rectangle spareShieldRect) {
        this.charLabels = charLabels;
        this.charBars = charBars;
        this.portraitRect = portraitRect;
        this.helmetRect = helmetRect;
        this.weaponRect = weaponRect;
        this.armorRect = armorRect;
        this.shieldRect = shieldRect;
        this.glovesRect = glovesRect;
        this.bootsRect = bootsRect;
        this.amuletRect = amuletRect;
        this.ring1Rect = ring1Rect;
        this.beltRect = beltRect;
        this.ring2Rect = ring2Rect;
        this.spareWeaponRect = spareWeaponRect;
        this.spareShieldRect = spareShieldRect;
    }

    public List<Label> getCharLabels() {
        return charLabels;
    }

    public List<ProgressBar> getCharBars() {
        return charBars;
    }

    public Rectangle getPortraitRect() {
        return portraitRect;
    }

    public Rectangle getHelmetRect() {
        return helmetRect;
    }

    public Rectangle getWeaponRect() {
        return weaponRect;
    }

    public Rectangle getArmorRect() {
        return armorRect;
    }

    public Rectangle getShieldRect() {
        return shieldRect;
    }

    public Rectangle getGlovesRect() {
        return glovesRect;
    }

    public Rectangle getBootsRect() {
        return bootsRect;
    }

    public Rectangle getAmuletRect() {
        return amuletRect;
    }

    public Rectangle getRing1Rect() {
        return ring1Rect;
    }

    public Rectangle getBeltRect() {
        return beltRect;
    }

    public Rectangle getRing2Rect() {
        return ring2Rect;
    }

    public Rectangle getSpareWeaponRect() {
        return spareWeaponRect;
    }

    public Rectangle getSpareShieldRect() {
        return spareShieldRect;
    }
}
