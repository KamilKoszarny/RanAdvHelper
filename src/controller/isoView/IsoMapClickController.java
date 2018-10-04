package controller.isoView;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import model.IsoBattleLoop;
import model.character.Character;
import model.map.MapPiece;

import java.awt.*;
import java.util.List;

public class IsoMapClickController {


    private Canvas mapCanvas;
    private IsoBattleLoop isoBattleLoop;
    private List<Label> charLabels;

    IsoMapClickController(Canvas mapCanvas, List<Label> charLabels, IsoBattleLoop isoBattleLoop, List<Character> characters) {
        this.mapCanvas = mapCanvas;
        this.charLabels = charLabels;
        this.isoBattleLoop = isoBattleLoop;
    }

    void initialize(){
        initInfoOnClick();
    }

    private void initInfoOnClick(){
        mapCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                isoBattleLoop.setMapClickPoint(new Point((int)mouseEvent.getX(), (int)mouseEvent.getY()));
                isoBattleLoop.setMapClickFlag(true);
            }
        });
    }
}
