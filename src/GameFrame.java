import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class GameFrame extends JPanel implements ActionListener, KeyListener {

    private Image background; // Background PNG

    Timer timer;
    Player player;
    Enemy enemy;
    Level level;
    Score score;

    JButton respawnBtn;
    boolean enemyRespawning = false;

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 700;

    public GameFrame() {
        setLayout(null);

        // 🔹 Load background PNG dengan MediaTracker supaya pasti muncul
        try {
            String bgPath = "assets/Background.png"; // pastikan path sesuai project
            File f = new File(bgPath);
            if (!f.exists()) {
                System.out.println("Background PNG tidak ditemukan! Path: " + bgPath);
                background = null;
            } else {
                background = new ImageIcon(bgPath).getImage();

                // Pastikan image selesai load
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(background, 0);
                tracker.waitForAll();

                if (tracker.isErrorAny()) {
                    System.out.println("Gagal load background image!");
                    background = null;
                } else {
                    System.out.println("Background siap digambar!");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            background = null;
        }

        // Inisialisasi level, player, musuh, score
        level = new Level();
        player = new Player(100, 500);
        enemy = new Enemy(600, 500);
        score = new Score();

        timer = new Timer(20, this);
        timer.start();

        // Tombol Respawn
        respawnBtn = new JButton("RESPAWN");
        respawnBtn.setBounds(1050, 20, 120, 40);
        respawnBtn.setFocusable(false);
        respawnBtn.setVisible(false);
        respawnBtn.addActionListener(e -> respawnGame());
        add(respawnBtn);

        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 🔹 Layer 0: Background
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.RED); // Debug kalau PNG gagal load
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // 🔹 Layer 1: Level / Ground
        level.draw(g);

        // 🔹 Layer 2: Player & Musuh
        player.draw(g);
        enemy.draw(g);

        // 🔹 Layer 3: UI (Score)
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score: " + score.getScore(), 20, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.update(level);
        enemy.update();

        if (enemy.isAlive() && player.getBounds().intersects(enemy.getBounds())) {
            if (player.getDY() > 0 &&
                    player.getBounds().y + player.getBounds().height - 10 < enemy.getBounds().y) {

                enemy.die();
                score.addScore(100);
                player.bounce();

                if (!enemyRespawning) {
                    enemyRespawning = true;
                    Timer respawnTimer = new Timer(1000, ev -> {
                        enemy.respawn();
                        enemyRespawning = false;
                    });
                    respawnTimer.setRepeats(false);
                    respawnTimer.start();
                }

            } else {
                gameOver();
            }
        }

        repaint();
    }

    private void gameOver() {
        timer.stop();
        respawnBtn.setVisible(true);
    }

    private void respawnGame() {
        player = new Player(100, 500);
        enemy.respawn();
        score.reset();

        enemyRespawning = false;
        respawnBtn.setVisible(false);
        timer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.moveRight();
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.moveLeft();
        if (e.getKeyCode() == KeyEvent.VK_SPACE) player.jump();
    }

    @Override
    public void keyReleased(KeyEvent e) { player.stop(); }
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mini Mario Bros");
        GameFrame game = new GameFrame();
        frame.add(game);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
