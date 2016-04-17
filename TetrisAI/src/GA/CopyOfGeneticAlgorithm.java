package GA;

import java.util.Vector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import AI.Features;
import AI.FeaturesMatrix;
import AI.Features.Feature;
import GA.FitnessFunction;
import GA.Individual;

/**
 * Created by RayZK on 04/04/16.
 */


public class CopyOfGeneticAlgorithm {

    public ArrayList<Individual> functions = new ArrayList<Individual>();
    public static final int NUM_OF_PARENTS = 10;

    public void Algorithm() throws IOException {
        FitnessFunction f = new FitnessFunction();
        Vector<FeaturesMatrix> fitfunction = f.getRandomize();

        for (int i = 0; i < fitfunction.size(); i++) {
            // System.out.println(i);
            Individual indiv = new Individual(fitfunction.elementAt(i));
            indiv.AvgScore();
            functions.add(indiv);

        }
        //this.LoadData();
        //this.Update();
        //this.PrintScore();
        Collections.sort(functions, new FunctionCompare());

        this.WriteFile();
        this.Generation();
    }


    public void Update() {
        for (int i = 0; i < functions.size(); i++) {
            Individual indiv = new Individual(functions.get(0).fmatrix);
            indiv.AvgScore();
            functions.add(indiv);
            functions.remove(0);
        }
    }


    public void LoadData() throws IOException {
        File data = new File("E:/workspace/Elite.txt");
        BufferedReader br = new BufferedReader(new FileReader(data));
        String regx = ",:{}";
        char[] ca = regx.toCharArray();
        String l;
        String[] StringArr;
        while (null != (l = br.readLine())) {
            for (char c : ca) {
                l = l.replace("" + c, "");
            }
            l = l.replace("=", " ");
            StringArr = l.split(" ");
            FeaturesMatrix fmatrix = new FeaturesMatrix();
            for (int i = 2; i <= 12; i = i + 2) {
                for (Feature feature : Feature.values()) {
                    if (feature.name().equals(StringArr[i])) {
                        fmatrix.set(feature, Double.parseDouble(StringArr[i + 1]));

                    }
                }
            }
            Individual indiv = new Individual(fmatrix, Double.parseDouble(StringArr[StringArr.length - 1]));
            functions.add(indiv);
        }


        br.close();
    }

    public void WriteFile() throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File("/Users/wang/Downloads/TetrisAI 2/Data.txt");

        PrintWriter writer = new PrintWriter(file, "UTF-8");

        for (int i = 0; i < functions.size(); i++) {

            writer.println("Feature Weight: " + functions.get(i).fmatrix + " Score: " + functions.get(i).score);
        }
        writer.close();
    }

    public void GetParent() {
        int size = functions.size();
        for (int i = 0; i < (size - NUM_OF_PARENTS); i++) {
            functions.remove(0);
        }
    }


    public void Generation() throws FileNotFoundException, UnsupportedEncodingException {
        int generation = 1;
        FeaturesMatrix fmatrix;
        Individual indiv;
        while (true) {
            System.out.println("Genaration: " + generation);
            this.GetParent();
            ArrayList<Individual> children = new ArrayList<Individual>();
            int sum = 0;
            for (int i = 0; i < NUM_OF_PARENTS - 1; i++) {

                sum += functions.get(i).score;
            }
            for (int i = 0; i < 20; i++) {

                int parent1 = 0, parent2 = 0;


                //select parent 1
                double temp = functions.get(0).score;
                double random = Math.random() * sum;
                for (parent1 = 0; parent1 < NUM_OF_PARENTS - 1; parent1++) {
                    if (temp > random)
                        break;
                    temp += functions.get(parent1).score;
                }

                //select parent 2
                random = Math.random() * sum;
                temp = functions.get(0).score;
                for (parent2 = 0; parent2 < NUM_OF_PARENTS - 1; parent2++) {
                    if (temp > random)
                        break;
                    temp += functions.get(parent2).score;
                }

                System.out.println("Parent1: " + parent1 + " Parent2: " + parent2);
                //crossover
                fmatrix = new FeaturesMatrix();
                random = Math.random();
                int k = 1;
                for (Feature feature : Feature.values()) {
                    if (random < (double) (k / 6)) {
                        fmatrix.set(feature, functions.get(parent2).fmatrix.getDouble(feature));
                        k = -6;
                    } else
                        fmatrix.set(feature, functions.get(parent1).fmatrix.getDouble(feature));
                    k++;
                }
                indiv = new Individual(fmatrix);
                indiv.AvgScore();
                System.out.println("Crossover Score: " + indiv.score);
                if (indiv.score > functions.get(0).score)
                    children.add(indiv);


                fmatrix = new FeaturesMatrix();
                random = Math.random();
                k = 1;
                for (Feature feature : Feature.values()) {
                    if (random < (double) (k / 6)) {
                        fmatrix.set(feature, functions.get(parent1).fmatrix.getDouble(feature));
                        k = -6;
                    } else
                        fmatrix.set(feature, functions.get(parent2).fmatrix.getDouble(feature));
                    k++;
                }
                indiv = new Individual(fmatrix);
                indiv.AvgScore();
                System.out.println("Crossover Score: " + indiv.score);
                if (indiv.score > functions.get(0).score)
                    children.add(indiv);
                //Average
                fmatrix = new FeaturesMatrix();

                for (Feature feature : Feature.values()) {
                    fmatrix.set(feature, (functions.get(parent1).fmatrix.getDouble(feature) + functions.get(parent2).fmatrix.getDouble(feature)) / 2);
                }
                indiv = new Individual(fmatrix);
                indiv.AvgScore();
                System.out.println("Average Score: " + indiv.score);
                if (indiv.score > functions.get(0).score)
                    children.add(indiv);
            }


            //mutation
            for (int j = 1; j < 3; j++) {
                for (int i = 0; i < 1; i++) {
                    fmatrix = new FeaturesMatrix();
                    double random = Math.random(), random2 = Math.random() * 2 - 1;
                    int k = 1;
                    for (Feature feature : Feature.values()) {
                        if (random < (double) (k / 6)) {
                            fmatrix.set(feature, functions.get(functions.size() - j).fmatrix.getDouble(feature) + random2);
                            k = -6;
                        }

                        k++;
                    }


                    indiv = new Individual(fmatrix);
                    indiv.AvgScore();
                    System.out.println("Mutation Score: " + indiv.score);
                    if (indiv.score > functions.get(0).score)
                        children.add(indiv);

                }
            }
            functions.addAll(children);
            Collections.sort(functions, new FunctionCompare());
            this.WriteFile();
            System.out.println("Population: " + functions.size());
            this.PrintScore();

            generation++;
        }
    }


    public void removeLine(int toRemove) throws IOException {

        File tmp = new File("E:/workspace/Elite.txt");
        File myfile = new File("E:/workspace/Data.txt");

        BufferedReader br = new BufferedReader(new FileReader(myfile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(tmp));

        for (int i = 0; i < toRemove; i++)
            br.readLine();


        String l;
        while (null != (l = br.readLine()))
            bw.write(String.format("%s%n", l));

        br.close();
        bw.close();


        tmp.renameTo(myfile);
        myfile.delete();
    }


    public static void main(String[] args) throws IOException {
        CopyOfGeneticAlgorithm GA = new CopyOfGeneticAlgorithm();
        GA.Algorithm();
    }

    public void PrintScore() {
        for (int i = 0; i < functions.size(); i++) {
            //if(functions.get(i).score>10)
            System.out.print(functions.get(i).score + ",");
        }
        System.out.println();
    }
}

class FunctionCompare implements Comparator<Individual> {


    public int compare(Individual indiv1, Individual indiv2) {

        return (int) (indiv1.score - indiv2.score);
    }
}
