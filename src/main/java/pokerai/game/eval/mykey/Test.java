package pokerai.game.eval.mykey;

// We use Steve Brecher's HandEval to validity testing
import pokerai.game.eval.stevebrecher.HandEval;

public class Test {
	
	public static void main (String [] args) throws Exception{
		//handTypes();
		//validityTest();
		speedTestBrecher();
		speedTestExp();
		speedTest();
	}

	public static void validityTest() {
		int c0,c1,c2,c3,c4,c5,c6;
		long b0,b1,b2,b3,b4,b5;
		long m0,m1,m2,m3,m4,m5;
		int count = 0;
		int error = 0;
		long ts = System.currentTimeMillis();
		for (c0 = 0; c0 < 52; c0++) {
		   b0 = (0x1L << c0);
		   m0 = HandMask.HandMasksTable[c0];
          	   for (c1 = c0 + 1; c1 < 52; c1++) {
		      b1 = b0 | (0x1L << c1);
		      m1 = m0 | HandMask.HandMasksTable[c1];
              	      for (c2 = c1 + 1; c2 < 52; c2++) {
			 b2 = b1 | (0x1L << c2);
			 m2 = m1 | HandMask.HandMasksTable[c2];
               		 for (c3 = c2 + 1; c3 < 52; c3++) {
		   	    b3 = b2 | (0x1L << c3);
			    m3 = m2 | HandMask.HandMasksTable[c3];
       			    for (c4 = c3 + 1; c4 < 52; c4++) {
			       b4 = b3 | (0x1L << c4);
			       m4 = m3 | HandMask.HandMasksTable[c4];
			       for (c5 = c4 + 1; c5 < 52; c5++) {
				  b5 = b4 | (0x1L << c5);
				  m5 = m4 | HandMask.HandMasksTable[c5];
       			          for (c6 = c5 + 1; c6 < 52; c6++) {
					int brecher = HandEval.hand7Eval(b5 | (0x1L << c6));
					long mask = m5 | HandMask.HandMasksTable[c6];
					int rank = Eval.rankHand(mask);
					if (brecher != Rank.rankToBrecher(rank, false)) {
						error++;
						System.out.println(HandMask.maskToString(mask) + " " + rank + " " + Rank.rankToString(rank));
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
		System.out.println(ts + " ms, validated " + count + " hands " + error + " errors");
	}
	
	public static void speedTest() {
		int c0,c1,c2,c3,c4,c5,c6;
		long m0,m1,m2,m3,m4,m5;
		int count = 0;
		long ts = System.currentTimeMillis();
		for (c0 = 0; c0 < 52; c0++) {
		   m0 = HandMask.HandMasksTable[c0];
          	   for (c1 = c0 + 1; c1 < 52; c1++) {
		      m1 = m0 | HandMask.HandMasksTable[c1];
              	      for (c2 = c1 + 1; c2 < 52; c2++) {
			 m2 = m1 | HandMask.HandMasksTable[c2];
               		 for (c3 = c2 + 1; c3 < 52; c3++) {
			    m3 = m2 | HandMask.HandMasksTable[c3];
       			    for (c4 = c3 + 1; c4 < 52; c4++) {
			       m4 = m3 | HandMask.HandMasksTable[c4];
			       for (c5 = c4 + 1; c5 < 52; c5++) {
				  m5 = m4 | HandMask.HandMasksTable[c5];
       			          for (c6 = c5 + 1; c6 < 52; c6++) {
					Eval.rankHand(m5 | HandMask.HandMasksTable[c6]);
					count++;
				  }
			       }
			    }
			 }
		      }
		   }
		}
		ts = System.currentTimeMillis() - ts;
		System.out.println("Mykey: " + ts + " ms, evaluated " + count + " hands");
		float res = ((float)count / ((float)ts / (float)1000));
		System.out.println(res/1000000 + " million hands per second");
	}
	
	public static void speedTestExp() {
		int c0,c1,c2,c3,c4,c5,c6;
		long m0,m1,m2,m3,m4,m5;
		int count = 0;
		long ts = System.currentTimeMillis();
		for (c0 = 0; c0 < 52; c0++) {
		   m0 = HandMask.HandMasksTable[c0];
          	   for (c1 = c0 + 1; c1 < 52; c1++) {
		      m1 = m0 | HandMask.HandMasksTable[c1];
              	      for (c2 = c1 + 1; c2 < 52; c2++) {
			 m2 = m1 | HandMask.HandMasksTable[c2];
               		 for (c3 = c2 + 1; c3 < 52; c3++) {
			    m3 = m2 | HandMask.HandMasksTable[c3];
       			    for (c4 = c3 + 1; c4 < 52; c4++) {
			       m4 = m3 | HandMask.HandMasksTable[c4];
			       for (c5 = c4 + 1; c5 < 52; c5++) {
				  m5 = m4 | HandMask.HandMasksTable[c5];
       			          for (c6 = c5 + 1; c6 < 52; c6++) {
					EvalExp.rankHand(m5 | HandMask.HandMasksTable[c6]);
					count++;
				  }
			       }
			    }
			 }
		      }
		   }
		}
		ts = System.currentTimeMillis() - ts;
		System.out.println("Mykey (experimental): " + ts + " ms, evaluated " + count + " hands");
		float res = ((float)count / ((float)ts / (float)1000));
		System.out.println(res/1000000 + " million hands per second");
	}
	
	public static void speedTestBrecher() {
		int c0,c1,c2,c3,c4,c5,c6;
		long b0,b1,b2,b3,b4,b5;
		int count = 0;
		long ts = System.currentTimeMillis();
		for (c0 = 0; c0 < 52; c0++) {
		   b0 = (0x1L << c0);
          	   for (c1 = c0 + 1; c1 < 52; c1++) {
		      b1 = b0 | (0x1L << c1);
              	      for (c2 = c1 + 1; c2 < 52; c2++) {
			 b2 = b1 | (0x1L << c2);
               		 for (c3 = c2 + 1; c3 < 52; c3++) {
		   	    b3 = b2 | (0x1L << c3);
       			    for (c4 = c3 + 1; c4 < 52; c4++) {
			       b4 = b3 | (0x1L << c4);
			       for (c5 = c4 + 1; c5 < 52; c5++) {
				  b5 = b4 | (0x1L << c5);
       			          for (c6 = c5 + 1; c6 < 52; c6++) {
					HandEval.hand7Eval(b5 | (0x1L << c6));
					count++;
				  }
			       }
			    }
			 }
		      }
		   }
		}
		ts = System.currentTimeMillis() - ts;
		System.out.println("Brecher: " + ts + " ms, evaluated " + count + " hands");
		float res = ((float)count / ((float)ts / (float)1000));
		System.out.println(res/1000000 + " million hands per second");
	}
	
	public static void handTypes() {
		//System.out.println("High Card");
		testHand("[AH QH JS TC 7S 5D 2C]");
		testHand("[AH QH JS TC 7S 6S 5D]");
		testHand("[AH QH JS TC 8S 6S 5D]");
		//System.out.println("One Pair");
		testHand("[AH QH JS JC 7S 5D 2C]");
		testHand("[AH QH JS JD 7S 6S 5D]");
		testHand("[AH QH QS JC 7S 5D 2C]");
		//System.out.println("Two Pair");
		testHand("[AH QH JS JD KS 2H 2S]");
		testHand("[AH QH JS JC 7S 2D 2C]");
		testHand("[AH QH JS JC 7S 7D 2C]");
		testHand("[AH QH QS TC 7S 7D 2C]");
		//System.out.println("Trips");
		testHand("[AH JH JS JC 7S 5D 2C]");
		testHand("[AH JH JS JD 7S 6S 4D]");
		testHand("[AH JH JS JC 8S 5D 2C]");
		testHand("[AH QH QS QC 8S 5D 2C]");
		//System.out.println("Straight");
		testHand("[9H KD QH JS TC 8S 6S]");
		testHand("[AH KS QH JS TC 8S 2C]");
		testHand("[AH KD QH JS TC 8S 6S]");
		//System.out.println("Flush");
		testHand("[AD QD JD TD 7D 6S JH]");
		testHand("[AH QH JH TH 7H 5D 2C]");
		testHand("[AH QH JH TH 8H 5D 2C]");
		//System.out.println("Full House");	
		testHand("[QH TH TS TD 7S 7D 7H]");
		testHand("[QH TH TS TD 7S AD AS]");
		testHand("[AH JH JS JC 7S 2D 2C]");
		testHand("[QH JH JS JD 7S 2D 2S]");
		//System.out.println("Quads");
		testHand("[KH TH TS TC TD 5D 5C]");
		testHand("[AH TH TS TC TD 5D 2C]");
		testHand("[AH TH TS TC TD QS 5D]");
		//System.out.println("Straight Flush");
		testHand("[AH 6D 5H 4H 3H 2H 2C]");
		testHand("[AH 6H 5H 4H 3H 2H 2C]");
		testHand("[AD KH QH JH TH 9H 2C]");
		testHand("[AH KH QH JH TH 9H 2C]");
		//System.out.println("");
	}
	
	public static void testHand(String handstring) {
		long mask = HandMask.stringToMask(handstring);
		int rank = Eval.rankHand(mask);
		System.out.println(HandMask.maskToString(mask) + " " + rank + " " + Rank.rankToString(rank));
	}
	
}
