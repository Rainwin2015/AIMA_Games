package GA;

import AI.AIPlayer;
import AI.FeaturesMatrix;

public class Individual implements Runnable {

    public static final int PLAYINGROUNDS = 10;

    FeaturesMatrix fmatrix;
    double score;

    public Individual(FeaturesMatrix fmatrix, double score) {
        this.fmatrix = fmatrix;
        this.score = score;
    }

    public Individual(FeaturesMatrix fmatrix) {
        this.fmatrix = fmatrix;
    }

    public void AvgScore() {
        score = 0;
        for (int i = 0; i < PLAYINGROUNDS; i++) {
            AIPlayer player = new AIPlayer(fmatrix);
            int currentScore = player.run();
            score += currentScore;
            if (currentScore < 30000 && score / (i + 1) < 100000)
                break;
        }
        score /= PLAYINGROUNDS;

    }

    public void run() {
        AvgScore();
    }


}