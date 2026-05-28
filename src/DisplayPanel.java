import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DisplayPanel extends JPanel implements MouseListener, KeyListener, ActionListener {
    private boolean yellowColor;
    private BufferedImage background;
    private BufferedImage darkTroop;
    private BufferedImage lightTroop;
    private BufferedImage health;
    private BufferedImage strength;
    private int darkTroopX;
    private int darkTroopY;
    private int lightTroopX;
    private int lightTroopY;
    private int darkHealth;
    private int lightHealth;
    private int darkStrength;
    private int lightStrength;
    private boolean[] pressedKeys;
    private ArrayList<Point> buffs;
    private Timer timer;

    public DisplayPanel() {
        yellowColor = true;
        darkTroopX = 10;
        darkTroopY = 200;
        lightTroopX = 800;
        lightTroopY = 200;
        darkHealth = 100;
        lightHealth = 100;
        darkStrength = 1;
        lightStrength = 1;
        pressedKeys = new boolean[128];
        buffs = new ArrayList<>();
        timer = new Timer(10,this);
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
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above
        timer.start();
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
        g.drawString("Dark Health: " + darkHealth, 50, 30);
        g.drawString("Light Health: " + lightHealth, 50, 50);
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
        if (pressedKeys[KeyEvent.VK_A]) {
            darkTroopX -= 5;
        }
        if (pressedKeys[KeyEvent.VK_D]) {
            darkTroopX += 5;
        }
        if (pressedKeys[KeyEvent.VK_W]) {
            darkTroopY -= 5;
        }
        if (pressedKeys[KeyEvent.VK_S]) {
            darkTroopY += 5;
        }
        repaint();
    }

    private void moveLightTroop() {
        if (pressedKeys[KeyEvent.VK_LEFT]) {
            lightTroopX -= 5;
        }
        if (pressedKeys[KeyEvent.VK_RIGHT]) {
            lightTroopX += 5;
        }
        if (pressedKeys[KeyEvent.VK_UP]) {
            lightTroopY -= 5;
        }
        if (pressedKeys[KeyEvent.VK_DOWN]) {
            lightTroopY += 5;
        }
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

    private boolean checkForDarkLightCollision() {
        Rectangle darkRect = darkRect();
        Rectangle lightRect = lightRect();
        return darkRect.intersects(lightRect);
    }

    private void attack() {
        if (checkForDarkLightCollision()) {
            if (pressedKeys[KeyEvent.VK_E]) {
                lightHealth -= darkStrength;
            }
            if (pressedKeys[KeyEvent.VK_L]) {
                darkHealth -= lightStrength;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveDarkTroop();
        moveLightTroop();
        repaint();
    }
}