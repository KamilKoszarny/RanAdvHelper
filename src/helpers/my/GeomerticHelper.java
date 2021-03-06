package helpers.my;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import model.map.Map;
import model.map.MapPiece;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GeomerticHelper {

    private GeomerticHelper() {
    }

    public static double distTo0(double x, double y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public static java.util.List<Point> pointsInRadius(Point centerPoint, int radius, Map map){
        List<Point> pointsInRadius = new ArrayList<>();
        for(int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                Point point = new Point(centerPoint.x + i, centerPoint.y + j);
                if (map.isOnMapPoints(point) && point.distance(centerPoint) < radius)
                    pointsInRadius.add(point);
            }
        }
        return pointsInRadius;
    }

    public static java.util.List<Point> pointsInSquare(Point centerPoint, int radius, Map map){
        List<Point> pointsInSquare = new ArrayList<>();
        for(int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                Point point = new Point(centerPoint.x + i, centerPoint.y + j);
                if (map.isOnMapPoints(point))
                    pointsInSquare.add(point);
            }
        }
        return pointsInSquare;
    }

    public static java.util.List<Point> pointsInRect(Point centerPoint, int xRad, int yRad, Map map){
        List<Point> pointsInRect = new ArrayList<>();
        for(int i = -xRad; i <= xRad; i++) {
            for (int j = -yRad; j <= yRad; j++) {
                Point point = new Point(centerPoint.x + i, centerPoint.y + j);
                if (map.isOnMapPoints(point))
                    pointsInRect.add(point);
            }
        }
        return pointsInRect;
    }

    public static Point PointByMapPiece(MapPiece mapPiece, Map map) {
        for (Point point: map.getPoints().keySet()) {
            if (map.getPoints().get(point).equals(mapPiece))
                return point;
        }
        return null;
    }

    public static void smooth(Map map) {
        final int RADIUS = 2, SLOPE_LIMIT_CAN = 5000, SLOPE_LIMIT_MUST = 100000;
        int slope;
        List<Point> closePoints;
        for (Point point: map.getPoints().keySet()) {
            slope = map.getPoints().get(point).getSlopeSize();
            if (slope > SLOPE_LIMIT_CAN + new Random().nextInt(SLOPE_LIMIT_MUST - SLOPE_LIMIT_CAN)) {
                closePoints = pointsInRadius(point, RADIUS, map);
                smoothPoints(closePoints, map);
            }
        }
    }

    private static void smoothPoints(List<Point> points, Map map) {
        int avgHeight = avgHeight(points, map, 0);
        MapPiece mapPiece;
        for (Point point: points) {
            mapPiece = map.getPoints().get(point);
            mapPiece.setHeight((avgHeight + mapPiece.getHeight()) / 2);
        }
    }

    public static void flatten(List<Point> points, Map map, int offset) {
        int avgHeight = avgHeight(points, map, offset);
        setHeights(avgHeight, points, map);
    }

    public static boolean allFlat(List<Point> points, Map map) {
        int height = map.getPoints().get(points.get(0)).getHeight(), nextHeight;
        for (Point point: points) {
            nextHeight = map.getPoints().get(point).getHeight();
            if (nextHeight != height)
                return false;
            height = nextHeight;
        }
        return true;
    }

    private static int avgHeight(List<Point> points, Map map, int offset) {
        int avgHeight;
        long sumHeight = 0;
        for (Point point: points) {
            sumHeight += map.getPoints().get(point).getHeight();
        }
        avgHeight = (int) (sumHeight / points.size() + offset);
        return avgHeight;
    }

    public static void setHeights(int height, List<Point> points, Map map) {
        for (Point point: points) {
            map.getPoints().get(point).setHeight(height);
        }
    }

    public static List<Point> generateMiddleOfStrip(Map map, boolean[] sides){
        //N, E, S, W
        Random r = new Random();
        List<Point> stripMidPoints = new ArrayList<>();

        if(sides[0] && !(sides[1] && sides[3])){
            Point start = new Point(r.nextInt(map.mapXPoints / 2) + map.mapXPoints / 4, 0);
            if(sides[2]) {
                guideStrip(stripMidPoints, start, 4, map);
                Point midPoint = stripMidPoints.get((int)(stripMidPoints.size() * (r.nextDouble() * 0.5 + 0.25)));
                if (sides[1]) {
                    guideStrip(stripMidPoints, midPoint, 2, map);
                }
                if (sides[3]) {
                    guideStrip(stripMidPoints, midPoint, 6, map);
                }
            } else if (sides[1] && !sides[3]){
                guideStrip(stripMidPoints, start, 3, map);
            } else if (sides[3] && !sides[1]){
                guideStrip(stripMidPoints, start, 5, map);
            }
        } else if (sides[1]){
            Point start = new Point(map.getWidth() - 1, r.nextInt(map.mapYPoints / 2) + map.mapYPoints / 4);
            if (sides[3]){
                guideStrip(stripMidPoints, start, 6, map);
                Point midPoint = stripMidPoints.get((int)(stripMidPoints.size() * (r.nextDouble() * 0.5 + 0.25)));
                if (sides[0]) {
                    guideStrip(stripMidPoints, midPoint, 8, map);
                }
                if (sides[2]) {
                    guideStrip(stripMidPoints, midPoint, 4, map);
                }
            } else if (sides[2]) {
                guideStrip(stripMidPoints, start, 5, map);
            }
        } else if (sides[2] && sides[3])
            guideStrip(stripMidPoints, new Point(r.nextInt(map.mapXPoints / 2) + map.mapXPoints / 4, map.getHeight() - 1), 7, map);
        return stripMidPoints;
    }

    public static List<Point> generateStripByMidPoints(List<Point> midPoints, Map map, int heightOffset, int baseWidth, int flattenOffset){
        List<Point> stripPoints = new ArrayList<>();
        Random r = new Random();
        int i = 0, width;
        List<Point> closePoints;

        for (Point midPoint: midPoints) {
            width = r.nextInt((int)((baseWidth / Map.RESOLUTION_M * Map.M_PER_POINT)));
            closePoints = pointsInRadius(midPoint, width / 2, map);
            stripPoints.addAll(closePoints);
            width = (int)(baseWidth / Map.RESOLUTION_M * Map.M_PER_POINT) - flattenOffset;
            closePoints = pointsInRadius(midPoint, width / 2, map);
            flatten(closePoints, map, heightOffset);
            i++;
        }

        return stripPoints;
    }

    private static void guideStrip(List<Point> stripMidPoints, Point start, int direction, Map map) {
        java.util.Map<Integer, Double> sideProbabilities = new HashMap<>();
        setStripDir(sideProbabilities, direction);
        Point currentPoint = start;
        while (map.isOnMapPoints(currentPoint)) {
            stripMidPoints.add(currentPoint);
            int side = shuffleSide(sideProbabilities);
            currentPoint = calcNextMidPoint(currentPoint, side);
        }
    }

    private static void setStripDir(java.util.Map<Integer, Double> sideProbabilities, int dir){
        Random r = new Random();
        sideProbabilities.put(0, Math.pow(Math.abs(4 - dir), 2) * (.1 + r.nextDouble()));
        sideProbabilities.put(1, Math.pow(Math.abs(4 - Math.abs(2 - dir)), 2) * (.1 + r.nextDouble()));
        sideProbabilities.put(2, Math.pow((4 - Math.abs(4 - dir)), 2) * (.1 + r.nextDouble()));
        sideProbabilities.put(3, Math.pow(Math.abs(4 - Math.abs(6 - dir)), 2) * (.1 + r.nextDouble()));
        recalcSideProbabilities(sideProbabilities);
    }

    private static void recalcSideProbabilities(java.util.Map<Integer, Double> sideProbabilities){
        double sumSideProbablity = 0;


        for (Double sideProbablity: sideProbabilities.values()) {
            sumSideProbablity += sideProbablity;
        }

        int i = 0;
        for (Double sideProbablity: sideProbabilities.values()) {
            sideProbabilities.put(i, sideProbablity / sumSideProbablity);
            i++;
        }

        i = 0;
        sumSideProbablity = 0;
        for (Double sideProbablity: sideProbabilities.values()) {
            sumSideProbablity += sideProbablity;
            sideProbabilities.put(i, sumSideProbablity);
            i++;
        }
    }

    private static int shuffleSide(java.util.Map<Integer, Double> sideProbabilities){
        Random r = new Random();
        double result = r.nextDouble();

        for (Integer side: sideProbabilities.keySet()){
            if (result < sideProbabilities.get(side))
                return side;
        }
        return 0;
    }

    private static Point calcNextMidPoint(Point currentPoint, int side){
        if (side == 0)
            currentPoint = new Point(currentPoint.x, currentPoint.y - 1);
        else if (side == 1)
            currentPoint = new Point(currentPoint.x + 1, currentPoint.y);
        else if (side == 2)
            currentPoint = new Point(currentPoint.x, currentPoint.y + 1);
        else
            currentPoint = new Point(currentPoint.x - 1, currentPoint.y);

        return currentPoint;
    }

    public static Rectangle screenRectangle(Node node) {
        Bounds nodeLocalBounds = node.getBoundsInLocal();
        Bounds nodeScreenBounds = node.localToScreen(nodeLocalBounds);
        int x = (int) nodeScreenBounds.getMinX();
        int y = (int) nodeScreenBounds.getMinY();
        int width = (int) nodeScreenBounds.getWidth();
        int height = (int) nodeScreenBounds.getHeight();
        return new Rectangle(x, y, width, height);
    }

    public static List<Point2D> pointsOnPath(List<Point2D> path, double span) {
        List<Point2D> pointsOnPath = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            Point2D pathPoint = path.get(i);
            Point2D nextPathPoint = path.get(i + 1);
            int pointsPerPathSection = (int) (pathPoint.distance(nextPathPoint) / span) + 1;
            for (int j = 0; j <= pointsPerPathSection; j++) {
                Point2D pointOnPathSection = new Point2D(
                        pathPoint.getX() + (nextPathPoint.getX() - pathPoint.getX())*j/pointsPerPathSection,
                        pathPoint.getY() + (nextPathPoint.getY() - pathPoint.getY())*j/pointsPerPathSection);
                pointsOnPath.add(pointOnPathSection);
            }

        }
        return pointsOnPath;
    }

    public static Point point2DtoPoint(Point2D point2D) {
        return new Point(Math.round(Math.round(point2D.getX())), Math.round(Math.round(point2D.getY())));
    }
}
