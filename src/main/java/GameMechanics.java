
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameMechanics {
    private double normalChance, cloudChance, platformChance, brokenChance, monsterChance, minimalSpeed;
    private int starterPlatforms;
    private GameLevel level;

    GameMechanics() {
        init();
        timer = new Timer(20, new TimerListener());
        timer.start();
        System.out.println("game");
    }

    GameMechanics(JPanel container) {
        this.container = container;
        init();
        timer = new Timer(25, new TimerListener());
        timer.start();
    }

    public void menu() {
        timer.stop();
        Graphics2D gr;
        gr = (Graphics2D) container.getGraphics();
        try {
            gr.drawImage(ImageIO.read(new File("background.png")), 0, 0, container);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        Color my = new Color(190, 29, 243);
        gr.setColor(my);
        gr.fillRect(175, 200, 105, 50);
        gr.fillRect(175, 270, 105, 50);
        gr.fillRect(175, 340, 105, 50);
        gr.setFont(new Font("Arial", Font.BOLD, 15));
        gr.setColor(Color.black);
        gr.drawString("Easy", 210, 230);
        gr.drawString("Medium", 202, 300);
        gr.drawString("Hard", 210, 370);
        timer.stop();
        pause();
    }

    public enum GameLevel {
        easy, medium, hard
    }

    private boolean startMenu;

    public void restart() {
        platforms = new ArrayList<>();
        generateStarterPlatforms();
        monsters = new ArrayList<>();
        doodle.setPlatforms(platforms);
        doodle.setMonsters(monsters);
        jumpSuccesful = false;
        doodle.setY((double) doodle.startY());
        doodle.setX((double) doodle.startX());
        doodle.setGameOver(false);
        score = 0;
        timer.start();
        container.repaint();
        initChances();
        System.out.println("restart");
    }

    public void pause() {
        timer.stop();
        if (!doodle.getGameOver()) {
            Graphics2D gr;
            gr = (Graphics2D) container.getGraphics();
            gr.setColor(Color.gray);
            gr.fillRect(0, 0, 455, 691);
            gr.setColor(Color.BLACK);
            gr.drawString("PAUSE", 195, 300);
            //gr.drawString("Press \"M\" to go to menu", 150, 320);
            gr.drawString("Press \"R\" to restart", 160, 340);
            gr.drawString("Controls:", 190, 370);
            gr.drawString("\"A\" - left", 192, 390);
            gr.drawString("\"D\" - right", 190, 410);
            gr.drawString("\"W\" - shoot", 190, 430);
            gr.drawString("space - pause", 180, 450);
        }
    }

    public void cont() {
        if (!doodle.getGameOver()) {
            timer.start();
            container.repaint();
            System.out.println("continue");
        }
    }

    public void easy() {
        level = GameLevel.easy;
        System.out.println("easy/");
        restart();
    }

    public void hard() {
        level = GameLevel.hard;
        restart();
    }

    public void medium() {
        level = GameLevel.medium;
        restart();
    }

    private void init() {
        random = new Random();
        doodle = new Doodle();
        platforms = new ArrayList<>();
        doodle.setPlatforms(platforms);
        doodle.setGameMechanics(this);
        monsters = new ArrayList<>();
        doodle.setMonsters(monsters);
        jumpSuccesful = false;
        level = GameLevel.medium;
        initChances();
        generateStarterPlatforms();
    }

    private void initChances() {
        System.out.println("chances");
        switch (level) {
            case easy: {
                normalChance = 0.6;
                brokenChance = 0.2;
                cloudChance = 0.3;
                monsterChance = 0.005;
                platformChance = 0.05;
                minimalSpeed = 1;
                starterPlatforms = 15;
                System.out.println("easy");
                break;
            }
            case medium: {
                normalChance = 0.4;
                brokenChance = 0.4;
                cloudChance = 0.3;
                monsterChance = 0.008;
                platformChance = 0.04;
                minimalSpeed = 2;
                starterPlatforms = 13;
                System.out.println("medium");
                break;
            }
            case hard: {
                normalChance = 0.3;
                brokenChance = 0.7;
                cloudChance = 0.2;
                monsterChance = 0.012;
                platformChance = 0.05;
                minimalSpeed = 3;
                starterPlatforms = 11;
                System.out.println("hard");
                break;
            }
        }
    }

    private void createPlatform(double platformType, boolean platformMove) {
        PlatformType type;
        if (platformType <= normalChance) {
            type = PlatformType.normal;
        } else if (platformType <= normalChance + cloudChance) {
            type = PlatformType.cloud;
        } else {
            type = PlatformType.broken;
        }
        System.out.println(type);
        platforms.add(new Platform(type, platformMove, minimalSpeed));
    }

    private void createPlatform(double platformType, boolean platformMove, double y) {
        PlatformType type;
        if (platformType < normalChance) {
            type = PlatformType.normal;
        } else if (platformType < normalChance + cloudChance) {
            type = PlatformType.cloud;
        } else {
            type = PlatformType.broken;
        }
        platforms.add(new Platform(type, platformMove, y));
    }

    private void createMonster() {
        monsters.add(new Monster(random.nextBoolean(), (int) minimalSpeed));
    }

    public Doodle getDoodle() {
        return doodle;
    }

    public JPanel getContainer() {
        return container;
    }

    public void setContainer(JPanel container) {
        this.container = container;
    }

    public ArrayList<Platform> getPlatforms() {
        return platforms;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    private Doodle doodle;
    private ArrayList<Platform> platforms;
    private ArrayList<Monster> monsters;
    private JPanel container;
    private Random random;
    double newPlatform, platformType, newMonster;
    boolean platformMove;
    protected Timer timer;
    private boolean jumpSuccesful;

    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            newPlatform = random.nextDouble();
            platformType = random.nextDouble();
            platformMove = random.nextBoolean();
            newMonster = random.nextDouble();
            timer.setDelay(20);
            if (platforms != null && doodle != null && container != null && monsters != null) {
                if (doodle.isShoot()) {
                    doodle.getWaddles().move();
                    if (doodle.getWaddles().getY() < 0) {
                        doodle.setShoot(false);
                    }
                    int delete = -1;
                    for (int i = 0; i < monsters.size(); i++) {
                        if (doodle.getWaddles().getY() < monsters.get(i).getY() + 125
                                && ((doodle.getWaddles().getX() > monsters.get(i).getX() && doodle.getWaddles().getX() < monsters.get(i).getX() + 81)
                                || (doodle.getWaddles().getX() + 20 > monsters.get(i).getX() && doodle.getWaddles().getX() + 20 < monsters.get(i).getX() + 81))) {
                            delete = i;
                            break;
                        }
                    }
                    if (delete > -1) {
                        if (monsters.get(delete).direction != Move.Direction.still) {
                            setScore(50);
                        } else {
                            setScore(25);
                        }
                        monsters.remove(delete);
                        doodle.setShoot(false);
                    }
                }
                if (doodle.getY() > doodle.startY() - 196 || doodle.getJumpDirection() == Doodle.JumpDirection.down) {
                    doodle.jump();
                } else {
                    for (Platform i : platforms) {
                        i.moveDown(doodle.getJumpProgress());
                    }
                    for (Monster i : monsters) {
                        i.moveDown(doodle.getJumpProgress());
                    }
                    score += 1;
                    doodle.progressJump();
                    if (newPlatform < platformChance) {
                        createPlatform(platformType, platformMove);
                    }
                    if (platforms.get(platforms.size() - 2).getY() > 100) {
                        createPlatform(0.3, platformMove);
                    }
                    if (newMonster < 0.01) {
                        createMonster();
                    }
                }
                for (Platform i : platforms) {
                    i.move();
                }
                for (Monster i : monsters) {
                    i.move();
                }
                if (!doodle.getGameOver()) {
                    container.repaint();
                }
            }
        }
    }

    public boolean isStartMenu() {
        return startMenu;
    }

    public void setStartMenu(boolean startMenu) {
        this.startMenu = startMenu;
    }

    private void generateStarterPlatforms() {
        if (platforms == null) {
            platforms = new ArrayList<>();
        }
        for (int i = 0; i < starterPlatforms; i++) {
            createPlatform(normalChance, random.nextBoolean(), (i + 1) * (691.0 / (starterPlatforms)));
        }
        Platform start = new Platform(PlatformType.normal, false);
        start.setY(doodle.startY() + doodle.getImage().getHeight(null));
        start.setX(doodle.startX());
        start.setDirection(Move.Direction.still);
        platforms.add(start);
    }

    int score;
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score += score;
    }
}

class Doodle {

    private boolean jump;

    public boolean getJump() {
        return jump;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setWaddles(Waddles waddles) {
        this.waddles = waddles;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(int jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public GameMechanics getGameMechanics() {
        return gameMechanics;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getJumpHeight() {
        return jumpHeight;
    }

    public void setJumpProgress(int jumpProgress) {
        this.jumpProgress = jumpProgress;
    }

    public Timer getStop() {
        return stop;
    }

    public ArrayList<Platform> getPlatforms() {
        return platforms;
    }

    public boolean isCollisionPlatform() {
        return collisionPlatform;
    }

    public void setCollisionPlatform(boolean collisionPlatform) {
        this.collisionPlatform = collisionPlatform;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getJumpProgress() {
        return jumpProgress;
    }

    public JumpDirection getJumpDirection() {
        return jumpDirection;
    }

    public void progressJump() {
        if (jumpDirection.equals(JumpDirection.up)) {
            jumpProgress = jumpProgress - 1;
            if (jumpProgress < 0) {
                jumpDirection = JumpDirection.down;
                collisionPlatform = false;
                jumpProgress = 0;
                jump = true;
            }
        } else {
            jumpProgress = jumpProgress + 1;
            if (jumpProgress == 28 && collisionPlatform) {
                jumpDirection = JumpDirection.up;
                jumpProgress = 27;
            } else if (jumpProgress == 28 && y < 691 - image.getHeight(null) + 2) {
                jumpProgress = 27;
            } else if (y > 691 - image.getHeight(null) + 2) {
                jumpProgress = 27;
                fail();
            }
        }
    }

    public void setPlatforms(ArrayList<Platform> platforms) {
        this.platforms = platforms;
    }

    public void collisionPlatforms() {
        ArrayList<Platform> delete = new ArrayList<>();
        for (Platform i : platforms) {
            if (i.getY() > 693) {
                delete.add(i);
            }
            if (x + 50 > i.getX() && x + 16 < i.getX() + 75 && y + image.getHeight(null) >= i.getY()
                    && y + image.getHeight(null) <= i.getY() + 15 && y < 691 - image.getHeight(null) + 2) {
                switch (i.getType()) {
                    case normal: {
                        jumpDirection = JumpDirection.up;
                        jumpProgress = 27;
                        collisionPlatform = true;
                        break;
                    }
                    case broken: {
                        delete.add(i);
                        break;
                    }
                    case cloud: {
                        jumpDirection = JumpDirection.up;
                        jumpProgress = 27;
                        collisionPlatform = true;
                        delete.add(i);
                        break;
                    }
                }
                if (i.getType() == PlatformType.normal || i.getType() == PlatformType.cloud) {
                    break;
                }
            }
        }
        platforms.removeAll(delete);
    }

    public void collisionMonsters() { 
        ArrayList<Monster> delete = new ArrayList<>();
        for (Monster i : monsters) {
            int xm = i.getX();
            int ym = i.getY();
            if (i.getY() > 691) {
                delete.add(i);
            }
            if (((x > xm + 36 && x < xm + 46 || x + 75 > xm + 36 && x + 75 < xm + 46)
                    || (x > xm + 18 && x < xm + 62 || x + 75 > xm + 18 && x + 75 < xm + 62)
                    || (x > xm + 0 && x < xm + 81 || x + 75 > xm + 0 && x + 75 < xm + 81)
                    || (x > xm + 10 && x < xm + 70 || x + 75 > xm + 10 && x + 75 < xm + 70))
                    && (y > ym && y < ym + 125 || y + image.getHeight(null) > ym && y + image.getHeight(null) < ym + 125)) {
                failMonster();
                gameMechanics.timer.stop();
            }
        }
        monsters.removeAll(delete);
    }

    public void failMonster() {
        fail();
    }

    public void fail() {
        gameMechanics.timer.stop();
        Graphics2D gr = (Graphics2D) gameMechanics.getContainer().getGraphics();
        gr.setColor(Color.BLACK);
        gr.fillRect(0, 0, 455, 691);
        gr.setColor(Color.white);
        gr.drawString("YOU LOST", 190, 300);
        gr.drawString("SCORE : " + gameMechanics.getScore(), 190, 320);
        gr.setColor(Color.lightGray);
        gr.drawString("press any key to restart", 160, 350);
        gr.drawString("Press \"M\" to go to menu", 160, 370);
        gameOver = true;
    }

    public void jump() {
        if (jumpDirection.equals(JumpDirection.up)) {
            y = y - (jumpProgress * 2 + 1) / 5;
        } else {
            y = y + (jumpProgress * 2 + 1) / 5;
        }
        progressJump();
        collisionPlatforms();
        collisionMonsters();
    }

    public int startX() {
        return 190;
    }

    public int startY() {
        return 467;
    }

    public void shoot() {
        shoot = true;
        waddles.setY(y);
        waddles.setX(x + 28);
    }

    private boolean shoot;
    private Waddles waddles;

    public boolean isShoot() {
        return shoot;
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    Doodle() {
        shoot = false;
        jumpDirection = JumpDirection.up;
        try {
            image = ImageIO.read(new File("aured.png"));
        } catch (Exception e) {

        }
        x = (double) startX();
        y = (double) startY();
        gameOver = false;
        jumpProgress = 27;
        waddles = new Waddles();
    }

    public Waddles getWaddles() {
        return waddles;
    }

    public void moveLeft() {
        x -= 5;
        collision();
    }

    public void moveRight() {
        x += 5;
        collision();
    }

    private void collision() {
        if (x > 379) {
            x = 379;

        }
        if (x < 0) {
            x = 0;
        }
    }

    public Image getImage() {
        return image;
    }

    public void setStop(Timer stop) {
        this.stop = stop;
    }

    public void setGameMechanics(GameMechanics gameMechanics) {
        this.gameMechanics = gameMechanics;
    }

    public void setJumpDirection(JumpDirection jumpDirection) {
        this.jumpDirection = jumpDirection;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public void setMonsters(ArrayList<Monster> monsters) {
        this.monsters = monsters;
    }

    public enum JumpDirection {
        up, down
    }

    private boolean gameOver;
    private JumpDirection jumpDirection;
    private int jumpSpeed;
    double x, y;
    private GameMechanics gameMechanics;
    private Image image;
    private final int jumpHeight = 196;
    private int jumpProgress;
    private Timer stop;
    private ArrayList<Platform> platforms;
    private boolean collisionPlatform;
    private ArrayList<Monster> monsters;
}

class Platform extends Move {
    Platform(PlatformType type, boolean move) {
        this.move = move;
        this.type = type;
        speed = new Random().nextInt(5) + 1;
        init();
        initImage(type);
    }

    Platform() {
        init();
    }

    Platform(PlatformType type, boolean move, double y) {
        this.move = move;
        this.type = type;
        speed = new Random().nextInt(5) + 1;
        init();
        this.y = y;
        initImage(type);
    }

    Platform(PlatformType type, boolean move, double y, int minSpeed) {
        this.move = move;
        this.type = type;
        speed = new Random().nextInt(5) + minSpeed;
        init();
        this.y = y;
        initImage(type);
    }

    private void init() {
        x = (double) (new Random()).nextInt(375);
        y = -15.0;
        if (move) {
            if (new Random().nextBoolean()) {
                direction = Direction.left;
            } else {
                direction = Direction.right;
            }
        } else {
            direction = Direction.still;
        }
    }

    private void initImage(PlatformType type) {
        switch (type) {
            case cloud: {
                try {
                    image = ImageIO.read(new File("cloudplatform.png"));
                } catch (IOException e) {
                }
                break;
            }
            case broken: {
                try {
                    image = ImageIO.read(new File("brokenplatform.png"));
                } catch (IOException e) {
                }
                break;
            }
            case normal: {
                try {
                    image = ImageIO.read(new File("normalplatform.png"));
                } catch (IOException e) {
                }
                break;
            }
            default: {
                try {
                    image = ImageIO.read(new File("normalplatform.png"));
                } catch (IOException e) {
                }
                break;
            }
        }
    }

    public Image getImage() {
        return image;
    }

    public void setType(PlatformType type) {
        this.type = type;
    }

    public PlatformType getType() {
        return type;
    }
    private PlatformType type;
}

enum PlatformType {
    cloud, normal, broken;
}

abstract class Move {

    protected Image image;
    protected Direction direction;
    protected double speed;
    protected double x, y;
    protected boolean move;

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Image getImage() {
        return image;
    }

    public void moveDown(int down) {
        y = y + (down * 2 + 1) / 5;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public enum Direction {
        left, right, still
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    private void collision() {
        if (x > 367) {
            direction = Direction.left;
        }
        if (x < 0) {
            direction = Direction.right;
        }
    }

    public void move() {
        switch (direction) {
            case left: {
                x -= speed;
                break;
            }
            case right: {
                x += speed;
                break;
            }
        }
        collision();
    }
}

class Monster extends Move {
    Monster() {
        init();
    }

    Monster(boolean move) {
        init();
        if (!move) {
            direction = Direction.still;
        }
        speed = new Random().nextInt(5) + 1;
    }

    Monster(boolean move, int minSpeed) {
        init();
        if (!move) {
            direction = Direction.still;
        }
        speed = new Random().nextInt(5) + minSpeed;
    }

    private void init() {
        try {
            image = ImageIO.read(new File("bill.png"));
        } catch (IOException e) {
        }
        if (new Random().nextBoolean()) {
            direction = Direction.left;
        } else {
            direction = Direction.right;
        }
        x = new Random().nextInt(300);
        y = -150;
    }
}

class Waddles {
    private Image image;
    Waddles() {
        init();
    }

    public void move() {
        y -= 10;
    }

    private void init() {
        try {
            image = ImageIO.read(new File("shot.png"));
        } catch (IOException e) {
        }
    }

    private double x, y;
    public void setImage(Image image) {
        this.image = image;
    }

    public int getX() {
        return (int) x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getY() {
        return (int) y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Image getImage() {
        return image;
    }
}
