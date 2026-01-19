/**
 * Manages the player's score and kill count.
 */
public class Score {

    private int score = 0;
    private int killCount = 0;

    public void addScore(int value) {
        if (value > 0) {
            score += value;
        }
    }

    public int getScore() {
        return score;
    }
    
    public void addKill() {
        killCount++;
    }
    
    public int getKillCount() {
        return killCount;
    }
    
    public void reset() {
        score = 0;
        killCount = 0;
    }
}
