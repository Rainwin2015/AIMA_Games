package GeneticAlgorithm;

import java.util.Random;
import java.util.Set;

import AI.Features;
import AI.FeaturesMatrix;
import framework.Util;

public class Gene {

    public static final double MUTATE_PROBABILITY = 0.1;

    private int length = 0;
    private FeaturesMatrix featureWeightMatrix;

    static Random random = new Random();

    /**
     * Constructor
     *
     * @param featureWeightMatrix
     */
    public Gene(FeaturesMatrix featureWeightMatrix) {
        this.length = featureWeightMatrix.getKeySet().size();
        this.featureWeightMatrix = featureWeightMatrix;
    }

    public int getLength() {
        return length;
    }

    public FeaturesMatrix getFeatureWeightMatrix() {
        return featureWeightMatrix;
    }

    /**
     * Crossover using the front part of local gene and the tail part of given
     * gene and cutting position.
     *
     * @param gene
     * @return
     */
    public Gene crossOverWith(Gene gene) {
        int counter = 0;
        double mutateProbability = 0.0;
        Set<Integer> mutationPositionSet = Util.getRandomIntSet(0, length, length / 2);
        FeaturesMatrix childMatrix = new FeaturesMatrix();

        for (Features.Feature feature : Features.Feature.values()) {
            if (featureWeightMatrix.getKeySet().contains(feature)) {
                mutateProbability = random.nextDouble();
                if (mutationPositionSet.contains(counter)) {
                    childMatrix.set(feature, featureWeightMatrix.getDouble(feature));
                } else {
                    childMatrix.set(feature, gene.getFeatureWeightMatrix().getDouble(feature));
                }
                if (mutateProbability < MUTATE_PROBABILITY) {
                    childMatrix.set(feature, featureWeightMatrix.getDouble(feature) + (random.nextDouble() - 0.5) / 10);
                }
                counter++;
            }
        }

        return new Gene(childMatrix);
    }

    /**
     * Randomly generates a gene containing all the features.
     *
     * @return
     */
    public static Gene randomGenerateGene() {
        FeaturesMatrix featuresMatrix = new FeaturesMatrix();
        for (Features.Feature feature : Features.Feature.values()) {
            featuresMatrix.set(feature, random.nextDouble() * 20 - 10);
        }
        return new Gene(featuresMatrix);
    }

    /**
     * Returns a gene that is near to given gene, given gene (+/- 0.1) * (given gene).
     *
     * @param gene
     * @return
     */
    public static Gene randomGenerateGeneAround(Gene gene) {
        FeaturesMatrix featuresMatrix = new FeaturesMatrix();
        FeaturesMatrix givenFM = gene.getFeatureWeightMatrix();
        for (Features.Feature feature : Features.Feature.values()) {
            featuresMatrix.set(feature, givenFM.getDouble(feature) * (1 + random.nextDouble() * 0.2 - 0.1));
        }
        return new Gene(featuresMatrix);
    }

    public String toString() {
        return featureWeightMatrix.toString();
    }

}
