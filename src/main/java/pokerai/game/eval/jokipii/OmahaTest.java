package pokerai.game.eval.jokipii;

import pokerai.game.eval.jokipii.OmahaLUTEvaluator;

import java.text.DecimalFormat;

public class OmahaTest {

	static int TESTRUNS = 1;
	static String[] handDescriptions = {"Invalid Hand", "High Card", "One Pair", "Two Pair", "Three of a Kind",
								"Straight", "Flush", "Full House", "Four of a Kind", "Straight Flush"};
	static final String[] cardString = new String[] {
	"2c", "3c", "4c", "5c", "6c", "7c", "8c", "9c", "Tc", "Jc", "Qc", "Kc", "Ac", 
	"2d", "3d", "4d", "5d", "6d", "7d", "8d", "9d", "Td", "Jd", "Qd", "Kd", "Ad", 
	"2h", "3h", "4h", "5h", "6h", "7h", "8h", "9h", "Th", "Jh", "Qh", "Kh", "Ah", 
	"2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "Ts", "Js", "Qs", "Ks", "As", 
	};
	static OmahaLUTEvaluator omaha;


	public static void main(String args[]) {
		init();
		for (int i = 0; i < TESTRUNS; i++) {
			System.out.println("");
			System.out.println("---- Test set run " + (i + 1) + "----");
			System.out.println("");
			testBasic();
			testOmaha();
			testCaclClasses();
		}
	}
	
	public static void init() {
		System.gc(); try { Thread.currentThread().sleep(1000); } catch (Exception e) {};
		long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		omaha = new OmahaLUTEvaluator();
		System.gc(); try { Thread.currentThread().sleep(1000); } catch (Exception e) {};
		long mem2 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Memory used: " + ((mem2-mem1)/(1024*1024)) + " Mb");
	}
	
	public static void testBasic() {
		int handRank;
		// flush on board [2c, 3c, 4c, 5c, Qc]
		omaha.setBoard(0, 1, 2, 3, 10);
		System.out.println("Board cards : [" + cardString[0] + " " + cardString[1] + " "
				+ cardString[2] + " " + cardString[3] + " " + cardString[10] + "]\n");
		// high card
		setHand(9, 17, 21, 50, 1);
		// one flush card and one strait card, and pair makes pair (not flush or strait)
		setHand(9, 17, 23, 50, 2);
		// now same with (51 = As) that makes pair (not flush)
		setHand(9, 17, 23, 51, 2);
		// now same with 3 A's in hand that makes one pair AA (not 3 of kind)
		setHand(9, 25, 38, 51, 2);
		// now same with 3 A's and 5 in hand that makes strait (not fullhouse)
		setHand(16, 25, 38, 51, 5);
		// now same with Ac in hand that makes strait
		setHand(16, 12, 38, 51, 5);
		// now we have flush
		setHand(4, 12, 38, 51, 6);
		// now we have strait flush
		setHand(4, 12, 38, 5, 9);
		System.out.println();
		// pair on board [2c, 3c, 4c, Qc, 2d]
		omaha.setBoard(0, 1, 2, 10, 13);
		System.out.println("Board cards : [" + cardString[0] + " " + cardString[1] + " "
				+ cardString[2] + " " + cardString[10] + " " + cardString[13] + "]\n");
		// 3 of kind
		setHand(12, 38, 26, 50, 4);
		// full house
		setHand(12, 23, 26, 50, 7);
		// 4 of kind
		setHand(12, 26, 39, 50, 8);
		// 2 pairs
		setHand(12, 33, 47, 51, 3);
		System.out.println();
	}
	
	public static void setHand(int c1, int c2, int c3, int c4, int result) {
		int handRank = omaha.evalHand(c1, c2, c3, c4);
		System.out.println("Hand cards : [" + cardString[c1] + " " + cardString[c2] + " "
			+ cardString[c3] + " " + cardString[c4] + "] best combination: " + handDescriptions[handRank >>> 12]);
		assert (handRank >>> 12) == result : handRank;
	}
	
	public static void testOmaha() {
		long sum = 0;
		int numHands = 0;
		int h1, h2, h3, h4, h5, h6, h7, h8, h9;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 1; h1++) {
			for (h2 = h1 + 1; h2 < 2; h2++) {
				for (h3 = h2 + 1; h3 < 46; h3++) {
					for (h4 = h3 + 1; h4 < 47; h4++) {
						for (h5 = h4 + 1; h5 < 48; h5++) {
							omaha.setBoard(h1, h2, h3, h4, h5);
							for (h6 = h5 + 1; h6 < 49; h6++) {
								for (h7 = h6 + 1; h7 < 50; h7++) {
									for (h8 = h7 + 1; h8 < 51; h8++) {
										for (h9 = h8 + 1; h9 < 52; h9++) {
											sum += omaha.evalHand(h6, h7, h8, h9);
											numHands++;
		}}}}}}}}}
		print(sum, time, numHands);
	}
	
	
	public static void testCaclClasses() {
		long sum = 0;
		int numHands = 0;
		int h1, h2, h3, h4, h5, h6, h7, h8, h9;
		int[] handEnumerations = new int[10];
//		int[][] equivalencyEnumerations = new int[10][3000];
		int handRank;
		long time = System.nanoTime();
		for (h1 = 0; h1 < 1; h1++) {
			for (h2 = h1 + 1; h2 < 2; h2++) {
				for (h3 = h2 + 1; h3 < 46; h3++) {
					for (h4 = h3 + 1; h4 < 47; h4++) {
						for (h5 = h4 + 1; h5 < 48; h5++) {
							omaha.setBoard(h1, h2, h3, h4, h5);
							for (h6 = h5 + 1; h6 < 49; h6++) {
								for (h7 = h6 + 1; h7 < 50; h7++) {
									for (h8 = h7 + 1; h8 < 51; h8++) {
										for (h9 = h8 + 1; h9 < 52; h9++) {
//											sum += omaha.evalHand(h6, h7, h8, h9);
//											handRank = omaha.evalHand(h6, h7, h8, h9);
											handEnumerations[(omaha.evalHand(h6, h7, h8, h9) >>> 12)]++;
//											equivalencyEnumerations[handRank >>> 12][handRank & 0xFFF]++;
											numHands++;
		}}}}}}}}}
		for (int i = 0; i < 10; i++) {
			System.out.println(handDescriptions[i] + "\t\t" + handEnumerations[i]);
		}
		print(sum, time, numHands);
	}
	
	
	public static void print(long sum, long time, long n) {
		double rtime = (System.nanoTime() - time)/1e9; // time given is start time
		double mhandsPerSec = ((double)n / rtime)/(double)1000000;
		DecimalFormat df = new DecimalFormat("#0.000000");
		System.out.println("Million hands per second: [b]" + df.format(mhandsPerSec) + "[/b], hands " + n + ", checksum " + sum + ", total time(s): " + rtime);
		System.out.println();
	}


}
