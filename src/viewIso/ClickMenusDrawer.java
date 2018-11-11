package viewIso;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import model.map.Map;
import model.map.MapPiece;
import model.map.buildings.Door;
import viewIso.map.MapDrawCalculator;
import viewIso.map.MapDrawer;
import viewIso.map.MapPieceDrawer;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ClickMenusDrawer {

    private Canvas canvas;
    static List<ClickMenuButton> char2PointMenu = Arrays.asList(ClickMenuButton.LOOK, ClickMenuButton.WALK, ClickMenuButton.RUN, ClickMenuButton.SNEAK);
    static List<ClickMenuButton> char2DoorMenu = Arrays.asList(ClickMenuButton.LOOK, ClickMenuButton.DOOR_OPEN, ClickMenuButton.DOOR_CLOSE);

    ClickMenusDrawer(MapDrawer mapDrawer) {
        canvas = mapDrawer.getCanvas();
        initMenu(char2PointMenu);
//        initMenu(char2DoorMenu);
    }

    public void drawChar2PointMenu(Point clickPoint) {
        MapPiece clickedMapPiece = MapDrawCalculator.mapPieceByClickPoint(clickPoint);
        if (!clickedMapPiece.isWalkable()) {
            ClickMenuButton.WALK.setGrayed(true);
            ClickMenuButton.RUN.setGrayed(true);
            ClickMenuButton.SNEAK.setGrayed(true);
            ClickMenuButton.colorButtons(char2PointMenu);
        } else {
            ClickMenuButton.WALK.setGrayed(false);
            ClickMenuButton.RUN.setGrayed(false);
            ClickMenuButton.SNEAK.setGrayed(false);
            ClickMenuButton.colorButtons(char2PointMenu);
        }

        for (ClickMenuButton button: char2PointMenu) {
            drawButton(button, clickPoint);
        }
    }

    public void drawChar2DoorMenu(Point clickPoint) {
        MapPiece clickedMapPiece = MapDrawCalculator.mapPieceByClickPoint(clickPoint);
        Door door = (Door) clickedMapPiece.getObject();
        ClickMenuButton.DOOR_OPEN.setGrayed(door.isOpen());
        ClickMenuButton.DOOR_CLOSE.setGrayed(!door.isOpen());
        ClickMenuButton.colorButtons(char2DoorMenu);
        for (ClickMenuButton button: char2DoorMenu) {
            drawButton(button, clickPoint);
        }
    }

    public void hideMenus() {
        hideMenu(char2PointMenu);
        hideMenu(char2DoorMenu);
    }

    public void hideMenu(List<ClickMenuButton> menu) {
        for (ClickMenuButton button: menu) {
            hideButton(button);
        }
    }

    public void moveMenus(Point mapMove) {
        for (ClickMenuButton button: char2PointMenu) {
            moveButton(button, mapMove);
        }
        for (ClickMenuButton button: char2DoorMenu) {
            moveButton(button, mapMove);
        }
    }

    public ClickMenuButton clickedButton () {
        for (ClickMenuButton button: ClickMenuButton.values()) {
            if (button.wasClicked())
                return button;
        }
        return null;
    }

    private void initMenu(List<ClickMenuButton> menu) {
        ClickMenuButton.groupButtons(menu);
        ClickMenuButton.shapeButtons(menu);
        ClickMenuButton.colorButtons(menu);
        for (ClickMenuButton button: menu) {
            Shape shape = button.getShape();
            shape.setVisible(false);
            Label label = button.getLabel();
            label.setVisible(false);
            Pane pane = (Pane) canvas.getParent();
            pane.getChildren().addAll(shape, label);
        }
    }

    private void drawButton(ClickMenuButton button, Point clickPoint) {
        Shape shape = button.getShape();
        shape.setTranslateX(clickPoint.x);
        shape.setTranslateY(clickPoint.y);
        shape.setVisible(true);

        Label label = button.getLabel();
        label.setTranslateX(button.getLabelVertex().x + clickPoint.x);
        label.setTranslateY(button.getLabelVertex().y + clickPoint.y);
        label.toFront();

        label.setVisible(true);
    }

    private void hideButton(ClickMenuButton button) {
        Shape shape = button.getShape();
        shape.setVisible(false);

        Label label = button.getLabel();
        label.setVisible(false);
    }

    private void moveButton(ClickMenuButton button, Point mapMove) {
        Shape shape = button.getShape();
        shape.setTranslateX(shape.getTranslateX() + mapMove.x * MapDrawer.MAP_PIECE_SCREEN_SIZE_X);
        shape.setTranslateY(shape.getTranslateY() + mapMove.y * MapDrawer.MAP_PIECE_SCREEN_SIZE_Y);

        Label label = button.getLabel();
        label.setTranslateX(label.getTranslateX() + mapMove.x * MapDrawer.MAP_PIECE_SCREEN_SIZE_X);
        label.setTranslateY(label.getTranslateY() + mapMove.y * MapDrawer.MAP_PIECE_SCREEN_SIZE_Y);
    }
}
