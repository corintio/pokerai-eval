package pokerai.game.eval;

//import com.biotools.meerkat.Hand;
//import pokerai.game.eval.alberta.HandEvaluatorAlberta;
import pokerai.game.eval.indiana.HandEvaluator;
import pokerai.game.eval.indiana.FastHandEvaluator;
import pokerai.game.eval.spears.SevenCardEvaluator;
import pokerai.game.eval.spears2p2.StateTableEvaluator;
import pokerai.game.eval.stevebrecher.CardSet;
import pokerai.game.eval.stevebrecher.HandEval;

public class PerformanceTest3 {

  /*

   Same as PerformanceTest2, but cards are passed backwards, i.e. to check out sorting algorithms
   This is not the representative benchmark, it is only used to find out if certain algorithm speed depends on
    the ordering of the cards in the hand to be evaluated.
    It is also not complete with regard to all available benchmarks. 

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
    //testIndiana();
    //testIndiana3();
    //testAlberta();
    //testSpears();
    testSpears2p2();
    testSteveBrecher();
  }

  public static void testIndiana() {
    int[] hand = new int[7];
    long time = System.currentTimeMillis();
    long sum = 0;
    int h1, h2, h3, h4, h5, h6, h7;
    for (h1 = 0; h1 < 52; h1++) {
      for (h2 = h1 + 1; h2 < 52; h2++) {
        for (h3 = h2 + 1; h3 < 52; h3++) {
          for (h4 = h3 + 1; h4 < 52; h4++) {
            for (h5 = h4 + 1; h5 < 52; h5++) {
              for (h6 = h5 + 1; h6 < 52; h6++) {
                for (h7 = h6 + 1; h7 < 52; h7++) {
                  // need to have all the assignments here, as the array is sorted internally
                  hand[0] = h7;
                  hand[1] = h6;
                  hand[2] = h5;
                  hand[3] = h4;
                  hand[4] = h3;
                  hand[5] = h2;
                  hand[6] = h1;
                  sum += HandEvaluator.defineHand(hand);
                }}}}}}}
    print(sum, time, 133784560, 0);
  }

  public static void testIndiana3() {
    int[] hand = new int[7];
    long time = System.currentTimeMillis();
    long sum = 0;
    int h1, h2, h3, h4, h5, h6, h7;
    for (h1 = 0; h1 < 52; h1++) {
      for (h2 = h1 + 1; h2 < 52; h2++) {
        for (h3 = h2 + 1; h3 < 52; h3++) {
          for (h4 = h3 + 1; h4 < 52; h4++) {
            for (h5 = h4 + 1; h5 < 52; h5++) {
              for (h6 = h5 + 1; h6 < 52; h6++) {
                for (h7 = h6 + 1; h7 < 52; h7++) {
                  // need to have all the assignments here, as the array is sorted internally
                  hand[0] = h7;
                  hand[1] = h6;
                  hand[2] = h5;
                  hand[3] = h4;
                  hand[4] = h3;
                  hand[5] = h2;
                  hand[6] = h1;
                  sum += FastHandEvaluator.evaluate7(hand);
                }}}}}}}
    print(sum, time, 133784560, 1);
  }

/*
  public static void testAlberta() {
    long time = System.currentTimeMillis();
    Hand h = new Hand();
    long sum = 0;
    int h1, h2, h3, h4, h5, h6, h7;
    for (h1 = 0; h1 < 52; h1++) {
      for (h2 = h1 + 1; h2 < 52; h2++) {
        for (h3 = h2 + 1; h3 < 52; h3++) {
          for (h4 = h3 + 1; h4 < 52; h4++) {
            for (h5 = h4 + 1; h5 < 52; h5++) {
              for (h6 = h5 + 1; h6 < 52; h6++) {
                for (h7 = h6 + 1; h7 < 52; h7++) {
                  h.makeEmpty();
                  h.addCard(h7);
                  h.addCard(h6);
                  h.addCard(h5);
                  h.addCard(h4);
                  h.addCard(h3);
                  h.addCard(h2);
                  h.addCard(h1);
                  sum += HandEvaluatorAlberta.rankHand(h);
                }}}}}}}
    print(sum, time, 133784560, 2);
  }
*/
  public static void testSpears() {
    SevenCardEvaluator eval = new SevenCardEvaluator();
    //pokerai.game.eval.spears.Card c = pokerai.game.eval.spears.Card.parse("Th");
    //System.out.println("should be 34: " + c.ordinal());
    pokerai.game.eval.spears.Card[] cards = new pokerai.game.eval.spears.Card[7];
    for (int i = 0; i < 7; i++) cards[i] = pokerai.game.eval.spears.Card.parse("AsAh");
    //pokerai.game.eval.spears.Card[] deck = pokerai.game.eval.spears.Card.values();
    long time = System.currentTimeMillis();
    long sum = 0;
    int h1, h2, h3, h4, h5, h6, h7;
    for (h1 = 0; h1 < 52; h1++) {
      for (h2 = h1 + 1; h2 < 52; h2++) {
        for (h3 = h2 + 1; h3 < 52; h3++) {
          for (h4 = h3 + 1; h4 < 52; h4++) {
            for (h5 = h4 + 1; h5 < 52; h5++) {
              for (h6 = h5 + 1; h6 < 52; h6++) {
                for (h7 = h6 + 1; h7 < 52; h7++) {
                  cards[0] = pokerai.game.eval.spears.Card.get(h7);
                  cards[1] = pokerai.game.eval.spears.Card.get(h6);
                  cards[2] = pokerai.game.eval.spears.Card.get(h5);
                  cards[3] = pokerai.game.eval.spears.Card.get(h4);
                  cards[4] = pokerai.game.eval.spears.Card.get(h3);
                  cards[5] = pokerai.game.eval.spears.Card.get(h2);
                  cards[6] = pokerai.game.eval.spears.Card.get(h1);
                  sum += eval.evaluate(cards);
                }}}}}}}
    print(sum, time, 133784560, 3);
  }

  public static void testSpears2p2() {
    //pokerai.game.eval.spears2p2.Card c = pokerai.game.eval.spears2p2.Card.parse("Th");
    //System.out.println("should be 34: " + c.ordinal());
    StateTableEvaluator.initialize();
    pokerai.game.eval.spears2p2.Card[] cards = new pokerai.game.eval.spears2p2.Card[7];
    for (int i = 0; i < 7; i++) cards[i] = pokerai.game.eval.spears2p2.Card.parse("AsAh");
    //pokerai.game.eval.spears.Card[] deck = pokerai.game.eval.spears.Card.values();
    long time = System.currentTimeMillis();
    long sum = 0;
    int h1, h2, h3, h4, h5, h6, h7;
    for (h1 = 0; h1 < 52; h1++) {
      for (h2 = h1 + 1; h2 < 52; h2++) {
        for (h3 = h2 + 1; h3 < 52; h3++) {
          for (h4 = h3 + 1; h4 < 52; h4++) {
            for (h5 = h4 + 1; h5 < 52; h5++) {
              for (h6 = h5 + 1; h6 < 52; h6++) {
                for (h7 = h6 + 1; h7 < 52; h7++) {
                  cards[0] = pokerai.game.eval.spears2p2.Card.get(h7);
                  cards[1] = pokerai.game.eval.spears2p2.Card.get(h6);
                  cards[2] = pokerai.game.eval.spears2p2.Card.get(h5);
                  cards[3] = pokerai.game.eval.spears2p2.Card.get(h4);
                  cards[4] = pokerai.game.eval.spears2p2.Card.get(h3);
                  cards[5] = pokerai.game.eval.spears2p2.Card.get(h2);
                  cards[6] = pokerai.game.eval.spears2p2.Card.get(h1);
                  sum += StateTableEvaluator.getRank(cards);
                  //System.out.println(sum);
                }}}}}}}
    print(sum, time, 133784560, 4);
  }


  public static void testSteveBrecher() {
    //pokerai.game.eval.stevebrecher.Card c = new pokerai.game.eval.stevebrecher.Card("Th");
    //System.out.println("should be 34: " + c.ordinal());
    //int[] cards = new int[7];
    //for (int i = 0; i < 7; i++) cards[i] = new pokerai.game.eval.stevebrecher.Card("AsAh");
    //pokerai.game.eval.spears.Card[] deck = pokerai.game.eval.spears.Card.values();
    //CardSet cs = new CardSet();
    long time = System.currentTimeMillis();
    long sum = 0;
    //*
    int h1, h2, h3, h4, h5, h6, h7;
    for (h1 = 0; h1 < 52; h1++) {
      for (h2 = h1 + 1; h2 < 52; h2++) {
        for (h3 = h2 + 1; h3 < 52; h3++) {
          for (h4 = h3 + 1; h4 < 52; h4++) {
            for (h5 = h4 + 1; h5 < 52; h5++) {
              for (h6 = h5 + 1; h6 < 52; h6++) {
                for (h7 = h6 + 1; h7 < 52; h7++) {
                  //pokerai.game.eval.spears.Hand h = new pokerai.game.eval.spears.Hand();
                  long key = 0;
                  key |= (0x1L << (h7));
                  key |= (0x1L << (h6));
                  key |= (0x1L << (h5));
                  key |= (0x1L << (h4));
                  key |= (0x1L << (h3));
                  key |= (0x1L << (h2));
                  key |= (0x1L << (h1));
                  sum += HandEval.hand7Eval(key);
                }}}}}}}
    print(sum, time, 133784560, 5);
  }

  public static void print(long sum, long time, long n, int ind) {
    time = System.currentTimeMillis() - time; // time given is start time
    //long handsPerSec = Math.round(1000 / ((time*1.0)/ n));
    long handsPerSec = Math.round(n / (time / 1000.0));
    System.out.println(ref[ind]);
    System.out.println(" --- Hands per second: [b]" + handsPerSec + "[/b], hands " + n + ", checksum " + sum + ", total time: " + time);
    System.out.println();
  }

}
