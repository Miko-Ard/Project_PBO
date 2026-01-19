import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Manages the game's ground level.
 */
public class Level {
    private int groundSurfaceY;
    private final int groundHeight = 250;
    private final int surfaceThickness = 85;
    private Image groundImage;

    public Level() {
        try {
            URL groundUrl = getClass().getResource("/assets/Ground.png");
            if (groundUrl != null) {
                groundImage = new ImageIcon(groundUrl).getImage();
            }
        } catch (Exception e) {
            System.err.println("Failed to load ground image.");
            groundImage = null;
        }
    }

    /**
     * Draws the ground, stretching to the panel's width.
     */
    public void draw(Graphics g, int panelWidth) {
        if (groundImage == null) return;

        int yDraw = groundSurfaceY - (groundHeight - surfaceThickness);
        g.drawImage(groundImage, 0, yDraw, panelWidth, groundHeight, null);
    }

    public int getGroundY() {
        return groundSurfaceY;
    }

    /**
     * Updates the ground's Y position based on the panel's height.
     */
    public void updateGroundY(int panelHeight) {
        groundSurfaceY = panelHeight - surfaceThickness;
    }
}
