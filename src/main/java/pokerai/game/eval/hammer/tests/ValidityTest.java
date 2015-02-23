package pokerai.game.eval.hammer.tests;

import pokerai.game.eval.stevebrecher.HandEval;

import pokerai.game.eval.hammer.HandEvaluator;
import pokerai.game.eval.hammer.classes.EquivalenceClasses;
import pokerai.game.eval.hammer.purejava.OneStagePJHE;
import pokerai.game.eval.hammer.purejava.TwoStagePJHE;
import pokerai.game.eval.hammer.purejava.CheckFlushPJHE;
import pokerai.game.eval.hammer.purejava.PartialStagePJHE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ValidityTest {
	public static final int[] map;

	public static void runtests(HandEvaluator he) {
		int c0,c1,c2,c3,c4,c5,c6;
		long b0,b1,b2,b3,b4,b5;
		int count = 0;
		int error5 = 0;
		int error6 = 0;
		int error7 = 0;
		int id, brecher;
		long ts = System.currentTimeMillis();
		for (c0 = 0; c0 < 52; c0++) {
		   b0 = (0x1L << ((c0 >> 2) + 13 * (c0 & 3)));
          	   for (c1 = c0 + 1; c1 < 52; c1++) {
		      b1 = b0 | (0x1L << ((c1 >> 2) + 13 * (c1 & 3)));
              	      for (c2 = c1 + 1; c2 < 52; c2++) {
			 b2 = b1 | (0x1L << ((c2 >> 2) + 13 * (c2 & 3)));
               		 for (c3 = c2 + 1; c3 < 52; c3++) {
		   	    b3 = b2 | (0x1L << ((c3 >> 2) + 13 * (c3 & 3)));
       			    for (c4 = c3 + 1; c4 < 52; c4++) {
			       b4 = b3 | (0x1L << ((c4 >> 2) + 13 * (c4 & 3)));
			       brecher = HandEval.hand5Eval(b4);
			       id = he.eval5(c0, c1, c2, c3, c4).getId();
			       if (map[id] != brecher) {
					error5++;
			       }
			       for (c5 = c4 + 1; c5 < 52; c5++) {
				  b5 = b4 | (0x1L << ((c5 >> 2) + 13 * (c5 & 3)));
				  brecher = HandEval.hand6Eval(b5);
				  id = he.eval6(c0, c1, c2, c3, c4, c5).getId();
				  if (map[id] != brecher) {
					error6++;
				  }
       			          for (c6 = c5 + 1; c6 < 52; c6++) {
					brecher = HandEval.hand7Eval(b5 | (0x1L << ((c6 >> 2) + 13 * (c6 & 3))));
					id = he.eval7(c0, c1, c2, c3, c4, c5, c6).getId();
					if (map[id] != brecher) {
System.out.println(c0 +" "+c1 +" "+c2 +" "+c3 +" "+c4 +" "+c5 +" "+c6);
System.out.println(id +"\t"+ brecher +"\t"+ map[id]);
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
		System.out.println(he.getClass().getName() + ", " + ts + " ms, validated " + count + " hands");
		System.out.println("errors in 7 card hands: " + error7 + " errors");
		System.out.println("errors in 6 card hands: " + error6 + " errors");
		System.out.println("errors in 5 card hands: " + error5 + " errors");
	}
	
	public static void runPartialStageTest(PartialStagePJHE he) {
		int c0,c1,c2,c3,c4,c5,c6;
		long b0,b1,b2,b3,b4,b5;
		int count = 0;
		int error5 = 0;
		int error6 = 0;
		int error7 = 0;
		int id, brecher;
		long ts = System.currentTimeMillis();
		for (c0 = 0; c0 < 52; c0++) {
		   he.setCard1(c0);
		   b0 = (0x1L << ((c0 >> 2) + 13 * (c0 & 3)));
          	   for (c1 = c0 + 1; c1 < 52; c1++) {
		      he.setCard2(c1);
		      b1 = b0 | (0x1L << ((c1 >> 2) + 13 * (c1 & 3)));
              	      for (c2 = c1 + 1; c2 < 52; c2++) {
			 he.setCard3(c2);
			 b2 = b1 | (0x1L << ((c2 >> 2) + 13 * (c2 & 3)));
               		 for (c3 = c2 + 1; c3 < 52; c3++) {
			    he.setCard4(c3);
		   	    b3 = b2 | (0x1L << ((c3 >> 2) + 13 * (c3 & 3)));
       			    for (c4 = c3 + 1; c4 < 52; c4++) {
			       b4 = b3 | (0x1L << ((c4 >> 2) + 13 * (c4 & 3)));
			    he.setCard5(c4);
//			       brecher = HandEval.hand5Eval(b4);
//			       id = he.setHand5(c4).getId();
//			       if (map[id] != brecher) {
//					error5++;
//			       }
			       for (c5 = c4 + 1; c5 < 52; c5++) {
				  b5 = b4 | (0x1L << ((c5 >> 2) + 13 * (c5 & 3)));
				  brecher = HandEval.hand6Eval(b5);
				  id = he.setHand6(c5).getId();
				  if (map[id] != brecher) {
					error6++;
				  }
       			          for (c6 = c5 + 1; c6 < 52; c6++) {
					brecher = HandEval.hand7Eval(b5 | (0x1L << ((c6 >> 2) + 13 * (c6 & 3))));
					id = he.setHand7(c6).getId();
					if (map[id] != brecher) {
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
		System.out.println(he.getClass().getName() + ", " + ts + " ms, validated " + count + " hands");
		System.out.println("errors in 7 card hands: " + error7 + " errors");
		System.out.println("errors in 6 card hands: " + error6 + " errors");
		System.out.println("errors in 5 card hands: " + error5 + " errors");
	}

	static {
		map = new int[7463];
		try {
			BufferedReader r = new BufferedReader(new FileReader("toBrecher"));
			String l;
			int i = 7462;
			while ((l = r.readLine()) != null) {
				map[i] = Integer.parseInt(l);
				i--;
			}
			r.close();
		} catch (IOException e) {
			System.out.println("Cannot load Brecher mapping");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		EquivalenceClasses.getInstance();	// assert equivalence class loading
		runtests(new OneStagePJHE());
		runtests(new TwoStagePJHE());
		runtests(new CheckFlushPJHE());
		runPartialStageTest(new PartialStagePJHE());
	}	

}
