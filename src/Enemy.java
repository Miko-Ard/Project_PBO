import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Enemy {

    private enum AnimationState {
        IDLE, RUN, DIE
    }

    private int x, y;
    private int width = 120, height = 120;
    private int hitboxWidth = 60, hitboxHeight = 60;
    private int dx = 2;
    private int dy = 0;
    private boolean alive = true;
    private boolean facingRight = true;

    private AnimationState currentState = AnimationState.IDLE;
    private int dieAnimTimer = 0;

    private Image idleSheet, runSheet, dieSheet;

    private final int idleFrameCount = 8;
    private final int runFrameCount = 8;
    private final int dieFrameCount = 15;

    private final int idleAnimSpeed = 8;
    private final int runAnimSpeed = 2;
    private final int dieAnimSpeed = 3;

    private int frameIndex = 0;
    private int animCounter = 0;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        loadSpriteSheets();
    }
    
    private void loadSpriteSheets() {
        try {
            URL idleUrl = getClass().getResource("/assets/Mushroom-Idle.png");
            if (idleUrl != null) idleSheet = new ImageIcon(idleUrl).getImage();
            
            URL runUrl = getClass().getResource("/assets/Mushroom-Run.png");
            if (runUrl != null) runSheet = new ImageIcon(runUrl).getImage();
            
            URL dieUrl = getClass().getResource("/assets/Mushroom-Die.png");
            if (dieUrl != null) dieSheet = new ImageIcon(dieUrl).getImage();
        } catch (Exception e) {
            System.err.println("Failed to load enemy sprite sheets.");
            e.printStackTrace();
        }
    }

    public void update(Level level) {
        if (!alive) {
            if (dieAnimTimer > 0) {
                dieAnimTimer--;
                updateAnimation();
            }
            return;
        }
        
        x += dx;
        if (x <= 0 || x + width >= GameFrame.WIDTH) {
            dx *= -1;
        }
        facingRight = (dx > 0);

        dy += 1; // Gravity
        y += dy;

        int groundY = level.getGroundY();
        if (y >= groundY - height) {
            y = groundY - height;
            dy = 0;
        }

        currentState = (dx != 0) ? AnimationState.RUN : AnimationState.IDLE;
        updateAnimation();
    }

    private void updateAnimation() {
        animCounter++;
        int currentAnimSpeed = 1;
        int currentFrameCount = 1;

        switch (currentState) {
            case IDLE:
                currentAnimSpeed = idleAnimSpeed;
                currentFrameCount = idleFrameCount;
                break;
            case RUN:
                currentAnimSpeed = runAnimSpeed;
                currentFrameCount = runFrameCount;
                break;
            case DIE:
                currentAnimSpeed = dieAnimSpeed;
                currentFrameCount = dieFrameCount;
                break;
        }

        if (animCounter >= currentAnimSpeed) {
            animCounter = 0;
            if (currentState == AnimationState.DIE) {
                if (frameIndex < currentFrameCount - 1) {
                    frameIndex++;
                }
            } else {
                frameIndex = (frameIndex + 1) % currentFrameCount;
            }
        }
    }

    public void draw(Graphics g) {
        Image currentSheet = getCurrentImageSheet();
        int currentFrameCount = getCurrentFrameCount();

        if (currentSheet == null || currentSheet.getWidth(null) <= 0) {
            return;
        }

        int frameW = currentSheet.getWidth(null) / currentFrameCount;
        int sx1 = frameIndex * frameW;

        Graphics2D g2d = (Graphics2D) g.create();
        if (facingRight) {
            g2d.drawImage(currentSheet, x, y, x + width, y + height, sx1, 0, sx1 + frameW, currentSheet.getHeight(null), null);
        } else {
            g2d.drawImage(currentSheet, x + width, y, x, y + height, sx1, 0, sx1 + frameW, currentSheet.getHeight(null), null);
        }
        g2d.dispose();
    }

    private Image getCurrentImageSheet() {
        if (!alive && dieAnimTimer > 0) {
            return dieSheet;
        }
        if (alive) {
            switch (currentState) {
                case IDLE: return idleSheet;
                case RUN: return runSheet;
                default: return idleSheet;
            }
        }
        return null;
    }

    private int getCurrentFrameCount() {
        if (!alive && dieAnimTimer > 0) {
            return dieFrameCount;
        }
        if (alive) {
            switch (currentState) {
                case IDLE: return idleFrameCount;
                case RUN: return runFrameCount;
                default: return idleFrameCount;
            }
        }
        return 1;
    }

    public Rectangle getBounds() {
        int hitboxX = x + (width - hitboxWidth) / 2;
        int hitboxY = y + (height - hitboxHeight); // Align hitbox with bottom
        return new Rectangle(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        if (!alive) return;
        alive = false;
        currentState = AnimationState.DIE;
        dieAnimTimer = dieFrameCount * dieAnimSpeed; // Animation duration
        frameIndex = 0;
        animCounter = 0;
    }

    public void respawn(Level level) {
        alive = true;
        currentState = AnimationState.IDLE;
        dieAnimTimer = 0;
        frameIndex = 0;

        int maxX = GameFrame.WIDTH - width;
        x = (int) (Math.random() * (maxX > 0 ? maxX : 0));

        y = level.getGroundY() - height;
        dy = 0;

        dx = (Math.random() > 0.5) ? 2 : -2;
    }
}
