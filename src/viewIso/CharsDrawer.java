package viewIso;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.character.Character;
import model.map.Map;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharsDrawer {

    private Dimension SPRITES_SPAN = new Dimension(192, 192);
    private Dimension SPRITE_SIZE = new Dimension(124, 160);
    private Dimension SPRITE_OFFSET =
            new Dimension((SPRITES_SPAN.width - SPRITE_SIZE.width)/2, (SPRITES_SPAN.height - SPRITE_SIZE.height)/2);
    private Dimension SPRITE_BASE = new Dimension(64, 132);

    private Map map;
    private Canvas canvas;
    private GraphicsContext gc;
    MapDrawer mapDrawer;
    private List<Character> characters;
    private java.util.Map<Character, CharSprite> charSpriteSheetMap = new HashMap<>();

    public CharsDrawer(Map map, Canvas canvas, MapDrawer mapDrawer, List<Character> characters) {
        this.map = map;
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        this.mapDrawer = mapDrawer;
        this.characters = characters;

        initCharSpriteMap();
        drawChars(characters);
    }

    private void initCharSpriteMap() {
        for (Character character: characters) {
            CharSprite sprite = chooseSpriteSheet(character);
            charSpriteSheetMap.put(character, sprite);
        }
    }

    public void drawAllChars() {
        drawChars(characters);
    }

    public void drawVisibleChars() {
        List<Character> visibleChars = new ArrayList<>();
        List<Point> visiblePoints = mapDrawer.calcVisiblePoints();
        for (Character character: characters) {
            if (visiblePoints.contains(character.getPosition())) {
                visibleChars.add(character);
            }
        }
        drawChars(visibleChars);
    }

    public void drawChars(List<Character> characters) {
        List<Point> visiblePoints = mapDrawer.calcVisiblePoints();
        for (Character character: characters) {
            if (visiblePoints.contains(character.getPosition()))
                drawChar(character);
        }
    }



    public void drawChar(Character character) {

        clearCharProximity(character);

        Point charScreenPos = mapDrawer.screenPositionWithHeight(character.getPosition());
        CharSprite charSprite = charSpriteSheetMap.get(character);
        Image spriteSheet = charSprite.getCharSpriteSheet();
        Point framePos = charSprite.getFramePos();

        gc.drawImage(spriteSheet,
                SPRITES_SPAN.width * framePos.x + SPRITE_OFFSET.width, SPRITES_SPAN.height * framePos.y + SPRITE_OFFSET.height,
                SPRITE_SIZE.width, SPRITE_SIZE.height,
                charScreenPos.x - SPRITE_BASE.width, charScreenPos.y - SPRITE_BASE.height,
                SPRITE_SIZE.width, SPRITE_SIZE.height);

        charSprite.nextFrame();
    }

    private void clearCharProximity(Character character) {
        List<Point> charClosePoints = calcCharClosePoints(character);
        mapDrawer.drawMapPoints(charClosePoints);
    }


    private CharSprite chooseSpriteSheet(Character character) {
        CharSprite charSprite = null;
        charSprite = new CharSprite(new Image("/sprites/flare/demo/vesuvvio.png"));

        return charSprite;
    }

    private List<Point> calcCharClosePoints (Character character) {
        Point charPos = character.getPosition();
        List<Point> charClosePoints = new ArrayList<>();
        for (int x = -4; x < 5; x++) {
            for (int y = -4; y < 5; y++) {
                charClosePoints.add(new Point(charPos.x + x, charPos.y + y));
            }
        }
        return charClosePoints;
    }

}