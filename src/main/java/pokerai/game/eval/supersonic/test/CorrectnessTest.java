package pokerai.game.eval.supersonic.test;

import pokerai.game.eval.stevebrecher.HandEval;
import pokerai.game.eval.supersonic.LookupHandEvaluator;

public class CorrectnessTest {

  public static void main(String args[]) {
    correctnessTest();
  }


  /*
   * Correctness Test
   * Run continiously and compares if two hand evaluators give the same results.
   * Usually used to compare the fastest found evaluator toward some evaluator known
   * working 100% correct and working very fast (currently: Steve Breacher's evaluator)
   *
   * Currently setup to benchmark Indiana's vs Steve Breachers's
   *
  /*
  Fixes (history) in indiana FastHandvaluator, 1: 5-high straight (A - 5) was not handled correctly
  Fixes (history) in indiana FastHandvaluator, 2: 5-high straight flush (A - 5) was not handled correctly
   */
  public static void correctnessTest() {
    LookupHandEvaluator eval = LookupHandEvaluator.getInstance("math\\HandRanks.dat");
    long tests = 1000;

    System.out.println("Performing " + tests + " correctness tests");
    for (int i = 0; i < tests; i++) {
      int[] h1 = dealCards(6);
      int[] h2 = dealCards(6);

      int resI1 = eval.rankHand6(HandEncoder.encodeToMeerkat(h1));
      int resI2 = eval.rankHand6(HandEncoder.encodeToMeerkat(h2));
      int resS1 = HandEval.hand6Eval(HandEncoder.encodeToSteveBrecher(h1));
      int resS2 = HandEval.hand6Eval(HandEncoder.encodeToSteveBrecher(h2));

      if (resI1 > resI2) {
        if (resS1 <= resS2) {  System.out.println("Error-I1>I2, S1<S2, Hand1=" + printHand(h1) + ", Hand2=" + printHand(h2)); System.out.println(" " + resI1 + " " + ( resI1 >> 12 ) + " " + + resI2 + " " + ( resI2 >> 12 ) ); }
      } else if (resI1 < resI2) {
        if (resS1 >= resS2) {  System.out.println("Error-I1<I2, S1>S2, Hand1=" + printHand(h1) + ", Hand2=" + printHand(h2)); System.out.println(" " + resI1 + " " + ( resI1 >> 12 ) + " " + + resI2 + " " + ( resI2 >> 12 ) ); }
      } else {
        if (resS1 != resS2) {  System.out.println("Error-I1=I2, S1!=S2, Hand1=" + printHand(h1) + ", Hand2=" + printHand(h2)); System.out.println(" " + resI1 + " " + ( resI1 >> 12 ) + " " + + resI2 + " " + ( resI2 >> 12 ) ); }
      }
    }
    System.out.println("Done. ");
  }

  public static int[] dealCards(int how) {
    int[] arr = new int[how];
    boolean[] deck = new boolean[52];
    for (int i = 0; i < 52; i++) deck[i] = false;
    for (int i = 0; i < arr.length; i++) {
      int ci = (int)(Math.random()*52);
      while (deck[ci]) {
        ci = (int)(Math.random()*52);
      }
      deck[ci] = true;
      arr[i] = ci;
    }
    return arr;
  }

  public static String printHand(int[] cards) {
    return printCards(cards, 0, cards.length);
  }

  public static String printCards(int[] cards, int start, int end) {
    String cardS = "";
    for (int i = start; i < end; i++) {
      cardS += " " + printCard(cards[i]);
    }
    return cardS;
  }

  public static String printCard(int card) {
    if (card == -1) return "-";
    //if (card == -1) return "";
    int size = card / 4;
    int suit = card % 4;
    String res = "";
    if (size < 8) res = (size+2) + ""; else if (size == 8) res = "T"; else
        if (size == 9) res = "J"; else if (size == 10) res = "Q"; else
            if (size == 11) res = "K"; else if (size == 12) res = "A";
    switch (suit) {
      case 0 : return res + "c";
      case 1 : return res + "d";
      case 2 : return res + "s";
      case 3 : return res + "h";
    }
    return "ER";
  }
}
