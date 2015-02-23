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

import java.text.DecimalFormat;

/**
 * Class performs enumeration prformnance test for evaluators
 */
public class EnumerationPerformanceTest {

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
	
	static int TESTRUNS = 1;

	public static void main(String args[]) {
		for (int i = 0; i < TESTRUNS; i++) {
			System.out.println("");
			System.out.println("---- Test set run " + (i + 1) + "----");
			System.out.println("");
			testIndiana();
			testIndiana3();
			testAlberta();
			testSpears();
			testSpears2p2();
			//testSteveBrecher();
			testRayW();
			testSteveBrecherE2();
			testPokerstove();
			//testMykey();
			testMykeyE2();
			//testMykeyExp();
			testMykeyExpE2();
			testKlaatu();
			testKlaatuE2();
			//testHammer();
			testHammerE2();
			testSupersonic();
			testJokipii();
		}
	}

	public static void testIndiana() {
		int[] hand = new int[7];
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			for (h2 = h1 + 1; h2 < 47; h2++) {
				for (h3 = h2 + 1; h3 < 48; h3++) {
					for (h4 = h3 + 1; h4 < 49; h4++) {
						for (h5 = h4 + 1; h5 < 50; h5++) {
							for (h6 = h5 + 1; h6 < 51; h6++) {
								for (h7 = h6 + 1; h7 < 52; h7++) {
									// need to have all the assignments here, as the array is sorted internally
									hand[0] = h1;
									hand[1] = h2;
									hand[2] = h3;
									hand[3] = h4;
									hand[4] = h5;
									hand[5] = h6;
									hand[6] = h7;
									sum += HandEvaluator.defineHand(hand);
		}}}}}}}
		print(sum, time, 133784560, 0);
	}

	public static void testIndiana3() {
		int[] hand = new int[7];
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			for (h2 = h1 + 1; h2 < 47; h2++) {
				for (h3 = h2 + 1; h3 < 48; h3++) {
					for (h4 = h3 + 1; h4 < 49; h4++) {
						for (h5 = h4 + 1; h5 < 50; h5++) {
							for (h6 = h5 + 1; h6 < 51; h6++) {
								for (h7 = h6 + 1; h7 < 52; h7++) {
									// need to have all the assignments here, as the array is sorted internally
									hand[0] = h1;
									hand[1] = h2;
									hand[2] = h3;
									hand[3] = h4;
									hand[4] = h5;
									hand[5] = h6;
									hand[6] = h7;
									sum += FastHandEvaluator.evaluate7(hand);
		}}}}}}}
		print(sum, time, 133784560, 1);
	}

	public static void testAlberta() {
		Hand h = new Hand();
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			for (h2 = h1 + 1; h2 < 47; h2++) {
				for (h3 = h2 + 1; h3 < 48; h3++) {
					for (h4 = h3 + 1; h4 < 49; h4++) {
						for (h5 = h4 + 1; h5 < 50; h5++) {
							for (h6 = h5 + 1; h6 < 51; h6++) {
								for (h7 = h6 + 1; h7 < 52; h7++) {
									h.makeEmpty();
									h.addCard(h1);
									h.addCard(h2);
									h.addCard(h3);
									h.addCard(h4);
									h.addCard(h5);
									h.addCard(h6);
									h.addCard(h7);
									sum += HandEvaluatorAlberta.rankHand(h);
		}}}}}}}
		print(sum, time, 133784560, 2);
	}

	public static void testSpears() {
		SevenCardEvaluator eval = new SevenCardEvaluator();
		pokerai.game.eval.spears.Card[] cards = new pokerai.game.eval.spears.Card[7];
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			for (h2 = h1 + 1; h2 < 47; h2++) {
				for (h3 = h2 + 1; h3 < 48; h3++) {
					for (h4 = h3 + 1; h4 < 49; h4++) {
						for (h5 = h4 + 1; h5 < 50; h5++) {
							for (h6 = h5 + 1; h6 < 51; h6++) {
								for (h7 = h6 + 1; h7 < 52; h7++) {
									cards[0] = pokerai.game.eval.spears.Card.get(h1);
									cards[1] = pokerai.game.eval.spears.Card.get(h2);
									cards[2] = pokerai.game.eval.spears.Card.get(h3);
									cards[3] = pokerai.game.eval.spears.Card.get(h4);
									cards[4] = pokerai.game.eval.spears.Card.get(h5);
									cards[5] = pokerai.game.eval.spears.Card.get(h6);
									cards[6] = pokerai.game.eval.spears.Card.get(h7);
									sum += eval.evaluate(cards);
		}}}}}}}
		print(sum, time, 133784560, 3);
	}


	public static void testSpears2p2() {
		StateTableEvaluator.initialize();
		pokerai.game.eval.spears2p2.Card[] cards = new pokerai.game.eval.spears2p2.Card[7];
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			for (h2 = h1 + 1; h2 < 47; h2++) {
				for (h3 = h2 + 1; h3 < 48; h3++) {
					for (h4 = h3 + 1; h4 < 49; h4++) {
						for (h5 = h4 + 1; h5 < 50; h5++) {
							for (h6 = h5 + 1; h6 < 51; h6++) {
								for (h7 = h6 + 1; h7 < 52; h7++) {
									cards[0] = pokerai.game.eval.spears2p2.Card.get(h1);
									cards[1] = pokerai.game.eval.spears2p2.Card.get(h2);
									cards[2] = pokerai.game.eval.spears2p2.Card.get(h3);
									cards[3] = pokerai.game.eval.spears2p2.Card.get(h4);
									cards[4] = pokerai.game.eval.spears2p2.Card.get(h5);
									cards[5] = pokerai.game.eval.spears2p2.Card.get(h6);
									cards[6] = pokerai.game.eval.spears2p2.Card.get(h7);
									sum += StateTableEvaluator.getRank(cards);
		}}}}}}}
		print(sum, time, 133784560, 4);
	}

	public static void testRayW() {
		System.gc(); try { Thread.currentThread().sleep(1000); } catch (Exception e) {};
		long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		StateTableEvaluator.initialize();
		System.gc(); try { Thread.currentThread().sleep(1000); } catch (Exception e) {};
		long mem2 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Memory used: " + ((mem2-mem1)/(1024*1024)) + " Mb");
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5;
		int[] handRanks = StateTableEvaluator.handRanks;
		int[] handEnumerations = new int[10];
		int[][] equivalencyEnumerations = new int[10][3000];
		String[] handDescriptions = {"Invalid Hand", "High Card", "One Pair", "Two Pair", "Three of a Kind",
								"Straight", "Flush", "Full House", "Four of a Kind", "Straight Flush"};
		int numHands = 0;
		int handRank;
		long time = System.nanoTime();
		long sum = 0;
		for (h1 = 1; h1 < 47; h1++) {
			u0 = handRanks[53 + h1];
			for (h2 = h1 + 1; h2 < 48; h2++) {
				u1 = handRanks[u0 + h2];
				for (h3 = h2 + 1; h3 < 49; h3++) {
					u2 = handRanks[u1 + h3];
					for (h4 = h3 + 1; h4 < 50; h4++) {
						u3 = handRanks[u2 + h4];
						for (h5 = h4 + 1; h5 < 51; h5++) {
							u4 = handRanks[u3 + h5];
							for (h6 = h5 + 1; h6 < 52; h6++) {
								u5 = handRanks[u4 + h6];
								for (h7 = h6 + 1; h7 < 53; h7++) {
									sum += handRanks[u5 + h7];
/*
									handRank = handRanks[u5 + h7];
									handEnumerations[handRank >>> 12]++;
									numHands++;
									equivalencyEnumerations[handRank >>> 12][handRank & 0xFFF]++;
*/
		}}}}}}}
		print(sum, time, 133784560, 6);
	}

	public static void testSteveBrecher() {
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			for (h2 = h1 + 1; h2 < 47; h2++) {
				for (h3 = h2 + 1; h3 < 48; h3++) {
					for (h4 = h3 + 1; h4 < 49; h4++) {
						for (h5 = h4 + 1; h5 < 50; h5++) {
							for (h6 = h5 + 1; h6 < 51; h6++) {
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += HandEval.hand7Eval((0x1L << (h1))
										| (0x1L << (h2))
										| (0x1L << (h3))
										| (0x1L << (h4))
										| (0x1L << (h5))
										| (0x1L << (h6))
										| (0x1L << (h7)));
		}}}}}}}
		print(sum, time, 133784560, 5);
	}

	public static void testSteveBrecherE2() {
		long sum = 0;
		long key1, key2, key3, key4, key5, key6;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			key1 = (0x1L << (h1));
			for (h2 = h1 + 1; h2 < 47; h2++) {
				key2 = key1 | (0x1L << (h2));
				for (h3 = h2 + 1; h3 < 48; h3++) {
					key3 = key2 | (0x1L << (h3));
					for (h4 = h3 + 1; h4 < 49; h4++) {
						key4 = key3 | (0x1L << (h4));
						for (h5 = h4 + 1; h5 < 50; h5++) {
							key5 = key4 | (0x1L << (h5));
							for (h6 = h5 + 1; h6 < 51; h6++) {
								key6 = key5 | (0x1L << (h6));
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += HandEval.hand7Eval(key6 | (0x1L << (h7)));
		}}}}}}}
		print(sum, time, 133784560, 7);
	}

	public static void testPokerstove() {
		int[] hand = new int[7];
		long time = System.nanoTime();
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		for (h1 = 0; h1 < 46; h1++) {
			for (h2 = h1 + 1; h2 < 47; h2++) {
				for (h3 = h2 + 1; h3 < 48; h3++) {
					for (h4 = h3 + 1; h4 < 49; h4++) {
						for (h5 = h4 + 1; h5 < 50; h5++) {
							for (h6 = h5 + 1; h6 < 51; h6++) {
								for (h7 = h6 + 1; h7 < 52; h7++) {
									// need to have all the assignments here, as the array is sorted internally
									hand[0] = h1;
									hand[1] = h2;
									hand[2] = h3;
									hand[3] = h4;
									hand[4] = h5;
									hand[5] = h6;
									hand[6] = h7;
									sum += pokerai.game.eval.jpoker.HandEvaluator.doFullEvaluation(hand);
		}}}}}}}
		print(sum, time, 133784560, 8);
	}


	public static void testMykey() {
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			for (h2 = h1 + 1; h2 < 47; h2++) {
				for (h3 = h2 + 1; h3 < 48; h3++) {
					for (h4 = h3 + 1; h4 < 49; h4++) {
						for (h5 = h4 + 1; h5 < 50; h5++) {
							for (h6 = h5 + 1; h6 < 51; h6++) {
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += Eval.rankHand((0x1L << (h1))
										| (0x1L << (h2))
										| (0x1L << (h3))
										| (0x1L << (h4))
										| (0x1L << (h5))
										| (0x1L << (h6))
										| (0x1L << (h7)));
		}}}}}}}
		print(sum, time, 133784560, 9);
	}

	public static void testMykeyE2() {
		long sum = 0;
		long key1, key2, key3, key4, key5, key6;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			key1 = (0x1L << (h1));
			for (h2 = h1 + 1; h2 < 47; h2++) {
				key2 = key1 | (0x1L << (h2));
				for (h3 = h2 + 1; h3 < 48; h3++) {
					key3 = key2 | (0x1L << (h3));
					for (h4 = h3 + 1; h4 < 49; h4++) {
						key4 = key3 | (0x1L << (h4));
						for (h5 = h4 + 1; h5 < 50; h5++) {
							key5 = key4 | (0x1L << (h5));
							for (h6 = h5 + 1; h6 < 51; h6++) {
								key6 = key5 | (0x1L << (h6));
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += Eval.rankHand(key6 | (0x1L << (h7)));
		}}}}}}}
		print(sum, time, 133784560, 10);
	}

	public static void testMykeyExp() {
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			for (h2 = h1 + 1; h2 < 47; h2++) {
				for (h3 = h2 + 1; h3 < 48; h3++) {
					for (h4 = h3 + 1; h4 < 49; h4++) {
						for (h5 = h4 + 1; h5 < 50; h5++) {
							for (h6 = h5 + 1; h6 < 51; h6++) {
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += EvalExp.rankHand((0x1L << (h1))
										| (0x1L << (h2))
										| (0x1L << (h3))
										| (0x1L << (h4))
										| (0x1L << (h5))
										| (0x1L << (h6))
										| (0x1L << (h7)));
		}}}}}}}
		print(sum, time, 133784560, 11);
	}

	public static void testMykeyExpE2() {
		long sum = 0;
		long key1, key2, key3, key4, key5, key6;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			key1 = (0x1L << (h1));
			for (h2 = h1 + 1; h2 < 47; h2++) {
				key2 = key1 | (0x1L << (h2));
				for (h3 = h2 + 1; h3 < 48; h3++) {
					key3 = key2 | (0x1L << (h3));
					for (h4 = h3 + 1; h4 < 49; h4++) {
						key4 = key3 | (0x1L << (h4));
						for (h5 = h4 + 1; h5 < 50; h5++) {
							key5 = key4 | (0x1L << (h5));
							for (h6 = h5 + 1; h6 < 51; h6++) {
								key6 = key5 | (0x1L << (h6));
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += EvalExp.rankHand(key6 | (0x1L << (h7)));
		}}}}}}}
		print(sum, time, 133784560, 12);
	}

	public static void testKlaatu() {
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			for (h2 = h1 + 1; h2 < 47; h2++) {
				for (h3 = h2 + 1; h3 < 48; h3++) {
					for (h4 = h3 + 1; h4 < 49; h4++) {
						for (h5 = h4 + 1; h5 < 50; h5++) {
							for (h6 = h5 + 1; h6 < 51; h6++) {
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += FastEval.eval7(h1,h2,h3,h4,h5,h6,h7);
		}}}}}}}
		print(sum, time, 133784560, 13);
	}

	public static void testKlaatuE2() {
		PartialStageFastEval he = new PartialStageFastEval();
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			he.setCard1(h1);
			for (h2 = h1 + 1; h2 < 47; h2++) {
				he.setCard2(h2);
				for (h3 = h2 + 1; h3 < 48; h3++) {
					he.setCard3(h3);
					for (h4 = h3 + 1; h4 < 49; h4++) {
						he.setCard4(h4);
						for (h5 = h4 + 1; h5 < 50; h5++) {
							he.setCard5(h5);
							for (h6 = h5 + 1; h6 < 51; h6++) {
								he.setCard6(h6);
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += he.setHand7(h7);
		}}}}}}}
		print(sum, time, 133784560, 14);
	}

	public static void testHammer() {
		CheckFlushPJHE he = new CheckFlushPJHE();
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			for (h2 = h1 + 1; h2 < 47; h2++) {
				for (h3 = h2 + 1; h3 < 48; h3++) {
					for (h4 = h3 + 1; h4 < 49; h4++) {
						for (h5 = h4 + 1; h5 < 50; h5++) {
							for (h6 = h5 + 1; h6 < 51; h6++) {
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += he.eval7(h1,h2,h3,h4,h5,h6,h7).getId();
		}}}}}}}
		print(sum, time, 133784560, 15);
	}

	public static void testHammerE2() {
		PartialStagePJHE he = new PartialStagePJHE();
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			he.setCard1(h1);
			for (h2 = h1 + 1; h2 < 47; h2++) {
				he.setCard2(h2);
				for (h3 = h2 + 1; h3 < 48; h3++) {
					he.setCard3(h3);
					for (h4 = h3 + 1; h4 < 49; h4++) {
						he.setCard4(h4);
						for (h5 = h4 + 1; h5 < 50; h5++) {
							he.setCard5(h5);
							for (h6 = h5 + 1; h6 < 51; h6++) {
								he.setCard6(h6);
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += he.setHand7(h7).getId();
		}}}}}}}
		print(sum, time, 133784560, 16);
	}

	public static void testSupersonic() {
		LookupHandEvaluator he = LookupHandEvaluator.getInstance("handRanks.ser");
		Hand h = new Hand();
		long sum = 0;
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 46; h1++) {
			for (h2 = h1 + 1; h2 < 47; h2++) {
				for (h3 = h2 + 1; h3 < 48; h3++) {
					for (h4 = h3 + 1; h4 < 49; h4++) {
						for (h5 = h4 + 1; h5 < 50; h5++) {
							for (h6 = h5 + 1; h6 < 51; h6++) {
								for (h7 = h6 + 1; h7 < 52; h7++) {
									h.makeEmpty();
									h.addCard(h1);
									h.addCard(h2);
									h.addCard(h3);
									h.addCard(h4);
									h.addCard(h5);
									h.addCard(h6);
									h.addCard(h7);
									sum += he.rankHand7(h);
		}}}}}}}
		print(sum, time, 133784560, 17);
	}
	
	public static void testJokipii() {
		System.gc(); try { Thread.currentThread().sleep(1000); } catch (Exception e) {};
		long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		LUTEvaluator.setMode(LUTEvaluator.MODE_COMPRESS);
		LUTEvaluator.initialize();
		System.gc(); try { Thread.currentThread().sleep(1000); } catch (Exception e) {};
		long mem2 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Memory used: " + ((mem2-mem1)/(1024*1024)) + " Mb");
		int[] handRanks = LUTEvaluator.handRanks;
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5;
		long time = System.nanoTime();
		long sum = 0;
		for (h1 = 0; h1 < 46; h1++) {
			u0 = handRanks[52 + h1];
			for (h2 = h1 + 1; h2 < 47; h2++) {
				u1 = handRanks[u0 + h2];
				for (h3 = h2 + 1; h3 < 48; h3++) {
					u2 = handRanks[u1 + h3];
					for (h4 = h3 + 1; h4 < 49; h4++) {
						u3 = handRanks[u2 + h4];
						for (h5 = h4 + 1; h5 < 50; h5++) {
							u4 = handRanks[u3 + h5];
							for (h6 = h5 + 1; h6 < 51; h6++) {
								u5 = handRanks[u4 + h6];
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += handRanks[u5 + h7];
		}}}}}}}
		print(sum, time, 133784560, 18);
	}
	
	public static void print(long sum, long time, long n, int ind) {
		double rtime = (System.nanoTime() - time)/1e9; // time given is start time
		double mhandsPerSec = ((double)n / rtime)/(double)1000000;
		DecimalFormat df = new DecimalFormat("#0.000000");
		System.out.println(ref[ind]);
		System.out.println(" --- Million hands per second: [b]" + df.format(mhandsPerSec) + "[/b], hands " + n + ", checksum " + sum + ", total time(s): " + rtime);
		System.out.println();
	}

}
