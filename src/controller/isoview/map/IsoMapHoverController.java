package controller.isoview.map;

import controller.CursorType;
import isoview.LabelsDrawer;
import isoview.clickmenus.ClickMenuButton;
import isoview.clickmenus.ClickMenusDrawer;
import isoview.map.objects.MapObjectDrawer;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import model.Battle;
import model.IsoBattleLoop;
import model.character.Character;
import model.items.weapon.Weapon;
import model.items.weapon.WeaponGroup;
import model.map.mapObjects.MapObject;

import java.awt.*;

public class IsoMapHoverController {

    private Canvas mapCanvas;
    private ImageCursor normalCursor;
    private ImageCursor attackCursor;
    private ImageCursor attackDistCursor;

    public IsoMapHoverController(Canvas mapCanvas) {
        this.mapCanvas = mapCanvas;

        initialize();
    }

    public void initialize(){
        loadCursors();
        initCanvasHover();
    }

    private void loadCursors() {
        final String CURSORS_CATALOG = "cursors/";
        normalCursor = new ImageCursor(new Image(CURSORS_CATALOG + CursorType.NORMAL.getPngName()), 0, 0);
        attackCursor = new ImageCursor(new Image(CURSORS_CATALOG + CursorType.ATTACK.getPngName()), 0, 0);
        attackDistCursor = new ImageCursor(new Image(CURSORS_CATALOG + CursorType.ATTACK_DIST.getPngName()), 0, 0);
    }

    private void initCanvasHover(){
        mapCanvas.getScene().setOnMouseMoved(mouseEvent -> {
            IsoBattleLoop.setHoverPoint(new Point((int)mouseEvent.getX(), (int)mouseEvent.getY()));
            setCursorAndLabels();
        });
    }

    private void setCursorAndLabels(){
        mapCanvas.getScene().setCursor(normalCursor);
        ClickMenuButton hoveredMenuButton = ClickMenusDrawer.hoveredButton();
        if (hoveredMenuButton != null) {
            switch (hoveredMenuButton) {
                case ATTACK_HEAD:
                case ATTACK_BODY:
                case ATTACK_ARMS:
                case ATTACK_LEGS:
                    Weapon chosenCharWeapon = Battle.getChosenCharacter().getItems().getWeapon();
                    if (WeaponGroup.isRange(chosenCharWeapon))
                        mapCanvas.getScene().setCursor(attackDistCursor);
                    else
                        mapCanvas.getScene().setCursor(attackCursor);
                    break;
                default:
                    break;
            }
        }

        Character hoverCharacter = LabelsDrawer.getHoverCharacter();
        if (hoverCharacter != null && hoverCharacter.getColor() != Battle.getPlayerColor()) {
            Weapon chosenCharWeapon = Battle.getChosenCharacter().getItems().getWeapon();
            if (WeaponGroup.isRange(chosenCharWeapon))
                mapCanvas.getScene().setCursor(attackDistCursor);
            else
                mapCanvas.getScene().setCursor(attackCursor);
        }

        MapObject hoverObject = MapObjectDrawer.getHoverObject();
    }
}
