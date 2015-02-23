package pokerai.game.eval.jokipii;

import pokerai.game.eval.jokipii.cactuskev.CactusKev;
import pokerai.game.eval.klaatu.FastEval;
import pokerai.game.eval.spears2p2.StateTableEvaluator;
import pokerai.game.eval.jokipii.LUTEvaluator;
import pokerai.game.eval.stevebrecher.HandEval;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test {

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
		"(19) Jokipii's compressed LUT Evaluator, http://pokerai.org/pf3/",
	};
	
	static int TESTRUNS = 10;
	static int handRanksj[];
	static int handRanks[];
	static char[] handRank7, handRank6, handRank5;
	static int[] handIndex1, handIndex2, handIndex3, handIndex4, handIndex5, handIndex6;	// split array
	static int[] offsets = new int[] {0, 0, 1277, 4137, 4995, 5853, 5863, 7140, 7296, 7452};
	public static final int[] toBrecherMap;
	static final int oldCards[] = new int[] {
	1, 5, 9, 13, 17, 21, 25, 29, 33, 37, 41, 45, 49, 
	2, 6, 10, 14, 18, 22, 26, 30, 34, 38, 42, 46, 50, 
	3, 7, 11, 15, 19, 23, 27, 31, 35, 39, 43, 47, 51, 
	4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 52
	};
	static final String[] cardString = new String[] {
	"2c", "3c", "4c", "5c", "6c", "7c", "8c", "9c", "Tc", "Jc", "Qc", "Kc", "Ac", 
	"2d", "3d", "4d", "5d", "6d", "7d", "8d", "9d", "Td", "Jd", "Qd", "Kd", "Ad", 
	"2h", "3h", "4h", "5h", "6h", "7h", "8h", "9h", "Th", "Jh", "Qh", "Kh", "Ah", 
	"2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "Ts", "Js", "Qs", "Ks", "As", 
	};
	static String[] handDescriptions = {"Invalid Hand", "High Card", "One Pair", "Two Pair", "Three of a Kind",
								"Straight", "Flush", "Full House", "Four of a Kind", "Straight Flush"};


	public static void main(String args[]) {
		try {
			//original();
			//splitOriginal();
			//compressed();
			//splitCompressed();
			//twostep();
			nosuit();
			//for (int i = 0; i < TESTRUNS; i++) {
				//System.out.println("\n---- Test set run " + (i + 1) + "----\n");
				testRayW();
				//testJokipiiGeneral();
			//}
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			aiobe.printStackTrace();
		}
	}
	
	public static void original() {
		// set correct mode before init
		LUTEvaluator.setMode(LUTEvaluator.MODE_ORIG_CARD_ORDER | LUTEvaluator.MODE_1BASED_INDEX);
		init();
		testCorrectnessOriginal();
		for (int i = 0; i < TESTRUNS; i++) {
			System.out.println("\n---- Test set run " + (i + 1) + "----\n");
			testJokipiiOriginal();
		}
	}
	
	public static void compressed() {
		// set correct mode before init
		LUTEvaluator.setMode(LUTEvaluator.MODE_COMPRESS);
		init();
		testCorrectnessCompressed();
		for (int i = 0; i < TESTRUNS; i++) {
			System.out.println("\n---- Test set run " + (i + 1) + "----\n");
			testJokipiiCompressed();
		}
	}
	
	public static void splitOriginal() {
		// set correct mode before init
		LUTEvaluator.setMode(LUTEvaluator.MODE_ORIG_CARD_ORDER | LUTEvaluator.MODE_SPLIT | LUTEvaluator.MODE_1BASED_INDEX);
		init();
		for (int i = 0; i < TESTRUNS; i++) {
			System.out.println("\n---- Test set run " + (i + 1) + "----\n");
			testJokipiiSplitOriginal();
		}
	}
	
	public static void splitCompressed() {
		// set correct mode before init
		LUTEvaluator.setMode(LUTEvaluator.MODE_COMPRESS | LUTEvaluator.MODE_SPLIT | LUTEvaluator.MODE_INCLUDE56);
		init();
		//searchEquivalenceOnCompressedSplittedHandranks();
		testAgainstBrecher();
		testCorrectnessSplitCompressed();
		for (int i = 0; i < TESTRUNS; i++) {
			System.out.println("\n---- Test set run " + (i + 1) + "----\n");
			testJokipiiSplitCompressed();
		}
	}
	
	public static void twostep() {
		// set correct mode before init
		LUTEvaluator.setMode(LUTEvaluator.MODE_COMPRESS | LUTEvaluator.MODE_SPLIT | LUTEvaluator.MODE_2STEP);
		init();
		testCorrectness2Step();
		for (int i = 0; i < TESTRUNS; i++) {
			System.out.println("\n---- Test set run " + (i + 1) + "----\n");
			testJokipii2Step();
		}
	}
	
	public static void nosuit() {
		// set correct mode before init
		LUTEvaluator.setMode(LUTEvaluator.MODE_COMPRESS | LUTEvaluator.MODE_SPLIT | LUTEvaluator.MODE_NOSUIT_COMPRESS);
		init();
		testCorrectnessNosuit();
		for (int i = 0; i < TESTRUNS; i++) {
			System.out.println("\n---- Test set run " + (i + 1) + "----\n");
			testJokipiiNosuit();
		}
	}
	
	public static void init() {
		System.gc(); try { Thread.currentThread().sleep(1000); } catch (Exception e) {};
		long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		LUTEvaluator.initialize();
		StateTableEvaluator.initialize();
		System.gc(); try { Thread.currentThread().sleep(1000); } catch (Exception e) {};
		long mem2 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Memory used: " + ((mem2-mem1)/(1024*1024)) + " Mb");
		handRanks = StateTableEvaluator.handRanks;
		handRanksj = LUTEvaluator.handRanks;
		handRank7 = LUTEvaluator.handRank7;
		handRank6 = LUTEvaluator.handRank6;
		handRank5 = LUTEvaluator.handRank5;
		handIndex1 = LUTEvaluator.handIndex1;
		handIndex2 = LUTEvaluator.handIndex2;
		handIndex3 = LUTEvaluator.handIndex3;
		handIndex4 = LUTEvaluator.handIndex4;
		handIndex5 = LUTEvaluator.handIndex5;
		handIndex6 = LUTEvaluator.handIndex6;
	}
	
	public static void testJokipiiNosuit() {
		int flushSuit;
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5;
		long time = System.nanoTime();
		long sum = 0;
		for (h1 = 0; h1 < 46; h1++) {
			u0 = handIndex1[52 + h1];
			for (h2 = h1 + 1; h2 < 47; h2++) {
				u1 = handIndex2[u0 + h2];
				for (h3 = h2 + 1; h3 < 48; h3++) {
					u2 = handIndex3[u1 + h3];
					for (h4 = h3 + 1; h4 < 49; h4++) {
						u3 = handIndex4[u2 + h4];
						for (h5 = h4 + 1; h5 < 50; h5++) {
							u4 = handIndex5[handIndex5[u3 + (h5 & 3)] + (h5 >> 2)];
							flushSuit = u4 & 0x7;
							for (h6 = h5 + 1; h6 < 51; h6++) {
								u5 = handIndex6[u4 >> 3 + (h6 >> 2) + (((h6 & 3) == flushSuit)? 13 : 0)];
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += handRank7[u5 + (h7 >> 2) + (((h7 & 3) == flushSuit)? 13 : 0)];
		}}}}}}}
		print(sum, time, 133784560, 18);
	}
	
	public static void testJokipii2Step() {
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5;
		long time = System.nanoTime();
		long sum = 0;
		for (h1 = 0; h1 < 46; h1++) {
			u0 = handIndex1[52 + h1];
			for (h2 = h1 + 1; h2 < 47; h2++) {
				u1 = handIndex2[u0 + h2];
				for (h3 = h2 + 1; h3 < 48; h3++) {
					u2 = handIndex3[u1 + h3];
					for (h4 = h3 + 1; h4 < 49; h4++) {
						u3 = handIndex4[u2 + h4];
						for (h5 = h4 + 1; h5 < 50; h5++) {
							u4 = handIndex5[handIndex5[u3 + (h5 / 13)] + (h5 % 13)];
							for (h6 = h5 + 1; h6 < 51; h6++) {
								u5 = handIndex6[handIndex6[u4 + (h6 / 13)] + (h6 % 13)];
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += handRank7[u5 + h7];
		}}}}}}}
		print(sum, time, 133784560, 18);
	}
	
	public static void testRayW() {
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5;
		int[]					handEnumerations = new int[10];
		int[][] equivalencyEnumerations = new int[10][3000];
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

	public static void testJokipiiCompressed() {
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5;
		int[] handEnumerations = new int[10];
		int[][] equivalencyEnumerations = new int[10][3000];
		int numHands = 0;
		int handRank;
		long time = System.nanoTime();
		long sum = 0;
		for (h1 = 0; h1 < 46; h1++) {
			u0 = handRanksj[52 + h1];
			for (h2 = h1 + 1; h2 < 47; h2++) {
				u1 = handRanksj[u0 + h2];
				for (h3 = h2 + 1; h3 < 48; h3++) {
					u2 = handRanksj[u1 + h3];
					for (h4 = h3 + 1; h4 < 49; h4++) {
						u3 = handRanksj[u2 + h4];
						for (h5 = h4 + 1; h5 < 50; h5++) {
							u4 = handRanksj[u3 + h5];
							for (h6 = h5 + 1; h6 < 51; h6++) {
								u5 = handRanksj[u4 + h6];
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += handRanksj[u5 + h7];
/*
									handRank = handRanksj[u5 + h7];
									handEnumerations[handRank >>> 12]++;
									numHands++;
									equivalencyEnumerations[handRank >>> 12][handRank & 0xFFF]++;
*/
		}}}}}}}
		print(sum, time, 133784560, 18);
	}
	
	public static void testJokipiiGeneral() {
		LUTEvaluator he = new LUTEvaluator();
		int h1, h2, h3, h4, h5, h6, h7;
		long time = System.nanoTime();
		long sum = 0;
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
		print(sum, time, 133784560, 18);
	}
	
	public static void testJokipiiSplitCompressed() {
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5;
		long time = System.nanoTime();
		long sum = 0;
		for (h1 = 0; h1 < 46; h1++) {
			u0 = handIndex1[52 + h1];
			for (h2 = h1 + 1; h2 < 47; h2++) {
				u1 = handIndex2[u0 + h2];
				for (h3 = h2 + 1; h3 < 48; h3++) {
					u2 = handIndex3[u1 + h3];
					for (h4 = h3 + 1; h4 < 49; h4++) {
						u3 = handIndex4[u2 + h4];
						for (h5 = h4 + 1; h5 < 50; h5++) {
							u4 = handIndex5[u3 + h5];
							for (h6 = h5 + 1; h6 < 51; h6++) {
								u5 = handIndex6[u4 + h6];
								for (h7 = h6 + 1; h7 < 52; h7++) {
									sum += handRank7[u5 + h7];
		}}}}}}}
		print(sum, time, 133784560, 18);
	}
	
	public static void testJokipiiSplitOriginal() {
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5;
		long time = System.nanoTime();
		long sum = 0;
		for (h1 = 1; h1 < 47; h1++) {
			u0 = handIndex1[53 + h1];
			for (h2 = h1 + 1; h2 < 48; h2++) {
				u1 = handIndex2[u0 + h2];
				for (h3 = h2 + 1; h3 < 49; h3++) {
					u2 = handIndex3[u1 + h3];
					for (h4 = h3 + 1; h4 < 50; h4++) {
						u3 = handIndex4[u2 + h4];
						for (h5 = h4 + 1; h5 < 51; h5++) {
							u4 = handIndex5[u3 + h5];
							for (h6 = h5 + 1; h6 < 52; h6++) {
								u5 = handIndex6[u4 + h6];
								for (h7 = h6 + 1; h7 < 53; h7++) {
									sum += handRank7[u5 + h7];
		}}}}}}}
		print(sum, time, 133784560, 18);
	}
	
	public static void testJokipiiOriginal() {
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5;
		long time = System.nanoTime();
		long sum = 0;
		for (h1 = 1; h1 < 47; h1++) {
			u0 = handRanksj[53 + h1];
			for (h2 = h1 + 1; h2 < 48; h2++) {
				u1 = handRanksj[u0 + h2];
				for (h3 = h2 + 1; h3 < 49; h3++) {
					u2 = handRanksj[u1 + h3];
					for (h4 = h3 + 1; h4 < 50; h4++) {
						u3 = handRanksj[u2 + h4];
						for (h5 = h4 + 1; h5 < 51; h5++) {
							u4 = handRanksj[u3 + h5];
							for (h6 = h5 + 1; h6 < 52; h6++) {
								u5 = handRanksj[u4 + h6];
								for (h7 = h6 + 1; h7 < 53; h7++) {
									sum += handRanksj[u5 + h7];
		}}}}}}}
		print(sum, time, 133784560, 18);
	}
	
	public static void testCorrectnessOriginal() {
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5, u6;
		int v0, v1, v2, v3, v4, v5, v6;
		int count = 0;
		int errors = 0;
		long time = System.nanoTime();
		long sum = 0, sumj = 0;
		for (h1 = 1; h1 < 47; h1++) {
			u0 = handRanksj[53 + h1];
			v0 = handRanks[53 + h1];
			for (h2 = h1 + 1; h2 < 48; h2++) {
				u1 = handRanksj[u0 + h2];
				v1 = handRanks[v0 + h2];
				for (h3 = h2 + 1; h3 < 49; h3++) {
					u2 = handRanksj[u1 + h3];
					v2 = handRanks[v1 + h3];
					for (h4 = h3 + 1; h4 < 50; h4++) {
						u3 = handRanksj[u2 + h4];
						v3 = handRanks[v2 + h4];
						for (h5 = h4 + 1; h5 < 51; h5++) {
							u4 = handRanksj[u3 + h5];
							v4 = handRanks[v3 + h5];
							for (h6 = h5 + 1; h6 < 52; h6++) {
								u5 = handRanksj[u4 + h6];
								v5 = handRanks[v4 + h6];
								for (h7 = h6 + 1; h7 < 53; h7++) {
									u6 = handRanksj[u5 + h7];
									v6 = handRanks[v5 + h7];
									sumj += u6;
									sum += v6;
									count++;
									if (u6 != v6) {
										StringBuilder sb = new StringBuilder()
											.append(cardString[h1-1]).append(" ")
											.append(cardString[h2-1]).append(" ")
											.append(cardString[h3-1]).append(" ")
											.append(cardString[h4-1]).append(" ")
											.append(cardString[h5-1]).append(" ")
											.append(cardString[h6-1]).append(" ")
											.append(cardString[h7-1])
										.append(" NEW ").append(u6).append(" ").append(handDescriptions[u6 >>> 12])
										.append(" OLD ").append(v6).append(" ").append(handDescriptions[v6 >>> 12]);
										System.out.println(sb.toString());
										errors++;
									}
		}}}}}}}
		System.out.println("errors: " + errors);
		System.out.println("count: " + count);
	}
	
	public static void testCorrectnessSplitCompressed() {
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5, u6;
		int v0, v1, v2, v3, v4, v5, v6;
		int count = 0;
		int errors = 0;
		long time = System.nanoTime();
		long sum = 0, sumj = 0;
		for (h1 = 0; h1 < 46; h1++) {
			u0 = handIndex1[52 + h1];
			v0 = handRanks[53 + oldCards[h1]];
			for (h2 = h1 + 1; h2 < 47; h2++) {
				u1 = handIndex2[u0 + h2];
				v1 = handRanks[v0 + oldCards[h2]];
				for (h3 = h2 + 1; h3 < 48; h3++) {
					u2 = handIndex3[u1 + h3];
					v2 = handRanks[v1 + oldCards[h3]];
					for (h4 = h3 + 1; h4 < 49; h4++) {
						u3 = handIndex4[u2 + h4];
						v3 = handRanks[v2 + oldCards[h4]];
						for (h5 = h4 + 1; h5 < 50; h5++) {
							u4 = handIndex5[u3 + h5];
							v4 = handRanks[v3 + oldCards[h5]];
							for (h6 = h5 + 1; h6 < 51; h6++) {
								u5 = handIndex6[u4 + h6];
								v5 = handRanks[v4 + oldCards[h6]];
								for (h7 = h6 + 1; h7 < 52; h7++) {
									u6 = handRank7[u5 + h7];
									v6 = handRanks[v5 + oldCards[h7]];
									sumj += u6;
									sum += v6;
									count++;
									if (u6 != v6) {
										StringBuilder sb = new StringBuilder()
											.append(cardString[h1]).append(" ")
											.append(cardString[h2]).append(" ")
											.append(cardString[h3]).append(" ")
											.append(cardString[h4]).append(" ")
											.append(cardString[h5]).append(" ")
											.append(cardString[h6]).append(" ")
											.append(cardString[h7])
										.append(" NEW ").append(u6).append(" ").append(rankToString(u6))
										.append(" OLD ").append(v6).append(" ").append(rankToString(v6));
										System.out.println(sb.toString());
										errors++;
									}
		}}}}}}}
		System.out.println("errors: " + errors);
		System.out.println("count: " + count);
	}
	
	public static void testCorrectnessCompressed() {
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5, u6;
		int v0, v1, v2, v3, v4, v5, v6;
		int count = 0;
		int errors = 0;
		long time = System.nanoTime();
		long sum = 0, sumj = 0;
		for (h1 = 0; h1 < 46; h1++) {
			u0 = handRanksj[52 + h1];
			v0 = handRanks[53 + oldCards[h1]];
			for (h2 = h1 + 1; h2 < 47; h2++) {
				u1 = handRanksj[u0 + h2];
				v1 = handRanks[v0 + oldCards[h2]];
				for (h3 = h2 + 1; h3 < 48; h3++) {
					u2 = handRanksj[u1 + h3];
					v2 = handRanks[v1 + oldCards[h3]];
					for (h4 = h3 + 1; h4 < 49; h4++) {
						u3 = handRanksj[u2 + h4];
						v3 = handRanks[v2 + oldCards[h4]];
						for (h5 = h4 + 1; h5 < 50; h5++) {
							u4 = handRanksj[u3 + h5];
							v4 = handRanks[v3 + oldCards[h5]];
							for (h6 = h5 + 1; h6 < 51; h6++) {
								u5 = handRanksj[u4 + h6];
								v5 = handRanks[v4 + oldCards[h6]];
								for (h7 = h6 + 1; h7 < 52; h7++) {
									u6 = handRanksj[u5 + h7];
									v6 = handRanks[v5 + oldCards[h7]];
									sumj += u6;
									sum += v6;
									count++;
									if (u6 != v6) {
										StringBuilder sb = new StringBuilder()
											.append(cardString[h1]).append(" ")
											.append(cardString[h2]).append(" ")
											.append(cardString[h3]).append(" ")
											.append(cardString[h4]).append(" ")
											.append(cardString[h5]).append(" ")
											.append(cardString[h6]).append(" ")
											.append(cardString[h7])
										.append(" NEW ").append(u6).append(" ").append(rankToString(u6))
										.append(" OLD ").append(v6).append(" ").append(rankToString(v6));
										System.out.println(sb.toString());
										errors++;
									}
		}}}}}}}
		System.out.println("errors: " + errors);
		System.out.println("count: " + count);
	}
	
	public static void testCorrectness2Step() {
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5, u6;
		int v0, v1, v2, v3, v4, v5, v6;
		int count = 0;
		int errors = 0;
		long time = System.nanoTime();
		long sum = 0, sumj = 0;
		for (h1 = 0; h1 < 46; h1++) {
			u0 = handIndex1[52 + h1];
			v0 = handRanks[53 + oldCards[h1]];
			for (h2 = h1 + 1; h2 < 47; h2++) {
				u1 = handIndex2[u0 + h2];
				v1 = handRanks[v0 + oldCards[h2]];
				for (h3 = h2 + 1; h3 < 48; h3++) {
					u2 = handIndex3[u1 + h3];
					v2 = handRanks[v1 + oldCards[h3]];
					for (h4 = h3 + 1; h4 < 49; h4++) {
						u3 = handIndex4[u2 + h4];
						v3 = handRanks[v2 + oldCards[h4]];
						for (h5 = h4 + 1; h5 < 50; h5++) {
							u4 = handIndex5[u3 + (h5 / 13)];
							u4 = handIndex5[u4 + (h5 % 13)];
							v4 = handRanks[v3 + oldCards[h5]];
							for (h6 = h5 + 1; h6 < 51; h6++) {
								u5 = handIndex6[u4 + (h6 / 13)];
								u5 = handIndex6[u5 + (h6 % 13)];
								v5 = handRanks[v4 + oldCards[h6]];
								for (h7 = h6 + 1; h7 < 52; h7++) {
									u6 = handRank7[u5 + h7];
									v6 = handRanks[v5 + oldCards[h7]];
									sumj += u6;
									sum += v6;
									count++;
									if (u6 != v6) {
										StringBuilder sb = new StringBuilder()
											.append(cardString[h1]).append(" ")
											.append(cardString[h2]).append(" ")
											.append(cardString[h3]).append(" ")
											.append(cardString[h4]).append(" ")
											.append(cardString[h5]).append(" ")
											.append(cardString[h6]).append(" ")
											.append(cardString[h7])
										.append(" NEW ").append(u6).append(" ").append(rankToString(u6))
										.append(" OLD ").append(v6).append(" ").append(rankToString(v6));
										System.out.println(sb.toString());
										errors++;
									}
		}}}}}}}
		System.out.println("errors: " + errors);
		System.out.println("count: " + count);
	}
	
	
	public static void testCorrectnessNosuit() {
		int flushSuit;
		int h1, h2, h3, h4, h5, h6, h7;
		int u0, u1, u2, u3, u4, u5, u6;
		int v0, v1, v2, v3, v4, v5, v6;
		int count = 0;
		int errors = 0;
		long time = System.nanoTime();
		long sum = 0, sumj = 0;
		for (h1 = 0; h1 < 46; h1++) {
			u0 = handIndex1[52 + h1];
			v0 = handRanks[53 + h1 + 1];
			for (h2 = h1 + 1; h2 < 47; h2++) {
				u1 = handIndex2[u0 + h2];
				v1 = handRanks[v0 + h2 + 1];
				for (h3 = h2 + 1; h3 < 48; h3++) {
					u2 = handIndex3[u1 + h3];
					v2 = handRanks[v1 + h3 + 1];
					for (h4 = h3 + 1; h4 < 49; h4++) {
						u3 = handIndex4[u2 + h4];
						v3 = handRanks[v2 + h4 + 1];
						for (h5 = h4 + 1; h5 < 50; h5++) {
							u4 = handIndex5[u3 + (h5 & 3)];
							u4 = handIndex5[u4 + (h5 >> 2)];
							flushSuit = u4 & 0x7;
							u4 = u4 >> 3;
							v4 = handRanks[v3 + h5 + 1];
							for (h6 = h5 + 1; h6 < 51; h6++) {
								u5 = handIndex6[u4 + (h6 >> 2) + (((h6 & 3) == flushSuit)? 13 : 0)];
								v5 = handRanks[v4 + h6 + 1];
								for (h7 = h6 + 1; h7 < 52; h7++) {
									u6 = handRank7[u5 + (h7 >> 2) + (((h7 & 3) == flushSuit)? 13 : 0)];
									v6 = handRanks[v5 + h7 + 1];
									sumj += u6;
									sum += v6;
									count++;
									if (u6 != v6) { 
										StringBuilder sb = new StringBuilder()
											.append(cardString[h1]).append(" ")
											.append(cardString[h2]).append(" ")
											.append(cardString[h3]).append(" ")
											.append(cardString[h4]).append(" ")
											.append(cardString[h5]).append(" ")
											.append(cardString[h6]).append(" ")
											.append(cardString[h7]).append(" ").append(u4)
										.append(" NEW ").append(u6).append(" ").append(rankToString(u6))
										.append(" OLD ").append(v6).append(" ").append(rankToString(v6));
										System.out.println(sb.toString());
										errors++;
									}
		}}}}}}}
		System.out.println("errors: " + errors);
		System.out.println("count: " + count);
	}
	
	public static String rankToString(int rank) {
		return FastEval.handString(toBrecher(rank));
	}
	
	public static int toBrecher(int rank) {
		int ck = (rank & 0xFFF) + offsets[(rank >>> 12)];
		ck = 7463 - ck;
		return toBrecherMap[ck];
	}

	public static void print(long sum, long time, long n, int ind) {
		double rtime = (System.nanoTime() - time)/1e9; // time given is start time
		double mhandsPerSec = ((double)n / rtime)/(double)1000000;
		DecimalFormat df = new DecimalFormat("#0.000000");
		System.out.println(ref[ind]);
		System.out.println(" --- Million hands per second: [b]" + df.format(mhandsPerSec) + "[/b], hands " + n + ", checksum " + sum + ", total time(s): " + rtime);
		System.out.println();
	}

	// extra compression search by bluegaspode
	private static void searchEquivalenceOnCompressedSplittedHandranks() {
		int range = 13;
		// create an index (rank1*rank2 of 52) pointing to all starting positions of (rank1*rank2 of 52)
		// this makes searching for duplicates faster later on
		Map<Integer, List<Integer>> handrankindex = new HashMap<Integer, List<Integer>>();
		System.out.println("creating handrank-index");
		for (int i = 0; i < handRank7.length; i += range) {
			Integer rank = Integer.valueOf(handRank7[i] * handRank7[i + 1]);
			List<Integer> position = handrankindex.get(rank);
			if (position == null) {
				position = new LinkedList<Integer>();
				handrankindex.put(rank, position);
			}
			position.add(Integer.valueOf(i));
		}
		
		System.out.println("starting pointer movement level7");
		int changed = 0;
		// work backwards (because we get faster in the end)
		for (int index6Position = handIndex6.length - 1; index6Position >= 0; index6Position--) {
			if (index6Position % 50000 == 0)
				System.out.println(index6Position / (handIndex6.length / 100) + "% of " + handIndex6.length + " done. Changed:" + changed);
			
			// start of first 52 block
			int compare1Start = handIndex6[index6Position];
			
			// get all blocks starting with the same 2 ranks for deeper analysis
			List<Integer> startPositions = handrankindex.get(Integer.valueOf(handRank7[compare1Start] * handRank7[compare1Start + 1]));
			if (startPositions == null) {
				continue;
			}
			for (Integer compare2Start : startPositions) {
				if (compare2Start >= compare1Start)
					break;
				
				boolean match = true;
				// compare all ranks for exact matches
				for (int i = 0; i < 52; i++) {
					int rank1 = handRank7[compare1Start + i];
					int rank2 = handRank7[compare2Start + i];
					if (((rank1 >> 12) == 0) || ((rank2 >> 12) == 0)) {
						// we ignore error ranks as noone should get there
						continue;
					}
					if (rank1 != rank2) {
						match = false;
						break;
					}
				}
				// on a match we can move the pointer to the other block
				if (match) {
					changed++;
					handIndex6[index6Position] = compare2Start;
					break;
				}
			}
		}
		System.out.println("calculation memory save");
		boolean needed[] = new boolean[handRank7.length / 13];
		for (int index6Position = 0; index6Position < handIndex6.length; index6Position++) {
			needed[handIndex6[index6Position] / 13] = true;
			needed[handIndex6[index6Position] / 13 + 1] = true;
			needed[handIndex6[index6Position] / 13 + 2] = true;
			needed[handIndex6[index6Position] / 13 + 3] = true;
		}
		long bytesNotNeeded = 0;
		for (int i = 0; i < handRank7.length / 13; i++) {
			if (!needed[i]) {
				bytesNotNeeded += 13 * 2;
			}
		}
		System.out.println(bytesNotNeeded);
	}
	
	static {
		toBrecherMap = new int[7463];
		try {
			BufferedReader r = new BufferedReader(new FileReader("toBrecher"));
			String l;
			int i = 7462;
			while ((l = r.readLine()) != null) {
				toBrecherMap[i] = Integer.parseInt(l);
				i--;
			}
			r.close();
		} catch (IOException e) {
			System.out.println("Cannot load Brecher mapping");
			e.printStackTrace();
		}
	}
	
	
	public static void testAgainstBrecher() {
		int c0,c1,c2,c3,c4,c5,c6;
		int u0, u1, u2, u3, u4, u5, u6;
		long b0,b1,b2,b3,b4,b5;
		int count = 0;
		int error5 = 0;
		int error6 = 0;
		int error7 = 0;
		int id, brecher;
		long ts = System.currentTimeMillis();
		for (c0 = 0; c0 < 52; c0++) {
			b0 = (0x1L << c0);
			u0 = handIndex1[52 + c0];
			for (c1 = c0 + 1; c1 < 52; c1++) {
				b1 = b0 | (0x1L << c1);
				u1 = handIndex2[u0 + c1];
				for (c2 = c1 + 1; c2 < 52; c2++) {
					b2 = b1 | (0x1L << c2);
					u2 = handIndex3[u1 + c2];
					for (c3 = c2 + 1; c3 < 52; c3++) {
						b3 = b2 | (0x1L << c3);
						u3 = handIndex4[u2 + c3];
		 				for (c4 = c3 + 1; c4 < 52; c4++) {
							b4 = b3 | (0x1L << c4);
							u4 = handIndex5[u3 + c4];
							brecher = HandEval.hand5Eval(b4);
							id = handRank5[u3 + c4];
							if (toBrecher(id) != brecher) {
								error5++;
							}
							for (c5 = c4 + 1; c5 < 52; c5++) {
								b5 = b4 | (0x1L << c5);
								u5 = handIndex6[u4 + c5];
								brecher = HandEval.hand6Eval(b5);
								id = handRank6[u4 + c5];
								if (toBrecher(id) != brecher) {
									error6++;
								}
								for (c6 = c5 + 1; c6 < 52; c6++) {
									brecher = HandEval.hand7Eval(b5 | (0x1L << c6));
									id = handRank7[u5 + c6];
									if (toBrecher(id) != brecher) {
										StringBuilder sb = new StringBuilder()
											.append(cardString[c0]).append(" ")
											.append(cardString[c1]).append(" ")
											.append(cardString[c2]).append(" ")
											.append(cardString[c3]).append(" ")
											.append(cardString[c4]).append(" ")
											.append(cardString[c5]).append(" ")
											.append(cardString[c6])
										.append("\n id ").append(id).append(" ").append(handDescriptions[id >>> 12])
										.append("\n brecher ").append(brecher).append(" ").append(FastEval.handString(brecher))
										.append("\n converted ").append(toBrecher(id)).append(" ").append(rankToString(id));
										System.out.println(sb.append("\n").toString());
										error7++;
									}
									count++;
								}
							}
						}
					}
				}
			}
		}
		ts = System.currentTimeMillis() - ts;
		System.out.println("Validation agains Brecher Hand evaluator, " + ts + " ms, validated " + count + " hands");
		System.out.println("errors in 7 card hands: " + error7 + " errors");
		System.out.println("errors in 6 card hands: " + error6 + " errors");
		System.out.println("errors in 5 card hands: " + error5 + " errors");
	}
	
}
