package pokerai.game.eval.mykey;

import java.text.DecimalFormat;

public class RandomTest {
	private static final int TRIALS = 15000000;

	public static void runtests(int trials, long overhead) {
		Deck d = new Deck();
		long ts = System.currentTimeMillis();
		for (int i=0; i < trials; i++) {
			if ((i+1) % 1000000 == 0) {
				System.out.println((System.currentTimeMillis() - ts) + "ms, evaluated " + ((i+1) / 1000000) + " million hands");
			}
			Eval.rankHand(d.getHand());
		}
		ts = System.currentTimeMillis() - ts;
		DecimalFormat df = new DecimalFormat("#");
		double ps = (double)trials / ((double)ts / (double)1000);
		System.out.println("with overhead: " + trials + " trials, " + ts + "ms elapsed, " + df.format(ps) + " trials per second");
		long actual = ts-overhead;
		ps = (double)trials / ((double)actual / (double)1000);
		System.out.println("without overhead: " + trials + " trials, " + actual + "ms elapsed, " + df.format(ps) + " trials per second");
	}
	
	public static long overhead(int trials) {
		Deck d = new Deck();
		long ts = System.currentTimeMillis();
		for (int i=0; i < trials; i++) {
			if ((i+1) % 1000000 == 0) {
				System.out.println((System.currentTimeMillis() - ts) + "ms, evaluated " + ((i+1) / 1000000) + " million hands");
			}
			d.getHand();
		}
		ts = System.currentTimeMillis() - ts;
		System.out.println("Overhead of " + trials + " trials: " + ts + "ms elapsed");
		return ts;
	}
	
	public static void main(String[] args) {
		long hand = HandMask.HandMasksTable[1]
			| HandMask.HandMasksTable[2]
			| HandMask.HandMasksTable[8]
			| HandMask.HandMasksTable[15]
			| HandMask.HandMasksTable[23]
			| HandMask.HandMasksTable[26]
			| HandMask.HandMasksTable[40];
		int ret = Eval.rankHand(hand);
		System.out.println(ret);
		long overhead = overhead(TRIALS);
		runtests(TRIALS, overhead);
	}	
}
