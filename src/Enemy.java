import java.awt.*;
import javax.swing.*;

public class Enemy {

    private int x, y;
    private int startX, startY;
    private int width = 60, height = 60; // 🔥 DIBESARIN
    private int dx = 2;
    private boolean alive = true;

    private Image image;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;

        image = new ImageIcon("assets/Bahlil.png").getImage();
    }

    public void update() {
        if (!alive) return;
        x += dx;
        if (x <= 0 || x >= 840) dx *= -1; // 🔥 SESUAI WINDOW 900
    }

    public void draw(Graphics g) {
        if (!alive) return;
        g.drawImage(image, x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }

    public void respawn() {
        alive = true;
        x = startX;
        y = startY;
    }
}
