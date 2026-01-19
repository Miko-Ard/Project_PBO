import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Represents the player character, handling its state, movement, and animation.
 */
public class Player {

    private enum AnimationState {
        IDLE, RUN, JUMP, FALL, HIT
    }

    private int x, y;
    private final int width = 60, height = 60;
    private int dx = 0, dy = 0;
    private boolean onGround = false;
    private boolean facingRight = true;

    private AnimationState currentState = AnimationState.IDLE;
    private int hitTimer = 0;

    private Image idleSheet, runSheet, jumpSheet, fallSheet, hitSheet;

    private final int[] frameCounts = {11, 12, 1, 1, 7}; // Corresponds to AnimationState enum order
    private final int[] animSpeeds = {2, 2, 1, 1, 1};

    private int frameIndex = 0;
    private int animCounter = 0;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        loadSpriteSheets();
    }

    private void loadSpriteSheets() {
        try {
            URL idleUrl = getClass().getResource("/assets/idle.png");
            if (idleUrl != null) idleSheet = new ImageIcon(idleUrl).getImage();
            
            URL runUrl = getClass().getResource("/assets/run.png");
            if (runUrl != null) runSheet = new ImageIcon(runUrl).getImage();
            
            URL jumpUrl = getClass().getResource("/assets/Jump (32x32).png");
            if (jumpUrl != null) jumpSheet = new ImageIcon(jumpUrl).getImage();
            
            URL fallUrl = getClass().getResource("/assets/fall.png");
            if (fallUrl != null) fallSheet = new ImageIcon(fallUrl).getImage();
            
            URL hitUrl = getClass().getResource("/assets/hit.png");
            if (hitUrl != null) hitSheet = new ImageIcon(hitUrl).getImage();
        } catch (Exception e) {
            System.err.println("Failed to load player sprite sheets.");
            e.printStackTrace();
        }
    }

    public void update(Level level) {
        x += dx;
        dy += 1; // Gravity
        y += dy;

        if (y >= level.getGroundY() - height) {
            y = level.getGroundY() - height;
            dy = 0;
            onGround = true;
        } else {
            onGround = false;
        }

        if (hitTimer > 0) {
            hitTimer--;
        }

        updateState();
        updateAnimation();
    }

    private void updateState() {
        if (hitTimer > 0) {
            currentState = AnimationState.HIT;
        } else if (!onGround) {
            currentState = (dy < 0) ? AnimationState.JUMP : AnimationState.FALL;
        } else {
            currentState = (dx != 0) ? AnimationState.RUN : AnimationState.IDLE;
        }
    }

    private void updateAnimation() {
        animCounter++;
        int stateIndex = currentState.ordinal();
        if (animCounter >= animSpeeds[stateIndex]) {
            animCounter = 0;
            frameIndex = (frameIndex + 1) % frameCounts[stateIndex];
        }
    }

    public void draw(Graphics g) {
        Image currentSheet = getCurrentImageSheet();
        if (currentSheet == null) return;

        int frameCount = frameCounts[currentState.ordinal()];
        int frameW = currentSheet.getWidth(null) / frameCount;
        int sx1 = frameIndex * frameW;
        int sy1 = 0;
        int sx2 = sx1 + frameW;
        int sy2 = currentSheet.getHeight(null);

        if (facingRight) {
            g.drawImage(currentSheet, x, y, x + width, y + height, sx1, sy1, sx2, sy2, null);
        } else {
            g.drawImage(currentSheet, x + width, y, x, y + height, sx1, sy1, sx2, sy2, null);
        }
    }

    private Image getCurrentImageSheet() {
        switch (currentState) {
            case IDLE: return idleSheet;
            case RUN: return runSheet;
            case JUMP: return jumpSheet;
            case FALL: return fallSheet;
            case HIT: return hitSheet;
            default: return null;
        }
    }

    public void moveRight() {
        dx = 5;
        facingRight = true;
    }

    public void moveLeft() {
        dx = -5;
        facingRight = false;
    }

    public void stop() {
        dx = 0;
    }

    public void jump() {
        if (onGround) {
            dy = -15;
            onGround = false;
        }
    }

    public void bounce() {
        dy = -10;
    }

    public void takeHit() {
        if (hitTimer <= 0) { // Prevent taking hits while already in hit state
            hitTimer = 30; // Hit animation duration
        }
    }
    
    public boolean isOnGround() { return onGround; }
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
    public int getDY() { return dy; }
}
