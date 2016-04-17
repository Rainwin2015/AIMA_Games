package AI;

import java.util.*;

/**
 * @author ZhangKai WangZhe
 */

public class Predictor {

	public static final int NUM_CANDIDATE = 7;
	public static final int START_BOUND_OF_PREDICTION = 5;

	// number of forwarding considered, default value is 1
	int numForwarding = 1;
	int numLinkRecord = 0;
	public static double[] weight = { 0.7, 0.3 };
	ArrayList<Integer> history;
	Hashtable<PredictionNode, Integer> nodeTable;
	Hashtable<PredictionLink, Integer> linkTable;

	/**
	 * Constructor.
	 * 
	 * @param numForwarding
	 */
	public Predictor(int numForwarding) {
		this.numForwarding = numForwarding;
		this.history = new ArrayList<Integer>();
		this.nodeTable = new Hashtable<PredictionNode, Integer>();
		this.linkTable = new Hashtable<PredictionLink, Integer>();
	}

	/**
	 * Records the next piece information.
	 * 
	 * @param turnNumber
	 * @param nextPieceIndex
	 */
	public void record(int nextPieceIndex) {
		// add next piece to history.
		history.add(nextPieceIndex);

		addToNodeTable(nextPieceIndex);

		if (history.size() > numForwarding) {
			addToLinkTable(nextPieceIndex);
		}
		return;
	}

	private void addToNodeTable(int nextPiece) {
		ArrayList<Integer> currList = new ArrayList<>();
		currList.add(nextPiece);
		PredictionNode currNode = new PredictionNode(currList);
		int weight = 1;
		if (nodeTable.contains(currNode)) {
			weight = nodeTable.get(currNode);
			weight++;
		}
		nodeTable.put(currNode, weight);
	}

	private void addToLinkTable(int nextPiece) {
		numLinkRecord++;
		ArrayList<Integer> leftList = new ArrayList<>();
		ArrayList<Integer> rightList = new ArrayList<>();

		for (int i = 0; i < numForwarding; i++) {
			leftList.add(history.get(history.size() - i - 2));
			rightList.add(history.get(history.size() - i - 1));
		}

		PredictionNode leftNode = new PredictionNode(leftList);
		PredictionNode rightNode = new PredictionNode(rightList);
		PredictionLink link = new PredictionLink(leftNode, rightNode);

		int weight = 1;
		if (linkTable.contains(link)) {
			weight = linkTable.get(link);
			weight++;
		}
		linkTable.put(link, weight);
	}

	/**
	 * Returns the probability of showing up in the next round for 7 pieces.
	 * 
	 * @return
	 */
	public double[] predictNext() {

		if (numLinkRecord < START_BOUND_OF_PREDICTION) { return defaultPrediction(); }

		int[] nodeResult = nodeTablePredictNext();
		int[] linkResult = linkTablePredictNext();
		double[] result = new double[NUM_CANDIDATE];

		for (int i = 0; i < NUM_CANDIDATE; i++) {
			result[i] = ((double) nodeResult[i] / (double) history.size()) * weight[0]
					+ ((double) linkResult[i] / (double) numLinkRecord) * weight[1];
		}

		return result;
	}

	private int[] nodeTablePredictNext() {
		int[] result = new int[NUM_CANDIDATE];

		Iterator<PredictionNode> iter = nodeTable.keySet().iterator();
		while (iter.hasNext()) {
			PredictionNode node = iter.next();
			result[node.sequence.get(0)] = nodeTable.get(node);
		}
		return result;
	}

	private int[] linkTablePredictNext() {
		int[] result = new int[NUM_CANDIDATE];

		// create current left node
		ArrayList<Integer> leftList = new ArrayList<>();
		for (int i = 0; i < numForwarding; i++) {
			leftList.add(history.get(history.size() - i - 2));
		}
		PredictionNode leftNode = new PredictionNode(leftList);

		// iterate through link table
		Iterator<?> iter = linkTable.keySet().iterator();
		while (iter.hasNext()) {
			PredictionLink link = (PredictionLink) iter.next();
			if (link.isLeftNode(leftNode)) {
				result[link.getRight().sequence.get(0)] = linkTable.get(link);
			}
		}
		return result;
	}

	private double[] defaultPrediction() {
		return new double[] { 0.142, 0.142, 0.142, 0.142, 0.142, 0.142, 0.142 };
	}

	//
	// debug methods
	public static void printArray(double[] probability) {
		for (int i = 0; i < probability.length; i++) {
			System.out.println(probability[i]);
		}
	}

	public static void main(String[] args) {
		Predictor p = new Predictor(2);
		Predictor q = new Predictor(1);

		for (int i = 0; i < 99; i++) {
			p.record(i % 3);
			q.record(i % 4);
		}

		printArray(p.predictNext());
		System.out.println();
		printArray(q.predictNext());
	}

}