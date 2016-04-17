/**
 * AIPlayer is a runnable class. Player starts to play Tetris using the weights
 * of features given.
 */
package AI;

import GameEngine.State;
import framework.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

/**
 * @author ZhangKai
 */
public class AIPlayer {

    // minimum score achievable for any state
    public static final double MIN_SCORE = -10000000000000000.1;
    // default number of games played for one set of feature weights
    public static final int DEFAULT_PLAYING_ROUNDS = 5;
    // default number of forwarding done in state evaluation in phase two
    public static final int DEFAULT_FORWARDING_TIMES = 1;
    // number of pieces that will shows in the game
    public static final int N_PIECES = State.N_PIECES;
    // default next piece setting, not influencing the game
    public static final int DUMMY_NEXT_PIECE = 0;
    // whether enable predictor
    public static final boolean ENABLE_PREDICTION = false;

    private int playingRounds;
    private int forwardingTimes;

    // contains the weights of features
    FeaturesMatrix weightMatrix;
    // contains the features' values of current state
    FeaturesMatrix stateFeatureValueMatrix;

    State state;
    Predictor predictor;
    StateAnalyzer analyzer;
    StateSimulator simulator;
    Random random = new Random();

    //
    // Constructors - START
    public AIPlayer() {
        this(FeaturesMatrix.getDefaultFeaturesMatrix());
    }

    public AIPlayer(FeaturesMatrix matrix) {
        this(matrix, DEFAULT_PLAYING_ROUNDS, DEFAULT_FORWARDING_TIMES);
    }

    public AIPlayer(int playingRounds, int forwardingTimes) {
        this(FeaturesMatrix.getDefaultFeaturesMatrix(), playingRounds, forwardingTimes);
    }

    public AIPlayer(FeaturesMatrix weightMatrix, int playingRounds, int forwardingTimes) {
        this.weightMatrix = weightMatrix;
        this.playingRounds = playingRounds;
        this.forwardingTimes = forwardingTimes;
        this.predictor = new Predictor(forwardingTimes);
        this.analyzer = new StateAnalyzer(weightMatrix.getKeySet());
        this.simulator = new StateSimulator();
    }
    // Constructors - END
    //

    public void setForwarding(int value) {
        this.forwardingTimes = value;
    }

    public int run() {
        if (Util.demoMode) {
            demoRun();
        } else {
            state = new State();
            while (!state.hasLost()) {
                state.makeMove(pickMove(state));
            }
        }
        return state.getRowsCleared();
    }

    public void demoRun() {
        state = new State();
        //TFrame frame = new TFrame(state);

        while (!state.hasLost()) {
            state.makeMove(pickMove(state));
            //state.draw();
            //state.drawNext(0, 0);
        }
        // System.out.print(state.getRowsCleared());
        //Util.demoPrintln("" + state.getRowsCleared());
    }


    /**
     * Picks the best evaluated move of given state.
     *
     * @param state
     * @return move
     */
    public int pickMove(State state) {
        if (ENABLE_PREDICTION) {
            predictor.record(state.getNextPiece());
        }
        simulator.simulate(state);

        double[] moveScore = new double[simulator.getLegalMoves().length];
        for (int i = 0; i < moveScore.length; i++) {
            StateSimulator ss = simulator.forward(i, DUMMY_NEXT_PIECE);
            // moveScore[i] = evaluateState(ss);
            moveScore[i] = evaluateStateWithForwarding(ss, DUMMY_NEXT_PIECE, 1);
        }

        //Util.debugPrintln(Arrays.toString(moveScore));
        return pickBestMove(moveScore);
    }

    /**
     * Evaluate state simulator in phase two with forwarding.
     *
     * @param simulator
     * @param prevNextPiece
     * @param lookAhead
     * @return
     */
    private double evaluateStateWithForwarding(StateSimulator simulator, int prevNextPiece, int lookAhead) {

        if (lookAhead >= forwardingTimes) {
            return evaluateState(simulator);
        }

        double nextStateExpectedScore = 0.0;
        double currerentStateScore = evaluateState(simulator);
        double[] nextStateScores = new double[N_PIECES];


        for (int i = 0; i < N_PIECES; i++) {
            double bestScore = MIN_SCORE;
            simulator.setNextPiece(i);
            for (int j = 0; j < simulator.getLegalMoves().length; j++) {
                StateSimulator newSimulator = simulator.forward(j, DUMMY_NEXT_PIECE);
                bestScore = Util.max(evaluateStateWithForwarding(newSimulator, prevNextPiece, lookAhead + 1),
                        bestScore);
            }
            nextStateScores[i] = bestScore;
        }

        if (ENABLE_PREDICTION) {
            double[] nextPieceProbability = predictor.predictNext();
            for (int i = 0; i < N_PIECES; i++) {
                nextStateExpectedScore += nextStateScores[i] * nextPieceProbability[i];
            }
        } else {
            for (int i = 0; i < N_PIECES; i++) {
                nextStateExpectedScore += nextStateScores[i] * (1.0/7.0);
            }
        }
        //Util.debugPrintln(nextStateExpectedScore + "");
        //return nextStateExpectedScore;
        return Util.sqrt(Util.abs(nextStateExpectedScore) * currerentStateScore);
    }

    /**
     * Returns the score of given state.
     *
     * @param simulator
     * @return
     */
    private double evaluateState(StateSimulator simulator) {

        FeaturesMatrix stateFeatureValueMatrix = analyzer.analyse(simulator);
        return evaluateFeatureMatrix(stateFeatureValueMatrix);
    }

    /**
     * Returns the score of a simulated state by multiply weight matrix and
     * state matrix.
     *
     * @param stateFeatureMatrix
     * @return
     */
    private double evaluateFeatureMatrix(FeaturesMatrix stateFeatureMatrix) {
        double result = 0.0;
        Iterator<Features.Feature> iterator = stateFeatureMatrix.getKeySet().iterator();

        while (iterator.hasNext()) {
            Features.Feature feature = iterator.next();
            result += weightMatrix.getDouble(feature) * ((double) stateFeatureMatrix.getDouble(feature));

        }

        return result;
    }

    /**
     * Returns the index of the best move, if multiple moves have same score,
     * randomly select one.
     *
     * @param evalScore
     * @return
     */
    private int pickBestMove(double[] evalScore) {

        ArrayList<Integer> result = new ArrayList<Integer>();
        double bestScore = MIN_SCORE;
        // find the best score
        for (int i = 0; i < evalScore.length; i++) {
            if (evalScore[i] > bestScore) {
                bestScore = evalScore[i];
            }
        }
        // there might be multiple moves with same best scores, randomly choose one
        for (int i = 0; i < evalScore.length; i++) {
            if (Double.compare(bestScore, evalScore[i]) == 0) {
                result.add(i);
            }
        }
        //Util.debugPrintln(Arrays.toString(evalScore));
        try {
            return result.get((int) random.nextInt(result.size()));
        }catch (Exception e){
            Util.debugPrintln(Arrays.toString(evalScore));
        }
        return 0;
    }

}
