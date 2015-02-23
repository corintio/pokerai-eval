package pokerai.game.eval.hammer.purejava;

import pokerai.game.eval.hammer.PartialStageHandEvaluator;
import pokerai.game.eval.hammer.classes.EquivalenceClass;

public final class PartialStagePJHE implements PartialStageHandEvaluator {
	protected static final DAGNode[] nodes = Dag.nodes;

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
	
	private int card1, card2, card3, card4, card5, card6, card7;
	private int sval1, sval2, sval3, sval4, sval5, sval6, sval7;
	private DAGNode node1, node2, node3, node4, node5, node6;
	
	
	public void setCard1(int card) {
		card1 = card;
		sval1 = SUIT_INIT_MASK + (1 << ((card & SUIT_MASK) << LOG2_SUIT_COUNT));
		node1 = nodes[card >> LOG2_SUIT_COUNT];
	}
	
	public void setCard2(int card) {
		card2 = card;
		sval2 = sval1 + (1 << ((card & SUIT_MASK) << LOG2_SUIT_COUNT));
		node2 = node1.next[card >> LOG2_SUIT_COUNT];
	}
	
	public void setCard3(int card) {
		card3 = card;
		sval3 = sval2 + (1 << ((card & SUIT_MASK) << LOG2_SUIT_COUNT));
		node3 = node2.next[card >> LOG2_SUIT_COUNT];
	}
	
	public void setCard4(int card) {
		card4 = card;
		sval4 = sval3 + (1 << ((card & SUIT_MASK) << LOG2_SUIT_COUNT));
		node4 = node3.next[card >> LOG2_SUIT_COUNT];
	}
	
	public void setCard5(int card) {
		card5 = card;
		sval5 = sval4 + (1 << ((card & SUIT_MASK) << LOG2_SUIT_COUNT));
		node5 = node4.next[card >> LOG2_SUIT_COUNT];
	}
	
	public void setCard6(int card) {
		card6 = card;
		sval6 = sval5 + (1 << ((card & SUIT_MASK) << LOG2_SUIT_COUNT));
		node6 = node5.next[card >> LOG2_SUIT_COUNT];
	}
	
	public EquivalenceClass setHand7(int card) {
		sval7 = sval6 + (1 << ((card & SUIT_MASK) << LOG2_SUIT_COUNT));
		if ((sval7 & FLUSH_MASK) == 0) {
			return node6.next[card >> LOG2_SUIT_COUNT].rankclass;
		}
		card7 = card;
		// Find the cards that form the flush
		int fc = (((sval7 & (CLUB_FLUSH | DIAMOND_FLUSH)) != 0) ? 
			(((sval7 & CLUB_FLUSH) != 0) ? CLUBS : DIAMONDS) :
			(((sval7 & SPADE_FLUSH) != 0) ? SPADES : HEARTS));
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
	
	public EquivalenceClass setHand6(int card) {
		card6 = card;
		sval6 = sval5 + (1 << ((card & SUIT_MASK) << LOG2_SUIT_COUNT));
		if ((sval6 & FLUSH_MASK) == 0) {
			return (node6 = node5.next[card >> LOG2_SUIT_COUNT]).rankclass;
		}
		// Find the cards that form the flush
		int fc = (((sval6 & (CLUB_FLUSH | DIAMOND_FLUSH)) != 0) ? 
			(((sval6 & CLUB_FLUSH) != 0) ? CLUBS : DIAMONDS) :
			(((sval6 & SPADE_FLUSH) != 0) ? SPADES : HEARTS));
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
	
	public EquivalenceClass setHand5(int card) {
		card5 = card;
		sval5 = sval4 + (1 << ((card & SUIT_MASK) << LOG2_SUIT_COUNT));
		if ((sval5 & FLUSH_MASK) == 0) {
			return (node5 = node4.next[card >> LOG2_SUIT_COUNT]).rankclass;
		}
		// Find the cards that form the flush
		int fc = (((sval5 & (CLUB_FLUSH | DIAMOND_FLUSH)) != 0) ? 
			(((sval5 & CLUB_FLUSH) != 0) ? CLUBS : DIAMONDS) :
			(((sval5 & SPADE_FLUSH) != 0) ? SPADES : HEARTS));
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
	
}
