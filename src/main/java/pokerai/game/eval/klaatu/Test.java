package pokerai.game.eval.klaatu;

public class Test {
	
	public static void runPartialStageTest() {
		PartialStageFastEval he = new PartialStageFastEval();
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
	
	public static void simpleTest() {
		int c1 = FastEval.encode(12,1);
		int c2 = FastEval.encode(5,2);
		int c3 = FastEval.encode(0,3);
		int c4 = FastEval.encode(9,1);
		int c5 = FastEval.encode(6,2);
		int c6 = FastEval.encode(6,4);
		int c7 = FastEval.encode(7,4);
		int ret = FastEval.eval7(c1, c2, c3, c4, c5, c6, c7);
		System.out.println("ret " +ret);
	}
	
	public static void speedTest() {
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
									FastEval.eval7(c0,c1,c2,c3,c4,c5,c6);
									count++;
								}
							}
						}
					}
				}
			}
		}
		ts = System.currentTimeMillis() - ts;
		System.out.println("klaatu.eval.FastEval, " + ts + "ms, evaluated " + count + " hands");
		float res = (float)count / ( (float)ts / (float)1000);
		System.out.println(res / 1000000 + " million hands per second");
	}
	
	public static void main (String [] args) throws Exception{
		simpleTest();
		speedTest();
		runPartialStageTest();
	}
}
