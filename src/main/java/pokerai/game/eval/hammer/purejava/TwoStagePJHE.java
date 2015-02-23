package pokerai.game.eval.hammer.purejava;

import pokerai.game.eval.hammer.classes.EquivalenceClass;

public final class TwoStagePJHE extends PureJavaHandEvaluator {

	public final EquivalenceClass calculateEquivalenceClass(int[] hand) {
		int[] color = new int[]{0, 0, 0, 0};
		boolean flush = false;
		int fc = 0;
		DAGNode n = null;;
		for (int b : hand) {
			if (++color[b & 3] >= 5) {
				flush = true;
				fc = b & 3;
				break;
			}			
			if (n == null) {
				n = nodes[b >> 2];
			} else {
				n = n.next[b >> 2];
			}
		}
		if (!flush) {
			return n.rankclass;
		}
		n = null;
		for (int b : hand) {
			if ((b & 3) == fc) {
				if (n == null) {
					n = nodes[b >> 2];
				} else {
					n = n.next[b >> 2];
				}
			}
		}
		return n.flushclass;
	}

}
