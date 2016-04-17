package AI;

public class PredictionLink {

	private PredictionNode left;
	private PredictionNode right;

	public PredictionLink(PredictionNode left, PredictionNode right) {
		this.left = left;
		this.right = right;
	}

	//
	// Getters
	public PredictionNode getLeft() {
		return left;
	}

	public PredictionNode getRight() {
		return right;
	}

	//
	// Setters
	/**
	 * Returns whether given node is the left node of this link.
	 * 
	 * @param node
	 * @return
	 */
	public boolean isLeftNode(PredictionNode node) {
		for (int i = 0; i < left.sequence.size(); i++) {
			if (node.sequence.get(i) != left.sequence.get(i)) return false;
		}
		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Link : {");
		sb.append(left.toString());
		sb.append(right.toString());
		sb.append("}" + "\n");
		return sb.toString();
	}

}
