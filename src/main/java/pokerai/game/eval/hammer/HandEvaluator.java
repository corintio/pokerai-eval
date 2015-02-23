package pokerai.game.eval.hammer;

import pokerai.game.eval.hammer.classes.EquivalenceClass;

public interface HandEvaluator {

	public EquivalenceClass calculateEquivalenceClass(int[] hand);
	public EquivalenceClass eval7(int card1, int card2, int card3, int card4, int card5, int card6, int card7);
	public EquivalenceClass eval6(int card1, int card2, int card3, int card4, int card5, int card6);
	public EquivalenceClass eval5(int card1, int card2, int card3, int card4, int card5);

}
