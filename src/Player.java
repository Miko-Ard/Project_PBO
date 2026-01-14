import java.awt.*;
import javax.swing.*;

public class Player {

    private int x, y;
    private int width = 60, height = 60; // 🔥 DIBESARIN
    private int dx = 0, dy = 0;
    private boolean onGround = false;

    private Image image;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;

        image = new ImageIcon("assets/mario.png").getImage();
    }

    public void update(Level level) {
        x += dx;
        y += dy;
        dy += 1; // gravity

        if (y >= level.getGroundY() - height) {
            y = level.getGroundY() - height;
            dy = 0;
            onGround = true;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

    public void moveRight() { dx = 5; }
    public void moveLeft() { dx = -5; }
    public void stop() { dx = 0; }

    public void jump() {
        if (onGround) {
            dy = -15;
            onGround = false;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getDY() {
        return dy;
    }

    public void bounce() {
        dy = -10;
    }
}
