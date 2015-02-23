package pokerai.game.eval.tests;

import com.biotools.meerkat.Hand;
import pokerai.game.eval.alberta.HandEvaluatorAlberta;
import pokerai.game.eval.indiana.FastHandEvaluator;
import pokerai.game.eval.indiana.HandEvaluator;
import pokerai.game.eval.spears.SevenCardEvaluator;
import pokerai.game.eval.spears2p2.StateTableEvaluator;
import pokerai.game.eval.jokipii.LUTEvaluator;
import pokerai.game.eval.stevebrecher.HandEval;
import pokerai.game.eval.mykey.Eval;
import pokerai.game.eval.mykey.EvalExp;
import pokerai.game.eval.klaatu.FastEval;
import pokerai.game.eval.klaatu.PartialStageFastEval;
import pokerai.game.eval.hammer.purejava.CheckFlushPJHE;
import pokerai.game.eval.hammer.purejava.PartialStagePJHE;
import pokerai.game.eval.supersonic.LookupHandEvaluator;
import pokerai.game.util.CardConverter;

import java.text.DecimalFormat;

/**
 * Class performs random hand prformnance test for evaluators
 * Class generetes 1 million random hands and evaluates those 20 times. 
 * This is because 20 million random hands with all different types of 
 * card versions takes too much memory to be convinient.
 */
public class RandomPerformanceTest {

	static String[] ref = {
		"( 1) Indiana-1, 2006, http://pokerai.org/pf3",
		"( 2) Indiana-3, 2007, http://pokerai.org/pf3",
		"( 3) University of Alberta, 2000, http://spaz.ca/poker",
		"( 4) Spears port of Kevin Suffecool's C evaluator, http://pokerai.org/pf3",
		"( 5) Spears port of 2+2 evaluator, http://pokerai.org/pf3, http://forumserver.twoplustwo.com",
		"( 6) Steve Brecher HandEval, http://www.stevebrecher.com/Software/software.html",
		"( 7) Spears adaptation of RayW LUT hand evaluator, http://pokerai.org/pf3, http://forumserver.twoplustwo.com",
		"( 8) Steve Brecher HandEval with [url=http://archives1.twoplustwo.com/showthreaded.php?Cat=0&Number=8582014&page=0&vc=1]partial state generation[/url], http://pokerai.org/pf3",
		"( 9) Pokerstove (Andrew Prock's) jpoker, http://www.pokerstove.com/download/jpoker.tar.gz",
		"(10) Mykey1961 algorithm java implementation, http://pokerai.org/pf3",
		"(11) Mykey1961 algorithm java implementation with partial state generation, http://pokerai.org/pf3",
		"(12) Mykey1961 algorithm java implementation (experimental optimization), http://pokerai.org/pf3",
		"(13) Mykey1961 algorithm java implementation (experimental optimization) with partial state generation, http://pokerai.org/pf3",
		"(14) Klaatu FastEval, http://pokerai.org/pf3",
		"(15) Klaatu FastEval with partial state generation, http://pokerai.org/pf3",
		"(16) Hammer Dag evaluator, http://www.pst.ifi.lmu.de/~hammer/poker/handeval.html",
		"(17) Hammer Dag evaluator with partial state generation, http://www.pst.ifi.lmu.de/~hammer/poker/handeval.html",
		"(18) Supersonic's evaluator, http://pokerai.org/pf3/viewtopic.php?p=15760#p15760",
		"(19) Jokipii's compressed RayW LUT Evaluator, http://pokerai.org/pf3/",
	};
	
	static int TESTRUNS = 10;
	static int RANDOMRUNS = 20;
	static int RANDOMTESTS = 1000000;
	static int[][] randomHands = new int[RANDOMTESTS][7];

	public static void main(String args[]) {
		initRandomCards();
		for (int i = 0; i < TESTRUNS; i++) {
			System.out.println("\n---- Test set run " + (i + 1) + "----\n");
			testIndiana();
			testIndiana3();
			testAlberta();
			testSpears();
			testSpears2p2();
			testSteveBrecher();
			testRayW();
			testPokerstove();
			testMykey();
			testMykeyExp();
			testKlaatu();
			testHammer();
			testSupersonic();
			testJokipii();
		}
	}
	
	public static void initRandomCards() {
		for (int i = 0; i < RANDOMTESTS; i++) {
			System.arraycopy(Deck.getCards(), 0, randomHands[i], 0, 7);
		}
	}
	
	public static void testIndiana() {
		long sum = 0;
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				sum += HandEvaluator.defineHand(randomHands[i]);
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 0);
	}

	public static void testIndiana3() {
		long sum = 0;
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				sum += FastHandEvaluator.evaluate7(randomHands[i]);
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 1);
	}

	public static void testAlberta() {
		long sum = 0;
		int[] hand;
		Hand h = new Hand();
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				hand = randomHands[i];
				h.makeEmpty();
				h.addCard(hand[0]);
				h.addCard(hand[1]);
				h.addCard(hand[2]);
				h.addCard(hand[3]);
				h.addCard(hand[4]);
				h.addCard(hand[5]);
				h.addCard(hand[6]);
				sum += HandEvaluatorAlberta.rankHand(h);
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 2);
	}

	public static void testSpears() {
		long sum = 0;
		int[] hand;
		SevenCardEvaluator eval = new SevenCardEvaluator();
		pokerai.game.eval.spears.Card[] cards = new pokerai.game.eval.spears.Card[7];
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				hand = randomHands[i];
				cards[0] = pokerai.game.eval.spears.Card.get(hand[0]);
				cards[1] = pokerai.game.eval.spears.Card.get(hand[1]);
				cards[2] = pokerai.game.eval.spears.Card.get(hand[2]);
				cards[3] = pokerai.game.eval.spears.Card.get(hand[3]);
				cards[4] = pokerai.game.eval.spears.Card.get(hand[4]);
				cards[5] = pokerai.game.eval.spears.Card.get(hand[5]);
				cards[6] = pokerai.game.eval.spears.Card.get(hand[6]);
				sum += eval.evaluate(cards);
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 3);
	}


	public static void testSpears2p2() {
		long sum = 0;
		int[] hand;
		StateTableEvaluator.initialize();
		pokerai.game.eval.spears2p2.Card[] cards = new pokerai.game.eval.spears2p2.Card[7];
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				hand = randomHands[i];
				cards[0] = pokerai.game.eval.spears2p2.Card.get(hand[0]);
				cards[1] = pokerai.game.eval.spears2p2.Card.get(hand[1]);
				cards[2] = pokerai.game.eval.spears2p2.Card.get(hand[2]);
				cards[3] = pokerai.game.eval.spears2p2.Card.get(hand[3]);
				cards[4] = pokerai.game.eval.spears2p2.Card.get(hand[4]);
				cards[5] = pokerai.game.eval.spears2p2.Card.get(hand[5]);
				cards[6] = pokerai.game.eval.spears2p2.Card.get(hand[6]);
				sum += StateTableEvaluator.getRank(cards);
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 4);
	}

	public static void testRayW() {
		long sum = 0;
		int[] hand;
		System.gc(); try { Thread.currentThread().sleep(1000); } catch (Exception e) {};
		long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		StateTableEvaluator.initialize();
		System.gc(); try { Thread.currentThread().sleep(1000); } catch (Exception e) {};
		long mem2 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Memory used: " + ((mem2-mem1)/(1024*1024)) + " Mb");
		int[] handRanks = StateTableEvaluator.handRanks;
		int[][] randoms = new int[RANDOMTESTS][7];
		for (int i = 0; i < RANDOMTESTS; i++) {
			randoms[i][0] = CardConverter.convertZeroBasedRankFirstToOneBasedRankFirst(randomHands[i][0]);
			randoms[i][1] = CardConverter.convertZeroBasedRankFirstToOneBasedRankFirst(randomHands[i][1]);
			randoms[i][2] = CardConverter.convertZeroBasedRankFirstToOneBasedRankFirst(randomHands[i][2]);
			randoms[i][3] = CardConverter.convertZeroBasedRankFirstToOneBasedRankFirst(randomHands[i][3]);
			randoms[i][4] = CardConverter.convertZeroBasedRankFirstToOneBasedRankFirst(randomHands[i][4]);
			randoms[i][5] = CardConverter.convertZeroBasedRankFirstToOneBasedRankFirst(randomHands[i][5]);
			randoms[i][6] = CardConverter.convertZeroBasedRankFirstToOneBasedRankFirst(randomHands[i][6]);
		}
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				hand = randoms[i];
				sum += handRanks[handRanks[handRanks[handRanks[handRanks[handRanks[handRanks[
					53 + hand[0]] + hand[1]] + hand[2]] + hand[3]] + hand[4]] + hand[5]] + hand[6]];
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 6);
	}

	public static void testSteveBrecher() {
		long sum = 0;
		long[] hand;
		long[][] randoms = new long[RANDOMTESTS][7];
		for (int i = 0; i < RANDOMTESTS; i++) {
			randoms[i][0] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][0]);
			randoms[i][1] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][1]);
			randoms[i][2] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][2]);
			randoms[i][3] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][3]);
			randoms[i][4] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][4]);
			randoms[i][5] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][5]);
			randoms[i][6] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][6]);
		}
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				hand = randoms[i];
				sum += HandEval.hand7Eval(hand[0] | hand[1] | hand[2] | hand[3] | hand[4] | hand[5] | hand[6]);
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 5);
	}

	public static void testPokerstove() {
		long sum = 0;
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				sum += pokerai.game.eval.jpoker.HandEvaluator.doFullEvaluation(randomHands[i]);
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 8);
	}


	public static void testMykey() {
		long sum = 0;
		long[] hand;
		long[][] randoms = new long[RANDOMTESTS][7];
		for (int i = 0; i < RANDOMTESTS; i++) {
			randoms[i][0] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][0]);
			randoms[i][1] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][1]);
			randoms[i][2] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][2]);
			randoms[i][3] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][3]);
			randoms[i][4] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][4]);
			randoms[i][5] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][5]);
			randoms[i][6] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][6]);
		}
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				hand = randoms[i];
				sum += Eval.rankHand(hand[0] | hand[1] | hand[2] | hand[3] | hand[4] | hand[5] | hand[6]);
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 9);
	}

	public static void testMykeyExp() {
		long sum = 0;
		long[] hand;
		long[][] randoms = new long[RANDOMTESTS][7];
		for (int i = 0; i < RANDOMTESTS; i++) {
			randoms[i][0] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][0]);
			randoms[i][1] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][1]);
			randoms[i][2] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][2]);
			randoms[i][3] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][3]);
			randoms[i][4] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][4]);
			randoms[i][5] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][5]);
			randoms[i][6] = CardConverter.convertZeroBasedRankFirstToBitSuitFirst(randomHands[i][6]);
		}
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				hand = randoms[i];
				sum += EvalExp.rankHand(hand[0] | hand[1] | hand[2] | hand[3] | hand[4] | hand[5] | hand[6]);
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 11);
	}

	public static void testKlaatu() {
		long sum = 0;
		int[] hand;
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				hand = randomHands[i];
				sum += FastEval.eval7(hand[0], hand[1], hand[2], hand[3], hand[4], hand[5], hand[6]);
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 13);
	}

	public static void testHammer() {
		CheckFlushPJHE he = new CheckFlushPJHE();
		long sum = 0;
		int[] hand;
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				hand = randomHands[i];
				sum += he.eval7(hand[0], hand[1], hand[2], hand[3], hand[4], hand[5], hand[6]).getId();
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 15);
	}

	public static void testSupersonic() {
		long sum = 0;
		int[] hand;
		LookupHandEvaluator he = LookupHandEvaluator.getInstance("handRanks.ser");
		Hand h = new Hand();
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				hand = randomHands[i];
				h.makeEmpty();
				h.addCard(hand[0]);
				h.addCard(hand[1]);
				h.addCard(hand[2]);
				h.addCard(hand[3]);
				h.addCard(hand[4]);
				h.addCard(hand[5]);
				h.addCard(hand[6]);
				sum += he.rankHand7(h);
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 17);
	}
	
	// this test uses compress mode
	public static void testJokipii() {
		long sum = 0;
		int[] hand;
		System.gc(); try { Thread.currentThread().sleep(1000); } catch (Exception e) {};
		long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		LUTEvaluator.setMode(LUTEvaluator.MODE_COMPRESS);
		LUTEvaluator.initialize();
		System.gc(); try { Thread.currentThread().sleep(1000); } catch (Exception e) {};
		long mem2 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Memory used: " + ((mem2-mem1)/(1024*1024)) + " Mb");
		int[] handRanks = LUTEvaluator.handRanks;
		int[][] randoms = new int[RANDOMTESTS][7];
		for (int i = 0; i < RANDOMTESTS; i++) {
			randoms[i][0] = CardConverter.convertZeroBasedRankFirstToZeroBasedSuitFirst(randomHands[i][0]);
			randoms[i][1] = CardConverter.convertZeroBasedRankFirstToZeroBasedSuitFirst(randomHands[i][1]);
			randoms[i][2] = CardConverter.convertZeroBasedRankFirstToZeroBasedSuitFirst(randomHands[i][2]);
			randoms[i][3] = CardConverter.convertZeroBasedRankFirstToZeroBasedSuitFirst(randomHands[i][3]);
			randoms[i][4] = CardConverter.convertZeroBasedRankFirstToZeroBasedSuitFirst(randomHands[i][4]);
			randoms[i][5] = CardConverter.convertZeroBasedRankFirstToZeroBasedSuitFirst(randomHands[i][5]);
			randoms[i][6] = CardConverter.convertZeroBasedRankFirstToZeroBasedSuitFirst(randomHands[i][6]);
		}
		long time = System.nanoTime();
		for (int j = 0; j < RANDOMRUNS; j++) {
			for (int i = 0; i < RANDOMTESTS; i++) {
				hand = randoms[i];
				sum += handRanks[handRanks[handRanks[handRanks[handRanks[handRanks[handRanks[
					52 + hand[0]] + hand[1]] + hand[2]] + hand[3]] + hand[4]] + hand[5]] + hand[6]];
			}
		}
		print(sum, time, RANDOMTESTS*RANDOMRUNS, 18);
	}
	
	public static void print(long sum, long time, long n, int ind) {
		double rtime = (System.nanoTime() - time)/1e9; // time given is start time
		double mhandsPerSec = ((double)n / rtime)/(double)1000000;
		DecimalFormat df = new DecimalFormat("#0.000000");
		System.out.println(ref[ind]);
		System.out.println(" --- Million random hands per second: [b]" + df.format(mhandsPerSec) + "[/b], hands " + n + ", checksum " + sum + ", total time(s): " + rtime);
		System.out.println();
	}

}
