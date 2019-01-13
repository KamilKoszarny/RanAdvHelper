package viewIso.panel;

import controller.isoView.isoMap.IsoMapClickController;
import controller.isoView.isoPanel.Panel;
import controller.isoView.isoPanel.PanelController;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.Battle;
import model.actions.ItemHandler;
import model.character.Character;
import model.items.Item;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CharDescriptor {

    private static Panel panel;
    private List<Character> characters;
    private static Rectangle inventoryRect;
    private static boolean inventoryRectsSet = false;

    public CharDescriptor(Panel panel, List<Character> characters) {
        this.panel = panel;
        this.characters = characters;
        initInvRect();
    }

    private void initInvRect() {
        Image inventoryImg = new Image("/items/inventory.png");
        Rectangle invFirstRect = PanelController.calcInventoryScreenRect(panel, new int[]{0, 0});
        inventoryRect = new Rectangle(invFirstRect.getX(), invFirstRect.getY(),
                ItemHandler.INVENTORY_X * ItemHandler.ITEM_SLOT_SIZE, ItemHandler.INVENTORY_Y * ItemHandler.ITEM_SLOT_SIZE);
        inventoryRect.setFill(new ImagePattern(inventoryImg));
        Pane pane = (Pane) panel.getHeldItemRect().getParent();
        inventoryRect.setVisible(true);
        pane.getChildren().add(inventoryRect);
    }

    public void refresh() {
        List<Character> chosenCharacters = calcChosenCharacters();
        if (chosenCharacters.size() == 1) {
            Character firstChosenCharacter = chosenCharacters.get(0);
            refreshLabels(firstChosenCharacter);
            refreshBars(firstChosenCharacter);
            refreshPortrait(firstChosenCharacter);
            refreshEquipment(firstChosenCharacter);
            if (!inventoryRectsSet) {
                refreshInventory(firstChosenCharacter);
            }
        }
    }

    private void refreshLabels(Character character){
        for (Label label: panel.getCharLabels()) {
            String parameterText = getParameterText(label, character);
            label.setText(parameterText);
        }
    }

    //use label Id
    private String getParameterText(Label label, Character character) {
        String parameter = label.getId().substring(0,label.getId().length() - 5);

        String parText = CharParameter.signByName(parameter) + findValue(character, parameter);

        if (CharParameter.isWithCurrent(parameter)){
            parameter += "Max";
            parText += "/" + findValue(character, parameter);
        }

        return parText;
    }

    private String findValue(Character character, String parameter) {
        String value = "";
        String getterName = "get" + parameter.substring(0, 1).toUpperCase() + parameter.substring(1, parameter.length());
        java.lang.reflect.Method method;
        Class returnType = null;
        try {
            method = character.getStats().getClass().getMethod(getterName);
            returnType = method.getReturnType();
            value = method.invoke(character.getStats()).toString();
        } catch (SecurityException | NoSuchMethodException e) {} catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (returnType != null && returnType.equals(Double.TYPE)) {
            Double doubleValue = Double.parseDouble(value);
            doubleValue = (double) Math.round(doubleValue * 10.) / 10;
            value = doubleValue.toString();
        }
        return value;
    }

    private void refreshBars(Character character) {
        double baseWidth = panel.getCharBars().get(0).getPrefWidth();
        ProgressBar hpBar = panel.getCharBars().get(0), manaBar = panel.getCharBars().get(1), vigorBar = panel.getCharBars().get(2);

        hpBar.setMinWidth(baseWidth * character.getStats().getHitPointsMax() / 100.);
        hpBar.setMaxWidth(baseWidth * character.getStats().getHitPointsMax() / 100.);
        hpBar.setTranslateY(baseWidth / 2 - hpBar.getMinWidth() / 2);
        hpBar.setProgress((double)character.getStats().getHitPoints() / (double)character.getStats().getHitPointsMax());
        manaBar.setMinWidth(baseWidth * character.getStats().getManaMax() * 5 / 100.);
        manaBar.setMaxWidth(baseWidth * character.getStats().getManaMax() * 5 / 100.);
        manaBar.setTranslateY(baseWidth / 2 - manaBar.getMinWidth() / 2);
        manaBar.setProgress((double)character.getStats().getMana() / (double)character.getStats().getManaMax());
        vigorBar.setMinWidth(baseWidth * character.getStats().getVigorMax() / 100.);
        vigorBar.setMaxWidth(baseWidth * character.getStats().getVigorMax() / 100.);
        vigorBar.setTranslateY(baseWidth / 2 - vigorBar.getMinWidth() / 2);
        vigorBar.setProgress((double)character.getStats().getVigor() / (double)character.getStats().getVigorMax());
    }

    private void refreshPortrait(Character character) {
        panel.getCharPortraitBackgroundRect().setFill(character.getColor());
        panel.getPortraitRect().setFill(new ImagePattern(character.getPortrait()));
    }

    private void refreshEquipment(Character character) {
        refreshEqRect(panel.getWeaponRect(), character.getItems().getWeapon());
        refreshEqRect(panel.getSpareWeaponRect(), character.getItems().getSpareWeapon());
        refreshEqRect(panel.getHelmetRect(), character.getItems().getHelmet());
        refreshEqRect(panel.getArmorRect(), character.getItems().getBodyArmorItem());
        refreshEqRect(panel.getShieldRect(), character.getItems().getShield());
        refreshEqRect(panel.getGlovesRect(), character.getItems().getGloves());
        refreshEqRect(panel.getBootsRect(), character.getItems().getBoots());
        refreshEqRect(panel.getBeltRect(), character.getItems().getBelt());
        refreshEqRect(panel.getAmuletRect(), character.getItems().getAmulet());
        refreshEqRect(panel.getRing1Rect(), character.getItems().getRing1());
        refreshEqRect(panel.getRing2Rect(), character.getItems().getRing2());
        refreshEqRect(panel.getSpareShieldRect(), character.getItems().getSpareShield());
    }

    private void refreshEqRect(Rectangle rectangle, Item item) {
        Image image = item.getImage();
        rectangle.setWidth(image.getWidth());
        rectangle.setHeight(image.getHeight());
        rectangle.setFill(new ImagePattern(image));

        String name = item.getName();
        Tooltip tooltip = new Tooltip(name);
        Tooltip.install(rectangle, tooltip);
    }

    public static void refreshInventory(Character character) {
        Pane pane = (Pane) panel.getHeldItemRect().getParent();
        redrawInventoryRect(pane);

        int[] itemInvPos;
        Rectangle itemInvFirstRect;
        for (Item item: character.getItems().getInventory().keySet()) {
            itemInvPos = character.getItems().getInventory().get(item);
            itemInvFirstRect = PanelController.calcInventoryScreenRect(panel, itemInvPos);
            Rectangle inventoryItemRect = new Rectangle(itemInvFirstRect.getX(), itemInvFirstRect.getY(),
                    item.getImage().getWidth(), item.getImage().getHeight());
            inventoryItemRect.setFill(new ImagePattern(item.getImage()));

            String name = item.getName();
            Tooltip tooltip = new Tooltip(name);
            Tooltip.install(inventoryItemRect, tooltip);

            PanelController.initInventoryItemClick(inventoryItemRect, item, character);

            pane.getChildren().add(inventoryItemRect);
        }
    }

    private static void redrawInventoryRect(Pane pane) {
        if (!inventoryRectsSet) {
            Rectangle invFirstRect = PanelController.calcInventoryScreenRect(panel, new int[]{0, 0});
            inventoryRect.setX(invFirstRect.getX());
            inventoryRect.setY(invFirstRect.getY());
            initInventoryRectangle();
            inventoryRectsSet = true;
        }
        pane.getChildren().remove(inventoryRect);
        pane.getChildren().add(inventoryRect);
    }

    private static void initInventoryRectangle() {
        PanelController.initInventoryClick(inventoryRect);
        inventoryRect.setVisible(true);
    }


    private List<Character> calcChosenCharacters(){
        List<Character> chosenCharacters = new ArrayList<>();
        for (Character character: characters) {
            if (character.isChosen()) {
                chosenCharacters.add(character);
            }
        }
        return chosenCharacters;
    }
}
