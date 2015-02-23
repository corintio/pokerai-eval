package pokerai.game.eval.jokipii;

import java.io.IOException;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

/**
 * http://forumserver.twoplustwo.com/showthreaded.php?Cat=0&Number=9765615&page=0&vc=1
 * http://forumserver.twoplustwo.com/showthreaded.php?Cat=0&Number=9774228&page=0&vc=1
 *
 * Based on spears adaptation of RayW LUT hand evaluator, http://pokerai.org/pf3, http://forumserver.twoplustwo.com
 * 
 * Jokipii's Compressed LUT Evaluator, http://pokerai.org/pf3
 * 
 * This class shows how to use all different types of LUTs.
 * 
 * @author Antti Jokipii
 */
public class LUTEvaluator extends AbstractLUTEvaluator  {
	
	private int handrank1, handrank2, handrank3, handrank4, handrank5, handrank6;
	
	/**
	 * Returns the equivalence class value of the best five-card high poker hand chosen
	 * from the specified seven cards, ordered such that better hands have higher equivalence
	 * class numbers.
	 * <p>
	 * The evaluation algorithm uses compressed lookup table (LUT) that contains all possible 
	 * combinations of the of the seven cards. 
	 * <p>
	 *
	 * @param card1 the first encoded card value
	 * @param card2 the second encoded card value
	 * @param card3 the third encoded card value
	 * @param card4 the fourth encoded card value
	 * @param card5 the fifth encoded card value
	 * @param card6 the sixth encoded card value
	 * @param card7 the seventh encoded card value
	 * @see #encode(int, int)
	 * @return the equivalence class value of the best five-card high poker hand chosen
	 *		 from the specified seven cards, ordered such that better hands have higher equivalence
	 *		 class numbers.
	 */
	public static final int eval7(int card1, int card2, int card3, int card4, int card5, int card6, int card7) {
		int base = 53;							// By default we assume no compression and we use 1-based tables
		if (isCompressed()) { base = 52; }		// compression is on and tables are 0-based
		// if Original card order or No compression card values must go from 1 to 52 not from 0 to 51
		if (isOriginalCardOrder() || isNotCompressed()) {
			card1++;
			card2++;
			card3++;
			card4++;
			card5++;
			card6++;
			card7++;
		}
		if (isSplitted()) {
			return handRank7[handIndex6[handIndex5[handIndex4[handIndex3[handIndex2[handIndex1[
				base + card1] + card2] + card3] + card4] + card5] + card6] + card7];		
		}
		// One big table
		return handRanks[handRanks[handRanks[handRanks[handRanks[handRanks[handRanks[
			base + card1] + card2] + card3] + card4] + card5] + card6] + card7];
	}
	
	
	public static final int eval6(int card1, int card2, int card3, int card4, int card5, int card6) {
		int base = 53;							// By default we assume no compression and we use 1-based tables
		if (isCompressed()) { base = 52; }		// compression is on and tables are 0-based
		// if Original card order or No compression card values must go from 1 to 52 not from 0 to 51
		if (isOriginalCardOrder() || isNotCompressed()) {
			card1++;
			card2++;
			card3++;
			card4++;
			card5++;
			card6++;
		}
		if (isSplitted()) {
			return handRank6[handIndex5[handIndex4[handIndex3[handIndex2[handIndex1[
				base + card1] + card2] + card3] + card4] + card5] + card6];		
		}
		if (isNotCompressed()) {
			return handRanks[handRanks[handRanks[handRanks[handRanks[handRanks[handRanks[
				base + card1] + card2] + card3] + card4] + card5] + card6] + 0];
		}
		return 0;
	}
	
	
	public static final int eval5(int card1, int card2, int card3, int card4, int card5) {
		int base = 53;							// By default we assume no compression and we use 1-based tables
		if (isCompressed()) { base = 52; }		// compression is on and tables are 0-based
		// if Original card order or No compression card values must go from 1 to 52 not from 0 to 51
		if (isOriginalCardOrder() || isNotCompressed()) {
			card1++;
			card2++;
			card3++;
			card4++;
			card5++;
		}
		if (isSplitted()) {
			return handRank5[handIndex4[handIndex3[handIndex2[handIndex1[
				base + card1] + card2] + card3] + card4] + card5];		
		}
		if (isNotCompressed()) {
			return handRanks[handRanks[handRanks[handRanks[handRanks[handRanks[
				base + card1] + card2] + card3] + card4] + card5] + 0];
		}
		return 0;
	}
	
	
	/**
	 * Set first card of hand. All above cards from brevious hands should be considered as reseted even
	 * actual implementation of method may not do that.
	 * @param card an encoded card
	 * @see #encode(int, int)
	 */
	public void setCard1(int card) {
		int base = 53;
		if (isCompressed()) { 
			base = 52;
			if (isOriginalCardOrder()) { card++; }					// Original card order and therefore index from 1...52
		}
		else {
			card++;											// No compression so we need card index from 1...52
		}
		if (isSplitted()) {										// Splitted to multiple tables
			handrank1 = handIndex1[base + card];
		}
		else {												// One big table
			handrank1 = handRanks[base + card];
		}
	}
	
	/**
	 * Set second card of hand. All above cards from brevious hands should be considered as reseted even
	 * actual implementation of method may not do that. Internal states of below cards are kept in memory,
	 * @param card an encoded card
	 * @see #encode(int, int)
	 */
	public void setCard2(int card) {
		if (isOriginalCardOrder() || isNotCompressed()) { card++; }
		if (isSplitted()) {
			handrank2 = handIndex2[handrank1 + card];
		}
		else {
			handrank2 = handRanks[handrank1 + card];
		}
	}
	
	/**
	 * Set 3rd card of hand. All above cards from brevious hands should be considered as reseted even
	 * actual implementation of method may not do that. Internal states of below cards are kept in memory.
	 * @param card an encoded card
	 * @see #encode(int, int)
	 */
	public void setCard3(int card) {
		if (isOriginalCardOrder() || isNotCompressed()) { card++; }
		if (isSplitted()) {
			handrank3 = handIndex3[handrank2 + card];
		}
		else {
			handrank3 = handRanks[handrank2 + card];
		}
	}
	
	/**
	 * Set 4th card of hand. All above cards from brevious hands should be considered as reseted even
	 * actual implementation of method may not do that. Internal states of below cards are kept in memory.
	 * @param card an encoded card
	 * @see #encode(int, int)
	 */
	public void setCard4(int card) {
		if (isOriginalCardOrder() || isNotCompressed()) { card++; }
		if (isSplitted()) {
			handrank4 = handIndex4[handrank3 + card];
		}
		else {
			handrank4 = handRanks[handrank3 + card];
		}
	}
	
	/**
	 * Set 5th card of hand. All above cards from brevious hands should be considered as reseted even
	 * actual implementation of method may not do that. Internal states of below cards are kept in memory.
	 * @param card an encoded card
	 * @see #encode(int, int)
	 */
	public void setCard5(int card) {
		if (isOriginalCardOrder() || isNotCompressed()) { card++; }
		if (isSplitted()) {
			handrank5 = handIndex5[handrank4 + card];
		}
		else {
			handrank5 = handRanks[handrank4 + card];
		}
	}
	
	/**
	 * Set 6th card of hand. All above cards from brevious hands should be considered as reseted even
	 * actual implementation of method may not do that. Internal states of below cards are kept in memory.
	 * @param card an encoded card
	 * @see #encode(int, int)
	 */
	public void setCard6(int card) {
		if (isOriginalCardOrder() || isNotCompressed()) { card++; }
		if (isSplitted()) {
			handrank6 = handIndex6[handrank5 + card];
		}
		else {
			handrank6 = handRanks[handrank5 + card];
		}
	}
	
	/**
	 * Set last card of 7 card hand and returns the equivalence class value of the best five-card
	 * high poker hand chosen from the specified seven cards, ordered such that better hands 
	 * have higher equivalence class numbers.
	 * <p>
	 * @param card an encoded card
	 * @see #encode(int, int)
	 * @return the equivalence class value of the best five-card high poker hand chosen
	 *		 from the specified seven cards, ordered such that better hands have higher equivalence
	 *		 class numbers.
	 */
	public int setHand7(int card) {
		if (isOriginalCardOrder() || isNotCompressed()) { card++; }
		if (isSplitted()) {
			return handRank7[handrank6 + card];
		}
		return handRanks[handrank6 + card];
	}
	
} // END class Evaluator
