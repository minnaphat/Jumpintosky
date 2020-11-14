
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class GamingPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        if (!gameMechanics.isStartMenu()) {
            canvas = (Graphics2D) g;
            drawBackground();
            drawPlatforms();
            drawMonsters();
            drawWaddles();
            drawDoodle();
            drawScore();
        }
    }
    private Graphics2D canvas;

    GamingPanel() {
        gameMechanics = new GameMechanics();
    }

    GamingPanel(GameMechanics gm) {
        gameMechanics = gm;
        gameMechanics.setContainer(this);
    }

    private void drawScore() {
        canvas.setColor(Color.BLACK);
        canvas.setStroke(new BasicStroke(5));
        canvas.setFont(new Font("Arial", Font.BOLD, 30));
        canvas.drawString(gameMechanics.getScore() + "", 350, 50);
        canvas.setFont(new Font("Arial", Font.BOLD, 1));
        canvas.setColor(Color.WHITE);
        canvas.setStroke(new BasicStroke(2));
        canvas.drawString(gameMechanics.getScore() + "", 400, 50);
    }

    private void drawDoodle() {
        canvas.drawImage(gameMechanics.getDoodle().getImage(), gameMechanics.getDoodle().getX(), gameMechanics.getDoodle().getY(), this);
    }

    private void drawBackground() {
        if (background == null) try {
            background = ImageIO.read(new File("ingamebg.png"));
        } catch (Exception e) {
            System.out.println("exception");
        }
        canvas.drawImage(background, 0, 0, this);
    }

    private void drawPlatforms() {
        for (Platform i : gameMechanics.getPlatforms()) {
            canvas.drawImage(i.getImage(), i.getX(), i.getY(), this);
        }
    }

    public void drawWaddles() {
        Waddles waddles = gameMechanics.getDoodle().getWaddles();
        if (gameMechanics.getDoodle().isShoot()) {
            canvas.drawImage(waddles.getImage(), waddles.getX(), waddles.getY(), this);
        }
    }

    private void drawMonsters() {
        for (Monster i : gameMechanics.getMonsters()) {
            canvas.drawImage(i.getImage(), i.getX(), i.getY(), this);
        }
    }
    private GameMechanics gameMechanics;
    private ArrayList<Platform> platforms;
    private Image background;
}

class MenuPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("menu");
        Graphics2D gr = (Graphics2D) g;
        try {
            gr.drawImage(ImageIO.read(new File("background.png")), 0, 0, this);
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
        System.out.println("kek");
    }
}
