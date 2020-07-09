package tankGame;


import java.awt.image.BufferedImage;

public abstract class Movables extends GameObject {
    public Movables(int x, int y) {
        super(x, y);
    }
    public abstract void update();
}
