package tankGame;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    protected int x, y, width, height,angle,vx,vy;
    BufferedImage image;

    public GameObject(int x, int y){
        this.x = x;
        this.y = y;
    }
    public abstract Rectangle getBounds();

    public void setX(int x){this.x=x;}
    public void setY(int y){this.y=y;}

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public abstract void drawImage(Graphics g);

}
