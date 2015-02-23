package pokerai.game.eval.hammer.purejava;

import pokerai.game.eval.hammer.classes.EquivalenceClass;

public final class DAGNode {
	private final int id;
	public final DAGNode[] next;
	public final EquivalenceClass flushclass;
	public final EquivalenceClass rankclass;

	public DAGNode(int id, EquivalenceClass flushclass, EquivalenceClass rankclass, DAGNode[] next) {
		this.id = id;
		this.flushclass = flushclass;
		this.rankclass = rankclass;
		this.next = next;
	}

	public final int getId() {
		return id;
	}

	public final EquivalenceClass getFlushClass() {
		return flushclass;
	}

	public final EquivalenceClass getRankClass() {
		return rankclass;
	}

}
