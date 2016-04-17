package framework;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Util {

    public static final String DEBUG_FLAG = "debug info : ";
    public static final String DEMO_FLAG = "demo : ";
    public static boolean debugMode = false;
    public static boolean demoMode = false;

    public static Random random = new Random();

    public static void setDebugMode(boolean value) {
        debugMode = value;
        demoPrintln("set debugMode : " + value);
    }

    public static void setDemoMode(boolean value) {
        demoMode = value;
        demoPrintln("set demoMode : " + value);
    }

    public static void copyTwoDArray(int[][] source, int[][] dest) {
        int row = source.length;
        int col = source[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                dest[i][j] = source[i][j];
            }
        }
    }

    public static void copyOneDArray(int[] source, int[] dest) {
        int col = source.length;
        for (int i = 0; i < col; i++) {
            dest[i] = source[i];
        }
    }

    public static double abs(double value) {
        return Math.abs(value);
    }

    public static double sqrt(double value) {
        double sign = 1;
        if (value < 0) {
            sign = -1;
        }
        return sign * Math.sqrt(abs(value));
    }

    public static Set<Integer> getRandomIntSet(int start, int end, int size) {
        Set<Integer> result = new HashSet<>();
        for (int i = 0; i < size; i++) {
            result.add(new Integer(random.nextInt(end - start) + start));
        }
        return result;
    }

    public static double max(double a, double b) {
        if (a > b) {
            return a;
        }
        return b;
    }

    public static void debugPrintln(String s) {
        if (debugMode) {
            System.out.println(s);
        }
    }

    public static void demoPrintln(String s) {
        if (demoMode) {
            System.out.println(DEMO_FLAG + s);
        }
    }

}
