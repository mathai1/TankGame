package tankGame;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Wall extends GameObject {

    public Wall(int x, int y) {
        super(x, y);
    }

    public abstract void drawImage(Graphics g);
}
