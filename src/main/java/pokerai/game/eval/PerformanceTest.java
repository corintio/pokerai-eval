package pokerai.game.eval;

import com.biotools.meerkat.Hand;
import pokerai.game.eval.alberta.HandEvaluatorAlberta;
import pokerai.game.eval.indiana.HandEvaluator;
import pokerai.game.eval.indiana.FastHandEvaluator;
import pokerai.game.eval.spears.SevenCardEvaluator;
import pokerai.game.eval.spears2p2.StateTableEvaluator;
import pokerai.game.eval.stevebrecher.CardSet;
import pokerai.game.eval.stevebrecher.HandEval;

public class PerformanceTest {

  /*

   This is the old benchmarking code, and is not used right now.
   
   Look at and use PerformanceTest2 insted.
   
    */

  static String[] ref = {
          "(1) Indiana-1, 2006, http://pokerai.org/pf3",
          "(2) Indiana-3, 2007, http://pokerai.org/pf3",
          "(3) University of Alberta, 2000, http://spaz.ca/poker",
          "(4) Spears port of Kevin Suffecool's C evaluator, http://pokerai.org/pf3",
          "(5) Spears port of 2+2 evaluator, http://pokerai.org/pf3",
          "(6) Steve Brecher HandEval, http://www.stevebrecher.com/Software/software.html"
  };
 
  public static void main(String args[]) {
    //correctnessTest();
    /*
    testIndiana();
    testIndiana3();
    testAlberta();
    testSpears();
    testSpears2p2();
    */
    testSteveBrecher();
  }

  public static void testIndiana() {
    int n = 0;
    int[] hand = new int[7];
    long time = System.currentTimeMillis();
    long sum = 0, ss = 0;
    for (int h1 = 0; h1 < 13; h1++)
      for (int h2 = 0; h2 < 13; h2++) {
//        System.out.println(h1 + " " + h2);
        for (int h3 = 0; h3 < 13; h3++)
          for (int h4 = 0; h4 < 13; h4++)
            for (int h5 = 0; h5 < 13; h5++)  if (h4 != h1)
              for (int h6 = 0; h6 < 13; h6++) if (h5 != h2)
                for (int h7 = 0; h7 < 13; h7++) if (h7 != h3) {
                  hand[0] = h1*4;
                  hand[1] = h2*4+1;
                  hand[2] = h3*4+2;
                  hand[3] = h4*4+3;
                  hand[4] = h5*4+1;
                  hand[5] = h6*4+2;
                  hand[6] = h7*4+3;
                  n++;
                  //ss = sum;
                  sum += HandEvaluator.defineHand(hand);
                  //System.out.println(sum - ss);
                }
      }
    print(sum, time, n, 0);
  }

  public static void testIndiana3() {
    int n = 0;
    int[] hand = new int[7];
    long time = System.currentTimeMillis();
    long sum = 0, ss = 0;
    for (int h1 = 0; h1 < 13; h1++)
      for (int h2 = 0; h2 < 13; h2++) {
//        System.out.println(h1 + " " + h2);
        for (int h3 = 0; h3 < 13; h3++)
          for (int h4 = 0; h4 < 13; h4++)
            for (int h5 = 0; h5 < 13; h5++)  if (h4 != h1)
              for (int h6 = 0; h6 < 13; h6++) if (h5 != h2)
                for (int h7 = 0; h7 < 13; h7++) if (h7 != h3) {
                  hand[0] = h1*4;
                  hand[1] = h2*4+1;
                  hand[2] = h3*4+2;
                  hand[3] = h4*4+3;
                  hand[4] = h5*4+1;
                  hand[5] = h6*4+2;
                  hand[6] = h7*4+3;
                  n++;
                  //ss = sum;
                  sum += FastHandEvaluator.evaluate7(hand);
                  //System.out.println(sum - ss);
                }
      }
    print(sum, time, n, 1);
  }


  public static void testAlberta() {
    int n = 0;
    int[] hand = new int[7];
    int[] reshand = new int[6];
    long time = System.currentTimeMillis();
    long sum = 0;
    for (int h1 = 0; h1 < 13; h1++)
      for (int h2 = 0; h2 < 13; h2++) {
//        System.out.println(h1 + " " + h2);
        for (int h3 = 0; h3 < 13; h3++)
          for (int h4 = 0; h4 < 13; h4++)
            for (int h5 = 0; h5 < 13; h5++)  if (h5 != h2)
              for (int h6 = 0; h6 < 13; h6++) if (h6 != h3)
                for (int h7 = 0; h7 < 13; h7++) if (h7 != h4) {
/* fully explicit card to integer conversions :

2c =  0    2d = 13    2h = 26    2s = 39
3c =  1    3d = 14    3h = 27    3s = 40
4c =  2    4d = 15    4h = 28    4s = 41
5c =  3    5d = 16    5h = 29    5s = 42
6c =  4    6d = 17    6h = 30    6s = 43
7c =  5    7d = 18    7h = 31    7s = 44
8c =  6    8d = 19    8h = 32    8s = 45
9c =  7    9d = 20    9h = 33    9s = 46
Tc =  8    Td = 21    Th = 34    Ts = 47
Jc =  9    Jd = 22    Jh = 35    Js = 48
Qc = 10    Qd = 23    Qh = 36    Qs = 49
Kc = 11    Kd = 24    Kh = 37    Ks = 50
Ac = 12    Ad = 25    Ah = 38    As = 51

*/
                  Hand h = new Hand();
                  h.addCard(h1);
                  h.addCard(13+h2);
                  h.addCard(26+h3);
                  h.addCard(39+h4);
                  h.addCard(13+h5);
                  h.addCard(26+h6);
                  h.addCard(39+h7);
                  n++;
                  sum += HandEvaluatorAlberta.rankHand(h);
                }
      }
    print(sum, time, n, 2);
  }

  public static void testSpears() {
    int n = 0;
    int[] hand = new int[7];
    int[] reshand = new int[6];
    SevenCardEvaluator eval = new SevenCardEvaluator();
    pokerai.game.eval.spears.Card c = pokerai.game.eval.spears.Card.parse("Th");
    System.out.println("should be 34: " + c.ordinal());
    pokerai.game.eval.spears.Card[] cards = new pokerai.game.eval.spears.Card[7];
    for (int i = 0; i < 7; i++) cards[i] = pokerai.game.eval.spears.Card.parse("AsAh");
    //pokerai.game.eval.spears.Card[] deck = pokerai.game.eval.spears.Card.values();
    long time = System.currentTimeMillis();
    long sum = 0;
    for (int h1 = 0; h1 < 13; h1++)
      for (int h2 = 0; h2 < 13; h2++) {
//        System.out.println(h1 + " " + h2);
        for (int h3 = 0; h3 < 13; h3++)
          for (int h4 = 0; h4 < 13; h4++)
            for (int h5 = 0; h5 < 13; h5++)  if (h5 != h2)
              for (int h6 = 0; h6 < 13; h6++) if (h6 != h3)
                for (int h7 = 0; h7 < 13; h7++) if (h7 != h4) {
/* fully explicit card to integer conversions :

2c =  0    2d = 13    2h = 26    2s = 39
3c =  1    3d = 14    3h = 27    3s = 40
4c =  2    4d = 15    4h = 28    4s = 41
5c =  3    5d = 16    5h = 29    5s = 42
6c =  4    6d = 17    6h = 30    6s = 43
7c =  5    7d = 18    7h = 31    7s = 44
8c =  6    8d = 19    8h = 32    8s = 45
9c =  7    9d = 20    9h = 33    9s = 46
Tc =  8    Td = 21    Th = 34    Ts = 47
Jc =  9    Jd = 22    Jh = 35    Js = 48
Qc = 10    Qd = 23    Qh = 36    Qs = 49
Kc = 11    Kd = 24    Kh = 37    Ks = 50
Ac = 12    Ad = 25    Ah = 38    As = 51

*/
                  //pokerai.game.eval.spears.Hand h = new pokerai.game.eval.spears.Hand();
                  cards[0] = pokerai.game.eval.spears.Card.get(h1);
                  cards[1] = pokerai.game.eval.spears.Card.get(13+h2);
                  cards[2] = pokerai.game.eval.spears.Card.get(26+h3);
                  cards[3] = pokerai.game.eval.spears.Card.get(39+h4);
                  cards[4] = pokerai.game.eval.spears.Card.get(13+h5);
                  cards[5] = pokerai.game.eval.spears.Card.get(26+h6);
                  cards[6] = pokerai.game.eval.spears.Card.get(39+h7);
                  n++;
                  sum += eval.evaluate(cards);
                  //System.out.println(sum);
                }
      }
    print(sum, time, n, 3);
  }

 public static void testSpears2p2() {
    int n = 0;
    int[] hand = new int[7];
    int[] reshand = new int[6];
    pokerai.game.eval.spears2p2.Card c = pokerai.game.eval.spears2p2.Card.parse("Th");
    System.out.println("should be 34: " + c.ordinal());
    StateTableEvaluator.initialize();
    pokerai.game.eval.spears2p2.Card[] cards = new pokerai.game.eval.spears2p2.Card[7];
    for (int i = 0; i < 7; i++) cards[i] = pokerai.game.eval.spears2p2.Card.parse("AsAh");
    //pokerai.game.eval.spears.Card[] deck = pokerai.game.eval.spears.Card.values();
    long time = System.currentTimeMillis();
    long sum = 0;
    for (int h1 = 0; h1 < 13; h1++)
      for (int h2 = 0; h2 < 13; h2++) {
//        System.out.println(h1 + " " + h2);
        for (int h3 = 0; h3 < 13; h3++)
          for (int h4 = 0; h4 < 13; h4++)
            for (int h5 = 0; h5 < 13; h5++)  if (h5 != h2)
              for (int h6 = 0; h6 < 13; h6++) if (h6 != h3)
                for (int h7 = 0; h7 < 13; h7++) if (h7 != h4) {
            //pokerai.game.eval.spears.Hand h = new pokerai.game.eval.spears.Hand();
                  cards[0] = pokerai.game.eval.spears2p2.Card.get(h1);
                  cards[1] = pokerai.game.eval.spears2p2.Card.get(13+h2);
                  cards[2] = pokerai.game.eval.spears2p2.Card.get(26+h3);
                  cards[3] = pokerai.game.eval.spears2p2.Card.get(39+h4);
                  cards[4] = pokerai.game.eval.spears2p2.Card.get(13+h5);
                  cards[5] = pokerai.game.eval.spears2p2.Card.get(26+h6);
                  cards[6] = pokerai.game.eval.spears2p2.Card.get(39+h7);
                  n++;
                  sum += StateTableEvaluator.getRank(cards);
                  //System.out.println(sum);
                }
      }
   print(sum, time, n, 4);
  }


 public static void testSteveBrecher() {
    int n = 0;
    int[] hand = new int[7];
    int[] reshand = new int[6];
    pokerai.game.eval.stevebrecher.Card c = new pokerai.game.eval.stevebrecher.Card("Th");
//    System.out.println("should be 34: " + c.ordinal());
    int[] cards = new int[7];
    //for (int i = 0; i < 7; i++) cards[i] = new pokerai.game.eval.stevebrecher.Card("AsAh");
    //pokerai.game.eval.spears.Card[] deck = pokerai.game.eval.spears.Card.values();
    //CardSet cs = new CardSet();
    long key = 0;
    long time = System.currentTimeMillis();
    long sum = 0;
    for (int h1 = 0; h1 < 13; h1++)
      for (int h2 = 0; h2 < 13; h2++) {
//        System.out.println(h1 + " " + h2);
        for (int h3 = 0; h3 < 13; h3++)
          for (int h4 = 0; h4 < 13; h4++)
            for (int h5 = 0; h5 < 13; h5++)  if (h5 != h2)
              for (int h6 = 0; h6 < 13; h6++) if (h6 != h3)
                for (int h7 = 0; h7 < 13; h7++) if (h7 != h4) {
            //pokerai.game.eval.spears.Hand h = new pokerai.game.eval.spears.Hand();
                  key = 0;
                  key |= (0x1L << (h1));
                  key |= (0x1L << (13+h2));
                  key |= (0x1L << (26+h3));
                  key |= (0x1L << (39+h4));
                  key |= (0x1L << (13+h5));
                  key |= (0x1L << (26+h6));
                  key |= (0x1L << (39+h7));
                  n++;
                  sum += HandEval.hand7Eval(key);
                  //System.out.println(sum);
                }
      }
   print(sum, time, n, 5);
  }

  public static void print(long sum, long time, long n, int ind) {
    time = System.currentTimeMillis() - time; // time given is start time
    long handsPerSec = Math.round(1000 / ((time*1.0)/ n));
    System.out.println(ref[ind]);
    System.out.println(" --- Hands per second: [b]" + handsPerSec + "[/b], hands " + n + ", checksum " + sum);
    System.out.println();
  }

}
