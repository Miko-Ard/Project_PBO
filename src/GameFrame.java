import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The main panel for the game, handling the game loop, rendering, and input.
 */
public class GameFrame extends JPanel implements ActionListener, KeyListener {

    private Image background;
    private Timer timer;
    private Player player;
    private Enemy enemy;
    private Level level;
    private Score score;

    private Image heartImage;
    private int lives = 3;
    
    private Font gameFont;
    private Font gameFontBold;
    
    private Image frameImage;
    private Image buttonImage;
    private Rectangle buttonBounds;

    private boolean isGameOver = false;
    private boolean enemyRespawning = false;
    
    private ArrayList<FloatingText> floatingTexts = new ArrayList<>();
    private AudioManager audioManager;

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1020;

    public GameFrame() {
        setLayout(null);

        loadAssets();
        
        audioManager = AudioManager.getInstance();
        audioManager.loadEnemyHitSound("/assets/enemy-hit.wav");
        audioManager.loadJumpSound("/assets/jump-se.wav");

        level = new Level();
        level.updateGroundY(HEIGHT);

        int groundY = level.getGroundY();
        player = new Player(100, groundY - 60);
        enemy = new Enemy(600, groundY - 60);
        score = new Score();

        timer = new Timer(20, this);
        timer.start();
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isGameOver && buttonBounds != null && buttonBounds.contains(e.getPoint())) {
                    respawnGame();
                }
            }
        });

        setFocusable(true);
        addKeyListener(this);
    }

    private void loadAssets() {
        try {
            URL bgUrl = getClass().getResource("/assets/bg.png");
            if (bgUrl != null) {
                background = new ImageIcon(bgUrl).getImage();
            }
            
            URL heartUrl = getClass().getResource("/assets/hearth.png");
            if (heartUrl != null) {
                heartImage = new ImageIcon(heartUrl).getImage();
            }
            
            URL frameUrl = getClass().getResource("/assets/frame.png");
            if (frameUrl != null) {
                frameImage = new ImageIcon(frameUrl).getImage();
            }
            
            URL buttonUrl = getClass().getResource("/assets/button.png");
            if (buttonUrl != null) {
                buttonImage = new ImageIcon(buttonUrl).getImage();
            }
            
            InputStream is = getClass().getResourceAsStream("/assets/PixelifySans-Medium.ttf");
            if (is != null) {
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
                gameFont = baseFont.deriveFont(18f);
                gameFontBold = baseFont.deriveFont(Font.BOLD, 32f);
                is.close();
            } else {
                gameFont = new Font("Arial", Font.PLAIN, 18);
                gameFontBold = new Font("Arial", Font.BOLD, 32);
            }
        } catch (Exception e) {
            System.err.println("Failed to load one or more assets. Using fallback.");
            e.printStackTrace();
            // Use default font as a fallback
            gameFont = new Font("Arial", Font.PLAIN, 18);
            gameFontBold = new Font("Arial", Font.BOLD, 32);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        level.draw(g, getWidth());
        player.draw(g);
        enemy.draw(g);
        
        for (FloatingText text : floatingTexts) {
            text.draw(g);
        }

        g.setColor(Color.BLACK);
        g.setFont(gameFont);
        g.drawString("Score: " + score.getScore(), 20, 30);

        if (heartImage != null) {
            int heartSize = 32;
            for (int i = 0; i < lives; i++) {
                g.drawImage(heartImage, 20 + i * (heartSize + 8), 40, heartSize, heartSize, null);
            }
        }

        if (isGameOver) {
            drawGameOver((Graphics2D) g);
        }
    }

    private void drawGameOver(Graphics2D g2) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        
        int frameWidth = 650;
        int frameHeight = 550;
        int frameX = (panelWidth - frameWidth) / 2;
        int frameY = (panelHeight - frameHeight) / 2;
        
        if (frameImage != null) {
            g2.drawImage(frameImage, frameX, frameY, frameWidth, frameHeight, null);
        }
        
        g2.setFont(gameFontBold.deriveFont(56f));
        FontMetrics fmGameOver = g2.getFontMetrics();
        String gameOverText = "GAME OVER";
        int gameOverX = frameX + (frameWidth - fmGameOver.stringWidth(gameOverText)) / 2;
        int gameOverY = frameY + 100;
        
        g2.setColor(Color.BLACK);
        g2.drawString(gameOverText, gameOverX + 2, gameOverY + 2);
        g2.setColor(Color.RED);
        g2.drawString(gameOverText, gameOverX, gameOverY);
        
        g2.setFont(gameFont.deriveFont(28f));
        FontMetrics fmScore = g2.getFontMetrics();
        String scoreText = "Score: " + score.getScore();
        int scoreX = frameX + (frameWidth - fmScore.stringWidth(scoreText)) / 2;
        int scoreY = gameOverY + 100;
        
        g2.setColor(Color.BLACK);
        g2.drawString(scoreText, scoreX + 2, scoreY + 2);
        g2.setColor(Color.WHITE);
        g2.drawString(scoreText, scoreX, scoreY);
        
        if (buttonImage != null) {
            int buttonWidth = 180;
            int buttonHeight = 70;
            int buttonX = frameX + (frameWidth - buttonWidth) / 2;
            int buttonY = scoreY + 70;
            
            buttonBounds = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
            g2.drawImage(buttonImage, buttonX, buttonY, buttonWidth, buttonHeight, null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameOver) return;

        level.updateGroundY(getHeight());
        player.update(level);
        enemy.update(level);
        
        Iterator<FloatingText> textIterator = floatingTexts.iterator();
        while (textIterator.hasNext()) {
            FloatingText text = textIterator.next();
            text.update();
            if (!text.isAlive()) {
                textIterator.remove();
            }
        }

        checkCollisions();
        repaint();
    }

    private void checkCollisions() {
        if (!enemy.isAlive() || !player.getBounds().intersects(enemy.getBounds())) {
            return;
        }

        if (player.getDY() > 0 && player.getBounds().y + player.getBounds().height < enemy.getBounds().y + 20) {
            enemy.die();
            score.addScore(100);
            score.addKill();
            player.bounce();
            audioManager.playEnemyHit();
            
            String killText = "+" + score.getKillCount() + " Kill";
            floatingTexts.add(new FloatingText(killText, enemy.getBounds().x, enemy.getBounds().y, 60, Color.YELLOW, gameFontBold));

            if (!enemyRespawning) {
                enemyRespawning = true;
                Timer respawnTimer = new Timer(1000, ev -> {
                    enemy.respawn(level);
                    enemyRespawning = false;
                });
                respawnTimer.setRepeats(false);
                respawnTimer.start();
            }
        } else {
            player.takeHit();
            loseLife();
        }
    }

    private void loseLife() {
        lives--;
        if (lives <= 0) {
            lives = 0;
            gameOver();
        } else {
            enemy.respawn(level);
        }
    }

    private void gameOver() {
        isGameOver = true;
        timer.stop();
    }

    private void respawnGame() {
        lives = 3;
        isGameOver = false;

        int groundY = level.getGroundY();
        player = new Player(100, groundY - 60);
        enemy.respawn(level);
        score.reset();
        floatingTexts.clear();

        enemyRespawning = false;
        buttonBounds = null;
        timer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            player.moveRight();
        } else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            player.moveLeft();
        } else if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (player.isOnGround()) {
                player.jump();
                audioManager.playJump();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D ||
            code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            player.stop();
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mini Mario Bros");
            GameFrame game = new GameFrame();
            frame.add(game);
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}
