package tankGame;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends Movables {

    BufferedImage bulletImage;
    int R = 9;
    private Tank shotBy;

    public Bullet(int x, int y, int angle ,BufferedImage bulletImage,Tank tank) {
        super(x, y);
        this.angle=angle;
        this.bulletImage=bulletImage;
        this.shotBy=tank;

    }
    public void moveFowards(){
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
    }
    public Rectangle getBounds() {
        return new Rectangle(x,y,bulletImage.getWidth(),bulletImage.getHeight());
    }


    public void update(){
        //bullet only shoots straight so only movefowards is needed
        moveFowards();
    }
    public Tank getShotBy(){return shotBy;}

    public void drawImage(Graphics buffer){
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.bulletImage.getWidth() / 2.0, this.bulletImage.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) buffer;
        g2d.drawImage(this.bulletImage, rotation, null);
    }





}
