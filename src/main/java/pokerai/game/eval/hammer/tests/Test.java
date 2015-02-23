package pokerai.game.eval.hammer.tests;

import pokerai.game.eval.hammer.HandEvaluator;
import pokerai.game.eval.hammer.classes.EquivalenceClasses;
import pokerai.game.eval.hammer.classes.EquivalenceClass;
import pokerai.game.eval.hammer.purejava.OneStagePJHE;
import pokerai.game.eval.hammer.purejava.TwoStagePJHE;
import pokerai.game.eval.hammer.purejava.CheckFlushPJHE;
import pokerai.game.eval.hammer.purejava.PartialStagePJHE;

public class Test {

	public static long runtests(HandEvaluator he) {
		int count = 0;
		int c0,c1,c2,c3,c4,c5,c6;
		long ts = System.currentTimeMillis();
		for (c0 = 0; c0 < 52; c0++) {
          		for (c1 = c0 + 1; c1 < 52; c1++) {
              		for (c2 = c1 + 1; c2 < 52; c2++) {
                  	for (c3 = c2 + 1; c3 < 52; c3++) {
                      	for (c4 = c3 + 1; c4 < 52; c4++) {
                       	for (c5 = c4 + 1; c5 < 52; c5++) {
                       		for (c6 = c5 + 1; c6 < 52; c6++) {
					he.eval7(c0, c1, c2, c3, c4, c5, c6);
					count++;
				}
			}
			}
			}
			}
			}
		}
		ts = System.currentTimeMillis() - ts;
		System.out.println(he.getClass().getName() + ", " + ts + "ms, evaluated " + count + " hands");
		float res = (float)count / ( (float)ts / (float)1000);
		System.out.println(res / 1000000 + " million hands per second");
		return ts;
	}
	
	public static void runPartialStageTest(PartialStagePJHE he) {
		int count = 0;
		int c0,c1,c2,c3,c4,c5,c6;
		long ts = System.currentTimeMillis();
		for (c0 = 0; c0 < 52; c0++) {
			he.setCard1(c0);
          		for (c1 = c0 + 1; c1 < 52; c1++) {
			he.setCard2(c1);
              		for (c2 = c1 + 1; c2 < 52; c2++) {
			he.setCard3(c2);
                  	for (c3 = c2 + 1; c3 < 52; c3++) {
			he.setCard4(c3);
                      	for (c4 = c3 + 1; c4 < 52; c4++) {
			he.setCard5(c4);
                       	for (c5 = c4 + 1; c5 < 52; c5++) {
				he.setCard6(c5);
                       		for (c6 = c5 + 1; c6 < 52; c6++) {
					he.setHand7(c6);
					count++;
				}
			}
			}
			}
			}
			}
		}
		ts = System.currentTimeMillis() - ts;
		System.out.println(he.getClass().getName() + ", " + ts + "ms, evaluated " + count + " hands");
		float res = (float)count / ( (float)ts / (float)1000);
		System.out.println(res / 1000000 + " million hands per second");
	}
	
	public static void main(String[] args) {
		EquivalenceClasses.getInstance();	// assert equivalence class loading
		for (int i = 0; i < 10; i++) {
			runtests(new OneStagePJHE());
			runtests(new TwoStagePJHE());
			runtests(new CheckFlushPJHE());
			runPartialStageTest(new PartialStagePJHE());
		}
	}
}
