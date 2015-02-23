package pokerai.game.eval.klaatu;

import java.text.DecimalFormat;

public class BulkTests {
	private static final int TRIALS = 15000000;

	public static void runtests(int trials, long overhead) {
		Deck d = new Deck();
		long ts = System.currentTimeMillis();
		for (int i=0; i < trials; i++) {
			if ((i+1) % 1000000 == 0) {
				System.out.println((System.currentTimeMillis() - ts) + "ms, evaluated " + ((i+1) / 1000000) + " million hands");
			}
			int [] cards = d.getCards();
			FastEval.eval7(cards[0],cards[1],cards[2],cards[3],cards[4],cards[5],cards[6]);
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
			d.getCards();
		}
		ts = System.currentTimeMillis() - ts;
		System.out.println("Overhead of " + trials + " trials: " + ts + "ms elapsed");
		return ts;
	}
	
	public static void main(String[] args) {
		int c0,c1 = FastEval.encode(12,1);
		int c2 = FastEval.encode(5,2);
		int c3 = FastEval.encode(0,3);
		int c4 = FastEval.encode(9,1);
		int c5 = FastEval.encode(6,2);
		int c6 = FastEval.encode(6,4);
		int c7 = FastEval.encode(7,4);
		int ret = FastEval.eval7(c1, c2, c3, c4, c5, c6, c7);
		System.out.println("ret " +ret);
		long overhead = BulkTests.overhead(TRIALS);
		BulkTests.runtests(TRIALS, overhead);
	}	
}
