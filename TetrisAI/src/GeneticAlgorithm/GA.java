package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import AI.FeaturesMatrix;
import framework.Util;

public class GA {

    public static final int POPULATION_SIZE = 100;
    // parent size ^ 2 is population size
    public static final double PARENT_SIZE = 10;
    public static final double MUTATION_PROBABILITY = 0.05;
    public static final int NUM_GENERATIONS = 50;
    public static final int RUNNING_TIMES = 10;


    public static ArrayList<Individual> population = new ArrayList<>();
    public static ArrayList<Individual> parents = new ArrayList<>();

    // singleton
    private GA() {
    }

    public static void algorithm() {
        Util.setDebugMode(true);
        Util.setDemoMode(false);
        Collection<Future<?>> futures = new LinkedList<Future<?>>();
        ExecutorService executorService = Executors.newFixedThreadPool(POPULATION_SIZE);

        Gene goodGene1 = new Gene(FeaturesMatrix.getDefaultFeaturesMatrix());

        int generation = 0;
        // initialize population
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(new Individual(Gene.randomGenerateGene()));
        }

        Util.debugPrintln("start ...");
        while (generation < NUM_GENERATIONS) {
            Util.debugPrintln("generation : " + generation);

            // play game
            for (int i = 0; i < POPULATION_SIZE; i++) {
                population.get(i).clearScore();
                futures.add(executorService.submit(population.get(i)));
            }
            // wait for all threads terminate
            try {
                for (Future<?> future : futures) {
                    future.get();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            futures.clear();

            // sort population
            Collections.sort(population);
            Collections.reverse(population);

            // select parents
            parents.clear();
            for (int i = 0; i < PARENT_SIZE; i++) {
                parents.add(population.get(i));
            }

            // prints the score of parents
            StringBuilder sbScore = new StringBuilder();
            sbScore.append("parents score : ");
            for (int i = 0; i < PARENT_SIZE; i++) {
                sbScore.append(parents.get(i).getAvgScore() + ", ");
            }
            Util.debugPrintln(sbScore.toString());

            // prints the gene of the best parent
            StringBuilder sbGene = new StringBuilder();
            sbGene.append("best parent gene : ");
            sbGene.append(parents.get(0).getGene());
            Util.debugPrintln(sbGene.toString());

            // crossover
            // add new children to existing population
            for (int i = 0; i < PARENT_SIZE; i++) {
                for (int j = 0; j < PARENT_SIZE; j++) {
                    population.add(parents.get(i).crossOverWith(parents.get(j), MUTATION_PROBABILITY));
                }
            }

            // select population
            Collections.sort(population);
            Collections.reverse(population);
            ArrayList<Individual> newPopulation = new ArrayList<>();
            for (int i=0; i<POPULATION_SIZE; i++){
                newPopulation.add(population.get(i));
            }
            population = newPopulation;

            Util.debugPrintln("");
            generation++;
        }
        Util.debugPrintln("... end");
        executorService.shutdown();

    }

    public static void main(String[] args) {
        algorithm();
    }

}
