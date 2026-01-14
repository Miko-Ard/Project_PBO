public class Score {

    private int score = 0;

    public void addScore(int value) {
        score += value;
    }

    public int getScore() {
        return score;
    }
    public void reset() {
        score = 0;
    }

}
