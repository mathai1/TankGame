package tankGame;

import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author anthony-pc
 */
public class Tank extends Movables{
    int health, dmg,lives;
    private final int R = 2;
    private final int ROTATION_SPEED = 2;

    private  static ArrayList<Bullet> ammo;
    private BufferedImage tankImage;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;


    //description of tank object
    Tank(int x, int y, int vx, int vy, int angle, BufferedImage tankImage,int health,int dmg,int lives) {
        super(x, y);
        this.vx = vx;
        this.vy = vy;
        this.health=health;
        this.dmg=dmg;
        this.lives=lives;
        this.angle = angle;
        this.tankImage=tankImage;
        this.ammo=new ArrayList<>();

    }
    //GETTER methods for tank stats
    public int getHealth(){return this.health;}
    public int getDmg(){return this.dmg;}
    public int getLives(){return this.lives;}

    public void setHealth(int health){this.health=health;}
    public void setLives(int lives){this.lives=lives;}

    public Rectangle getBounds() {
        return new Rectangle(x,y,tankImage.getWidth(),tankImage.getHeight());
    }
    //SETTER methods for tank stats
    public int getVx(){return this.vx;}
    public int getVy(){return this.vy;}

    void toggleUpPressed() {
        this.UpPressed = true;
    }
    void toggleDownPressed() {
        this.DownPressed = true;
    }
    void toggleRightPressed() {
        this.RightPressed = true;
    }
    void toggleLeftPressed() {
        this.LeftPressed = true;
    }
    void toggleShootPressed(){this.ShootPressed=true;}

    void unToggleUpPressed() {
        this.UpPressed = false;
    }
    void unToggleDownPressed() {
        this.DownPressed = false;
    }
    void unToggleRightPressed() {
        this.RightPressed = false;
    }
    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }
    void unToggleShootPressed(){this.ShootPressed=false;}

    public boolean isDownPressed() {
        return DownPressed;
    }
    public boolean isUpPressed() {
        return UpPressed;
    }
    public boolean isShootPressed(){return ShootPressed;}



    public void update() {
        //foward movement
        if (this.UpPressed) {
            this.moveForwards();
        }
        //backward movement
        if (this.DownPressed) {
            this.moveBackwards();
        }
        //rotate left
        if (this.LeftPressed) {
            this.rotateLeft();
        }
        //rotate right
        if (this.RightPressed) {
            this.rotateRight();
        }
        if(this.ShootPressed && GameWorld.tick % 20==0){
            Bullet bullet = new Bullet(x,y,angle,GameWorld.bulletImage,this);
            this.ammo.add(bullet);
        }
        this.ammo.forEach(bullet -> bullet.update());

    }

    public static ArrayList<Bullet> getBullet(){
        return ammo;
    }

    private void rotateLeft() {
        this.angle -= this.ROTATION_SPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATION_SPEED;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    @Override
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.tankImage.getWidth() / 2.0, this.tankImage.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.tankImage, rotation, null);
        this.ammo.forEach(bullet -> bullet.drawImage((Graphics2D) g));
    }



}
