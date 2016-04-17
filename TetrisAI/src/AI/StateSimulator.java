/**
 * This class simulates a game state by taking in a list of fields from given
 * State instance. And generate the result of next move based on the given
 * state. The relevant fields that changes during the execution of makeMove()
 * method will be restored through undo() method.
 */
package AI;

import GameEngine.State;
import framework.Util;

/**
 * @author ZhangKai
 */
public class StateSimulator extends State {

    //
    // Constants in Class State - START
    public static final int COLS = State.COLS;
    public static final int ROWS = State.ROWS;
    public static final int N_PIECES = State.N_PIECES;
    public static final int ORIENT = State.ORIENT;
    public static final int SLOT = State.SLOT;

    //
    // Current state fields
    private int[][] field = new int[ROWS][COLS];
    private int[] top = new int[COLS];
    private boolean lost = false;
    private int nextPiece;
    private int turn;
    private int cleared;
    private int rowsCleared;

    //
    // Initial State fields of given state
    // For undo method
    State state;

    private int[][] initField = new int[ROWS][COLS];
    private int[] initTop = new int[COLS];
    private boolean initLost;
    private int initNextPiece;
    private int initTurn;
    private int initCleared;

    /**
     * Constructor.
     */
    public StateSimulator() {
    }

    public StateSimulator(State state) {
        simulate(state);
    }

    /**
     * Constructs a simulator using the given state and save the initial state
     * of the given state.
     *
     * @param state
     */
    public void simulate(State state) {
        this.state = state;
        Util.copyTwoDArray(state.getField(), initField);
        Util.copyOneDArray(state.getTop(), initTop);
        this.initLost = state.hasLost();
        this.initNextPiece = state.getNextPiece();
        this.initTurn = state.getTurnNumber();
        this.initCleared = state.getRowsCleared();
        undo();
    }

    //
    // Forward and Backward
    //
    public StateSimulator forward(int move, int nextPiece) {
        makeMove(move);
        StateSimulator result = new StateSimulator(this);
        result.setNextPiece(nextPiece);
        undo();
        return result;
    }

    public void undo() {
        Util.copyTwoDArray(initField, field);
        Util.copyOneDArray(initTop, top);
        this.lost = initLost;
        this.nextPiece = initNextPiece;
        this.turn = initTurn;
        this.cleared = initCleared;
        this.rowsCleared = 0; // rows cleared in current round
    }

    //
    // State Class Getters - START
    public int[][] getField() {
        return field;
    }

    public int[] getTop() {
        return top;
    }

    public boolean hasLost() {
        return lost;
    }

    public int getRowsClearedOfCurrentMove() {
        return rowsCleared;
    }

    public int getRowsClearedTotal() {
        return cleared;
    }

    public int getTurnNumber() {
        return turn;
    }

    public int getNextPiece() {
        return nextPiece;
    }

    public void setNextPiece(int nextPiece) {
        this.nextPiece = nextPiece;
    }

    public int[][] getLegalMoves() {
        return super.legalMoves[getNextPiece()];
    }
    // State Class Getters - END
    //

    //
    // Move Method - START
    public void makeMove(int move) {
        makeMove(getLegalMoves()[move]);
    }

    public void makeMove(int[] move) {
        makeMove(move[ORIENT], move[SLOT]);
    }

    public boolean makeMove(int orient, int slot) {
        turn++;
        int height = top[slot] - super.getpBottom()[getNextPiece()][orient][0];

        try {
            //for each column beyond the first in the piece
            for (int c = 1; c < super.getpWidth()[getNextPiece()][orient]; c++) {
                height = Math.max(height, top[slot + c] - super.getpBottom()[getNextPiece()][orient][c]);
            }
        } catch (Exception e) {
            System.out.println(slot + "," + getNextPiece() + "," + orient);
        }

        if (height + super.getpHeight()[getNextPiece()][orient] >= ROWS) {
            lost = true;
            return false;
        }

        // for each column in the piece - fill in the appropriate blocks
        for (int i = 0; i < super.getpWidth()[getNextPiece()][orient]; i++) {

            // from bottom to top of brick
            for (int h = height + super.getpBottom()[getNextPiece()][orient][i]; h < height
                    + super.getpTop()[getNextPiece()][orient][i]; h++) {
                field[h][i + slot] = turn;
            }
        }

        for (int c = 0; c < super.getpWidth()[getNextPiece()][orient]; c++) {
            top[slot + c] = height + super.getpTop()[getNextPiece()][orient][c];
        }

        rowsCleared = 0;

        for (int r = height + super.getpHeight()[getNextPiece()][orient] - 1; r >= height; r--) {
            // check all columns in the row
            boolean full = true;
            for (int c = 0; c < COLS; c++) {
                if (field[r][c] == 0) {
                    full = false;
                    break;
                }
            }
            // if the row was full - remove it and slide above stuff down
            if (full) {
                rowsCleared++;
                cleared++;
                // for each column
                for (int c = 0; c < COLS; c++) {

                    // slide down all bricks
                    for (int i = r; i < top[c]; i++) {
                        field[i][c] = field[i + 1][c];
                    }
                    // lower the top
                    top[c]--;
                    while (top[c] >= 1 && field[top[c] - 1][c] == 0)
                        top[c]--;
                }
            }
        }

        return true;
    }

    // Move Method - End
    //

}
