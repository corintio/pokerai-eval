package pokerai.game.eval.mykey;

import java.util.Random;

public final class Deck {
	private static long seed = new Random().nextLong();
	private static long[] deck;

	static {
		deck = new long[52];
		for (int i=0; i < 52; i++) {
			deck[i] = HandMask.HandMasksTable[i];
		}
	}

	public static final long getHand() {
		randomHand(7);
		return deck[0] | deck[1] | deck[2] | deck[3] | deck[4] | deck[5] | deck[6];
	}
	
	public static final void randomHand(int cards) {		
		for (int i=0; i < cards; i++) {
			int idx = (int)(random() * (double)(52 - i)) + i;
			long swap = deck[i];
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
