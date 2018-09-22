package viewIso;

import controller.isoView.IsoMapMoveController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.map.Map;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MapDrawer {

    private static final Color BACKGROUND_COLOR = Color.BLACK;

    private Point zeroScreenPosition = new Point(600, -100);
    private Map map;
    private Canvas canvas;
    private MapPieceDrawer mPDrawer;
    private GraphicsContext gc;
    private int mapPieceScreenSizeX  = 24;
    private int mapPieceScreenSizeY = 16;

    MapDrawer(Map map, Canvas canvas) {
        this.map = map;
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        mPDrawer = new MapPieceDrawer(map, gc, this, mapPieceScreenSizeX, mapPieceScreenSizeY);
    }

    public void drawMap(){
        for (Point point: visiblePoints()) {
            mPDrawer.drawMapPiece(point);
        }
    }

    private void moveMap(Point move){
        changeZeroScreenPosition(move);
        clearMapBounds();
        drawMap();
    }

    public void clearMapBounds(){
        //dir 0: N-S, 1: E_W
        double[] xCoords = new double[8];
        double[] yCoords = new double[8];
        Point point;

        point = new Point(0, 0);
        xCoords[0] = screenPosition(point).x;
        yCoords[0] = screenPosition(point).y - mapPieceScreenSizeY / 2 - map.MAX_HEIGHT - IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        point = new Point(map.mapXPoints, 0);
        xCoords[1] = screenPosition(point).x + mapPieceScreenSizeX / 2 + IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeX;
        yCoords[1] = screenPosition(point).y - map.MAX_HEIGHT - IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        xCoords[2] = screenPosition(point).x + mapPieceScreenSizeX / 2 + IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeX;
        yCoords[2] = screenPosition(point).y - map.MIN_HEIGHT + IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        xCoords[3] = screenPosition(point).x;
        yCoords[3] = screenPosition(point).y - map.MIN_HEIGHT + IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        point = new Point(0, 0);
        xCoords[4] = screenPosition(point).x;
        yCoords[4] = screenPosition(point).y + mapPieceScreenSizeY / 2 - map.MIN_HEIGHT + IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        point = new Point(0, map.mapYPoints);
        xCoords[5] = screenPosition(point).x;
        yCoords[5] = screenPosition(point).y - map.MIN_HEIGHT + IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        xCoords[6] = screenPosition(point).x - mapPieceScreenSizeX / 2 - IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeX;
        yCoords[6] = screenPosition(point).y - map.MIN_HEIGHT + IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        xCoords[7] = screenPosition(point).x - mapPieceScreenSizeX / 2 - IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeX;
        yCoords[7] = screenPosition(point).y - map.MAX_HEIGHT - IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        gc.setFill(BACKGROUND_COLOR);
        gc.fillPolygon(xCoords, yCoords, 8);



        point = new Point(map.mapXPoints, map.mapYPoints);
        xCoords[0] = screenPosition(point).x;
        yCoords[0] = screenPosition(point).y - mapPieceScreenSizeY / 2 - map.MAX_HEIGHT - IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        point = new Point(map.mapXPoints, 0);
        xCoords[1] = screenPosition(point).x;
        yCoords[1] = screenPosition(point).y - map.MAX_HEIGHT - IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        xCoords[2] = screenPosition(point).x + mapPieceScreenSizeX / 2 + IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeX;
        yCoords[2] = screenPosition(point).y - map.MAX_HEIGHT - IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        xCoords[3] = screenPosition(point).x + mapPieceScreenSizeX / 2 + IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeX;
        yCoords[3] = screenPosition(point).y - map.MIN_HEIGHT + IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        point = new Point(map.mapXPoints, map.mapYPoints);
        xCoords[4] = screenPosition(point).x;
        yCoords[4] = screenPosition(point).y + mapPieceScreenSizeY / 2 - map.MIN_HEIGHT + IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY +
                50;

        point = new Point(0, map.mapYPoints);
        xCoords[5] = screenPosition(point).x - mapPieceScreenSizeX / 2 - IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeX;
        yCoords[5] = screenPosition(point).y - map.MIN_HEIGHT + IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        xCoords[6] = screenPosition(point).x - mapPieceScreenSizeX / 2 - IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeX;
        yCoords[6] = screenPosition(point).y - map.MAX_HEIGHT - IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        xCoords[7] = screenPosition(point).x;
        yCoords[7] = screenPosition(point).y - map.MAX_HEIGHT - IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;

        gc.setFill(BACKGROUND_COLOR);
        gc.fillPolygon(xCoords, yCoords, 8);
    }

    public void setZeroScreenPosition(Point zeroScreenPosition) {
        this.zeroScreenPosition = zeroScreenPosition;
    }

    public void changeZeroScreenPosition(Point zSPChange) {
        this.zeroScreenPosition.x += zSPChange.x * mapPieceScreenSizeX;
        this.zeroScreenPosition.y += zSPChange.y * mapPieceScreenSizeY;
    }

    Point screenPosition(Point point){
        return new Point(zeroScreenPosition.x + point.x * mapPieceScreenSizeX/2 - point.y * mapPieceScreenSizeX/2,
                zeroScreenPosition.y + point.x * mapPieceScreenSizeY/2 + point.y * mapPieceScreenSizeY/2);
    }

    private Point relativeScreenPosition(Point point){
        return new Point(point.x * mapPieceScreenSizeX/2 - point.y * mapPieceScreenSizeX/2,
                point.x * mapPieceScreenSizeY/2 + point.y * mapPieceScreenSizeY/2);
    }

    public boolean mapOnScreen(){
        int bonus = IsoMapMoveController.MAP_MOVE_STEP * mapPieceScreenSizeY;
        boolean mapOnScreen = zeroScreenPosition.x < - relativeScreenPosition(new Point(0, map.getHeight())).x + bonus &&
                zeroScreenPosition.x > - relativeScreenPosition(new Point(map.getWidth(), 0)).x + canvas.getWidth() - bonus &&
                zeroScreenPosition.y < - relativeScreenPosition(new Point(0, 0)).y + bonus + map.MAX_HEIGHT &&
                zeroScreenPosition.y > - relativeScreenPosition(new Point(map.getWidth(), map.getHeight())).y + canvas.getHeight() - bonus + map.MIN_HEIGHT;
        return mapOnScreen;
    }

    private List<Point> visiblePoints(){
        List<Point> visiblePoints = new ArrayList<>();
        for (Point point: map.getPoints().keySet()) {
            if (isOnCanvas(screenPosition(point)))
                visiblePoints.add(point);
        }

        return visiblePoints;
    }

    private boolean isOnCanvas(Point screenPoint){
        return screenPoint.x >= 0 && screenPoint.x <= canvas.getWidth() + mapPieceScreenSizeX &&
                screenPoint.y >= 0 + map.MIN_HEIGHT * 10 && screenPoint.y <= canvas.getHeight() + map.MAX_HEIGHT * 10;
    }
}