import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserInterace extends JFrame {
    private GamingPanel gp;
    private boolean pause;
    private GameMechanics game;
    JButton restart;
    private MenuPanel menuPanel;

    UserInterace() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(455, 691);
        setLocationRelativeTo(null);
        this.addKeyListener(new PauseListener());
        this.addKeyListener(new DoodleListener());
        this.setTitle("Jumping into Sky");
        this.addKeyListener(new StartMenuListener());
        this.addMouseListener(new MenuButtonListener());
        this.setResizable(false);
        this.setIconImage(new ImageIcon("icon.png").getImage());
        init();
        add(menuPanel);
    }
    private class MenuButtonListener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (startMenu){
                if (new Rectangle(175, 200, 105, 50).contains(e.getPoint())) {
                    System.out.println("EASY");
                    menuPanel.setVisible(false);
                    getThis().add(gp);
                    getThis().repaint();
                    gp.repaint();
                    pause = false;
                    startMenu = false;
                    game.easy();
                }
                if (new Rectangle(175, 270, 105, 50).contains(e.getPoint())) {
                    menuPanel.setVisible(false);
                    getThis().add(gp);
                    getThis().repaint();
                    gp.repaint();
                    pause = false;
                    startMenu = false;
                    System.out.println("MEDIUM");
                    game.medium();
                }
                if (new Rectangle(175, 340, 105, 50).contains(e.getPoint())) {
                    System.out.println("HARD");
                    menuPanel.setVisible(false);
                    getThis().add(gp);
                    getThis().repaint();
                    gp.repaint();
                    pause = false;
                    startMenu = false;
                    game.hard();
                }
            }
            if (menu){
                if (new Rectangle(175, 200, 105, 50).contains(e.getPoint())) {
                    game.easy();
                }
                if (new Rectangle(175, 270, 105, 50).contains(e.getPoint())) {
                    game.medium();
                }
                if (new Rectangle(175, 340, 105, 50).contains(e.getPoint())) {
                    game.hard();
                }
            }
        }
    }
    

    private class StartMenuListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            if (startMenu) {
                if (e.getKeyChar() == 'e') {
                    System.out.println("EASY");
                    menuPanel.setVisible(false);
                    getThis().add(gp);
                    getThis().repaint();
                    gp.repaint();
                    pause = false;
                    startMenu = false;
                    game.easy();
                }
                if (e.getKeyChar() == 'm') {
                    menuPanel.setVisible(false);
                    getThis().add(gp);
                    getThis().repaint();
                    gp.repaint();
                    pause = false;
                    startMenu = false;
                    System.out.println("MEDIUM");
                    game.medium();
                }
                if (e.getKeyChar() == 'h') {
                    System.out.println("HARD");
                    menuPanel.setVisible(false);
                    getThis().add(gp);
                    getThis().repaint();
                    gp.repaint();
                    pause = false;
                    startMenu = false;
                    game.hard();
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private JFrame getThis() {
        return this;
    }

    private boolean startMenu;

    private void init() {
        game = new GameMechanics();
        gp = new GamingPanel(game);
        startMenu = true;
        menuPanel = new MenuPanel();
        pause = true;
    }

    public class DoodleListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            if (!startMenu && !menu)
                if ((e.getKeyChar() == 'w' || e.getKeyChar() == 'W') && !pause && !game.getDoodle().isShoot())
                    game.getDoodle().shoot();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!startMenu && !menu) {
                if ((e.getKeyChar() == 'd' || e.getKeyChar() == 'D') && !pause) game.getDoodle().moveRight();
                else if ((e.getKeyChar() == 'a' || e.getKeyChar() == 'A') && !pause) game.getDoodle().moveLeft();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private class PauseListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

            if (!startMenu && !menu) {
                if (e.getKeyChar() == ' ' && !pause && !game.getDoodle().getGameOver()) {
                    game.pause();
                    pause = true;
                } else if (e.getKeyChar() == ' ' && !game.getDoodle().getGameOver() && pause) {
                    game.cont();
                    pause = false;
                } else if ((e.getKeyChar() == 'm' || e.getKeyChar() == 'M') && (pause || game.getDoodle().getGameOver())) {
                    menu = true;
                    game.pause();
                    game.menu();
                    System.out.println("Menu");
                } else if ((e.getKeyChar() == 'r' || e.getKeyChar() == 'R') && (pause || game.getDoodle().getGameOver())) {
                    game.restart();
                } else if (game.getDoodle().getGameOver()) {
                    game.restart();
                }
            } else if (menu) {
                if (e.getKeyChar()=='e') game.easy();
                if (e.getKeyChar()=='m') game.medium();
                if (e.getKeyChar()=='h') game.hard();
                menu=false;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private boolean menu;
}