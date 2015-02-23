package pokerai.game.eval.hammer.tests;

import pokerai.game.eval.hammer.Deck;
import pokerai.game.eval.hammer.HandEvaluator;
import pokerai.game.eval.hammer.classes.EquivalenceClasses;
import pokerai.game.eval.hammer.purejava.OneStagePJHE;
import pokerai.game.eval.hammer.purejava.TwoStagePJHE;
import pokerai.game.eval.hammer.purejava.CheckFlushPJHE;
import pokerai.game.eval.hammer.purejava.PartialStagePJHE;

import java.text.DecimalFormat;

public class BulkTests {
	private static final int TRIALS = 15000000;

	public static void runtests(HandEvaluator he, int trials, long overhead) {
		Deck d = new Deck();
		long ts = System.currentTimeMillis();
		for (int i=0; i < trials; i++) {
			if ((i+1) % 1000000 == 0) {
				System.out.println((System.currentTimeMillis() - ts) + "ms, evaluated " + ((i+1) / 1000000) + " million hands");
			}
			he.calculateEquivalenceClass(d.getCards());
		}
		ts = System.currentTimeMillis() - ts;
		DecimalFormat df = new DecimalFormat("#");
		double ps = (double)trials / ((double)ts / (double)1000);
		System.out.println(he.getClass().getName());
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
		EquivalenceClasses.getInstance();	// assert equivalence class loading
		long overhead = BulkTests.overhead(TRIALS);
		BulkTests.runtests(new OneStagePJHE(), TRIALS, overhead);
		BulkTests.runtests(new TwoStagePJHE(), TRIALS, overhead);
		BulkTests.runtests(new CheckFlushPJHE(), TRIALS, overhead);
	}	
}
