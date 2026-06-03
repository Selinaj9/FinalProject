import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.BiFunction;

public class DisplayPanel extends JPanel implements MouseListener, KeyListener, ActionListener {
    private boolean yellowColor;
    private BufferedImage background;
    private BufferedImage darkTroop;
    private BufferedImage lightTroop;
    private BufferedImage health;
    private BufferedImage strength;
    private BufferedImage debuff;
    private BufferedImage poison;
    private BufferedImage weaken;
    private int darkTroopX;
    private int darkTroopY;
    private int lightTroopX;
    private int lightTroopY;
    private int darkHealth;
    private int lightHealth;
    private int darkStrength;
    private int lightStrength;
    private boolean[] pressedKeys;
    private ArrayList<Point> healths;
    private ArrayList<Point> strengths;
    private ArrayList<Point> debuffs;
    private ArrayList<Point> poisons;
    private ArrayList<Point> weakens;
    private boolean gameOver;
    private Timer timer;
    private Timer attackTimer;
    private Timer buffTimer;
    private JButton resetButton;

    public DisplayPanel() {
        yellowColor = true;
        darkTroopX = 10;
        darkTroopY = 200;
        lightTroopX = 790;
        lightTroopY = 200;
        darkHealth = 100;
        lightHealth = 100;
        darkStrength = 1;
        lightStrength = 1;
        pressedKeys = new boolean[128];
        healths = new ArrayList<>();
        strengths = new ArrayList<>();
        debuffs = new ArrayList<>();
        poisons = new ArrayList<>();
        weakens = new ArrayList<>();
        gameOver = false;
        timer = new Timer(10,this);
        attackTimer = new Timer(50, this);
        buffTimer = new Timer(1500, this);
        try {
            background = ImageIO.read(new File("src/background.jpg"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            darkTroop = ImageIO.read(new File("src/darktroop.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            lightTroop = ImageIO.read(new File("src/lighttroop.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            strength = ImageIO.read(new File("src/strength.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            health = ImageIO.read(new File("src/health.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            debuff = ImageIO.read(new File("src/debuff.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            poison = ImageIO.read(new File("src/poison.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            weaken = ImageIO.read(new File("src/weaken.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        resetButton = new JButton("Reset");
        add(resetButton);
        resetButton.addActionListener(this);
        resetButton.setVisible(false);

        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above
        timer.start();
        attackTimer.start();
        buffTimer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        g.drawImage(darkTroop,darkTroopX, darkTroopY,null);
        g.drawImage(lightTroop, lightTroopX, lightTroopY, null);

        // set font and color of text
        g.setFont(new Font("Arial", Font.BOLD, 16));
        if (yellowColor) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawString("Dark Health: " + darkHealth, darkTroopX - 10, darkTroopY - 15);
        g.drawString("Dark Strength: " + darkStrength, darkTroopX - 10, darkTroopY);
        g.drawString("Light Health: " + lightHealth, lightTroopX - 30, lightTroopY - 15);
        g.drawString("Light Strength: " + lightStrength, lightTroopX - 30, lightTroopY);
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 32));
            if (lightHealth <= 0) {
                g.drawString("GAME OVER, Dark WINS!", 250, 240);
            } else if (darkHealth <= 0) {
                g.drawString("GAME OVER, Light WINS", 250, 240);
            }
            resetButton.setVisible(true);
            resetButton.setLocation(40, 60);
        }

        for (Point h : healths) {
            g.drawImage(health, h.x, h.y, null);
        }
        for (Point s : strengths) {
            g.drawImage(strength, s.x, s.y, null);
        }
        for (Point d : debuffs) {
            g.drawImage(debuff, d.x, d.y, null);
        }
        for (Point p : poisons) {
            g.drawImage(poison, p.x, p.y, null);
        }
        for (Point w : weakens) {
            g.drawImage(weaken, w.x, w.y, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { } // unimplemented
    // unimplemented because if you move your mouse while clicking, this method isn't
    // called, so mouseReleased is best

    @Override
    public void mousePressed(MouseEvent e) { } // unimplemented

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) { } // unimplemented

    @Override
    public void mouseExited(MouseEvent e) { } // unimplemented

    @Override
    public void keyTyped(KeyEvent e) {
        attack();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys[keyCode] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    private void moveDarkTroop() {
        if (darkTroopX != 10) {
            if (pressedKeys[KeyEvent.VK_A]) {
                darkTroopX -= 5;
            }
        }
        if (darkTroopX != 790) {
            if (pressedKeys[KeyEvent.VK_D]) {
                darkTroopX += 5;
            }
        }
        if (darkTroopY != 30) {
            if (pressedKeys[KeyEvent.VK_W]) {
                darkTroopY -= 5;
            }
        }
        if (darkTroopY != 370) {
            if (pressedKeys[KeyEvent.VK_S]) {
                darkTroopY += 5;
            }
        }
        repaint();
    }

    private void moveLightTroop() {
        if (lightTroopX != 10) {
            if (pressedKeys[KeyEvent.VK_LEFT]) {
                lightTroopX -= 5;
            }
        }
        if (lightTroopX != 790) {
            if (pressedKeys[KeyEvent.VK_RIGHT]) {
                lightTroopX += 5;
            }
        }
        if (lightTroopY != 30) {
            if (pressedKeys[KeyEvent.VK_UP]) {
                lightTroopY -= 5;
            }
        }
        if (lightTroopY != 370) {
            if (pressedKeys[KeyEvent.VK_DOWN]) {
                lightTroopY += 5;
            }
        }
    }

    private void createBuffs() {
        int x = (int) (Math.random() * 780 + 10);
        int y = (int) (Math.random() * 340 + 30);
        Point point = new Point(x,y);
        int ran = (int) (Math.random() * 5 + 1);
        if (ran == 1) {
            strengths.add(point);
        } else if (ran == 2) {
            healths.add(point);
        } else if (ran == 3) {
            debuffs.add(point);
        } else if (ran == 4) {
            poisons.add(point);
        } else if (ran == 5) {
            weakens.add(point);
        }
        repaint();
    }

    private Rectangle darkRect() {
        int imgH = darkTroop.getHeight();
        int imgW = darkTroop.getWidth();
        Rectangle r = new Rectangle(darkTroopX, darkTroopY, imgW, imgH);
        return r;
    }

    private Rectangle lightRect() {
        int imgH = lightTroop.getHeight();
        int imgW = lightTroop.getWidth();
        Rectangle r = new Rectangle(lightTroopX, lightTroopY, imgW, imgH);
        return r;
    }

    private Rectangle buffRect(Point point) {
        int imgH = health.getHeight();
        int imgW = health.getWidth();
        Rectangle r = new Rectangle(point.x, point.y, imgW, imgH);
        return r;
    }

    private Rectangle darkHitBox() {
        return new Rectangle(darkTroopX + 75, darkTroopY + 25, 25, 25);
    }

    private Rectangle lightHitBox() {
        return new Rectangle(lightTroopX - 10, lightTroopY + 20, 25, 25);
    }

    private boolean checkForDarkLightCollision() {
        Rectangle darkRect = darkRect();
        Rectangle lightHit = lightHitBox();
        return darkRect.intersects(lightHit);
    }

    private boolean checkForLightDarkCollison() {
        Rectangle darkHit = darkHitBox();
        Rectangle lightRect = lightRect();
        return lightRect.intersects(darkHit);
    }

    private void checkForDarkBuffCollision() {
        Rectangle darkRect = darkRect();
        for (int i = 0; i < strengths.size(); i++) {
            Rectangle buffRect = buffRect(strengths.get(i));
            if (darkRect.intersects(buffRect)) {
                darkStrength += (int) (Math.random() * 10 + 1);
                strengths.remove(i);
                i--;
            }
        }
        for (int i = 0; i < healths.size(); i++) {
            Rectangle buffRect = buffRect(healths.get(i));
            if (darkRect.intersects(buffRect)) {
                darkHealth += (int) (Math.random() * 10 + 1);
                healths.remove(i);
                i--;
            }
        }
        for (int i = 0; i < debuffs.size(); i++) {
            Rectangle buffRect = buffRect(debuffs.get(i));
            if (darkRect.intersects(buffRect)) {
                darkHealth -= (int) (Math.random() * (darkHealth - 10) + 1);
                darkStrength -= (int) (Math.random() * (darkStrength - 10) + 1);
                debuffs.remove(i);
                i--;
                if (darkStrength < 1) {
                    darkStrength = 1;
                }
            }
        }
        for (int i = 0; i < poisons.size(); i++) {
            Rectangle buffRect = buffRect(poisons.get(i));
            if (darkRect.intersects(buffRect)) {
                darkHealth -= (int) (Math.random() * 10 + 1);
                poisons.remove(i);
                i--;
            }
        }
        for (int i = 0; i < weakens.size(); i++) {
            Rectangle buffRect = buffRect(weakens.get(i));
            if (darkRect.intersects(buffRect)) {
                darkStrength -= (int) (Math.random() * 10 + 1);
                weakens.remove(i);
                i--;
                if (darkStrength < 1) {
                    darkStrength = 1;
                }
            }
        }
    }

    private void checkForLightBuffCollision() {
        Rectangle lightRect = lightRect();
        for (int i = 0; i < strengths.size(); i++) {
            Rectangle buffRect = buffRect(strengths.get(i));
            if (lightRect.intersects(buffRect)) {
                lightStrength += (int) (Math.random() * 10 + 1);
                strengths.remove(i);
                i--;
            }
        }
        for (int i = 0; i < healths.size(); i++) {
            Rectangle buffRect = buffRect(healths.get(i));
            if (lightRect.intersects(buffRect)) {
                lightHealth += (int) (Math.random() * 10 + 1);
                healths.remove(i);
                i--;
            }
        }
        for (int i = 0; i < debuffs.size(); i++) {
            Rectangle buffRect = buffRect(debuffs.get(i));
            if (lightRect.intersects(buffRect)) {
                lightHealth -= (int) (Math.random() * (lightHealth - 10) + 1);
                lightStrength -= (int) (Math.random() * (lightStrength - 10) + 1);
                debuffs.remove(i);
                i--;
                if (lightStrength < 1) {
                    lightStrength = 1;
                }
            }
        }
        for (int i = 0; i < poisons.size(); i++) {
            Rectangle buffRect = buffRect(poisons.get(i));
            if (lightRect.intersects(buffRect)) {
                lightHealth -= (int) (Math.random() * 10 + 1);
                poisons.remove(i);
                i--;
            }
        }
        for (int i = 0; i < weakens.size(); i++) {
            Rectangle buffRect = buffRect(weakens.get(i));
            if (lightRect.intersects(buffRect)) {
                lightStrength -= (int) (Math.random() * 10 + 1);
                weakens.remove(i);
                i--;
                if (lightStrength < 1) {
                    lightStrength = 1;
                }
            }
        }
    }

    private void attack() {
        if (!gameOver) {
            if (checkForDarkLightCollision()) {
                if (pressedKeys[KeyEvent.VK_E]) {
                    lightHealth -= darkStrength;
                }
            }
            if (checkForLightDarkCollison()) {
                if (pressedKeys[KeyEvent.VK_L]) {
                    darkHealth -= lightStrength;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            moveDarkTroop();
            moveLightTroop();
            if (lightHealth <= 0 || darkHealth <= 0) {
                gameOver = true;
                timer.stop();
                attackTimer.stop();
                buffTimer.stop();
            }
            repaint();
        }
        if (e.getSource() == attackTimer) {
            checkForDarkBuffCollision();
            checkForLightBuffCollision();
        }
        if (e.getSource() == buffTimer) {
            createBuffs();
        }
        if (e.getSource() == resetButton) {
            reset();
        }
    }

    private void reset() {
        gameOver = false;
        darkTroopX = 10;
        darkTroopY = 200;
        lightTroopX = 790;
        lightTroopY = 200;
        darkHealth = 100;
        lightHealth = 100;
        darkStrength = 1;
        lightStrength = 1;
        healths = new ArrayList<>();
        strengths = new ArrayList<>();
        debuffs = new ArrayList<>();
        poisons = new ArrayList<>();
        weakens = new ArrayList<>();
        requestFocusInWindow();  // must request focus since clicking the JButton shifts key focus from the DisplayPanel to the JFrame
        timer.start();
        attackTimer.start();
        buffTimer.start();
    }
}