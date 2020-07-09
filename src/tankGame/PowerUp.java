package tankGame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUp extends GameObject {
    BufferedImage powerUp;
    public PowerUp (int x,int y,BufferedImage powerUp){
        super(x,y);
        this.powerUp=powerUp;
    }
    public Rectangle getBounds() {
        return new Rectangle(x,y,powerUp.getWidth(),powerUp.getHeight());
    }

    public void drawImage(Graphics g){
        Graphics2D g2 =(Graphics2D)g;
        g2.drawImage(this.powerUp,x,y,null);
    }
}
