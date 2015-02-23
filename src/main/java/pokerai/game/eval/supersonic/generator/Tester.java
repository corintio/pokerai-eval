package pokerai.game.eval.supersonic.generator;

import com.biotools.meerkat.Hand;

import pokerai.game.eval.supersonic.LookupHandEvaluator;

public class Tester {

  private static LookupHandEvaluator evaluator;

  public static void main(String[] args) {
    evaluator = LookupHandEvaluator.getInstance("math\\HandRanks.dat");

    test1();
    test2();
  }

  private static void test1() {
    String[][] hands = new String[][] { { "Straight Flush", "As Ks Qs Js Ts" },
        { "Quads", "Ac Ad Ah As Kc Kd Ks" },
        { "Full House", "Ac Ad Ah Kc Kd Qc Qd" },
        { "Flush", "Ac Kc Qc Jc 9c 8c 7c" },
        { "Straight", "Ac Kc Qc Jc Td 9d 8d" },
        { "Trips", "Ac Ad Ah Kc Qc Jc 9d" },
        { "Two Pair", "Ac Ad Kc Kd Qc Jc 9d" },
        { "Pair", "Ac Ad Kc Qc Jc 9d 8d" },
        { "High Card", "Ac Kc Qc Jc 9d 8d 7d" }, };

    int[] cactusKevHandRanks = new int[] { 1, 11, 167, 323, 1600, 1610, 2468,
        3326, 6186 };

    for (int i = 0; i < hands.length; i++) {
      String name = hands[i][0];
      String handString = hands[i][1];
      int correctRank = 7462 - cactusKevHandRanks[i];
      Hand hand = new Hand(handString);
      int rank = evaluator.rankHand(hand);
      String check = (rank == correctRank) ? "CORRECT" : "INCORRECT";

      System.out.println("Hand: " + name + " " + hand + " rank: " + rank + " "
          + check);
    }
    System.out.println();
  }

  private static void test2() {
    String[][] hands = new String[][] {
        { "Straight Flush", "2h 3h Ts Js Qs Ks As" },
        { "Quads", "2c Ac Ad 3h Ah Ts As" },
        { "Boat", "Ac 4d 2h Jh Ah Js As" },
        { "Flush", "Jh Ah 2s 4s 7s Js As" },
        { "Trips", "6d Ad Jh Ah 2s 4s As" },
        { "2 Pair", "4d Jh Ah 2s 7s Js As" },
        { "High Card", "4d 9h Jh 2s 7s Qs As" }, };

    for (int i = 0; i < hands.length; i++) {
      String name = hands[i][0];
      String handString = hands[i][1];
      Hand hand = new Hand(handString);
      int rank = evaluator.rankHand(hand);
      System.out.println("Hand: " + name + " " + hand + " " + handString
          + " rank: " + rank);
    }
    System.out.println();
  }

}
