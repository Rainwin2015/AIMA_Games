/**
 * This class is used to analyze and extract different features of given
 * StateSimulator{@link AI.StateSimulator}. Returns a FeaturesMatrix containing
 * the values of features.
 */

package AI;

import java.util.Iterator;
import java.util.Set;

import AI.Features.Feature;
import GameEngine.State;
import framework.Util;

/**
 * @author ZhangKai
 */
public class StateAnalyzer {

    public static final int EMPTY = 0; // represent empty slot in field
    public static int ROWS = State.ROWS;
    public static int COLS = State.COLS;

    //
    // Fields of the state that used to get features values from
    int[][] field = new int[ROWS][COLS];
    int[] top = new int[COLS];
    int numOfTurns = 0;
    int score = 0;
    int rowscleared = 0; // rows cleared in current round
    //
    //

    FeaturesMatrix matrix;

    /**
     * Constructs StateAnalyzer using the given set of features.
     *
     * @param keySet
     */
    public StateAnalyzer(Set<Features.Feature> keySet) {
        matrix = FeaturesMatrix.getZeroFeaturesMatrix(keySet);
    }

    /**
     * Analyzes a given state simulator, returns a feature matrix containing the
     * results.
     *
     * @param simulator
     * @return feature matrix
     */
    public FeaturesMatrix analyse(StateSimulator simulator) {
        FeaturesMatrix result = new FeaturesMatrix();

        // extract needed fields
        Util.copyTwoDArray(simulator.getField(), field);
        Util.copyOneDArray(simulator.getTop(), top);
        numOfTurns = simulator.getTurnNumber();
        score = simulator.getRowsCleared();
        rowscleared = simulator.getRowsClearedOfCurrentMove();

        // iterate through the features
        Iterator<?> iterator = matrix.getKeySet().iterator();
        while (iterator.hasNext()) {
            Features.Feature nextFeature = (Feature) iterator.next();
            result.set(nextFeature, getFeature(nextFeature));
        }

        return result;
    }

    /**
     * Returns the value of given features.
     *
     * @param feature
     * @return String value
     */
    public String getFeature(Features.Feature feature) {
        switch (feature) {


            case NUM_HOLES:
                return getNumOfHoles();

            case SCORE:
                return getScore();

            case ROW_TRANSITION:
                return getRowTransition();

            case COL_TRANSITION:
                return getColTransition();

            case WELL_SUM:
                return getWellSum();

            case LANDING_HEIGHT:
                return getLandingHeight();

            default:
                return null;
        }
    }

    //
    // Features Getter Methods
    //

    private String getPhaseOneRowsCleared() {
        return Integer.toString(rowscleared);
    }

    private String getNumBlockingCellsLastCol() {
        return Integer.toString(top[COLS - 1]);
    }

    private String getNumVerticalSurfaceOverThree() {
        int result = 0;
        int counter = 0;
        for (int colidx = 1; colidx < COLS - 1; colidx++) {
            counter = 0;
            for (int rowidx = 0; rowidx < ROWS; rowidx++) {
                if (isDifferentCell(field[rowidx][colidx], field[rowidx][colidx - 1])) {
                    counter++;
                }
            }
            if (counter >= 2) {
                result++;
            }
        }
        return Integer.toString(result);
    }

    private String getHaveHoles() {
        int result = 0;
        for (int colidx = 0; colidx < COLS; colidx++) {
            for (int rowidx = 0; rowidx < top[colidx]; rowidx++) {
                if (field[rowidx][colidx] == EMPTY) {
                    result++;
                }
            }
        }
        return Integer.toString(result);
    }


    //
    // Phase two features
    //

    /**
     * Returns the number of holes inside field.
     *
     * @return
     */
    private String getNumOfHoles() {
        int result = 0;
        for (int colidx = 0; colidx < COLS; colidx++) {
            for (int rowidx = 0; rowidx < top[colidx]; rowidx++) {
                if (field[rowidx][colidx] == EMPTY) {
                    result++;
                }
            }
        }
        return Integer.toString(result);
    }

    /**
     * Returns the total number of height of all columns.
     *
     * @return
     */
    private String getTotalHeight() {
        int result = 0;
        for (int colidx = 0; colidx < COLS; colidx++) {
            result += top[colidx];
        }

        return Integer.toString(result);
    }

    /**
     * Returns the current move score.
     *
     * @return
     */
    private String getScore() {
        return Integer.toString(rowscleared);
    }

    /**
     * Returns roughness of the field, sum of absolute difference between
     * adjacent columns
     *
     * @return
     */
    private String getRoughness() {
        int result = 0;
        for (int colidx = 1; colidx < COLS; colidx++) {
            result += Util.abs(top[colidx] - top[colidx - 1]);
        }

        return Integer.toString(result);
    }

    /**
     * Returns the total number of row transitions. A row transition occurs when
     * an empty cell is adjacent to a filled cell on the same row and vice
     * versa.
     *
     * @return
     */
    private String getRowTransition() {
        int result = 0;
        for (int rowidx = 0; rowidx < ROWS; rowidx++) {
            for (int colidx = 1; colidx < COLS; colidx++) {
                if (isDifferentCell(field[rowidx][colidx], field[rowidx][colidx - 1])) {
                    result++;
                }
            }
            if (field[rowidx][0] == EMPTY) {
                result++;
            }
            if (field[rowidx][COLS - 1] == EMPTY) {
                result++;
            }
        }
        return Integer.toString(result);
    }

    /**
     * Returns the total number of column transitions. A column transition
     * occurs when an empty cell is adjacent to a filled cell on the same column
     * and vice versa.
     *
     * @return
     */
    private String getColTransition() {
        int result = 0;

        for (int colidx = 0; colidx < COLS; colidx++) {
            for (int rowidx = 1; rowidx < top[colidx] + 1; rowidx++) {
                if (isDifferentCell(field[rowidx][colidx], field[rowidx - 1][colidx])) {
                    result++;
                }
            }
            if (field[0][colidx] == EMPTY) {
                result++;
            }
        }

        return Integer.toString(result);
    }

    /**
     * Returns the sum of wells in field. A well is a succession of empty cells
     * such that their left cells and right cells are both filled.
     *
     * @return
     */
    private String getWellSum() {
        int result = 0;

        for (int colidx = 0; colidx < COLS; colidx++) {
            for (int rowidx = top[colidx]; rowidx < ROWS; rowidx++) {
                // if current col is most left column
                if (colidx == 0) {
                    if (isDifferentCell(field[rowidx][colidx], field[rowidx][colidx + 1])) {
                        result++;
                    }
                }
                // if current col is right most column
                else if (colidx == COLS - 1) {

                    if (isDifferentCell(field[rowidx][colidx], field[rowidx][colidx - 1])) {
                        result++;
                    }
                }
                // if current column is in the middle
                else {
                    if (isDifferentCell(field[rowidx][colidx], field[rowidx][colidx - 1])
                            && isDifferentCell(field[rowidx][colidx], field[rowidx][colidx + 1])) {
                        result++;
                    }
                }
            }
        }

        return Integer.toString(result);
    }

    /**
     * Returns the number of blocking areas in the field.
     *
     * @return
     */
    private String getBlockingCells() {
        int result = 0;
        boolean blocking = false;

        for (int colidx = 0; colidx < COLS; colidx++) {
            blocking = false;
            for (int rowidx = 0; rowidx < top[colidx]; rowidx++) {
                if (field[rowidx][colidx] == EMPTY) {
                    blocking = true;
                } else {
                    if (blocking) {
                        result++;
                    }
                }
            }
        }

        return Integer.toString(result);
    }

    /**
     * Returns the landing height of last piece.
     *
     * @return
     */
    private String getLandingHeight() {
        int lowBound = ROWS;
        int highBound = 0;
        for (int colidx = 0; colidx < COLS; colidx++) {
            for (int rowidx = 0; rowidx < top[colidx]; rowidx++) {
                if (field[rowidx][colidx] == numOfTurns) {
                    lowBound = Math.min(lowBound, rowidx);
                    highBound = Math.max(highBound, rowidx);
                }
            }
        }
        return Double.toString((double) (lowBound + highBound) / 2.0);
    }

    //
    // Helper Methods
    private boolean isDifferentCell(int first, int second) {
        if ((first == EMPTY && second != EMPTY) || (first != EMPTY && second == EMPTY)) {
            return true;
        }
        return false;
    }

}
