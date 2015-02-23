package pokerai.game.eval.jokipii.cactuskev;

/**
 * The Class contains {@method getHandRank}, a modified version of "Cactus Kev's Five-Card 	
 * Poker Hand Evaluator" and all necessary helper methods 
 */
public class CactusKev {
	
	// The following methods implements a modified version of "Cactus Kev's Five-Card 
	// Poker Hand Evaluator" to determine the relative strength of two five-card hands.  
	// Reference: http://www.suffecool.net/poker/evaluator.html


	// Determines the relative strength of a hand (the hand is given by its unique key value).
	public static int getHandRank(long key) {
		
		int[] hand = new int[8];
		int handRank = 9999;
		int numCards = 0;
		
		if (key != 0) {
			numCards = setHand(hand, key);
			handRank = evalHand(numCards, hand);
		}
		return handRank;
		
	} // END getHandRank method
	
	
	private static int setHand(int[] hand, long key) {
		
		int currentCard;
		int rank;
		int suit     = 0;
		int numCards = 0;
		
		final int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41};
		
		for (int cardIndex = 0; cardIndex < 7; cardIndex++) {
			
			currentCard = (int)((key >>> (cardIndex << 3)) & 0xFF);
			if (currentCard == 0) break;
			if (currentCard != (14 << 4)) {
				// Cactus Kev Card Representation
				// +--------+--------+--------+--------+
				// |xxxbbbbb|bbbbbbbb|cdhsrrrr|xxpppppp|
				// +--------+--------+--------+--------+
				// p	= prime number of rank	(deuce = 2, trey = 3, four = 5, five = 7,..., ace = 41)
				// r	= rank of card		(deuce = 0, trey = 1, four = 2, five = 3,..., ace = 12)
				// cdhs = suit of card
				// b	= bit turned on depending on rank of card
				
				// extract suit and rank from 8-bit packed representation
				rank = (currentCard >>> 4) - 1;	
				suit = currentCard & 0xF; 
				
				// change card representation to Cactus Kev Representation
				hand[numCards] = primes[rank] | (rank << 8) | (1 << (suit + 11)) | (1 << (16 + rank));
				numCards++;
			}
		}
		return numCards;
	} // END setHand method
	
	
	private static int evalHand(int numCards, int[] hand) {
		int holdrank = 9999;
		switch (numCards) { 
			case 5 :  
				
				holdrank = eval_5hand(hand[0],hand[1],hand[2],hand[3],hand[4]);
				break;
				
			case 6 :
				
				// Cactus Kev's Evaluator ranks hands from 1 (Royal Flush) to 7462 (Seven High Card)
				holdrank = eval_5hand(			 	 hand[0],hand[1],hand[2],hand[3],hand[4]);
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[1],hand[2],hand[3],hand[5]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[1],hand[2],hand[4],hand[5]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[1],hand[3],hand[4],hand[5]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[2],hand[3],hand[4],hand[5]));
				holdrank = Math.min(holdrank, eval_5hand(hand[1],hand[2],hand[3],hand[4],hand[5]));
				break;
				
			case 7 :
				
				holdrank = eval_5hand(				 hand[0],hand[1],hand[2],hand[3],hand[4]);
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[1],hand[2],hand[3],hand[5]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[1],hand[2],hand[3],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[1],hand[2],hand[4],hand[5]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[1],hand[2],hand[4],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[1],hand[2],hand[5],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[1],hand[3],hand[4],hand[5]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[1],hand[3],hand[4],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[1],hand[3],hand[5],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[1],hand[4],hand[5],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[2],hand[3],hand[4],hand[5]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[2],hand[3],hand[4],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[2],hand[3],hand[5],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[2],hand[4],hand[5],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[0],hand[3],hand[4],hand[5],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[1],hand[2],hand[3],hand[4],hand[5]));
				holdrank = Math.min(holdrank, eval_5hand(hand[1],hand[2],hand[3],hand[4],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[1],hand[2],hand[3],hand[5],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[1],hand[2],hand[4],hand[5],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[1],hand[3],hand[4],hand[5],hand[6]));
				holdrank = Math.min(holdrank, eval_5hand(hand[2],hand[3],hand[4],hand[5],hand[6]));
				break;
				
			default :
				
				System.out.println("ERROR: Invalid hand in evalHand method.");
				break;
				
		}
		return toHandRankPresentation(holdrank);
	} // END evalHand method
	
	
	/**
	 * Method convers Cactus Kev rank to actual handrank
      * Hand Rank Representation
	 * +--------+--------+
	 * |hhhheeee|eeeeeeee|
	 * +--------+--------+
	 * h	= poker hand	(1 = High Card, 2 = One Pair, 3 = Two Pair,..., 9 = Straight Flush)
	 * e	= equivalency class (Rank of equivalency class relative to base hand)
	 *
	 * +-----------------------------------+----------------------------------+-----------------+
	 * 		5-Card Equivalency Classes			7-Card Equivalency Classes
	 * +-----------------------------------+----------------------------------+-----------------+
	 * 		1277	 							 	 407		High Card
	 *		2860 								1470		One Pair
	 *		 858 								 763		Two Pair
	 *		 858 								 575		Three of a Kind
	 *		  10 								  10		Straight
	 *		1277 								1277		Flush
	 *		 156 								 156		Full House
	 *		 156 								 156		Four of a Kind
	 *		  10								 	  10		Straight Flush
	 * +----------+------------------------+----------------------------------+-----------------+
	 * Total: 7462									4824
	 * +----------+------------------------+----------------------------------+-----------------+
	*/
	public static int toHandRankPresentation(int holdrank) {
		int handRank = 7463 - holdrank;  // Invert ranking metric (1 is now worst hand)
		
		if	   (handRank < 1278) handRank = handRank -	 0 + 4096 * 1; // High Card 
		else if (handRank < 4138) handRank = handRank - 1277 + 4096 * 2; // One Pair  
		else if (handRank < 4996) handRank = handRank - 4137 + 4096 * 3; // Two Pair   
		else if (handRank < 5854) handRank = handRank - 4995 + 4096 * 4; // Three of a Kind   
		else if (handRank < 5864) handRank = handRank - 5853 + 4096 * 5; // Straight	 
		else if (handRank < 7141) handRank = handRank - 5863 + 4096 * 6; // Flush   
		else if (handRank < 7297) handRank = handRank - 7140 + 4096 * 7; // Full House	
		else if (handRank < 7453) handRank = handRank - 7296 + 4096 * 8; // Four of a Kind	
		else					 handRank = handRank - 7452 + 4096 * 9; // Straight Flush	 
		return handRank;
	} // END toHandRankPresentation method
	
	
	private static int getIndex(int key) {
		// use binary search to find key
		int low = -1;
		int high = 4888;
		int pivot;
		
		while (high - low > 1) {
			pivot = (low + high) >>> 1;
			if (Products.table[pivot] > key) {
				high = pivot;
			}
			else if (Products.table[pivot] < key) {
				low = pivot;
			}
			else {
				return pivot;	
			}
		}
		return -1;
	} // END getIndex method
	
	
	private static int eval_5hand(int c1, int c2, int c3, int c4, int c5) {
		int   q = (c1 | c2 | c3 | c4 | c5) >> 16;
		short s;
		
		// check for Flushes and Straight Flushes
		if ((c1 & c2 & c3 & c4 & c5 & 0xF000) != 0) return Flushes.table[q];
		
		// check for Straights and High Card hands
		if ((s = Unique.table[q]) != 0) return s;
		
		q = (c1 & 0xFF) * (c2 & 0xFF) * (c3 & 0xFF) * (c4 & 0xFF) * (c5 & 0xFF);
		q = getIndex(q);
		 
		return Values.table[q];
		
	} // END eval_5hand method
	
	
} // END class HandRank
