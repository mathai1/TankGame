/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankGame;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


import static javax.imageio.ImageIO.read;

/**
 * Main driver class of Tank Example.
 * Class is responsible for loading resources and
 * initializing game objects. Once completed, control will
 * be given to infinite loop which will act as our game loop.
 * A very simple game loop.
 * @author anthony-pc
 */
public class GameWorld extends JPanel  {

    public static final int WORLD_WIDTH = 1500;
    public static final int WORLD_HEIGHT = 1500;
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 1000;
    int leftX,leftY;
    int rightX,rightY;
    private static CollisionDetector cd;
    private BufferedImage world;
    private Graphics2D buffer;
    private JFrame jFrame;
    public Tank tankOne;
    public Tank tankTwo;
    public static BufferedImage bulletImage;
    static long tick=0;
    public static ArrayList<Wall> walls;
    public static ArrayList<PowerUp> powers;

    public static void main(String[] args) {
        tankGame.GameWorld tankGame = new tankGame.GameWorld();
        tankGame.init();
        try {

            while (true) {
                tankGame.tankOne.update();
                tankGame.tankTwo.update();
                tankGame.repaint();
                tick++;

                //checking for Collisions
                //tank vs tank collision
                cd.PlayerCollision(tankGame.tankOne,tankGame.tankTwo);
                //tank vs wall collision
                cd.WallCollision(tankGame.tankOne, tankGame.getWalls());
                cd.WallCollision(tankGame.tankTwo, tankGame.getWalls());
                //bullet vs wall collision
                cd.bulletWallCollision(tankGame.tankOne,tankGame.getWalls());
                cd.bulletWallCollision(tankGame.tankTwo,tankGame.getWalls());
                //tank vs powerup collision
                cd.PowerUpCollision(tankGame.tankOne,tankGame.getPowers());
                cd.PowerUpCollision(tankGame.tankTwo,tankGame.getPowers());
                //bullet vs tank collision
                cd.bulletTankCollision(tankGame.tankOne,tankGame.tankTwo);
                cd.bulletTankCollision(tankGame.tankTwo,tankGame.tankOne);


              //  System.out.println(tankExample.t1);
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }

    }
    public int getWorldWidth(){return WORLD_WIDTH;}
    public int getWorldHeight(){return WORLD_HEIGHT;}
    public static ArrayList<Wall> getWalls(){ return walls; }
    public static ArrayList<PowerUp> getPowers(){ return powers; }
    private void init() {
        this.jFrame = new JFrame("Tank Game");
        this.world = new BufferedImage(GameWorld.WORLD_WIDTH,GameWorld.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage tankImage1 = null;
        BufferedImage tankImage2 = null;
        BufferedImage breakable = null;
        BufferedImage unbreakable = null;
        BufferedImage power= null;
        walls =new ArrayList<>();
        powers =new ArrayList<>();
        try {

            /*
             * There is a subtle difference between using class
             * loaders and File objects to read in resources for your
             * tank games. First, File objects when given to input readers
             * will use your project's working directory as the base path for
             * finding the file. Class loaders will use the class path of the project
             * as the base path for finding files. For Intellij, this will be in the out
             * folder. if you expand the out folder, the expand the production folder, you
             * will find a folder that has the same name as your project. This is the folder
             * where your class path points to by default. Resources, will need to be stored
             * in here for class loaders to load resources correctly.
             *
             * Also one important thing to keep in mind, Java input readers given File objects
             * cannot be used to access file in jar files. So when building your jar, if you want
             * all files to be stored inside the jar, you'll need to class loaders and not File
             * objects.
             *
             */
            //Using class loaders to read in resources
            tankImage1 = read(tankGame.GameWorld.class.getClassLoader().getResource("tank1.png"));
            tankImage2 = read(tankGame.GameWorld.class.getClassLoader().getResource("tank2.png"));
            bulletImage = read(GameWorld.class.getClassLoader().getResource("bullet.png"));
            breakable = read(tankGame.GameWorld.class.getClassLoader().getResource("BWall.png"));
            unbreakable = read(tankGame.GameWorld.class.getClassLoader().getResource("UWall.png"));
            power = read(tankGame.GameWorld.class.getClassLoader().getResource("Health.png"));

            //Using file objects to read in resources.
            //tankImage = read(new File("tank1.png"));

            InputStreamReader isr = new InputStreamReader(GameWorld.class.getClassLoader().getResourceAsStream("maps/map1"));
            BufferedReader mapReader =new BufferedReader(isr);

            String row =mapReader.readLine();
            String[] mapInfo=row.split("\t");
            int numCols = Integer.parseInt(mapInfo[0]);
            int numRows = Integer.parseInt(mapInfo[1]);

            for(int curRow=0;curRow<numRows;curRow++){
                row=mapReader.readLine();
                mapInfo=row.split("\t");
                for(int curCol=0;curCol<numCols;curCol++){
                    switch(mapInfo[curCol]){
                        //breakable wall
                        case "2":
                            this.walls.add(new BreakWall(curCol*50,curRow*50,breakable));
                            break;
                        //health powerup
                        case "4":
                            this.powers.add(new PowerUp(curCol*50,curRow*50,power));
                            break;
                        //unbreakable wall
                        case "3":
                        case "9":
                            this.walls.add(new UnbreakWall(curCol*50,curRow*50,unbreakable));

                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        tankOne = new Tank(450, 450, 0, 0, 0, tankImage1,100,25,3);
        tankTwo = new Tank(1000, 1000, 0, 0, 80, tankImage2,100,25,3);
        cd = new CollisionDetector(tankOne,tankTwo);

        TankControl tankOneControl = new TankControl(tankOne, KeyEvent.VK_W,
                KeyEvent.VK_S,
                KeyEvent.VK_A,
                KeyEvent.VK_D,
                KeyEvent.VK_SPACE);
        TankControl tankTwoControl = new TankControl(tankTwo, KeyEvent.VK_UP,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_LEFT,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_ENTER);

        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.add(this);
        this.jFrame.addKeyListener(tankOneControl);
        this.jFrame.addKeyListener(tankTwoControl);
        this.jFrame.setSize(tankGame.GameWorld.SCREEN_WIDTH, tankGame.GameWorld.SCREEN_HEIGHT );
        this.jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        buffer = world.createGraphics();
        if (tankOne.getLives() > 0 && tankTwo.getLives() > 0) {
            buffer.setColor(Color.darkGray);
            buffer.fillRect(0, 0, GameWorld.WORLD_WIDTH, GameWorld.WORLD_HEIGHT);
            this.walls.forEach(wall -> wall.drawImage(buffer));
            this.powers.forEach(powerUp -> powerUp.drawImage(buffer));

            this.tankOne.drawImage(buffer);
            this.tankTwo.drawImage(buffer);

            //Tank one splitscreen values - LEFT
            // X value
            if (tankOne.getX() - 251 <= 0) {
                leftX = 0;
            } else if (tankOne.getX() + 251 >= WORLD_WIDTH) {
                leftX = WORLD_WIDTH - (SCREEN_WIDTH / 2);
            } else if (tankOne.getX() > 0 && tankOne.getX() < WORLD_WIDTH) {
                leftX = tankOne.getX() - 251;
            }
            //Y value
            if (tankOne.getY() - 320 <= 0) {
                leftY = 0;
            } else if (tankOne.getY() + 360 >= WORLD_HEIGHT) {
                leftY = WORLD_HEIGHT - SCREEN_HEIGHT + 320;
            } else if (tankOne.getY() > 0 && tankOne.getY() < WORLD_HEIGHT) {
                leftY = tankOne.getY() - 320;
            }
            //setting up LEFT split screen using previous values
            BufferedImage left = world.getSubimage(leftX, leftY, GameWorld.SCREEN_WIDTH / 2, GameWorld.SCREEN_HEIGHT - 320);

            //Tank Two splitscreen values - RIGHT
            // X value
            if (tankTwo.getX() - 251 <= 0) {
                rightX = 0;
            } else if (tankTwo.getX() + 251 >= WORLD_WIDTH) {
                rightX = WORLD_WIDTH - (SCREEN_WIDTH / 2);
            } else if (tankTwo.getX() > 0 && tankTwo.getX() < WORLD_WIDTH) {
                rightX = tankTwo.getX() - 251;
            }
            //Y value
            if (tankTwo.getY() - 320 <= 0) {
                rightY = 0;
            } else if (tankTwo.getY() + 360 >= WORLD_HEIGHT) {
                rightY = WORLD_HEIGHT - SCREEN_HEIGHT + 320;
            } else if (tankTwo.getY() > 0 && tankTwo.getY() < WORLD_HEIGHT) {
                rightY = tankTwo.getY() - 320;
            }
            //setting up RIGHT split screen using previous values
            BufferedImage right = world.getSubimage(rightX, rightY, GameWorld.SCREEN_WIDTH / 2, GameWorld.SCREEN_HEIGHT - 320);

            g2.setColor(Color.black);
            g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            //display health and lives
            g2.setColor(Color.white);
            g2.setFont(new Font("TimesNewRoman", Font.BOLD, 25));

            //drawing tank health/lives/controls for both tanks
            g2.drawString("TANK 1", 10, 878);
            g2.drawString("HEALTH: " + tankOne.getHealth(), 10, 904);
            g2.drawString("LIVES: " + tankOne.getLives(), 10, 930);
            g2.drawString("(W,A,S,D,SPACE)", 10, 956);

            g2.drawString("TANK 2", 800, 878);
            g2.drawString("HEALTH: " + tankTwo.getHealth(), 800, 904);
            g2.drawString("LIVES: " + tankTwo.getLives(), 800, 930);
            g2.drawString("(^,<,v,>,ENTER)", 800, 956);


            //drawing left split screen
            g2.drawImage(left, 0, 0, null);
            //drawing right split screen
            g2.drawImage(right, GameWorld.SCREEN_WIDTH / 2 + 4, 0, null);

            //minimap
            BufferedImage mini = world.getSubimage(0, 0, GameWorld.WORLD_WIDTH, WORLD_HEIGHT);
            g2.scale(.20, .20); //scaling the map to 20% to become minimap
            g2.drawImage(mini, 1750, 3400, null);    //drawing minimap

            // checking lives of both tanks
            // if one tanks lives are less than or equal to zero then the game ends
            // and the other rank wins
            if (tankTwo.getHealth() <= 0) {
                tankTwo.setLives(tankTwo.getLives() - 1);
            }
            else if (tankOne.getHealth() <= 0) {
                tankOne.setLives(tankOne.getLives() - 1);
            }
        }
        //prints game over screen when one of the tanks lives is less than or equal to zero
        if (tankOne.getLives() <= 0 || tankTwo.getLives() <= 0) {
            g2.setColor(Color.CYAN);
            g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            g2.setColor(Color.red);
            g2.setFont(new Font("TimesNewRoman", Font.BOLD, 100));
            g2.drawString("GAME OVER", 175, 450);
            g2.setFont(new Font("TimesNewRoman", Font.PLAIN, 50));
            //if tank one has zero lives then tank two wins
            if (tankOne.getLives() <= 0) {
                g2.drawString("Tank 2 wins", 350, 525);
            }
            //if tank two hs zero lives then tank one ones
            else if (tankTwo.getLives() <= 0) {
                g2.drawString("Tank 1 wins", 350, 525);
            }
        }
    }
}
