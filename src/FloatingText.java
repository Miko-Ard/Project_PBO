import java.awt.*;

/**
 * Represents a piece of text that floats up and fades out on the screen.
 * Used for score pop-ups, kill counts, etc.
 */
public class FloatingText {
    private String text;
    private int x, y;
    private int life;
    private final int maxLife;
    private Color color;
    private Font font;
    
    public FloatingText(String text, int x, int y, int duration, Color color, Font font) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.maxLife = duration;
        this.life = duration;
        this.color = color;
        this.font = font;
    }
    
    /**
     * Updates the text's position and transparency.
     */
    public void update() {
        if (isAlive()) {
            life--;
            y -= 2; // Move upward
        }
    }
    
    public boolean isAlive() {
        return life > 0;
    }
    
    /**
     * Draws the text with a fade-out effect and an outline.
     */
    public void draw(Graphics g) {
        if (!isAlive()) return;
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        float alpha = (float) life / maxLife;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, alpha)));
        
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int drawX = x - textWidth / 2;
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, drawX + 1, y + 1);
        g2d.drawString(text, drawX - 1, y - 1);
        g2d.drawString(text, drawX + 1, y - 1);
        g2d.drawString(text, drawX - 1, y + 1);
        
        // Draw main text
        g2d.setColor(color);
        g2d.drawString(text, drawX, y);
        
        g2d.dispose();
    }
}
