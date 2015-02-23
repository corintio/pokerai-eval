package pokerai.game.eval.tests;

public final class Deck {
	// we use same seed to get same same result every time
     private static long seed = 19275913L;
	private static int[] deck;
	private static	int[] ret = new int[7];

	static {
		deck = new int[52];
		for (int i=0; i < 52; i++) {
			deck[i] = i;
		}
	}

	public static final int[] getCards() {
		randomHand(7);
		System.arraycopy(deck, 0, ret, 0, 7);
		return ret;
	}
	
	public static final void randomHand(int cards) {		
		for (int i=0; i < cards; i++) {
			int idx = (int)(random() * (double)(52 - i)) + i;
			int swap = deck[i];
			deck[i] = deck[idx];
			deck[idx] = swap;
		}
	}

	/**
	 * XorShift random number generation
	 * it is faster and statically more random than java.util.Random
	 */
	private static final double random() {
  		seed ^= (seed << 21);
  		seed ^= (seed >>> 35);
  		seed ^= (seed << 4);
  		return (double) Math.abs(seed) / Long.MAX_VALUE;
	}

}
