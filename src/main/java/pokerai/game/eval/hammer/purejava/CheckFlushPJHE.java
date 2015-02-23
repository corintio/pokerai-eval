package pokerai.game.eval.hammer.purejava;

import pokerai.game.eval.hammer.classes.EquivalenceClass;

public final class CheckFlushPJHE extends PureJavaHandEvaluator {

	// arbitrary order
	public static final int HEARTS = 0;
	public static final int SPADES = 1;
	public static final int CLUBS = 2;
	public static final int DIAMONDS = 3;
	
	public static final int SUIT_MASK = 3;
	public static final int LOG2_SUIT_COUNT = 2;
	
	private static final int FLUSH_COUNT = 5; // cards needed for a flush
	// SUIT_INIT value chosen to push made-flush suit counts into next nibble bits
	private static final int SUIT_INIT = 3;

	private static final int HEART_FLUSH = (1 << (HEARTS << LOG2_SUIT_COUNT)) * (SUIT_INIT + FLUSH_COUNT);
	private static final int SPADE_FLUSH = (1 << (SPADES << LOG2_SUIT_COUNT)) * (SUIT_INIT + FLUSH_COUNT);
	private static final int CLUB_FLUSH = (1 << (CLUBS << LOG2_SUIT_COUNT)) * (SUIT_INIT + FLUSH_COUNT);
	private static final int DIAMOND_FLUSH = (1 << (DIAMONDS << LOG2_SUIT_COUNT)) * (SUIT_INIT + FLUSH_COUNT);

	private static final int FLUSH_MASK = CLUB_FLUSH | DIAMOND_FLUSH | HEART_FLUSH | SPADE_FLUSH;

	private static final int SUIT_INIT_MASK = 
		((1 << (HEARTS << LOG2_SUIT_COUNT)) * SUIT_INIT) |
		((1 << (SPADES << LOG2_SUIT_COUNT)) * SUIT_INIT) |
		((1 << (CLUBS << LOG2_SUIT_COUNT)) * SUIT_INIT) |
		((1 << (DIAMONDS << LOG2_SUIT_COUNT)) * SUIT_INIT);


	public final EquivalenceClass eval7(int card1, int card2, int card3, int card4, int card5, int card6, int card7) {
		// check flushes first
		int sval = SUIT_INIT_MASK +
			(1 << ((card1 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card2 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card3 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card4 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card5 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card6 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card7 & SUIT_MASK) << LOG2_SUIT_COUNT));
		
		if ((sval & FLUSH_MASK) == 0) {
			// done with the 96.94% of all hands that aren't flushes or straight flushes
			// return rankclass
			return nodes[card1 >> LOG2_SUIT_COUNT].next[card2 >> LOG2_SUIT_COUNT]
				.next[card3 >> LOG2_SUIT_COUNT].next[card4 >> LOG2_SUIT_COUNT]
				.next[card5 >> LOG2_SUIT_COUNT].next[card6 >> LOG2_SUIT_COUNT]
				.next[card7 >> LOG2_SUIT_COUNT].rankclass;
		}
		// Find the cards that form the flush
		int fc = (((sval & (CLUB_FLUSH | DIAMOND_FLUSH)) != 0) ? 
			(((sval & CLUB_FLUSH) != 0) ? CLUBS : DIAMONDS) :
			(((sval & SPADE_FLUSH) != 0) ? SPADES : HEARTS));
		DAGNode n = null;
		if ((card1 & SUIT_MASK) == fc) {
			// no check needed n is allways null
			n = nodes[card1 >> LOG2_SUIT_COUNT];
		}
		if ((card2 & SUIT_MASK) == fc) {
			if (n == null) {
				n = nodes[card2 >> LOG2_SUIT_COUNT];
			} else {
				n = n.next[card2 >> LOG2_SUIT_COUNT];
			}
		}
		if ((card3 & SUIT_MASK) == fc) {
			if (n == null) {
				n = nodes[card3 >> LOG2_SUIT_COUNT];
			} else {
				n = n.next[card3 >> LOG2_SUIT_COUNT];
			}
		}
		if ((card4 & SUIT_MASK) == fc) {
			// no check needed n is allways set
			n = n.next[card4 >> LOG2_SUIT_COUNT];
		}
		if ((card5 & SUIT_MASK) == fc) {
			n = n.next[card5 >> LOG2_SUIT_COUNT];
		}
		if ((card6 & SUIT_MASK) == fc) {
			n = n.next[card6 >> LOG2_SUIT_COUNT];
		}
		if ((card7 & SUIT_MASK) == fc) {
			n = n.next[card7 >> LOG2_SUIT_COUNT];
		}
		// return flushclass
		return n.flushclass;
	}
	
	public final EquivalenceClass eval6(int card1, int card2, int card3, int card4, int card5, int card6) {
		// check flushes first
		int sval = SUIT_INIT_MASK +
			(1 << ((card1 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card2 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card3 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card4 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card5 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card6 & SUIT_MASK) << LOG2_SUIT_COUNT));
		
		if ((sval & FLUSH_MASK) == 0) {
			// done with the 96.94% of all hands that aren't flushes or straight flushes
			// return rankclass
			return nodes[card1 >> LOG2_SUIT_COUNT].next[card2 >> LOG2_SUIT_COUNT]
				.next[card3 >> LOG2_SUIT_COUNT].next[card4 >> LOG2_SUIT_COUNT]
				.next[card5 >> LOG2_SUIT_COUNT].next[card6 >> LOG2_SUIT_COUNT].rankclass;
		}
		// Find the cards that form the flush
		int fc = (((sval & (CLUB_FLUSH | DIAMOND_FLUSH)) != 0) ? 
			(((sval & CLUB_FLUSH) != 0) ? CLUBS : DIAMONDS) :
			(((sval & SPADE_FLUSH) != 0) ? SPADES : HEARTS));
		DAGNode n = null;
		if ((card1 & SUIT_MASK) == fc) {
			// no check needed n is allways null
			n = nodes[card1 >> LOG2_SUIT_COUNT];
		}
		if ((card2 & SUIT_MASK) == fc) {
			if (n == null) {
				n = nodes[card2 >> LOG2_SUIT_COUNT];
			} else {
				n = n.next[card2 >> LOG2_SUIT_COUNT];
			}
		}
		if ((card3 & SUIT_MASK) == fc) {
			if (n == null) {
				n = nodes[card3 >> LOG2_SUIT_COUNT];
			} else {
				n = n.next[card3 >> LOG2_SUIT_COUNT];
			}
		}
		if ((card4 & SUIT_MASK) == fc) {
			// no check needed n is allways set
			n = n.next[card4 >> LOG2_SUIT_COUNT];
		}
		if ((card5 & SUIT_MASK) == fc) {
			n = n.next[card5 >> LOG2_SUIT_COUNT];
		}
		if ((card6 & SUIT_MASK) == fc) {
			n = n.next[card6 >> LOG2_SUIT_COUNT];
		}
		// return flushclass
		return n.flushclass;
	}
	
	public final EquivalenceClass eval5(int card1, int card2, int card3, int card4, int card5) {
		// check flushes first
		int sval = SUIT_INIT_MASK +
			(1 << ((card1 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card2 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card3 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card4 & SUIT_MASK) << LOG2_SUIT_COUNT)) +
			(1 << ((card5 & SUIT_MASK) << LOG2_SUIT_COUNT));
		
		if ((sval & FLUSH_MASK) == 0) {
			// done with the 96.94% of all hands that aren't flushes or straight flushes
			// return rankclass
			return nodes[card1 >> LOG2_SUIT_COUNT].next[card2 >> LOG2_SUIT_COUNT]
				.next[card3 >> LOG2_SUIT_COUNT].next[card4 >> LOG2_SUIT_COUNT]
				.next[card5 >> LOG2_SUIT_COUNT].rankclass;
		}
		// Find the cards that form the flush
		int fc = (((sval & (CLUB_FLUSH | DIAMOND_FLUSH)) != 0) ? 
			(((sval & CLUB_FLUSH) != 0) ? CLUBS : DIAMONDS) :
			(((sval & SPADE_FLUSH) != 0) ? SPADES : HEARTS));
		DAGNode n = null;
		if ((card1 & SUIT_MASK) == fc) {
			// no check needed n is allways null
			n = nodes[card1 >> LOG2_SUIT_COUNT];
		}
		if ((card2 & SUIT_MASK) == fc) {
			if (n == null) {
				n = nodes[card2 >> LOG2_SUIT_COUNT];
			} else {
				n = n.next[card2 >> LOG2_SUIT_COUNT];
			}
		}
		if ((card3 & SUIT_MASK) == fc) {
			if (n == null) {
				n = nodes[card3 >> LOG2_SUIT_COUNT];
			} else {
				n = n.next[card3 >> LOG2_SUIT_COUNT];
			}
		}
		if ((card4 & SUIT_MASK) == fc) {
			// no check needed n is allways set
			n = n.next[card4 >> LOG2_SUIT_COUNT];
		}
		if ((card5 & SUIT_MASK) == fc) {
			n = n.next[card5 >> LOG2_SUIT_COUNT];
		}
		// return flushclass
		return n.flushclass;
	}
	
	public final EquivalenceClass calculateEquivalenceClass(int[] hand) {
		switch (hand.length) {
			case 5 :
				return eval5(hand[0], hand[1], hand[2], hand[3], hand[4]);
			case 6 :
				return eval6(hand[0], hand[1], hand[2], hand[3], hand[4], hand[5]);
			case 7 :
				return eval7(hand[0], hand[1], hand[2], hand[3], hand[4], hand[5], hand[6]);
		}
		return null;
	}
}
