package tankGame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UnbreakWall extends Wall {
    BufferedImage wallImage;
    public UnbreakWall (int x,int y,BufferedImage wallImage){
        super(x,y);
        this.wallImage=wallImage;
    }


    public Rectangle getBounds() {
        return new Rectangle(x,y,wallImage.getWidth(),wallImage.getHeight());
    }

    public void drawImage(Graphics g){
        Graphics2D g2 =(Graphics2D)g;
        g2.drawImage(this.wallImage,x,y,null);
    }
}