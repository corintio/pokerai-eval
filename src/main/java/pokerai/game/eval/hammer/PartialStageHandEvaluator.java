package pokerai.game.eval.hammer;

import pokerai.game.eval.hammer.classes.EquivalenceClass;

/**
 * PartialStageHandEvaluator interface makes possible of building HandEvaluator that remembers some cards from
 * previous hand. This can be speed up some most common HandEvaluator tasks, example when multiple hands are 
 * compared whithout changes in community cards.
 * <p>
 * 
 */
public interface PartialStageHandEvaluator {
	
	/**
	 * Set first card of hand. All above cards from brevious hands should be considered as reseted even
	 * actual implementation of method may not do that.
	 * @param card an encoded card
	 */
	public void setCard1(int card);
	
	/**
	 * Set second card of hand. All above cards from brevious hands should be considered as reseted even
	 * actual implementation of method may not do that. Internal states of below cards are kept in memory,
	 * @param card an encoded card
	 */
	public void setCard2(int card);
	
	/**
	 * Set 3rd card of hand. All above cards from brevious hands should be considered as reseted even
	 * actual implementation of method may not do that. Internal states of below cards are kept in memory.
	 * @param card an encoded card
	 */
	public void setCard3(int card);
	
	/**
	 * Set 4th card of hand. All above cards from brevious hands should be considered as reseted even
	 * actual implementation of method may not do that. Internal states of below cards are kept in memory.
	 * @param card an encoded card
	 */
	public void setCard4(int card);
	
	/**
	 * Set 5th card of hand. All above cards from brevious hands should be considered as reseted even
	 * actual implementation of method may not do that. Internal states of below cards are kept in memory.
	 * @param card an encoded card
	 */
	public void setCard5(int card);
	
	/**
	 * Set 6th card of hand. All above cards from brevious hands should be considered as reseted even
	 * actual implementation of method may not do that. Internal states of below cards are kept in memory.
	 * @param card an encoded card
	 */
	public void setCard6(int card);
	
	/**
	 * Set last card of 7 card hand and returns hand {@code EquivalenceClass}.
	 * @param card an encoded card
	 * @return current hand EquivalenceClass
	 */
	public EquivalenceClass setHand7(int card);
	
	/**
	 * Set last card of 6 card hand and returns hand {@code EquivalenceClass}. Internal state is
         * also updated, so it is possible next to use {@method setHand7}.
	 * @param card an encoded card
	 * @return current hand EquivalenceClass
	 */
	public EquivalenceClass setHand6(int card);
	
	/**
	 * Set last card of 5 card hand and returns hand {@code EquivalenceClass}.  Internal state is
         * also updated, so it is possible next to use {@method setHand6}.
	 * @param card an encoded card
	 * @return current hand EquivalenceClass
	 */
	public EquivalenceClass setHand5(int card);

}
