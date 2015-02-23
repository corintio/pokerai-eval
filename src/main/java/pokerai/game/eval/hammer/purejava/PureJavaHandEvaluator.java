package pokerai.game.eval.hammer.purejava;

import pokerai.game.eval.hammer.HandEvaluator;
import pokerai.game.eval.hammer.classes.EquivalenceClasses;
import pokerai.game.eval.hammer.classes.EquivalenceClass;

public abstract class PureJavaHandEvaluator implements HandEvaluator {
	protected static final DAGNode[] nodes = Dag.nodes;

	public EquivalenceClass calculateEquivalenceClass(int[] hand) {
		return null;
	}

	public EquivalenceClass eval7(int card1, int card2, int card3, int card4, int card5, int card6, int card7) {
		return calculateEquivalenceClass(new int[]{card1, card2, card3,  card4, card5, card6, card7});
	}
	
	public EquivalenceClass eval6(int card1, int card2, int card3, int card4, int card5, int card6) {
		return calculateEquivalenceClass(new int[]{card1, card2, card3,  card4, card5, card6});
	}
	
	public EquivalenceClass eval5(int card1, int card2, int card3, int card4, int card5) {
		return calculateEquivalenceClass(new int[]{card1, card2, card3,  card4, card5});
	}
	
}
