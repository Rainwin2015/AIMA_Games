package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Random;

import AI.AIPlayer;

/**
 * @author
 */
public class Individual implements Comparable<Individual>, Runnable {

    public ArrayList<Integer> scores;
    Gene gene;
    AIPlayer player;
    public Random random = new Random();

    /**
     * Constructs individual from weight matrix.
     *
     * @param gene
     */
    public Individual(Gene gene) {
        this.scores = new ArrayList<>();
        this.gene = gene;
        this.player = new AIPlayer(gene.getFeatureWeightMatrix());
    }

    @Override
    public void run() {
        play();
    }

    /**
     * Creates a player and let it plays the game multiple times.
     */
    public void play() {
        for (int i = 0; i < GA.RUNNING_TIMES; i++) {
            scores.add(player.run());
        }
    }

    /**
     * Cross over with another parent.
     *
     * @param parent
     * @return
     */
    public Individual crossOverWith(Individual parent, double probOfMutation) {
        int cuttingPosition = random.nextInt(gene.getLength());
        return new Individual(gene.crossOverWith(parent.gene));
    }

    public Gene getGene() {
        return gene;
    }

    /**
     * Returns the average score.
     *
     * @return
     */
    public double getAvgScore() {
        int result = 0;
        for (int i = 0; i < scores.size(); i++) {
            result += scores.get(i);
        }

        return ((double) result) / (double) scores.size();
    }

    public void clearScore(){
        scores.clear();
    }

    @Override
    public int compareTo(Individual o) {
        // TODO Auto-generated method stub
        return Double.compare(getAvgScore(), o.getAvgScore());
    }

}
