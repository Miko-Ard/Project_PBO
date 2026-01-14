import java.awt.*;
import javax.swing.*;

public class Level {

    private int groundY = 550; // 🔥 TANAH LEBIH NAIK
    private Image groundImage;

    public Level() {
        groundImage = new ImageIcon("assets/ground.png").getImage();
    }

    public void draw(Graphics g) {
        g.drawImage(groundImage, 0, groundY, 1800, 150, null);
    }

    public int getGroundY() {
        return groundY;
    }
}
