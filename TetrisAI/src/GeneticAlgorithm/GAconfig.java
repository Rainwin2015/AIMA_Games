package GeneticAlgorithm;

/**
 * Created by RayZK on 15/04/16.
 */
public class GAconfig {

    // number of playing rounds for one individual
    public static final int NUM_PLAYING_ROUNDS = 10;
    // size of population
    public static final int POPULATION_SIZE = 10000;
    // size of parent set, parent_size ^ 2 = population_size
    public static final int PARENT_SIZE = 100;
    // rate of mutate one value in gene
    public static final double MUTATION_RATE = 0.05;
}
