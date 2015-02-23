package pokerai.game.eval.supersonic.test;

import com.biotools.meerkat.Card;
import com.biotools.meerkat.Hand;

public class HandEncoder {
   /*
    * This class encodes and decodes hands to and from Indiana's hand represenation format (which is given below).
    * For example, given integer array of 7 cards, each integer representing one poker card (accoring to Indiana's
    * defined internal encoding) one can conver this array to another array, representing e.g. Steve Brecher's
    * hand encoding, or JukOfYork, or others (and vice versa).
    */

  /* Indiana's hand encoding:

  CARDS:
      0,  1,  2,  3 - 2c 2d 2s 2h
      4,  5,  6,  7 - 3c 3d 3s 3h
      8,  9, 10, 11 - 4c 4d 4s 4h
     12, 13, 14, 15 - 5
     16, 17, 18, 19 - 6
     20, 21, 22, 23 - 7
     24, 25, 26, 27 - 8
     28, 29, 30, 31 - 9
     32, 33, 34, 35 - T
     36, 37, 38, 39 - Jc Jd Js Jh
     40, 41, 42, 43 - Qc Qd Qs Qh
     44, 45, 46, 47 - Kc Kd Ks Kh
     48, 49, 50, 51 - Ac Ad As Ah

  Value -1 is ignored.
  If N is given card, rank (0,...,12) is obtained by N/4, and suit (0,...,3) is obtained by N % 4  

  */

  public static Hand encodeToMeerkat(int[] hand) {
    Hand h = new Hand();
    for( int i : hand ) {
      h.addCard(new Card( printCard(i) ));
    }
    return h;
  }

  public static long encodeToSteveBrecher(int[] hand) {
    long result = 0;
    for (int i = 0; i < hand.length; i++) {
      //return 0x1L << (card.suitOf().ordinal()*13 + card.rankOf().ordinal());
      result |= (0x1L << ((hand[i] % 4)*13 + (hand[i]/4)));
    }
    return result;
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
