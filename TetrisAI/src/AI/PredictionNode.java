package AI;

import java.util.ArrayList;

public class PredictionNode {

	// sequence of next pieces in this node
	ArrayList<Integer> sequence;

	/**
	 * Constructor
	 * 
	 * @param sequence
	 */
	PredictionNode(ArrayList<Integer> sequence) {
		this.sequence = sequence;
	}

	/**
	 * Returns a string representation of node.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PredictNode : [");
		for (int i = 0; i < sequence.size(); i++) {
			sb.append(sequence.get(i) + ", ");
		}
		sb.append("]\n");
		return sb.toString();
	}

}
