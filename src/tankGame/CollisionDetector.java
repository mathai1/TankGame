package tankGame;

import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.ArrayList;

public class CollisionDetector {
    GameObject obj1,obj2;
    CollisionDetector(GameObject one,GameObject two){
        this.obj1=one;
        this.obj2=two;
    }

    //player(tank) collision with player(tank)
    public void PlayerCollision(Tank tank1,Tank tank2){
        Rectangle one = new Rectangle(tank1.getBounds());
        Rectangle two = new Rectangle(tank2.getBounds());
        if(one.intersects(two)){
            if(tank1.isDownPressed()){
                tank1.setX(tank1.getX()+tank1.getVx());
                tank1.setY(tank1.getY()+tank1.getVy());
            }
            if(tank1.isUpPressed()){
                tank1.setX(tank1.getX()-tank1.getVx());
                tank1.setY(tank1.getY()-tank1.getVy());
            }
            if(tank2.isDownPressed()){
                tank2.setX(tank2.getX()+tank2.getVx());
                tank2.setY(tank2.getY()+tank2.getVy());
            }
            if(tank2.isUpPressed()){
                tank2.setX(tank2.getX()-tank2.getVx());
                tank2.setY(tank2.getY()-tank2.getVy());
            }
        }
    }
    //checking collision between the tank and wall
    public void WallCollision(Tank tank, ArrayList<Wall> wall){
        Rectangle tankR =new Rectangle(tank.getBounds());
        for(int i =0;i<wall.size();i++){
            if(tankR.intersects(wall.get(i).getBounds())){
                if(tank.isUpPressed() ){
                    tank.setX(tank.getX()-tank.getVx());
                    tank.setY(tank.getY()-tank.getVy());
                }
                else if(tank.isDownPressed()){
                    tank.setX(tank.getX()+tank.getVx());
                    tank.setY(tank.getY()+tank.getVy());
                }
            }
        }
    }
    //checking collision between bullet and wall
    public void bulletWallCollision(Tank tank,ArrayList<Wall> wall){
        Bullet bullet;
        Wall walls;
        ArrayList <Bullet> bullets =tank.getBullet();
        for(int i =0;i<bullets.size();i++){
            bullet=bullets.get(i);
            Rectangle one =new Rectangle(bullet.getBounds());
            for(int j =0;j < wall.size();j++){
                walls = wall.get(j);
                Rectangle two=new Rectangle(walls.getBounds());
                if(one.intersects(two)){
                    bullets.remove(bullet);
                    if(walls instanceof BreakWall){
                        wall.remove(walls);
                    }
                }
            }
        }
    }
    //checking tank collision with power up
    public void PowerUpCollision(Tank tank,ArrayList<PowerUp> power){
        PowerUp powers;
        Rectangle tankR =new Rectangle(tank.getBounds());
        for(int i =0;i<power.size();i++){
            powers=power.get(i);
            if(tankR.intersects(powers.getBounds())){
                power.remove(powers);
                tank.setHealth(tank.getHealth()+25);
            }
        }
    }
    //checking collision between bullet and tank
    //tank1 is tank that is shooting
    //tank2 is tank getting hit
    public void bulletTankCollision(Tank tank1,Tank tank2){
        Bullet bullet1;
        ArrayList<Bullet>bullets1=tank1.getBullet();
        for(int i =0;i<bullets1.size();i++){
            bullet1=bullets1.get(i);
            if(bullet1.getShotBy()==tank1){
                if(bullet1.getBounds().intersects(tank2.getBounds())){
                    bullets1.remove(bullet1);
                    tank2.setHealth(tank2.getHealth()-tank1.getDmg());
                }
                if(tank2.getHealth()==0){
                    tank2.setLives(tank2.getLives()-1);
                    //after tank loses a live then set its X and Y values to somewhere random on the map
                    int x = (int)(Math.random() * ((1450 - 50) + 1)) + 50;
                    int y = (int)(Math.random() * ((1450 - 50) + 1)) + 50;
                    tank2.setX(x);
                    tank2.setY(y);
                    tank2.setHealth(100);
                }
            }
            break;
        }
    }
}



    

