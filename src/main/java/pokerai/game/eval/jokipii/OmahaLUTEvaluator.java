package pokerai.game.eval.jokipii;

/**
 * This class evaluates omaha hands.
 * 
 * @author Antti Jokipii
 */
public class OmahaLUTEvaluator extends AbstractLUTEvaluator  {
	
	private int board1, board2, board3, board4, board5, board6, board7, board8, board9, board10;
	
	/**
	 *
	 */
	public OmahaLUTEvaluator() {
		handRanksFile = "omahaHandRanks.ser";
		mode	= MODE_COMPRESS | MODE_SPLIT | MODE_OMAHA;
		initialize();
	}
	
	/**
	 * Set Board cards for omaha hand
	 * @param boardcard1 the first encoded board card value
	 * @param boardcard2 the second encoded board card value
	 * @param boardcard3 the third encoded board card value
	 * @param boardcard4 the fourth encoded board card value
	 * @param boardcard5 the fifth encoded board card value
	 * @see #encode(int, int)
	 */
	public final void setBoard(int boardcard1, int boardcard2, int boardcard3, int boardcard4, int boardcard5) {
		board1 = handIndex3[handIndex2[handIndex1[52 + boardcard1] + boardcard2] + boardcard3];
		board2 = handIndex3[handIndex2[handIndex1[52 + boardcard1] + boardcard2] + boardcard4];
		board3 = handIndex3[handIndex2[handIndex1[52 + boardcard1] + boardcard2] + boardcard5];
		board4 = handIndex3[handIndex2[handIndex1[52 + boardcard1] + boardcard3] + boardcard4];
		board5 = handIndex3[handIndex2[handIndex1[52 + boardcard1] + boardcard3] + boardcard5];
		board6 = handIndex3[handIndex2[handIndex1[52 + boardcard1] + boardcard4] + boardcard5];
		board7 = handIndex3[handIndex2[handIndex1[52 + boardcard2] + boardcard3] + boardcard4];
		board8 = handIndex3[handIndex2[handIndex1[52 + boardcard2] + boardcard3] + boardcard5];
		board9 = handIndex3[handIndex2[handIndex1[52 + boardcard2] + boardcard4] + boardcard5];
		board10 = handIndex3[handIndex2[handIndex1[52 + boardcard3] + boardcard4] + boardcard5];
	}
	
	
	/**
	 * Returns the equivalence class value of the best five-card high poker hand chosen
	 * from the specified board and hand cards, ordered such that better hands have higher equivalence
	 * class numbers.
	 * <p>
	 * setBoard method must be called before calling this method
	 * <p>
	 * The evaluation algorithm uses brute for and compressed lookup table (LUT) that contains all possible 
	 * combinations of the of the 5 cards. 
	 * <p>
	 *
	 * @param card1 the first encoded card value
	 * @param card2 the second encoded card value
	 * @param card3 the third encoded card value
	 * @param card4 the fourth encoded card value
	 * @see #encode(int, int)
	 * @return the equivalence class value of the best five-card high poker hand chosen
	 *		 from the specified board and hand cards, ordered such that better hands have
	 * 		 higher equivalence class numbers.
	 */
	public final int evalHand(int card1, int card2, int card3, int card4) {
		int rank = Math.max(evalHand(board1, card1, card2, card3, card4), evalHand(board2, card1, card2, card3, card4));
		rank = Math.max(rank, Math.max(evalHand(board3, card1, card2, card3, card4), evalHand(board4, card1, card2, card3, card4)));
		rank = Math.max(rank, Math.max(evalHand(board5, card1, card2, card3, card4), evalHand(board6, card1, card2, card3, card4)));
		rank = Math.max(rank, Math.max(evalHand(board7, card1, card2, card3, card4), evalHand(board8, card1, card2, card3, card4)));
		return Math.max(rank, Math.max(evalHand(board9, card1, card2, card3, card4), evalHand(board10, card1, card2, card3, card4)));
	}
	
	
	private final int evalHand(int board, int card1, int card2, int card3, int card4) {
		int rank;
		int index = handIndex4[board + card1];
		rank = Math.max(Math.max(handRank5[index + card2], handRank5[index + card3]), handRank5[index + card4]);
		index = handIndex4[board + card2];
		rank = Math.max(Math.max(handRank5[index + card3], handRank5[index + card4]), rank);
		return Math.max(handRank5[handIndex4[board + card3] + card4], rank);
	}
	
} // END class OmahaLUTEvaluator
