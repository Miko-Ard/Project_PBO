import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;

/**
 * Manages all audio playback for the game.
 * Uses Java Sound API for audio playback.
 */
public class AudioManager {
    private Clip enemyHitSound;
    private Clip jumpSound;
    
    private static AudioManager instance;
    
    private AudioManager() {
        // Private constructor for Singleton pattern.
    }
    
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
    
    public void loadEnemyHitSound(String path) {
        try {
            URL soundUrl = getClass().getResource(path);
            if (soundUrl == null) {
                System.out.println("Enemy hit sound file not found: " + path);
                return;
            }
            
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundUrl);
            enemyHitSound = AudioSystem.getClip();
            enemyHitSound.open(audioIn);
            System.out.println("Enemy hit sound loaded successfully.");
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio format not supported for: " + path + " (Java Sound API only supports WAV, AIFF, AU)");
        } catch (Exception e) {
            System.out.println("Error loading enemy hit sound: " + e.getMessage());
        }
    }
    
    public void loadJumpSound(String path) {
        try {
            URL soundUrl = getClass().getResource(path);
            if (soundUrl == null) {
                System.out.println("Jump sound file not found: " + path);
                return;
            }
            
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundUrl);
            jumpSound = AudioSystem.getClip();
            jumpSound.open(audioIn);
            System.out.println("Jump sound loaded successfully.");
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio format not supported for: " + path + " (Java Sound API only supports WAV, AIFF, AU)");
        } catch (Exception e) {
            System.out.println("Error loading jump sound: " + e.getMessage());
        }
    }
    
    public void playEnemyHit() {
        if (enemyHitSound == null) return;
        
        try {
            if (enemyHitSound.isRunning()) {
                enemyHitSound.stop();
            }
            enemyHitSound.setFramePosition(0);
            enemyHitSound.start();
        } catch (Exception e) {
            System.out.println("Error playing enemy hit sound: " + e.getMessage());
        }
    }
    
    public void playJump() {
        if (jumpSound == null) return;
        
        try {
            if (jumpSound.isRunning()) {
                jumpSound.stop();
            }
            jumpSound.setFramePosition(0);
            jumpSound.start();
        } catch (Exception e) {
            System.out.println("Error playing jump sound: " + e.getMessage());
        }
    }
    
    public void dispose() {
        if (enemyHitSound != null) {
            enemyHitSound.close();
        }
        if (jumpSound != null) {
            jumpSound.close();
        }
    }
}
