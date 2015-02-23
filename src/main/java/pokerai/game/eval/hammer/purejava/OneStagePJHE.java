package pokerai.game.eval.hammer.purejava;

import pokerai.game.eval.hammer.classes.EquivalenceClass;

public final class OneStagePJHE extends PureJavaHandEvaluator {

	public final EquivalenceClass calculateEquivalenceClass(int[] hand) {
		DAGNode fn[] = new DAGNode[4];
		DAGNode rn = null;
		for (int b : hand) {
			int col = b & 3;
			if (fn[col] == null) {
				fn[col] = nodes[b >> 2];
			} else {
				fn[col] = fn[col].next[b >> 2];
			}
			if (rn == null) {
				rn = nodes[b >> 2];
			} else {
				rn = rn.next[b >> 2];
			}
		}
		for (DAGNode f : fn) {
			if (f != null && f.flushclass != null) {
				return f.flushclass;
			}
		}
		return rn.rankclass;
	}

}

