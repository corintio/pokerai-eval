package pokerai.game.eval.supersonic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.biotools.meerkat.Card;
import com.biotools.meerkat.Hand;
import com.biotools.meerkat.HandEval;

//import pokerai.dealer.Logger;
import pokerai.game.eval.supersonic.generator.StateTableGenerator;

public class LookupHandEvaluator implements HandEval {
  static int[] offsets = new int[] { 0, 1277, 4137, 4995, 5853, 5863, 7140,
      7296, 7452 };

  private static final int HAND_RANKS_SIZE = 32487834;
  private int handRanks[];

  private static LookupHandEvaluator instance = null;

  public static LookupHandEvaluator getInstance(String fileName) {
    if (instance == null) {
      instance = new LookupHandEvaluator(fileName);
    }

    return (instance);
  }

  private LookupHandEvaluator(String fileName) {
    try {
//      System.out.println("Loading evaluation tables ...");
      File f = new File(fileName);
      if (!f.exists()) {
        System.out
            .println("Evaluation tables do not exist, this is first time run. Generating them ...");

        handRanks = new int[HAND_RANKS_SIZE];
        StateTableGenerator.generateTables(handRanks);
        StateTableGenerator.saveTables(fileName, handRanks);

//        System.out.println("Loading evaluation tables (again) ...");
      }

      long t = System.currentTimeMillis();

      ObjectInputStream s = new ObjectInputStream(new FileInputStream(fileName));
      handRanks = (int[]) s.readObject();
      t = System.currentTimeMillis() - t;
      System.out.println("Evaluation tables loaded in " + t / 1000.0
          + " seconds");
    } catch (IOException ioe) {
//      Logger.error(this, 1, ioe.getMessage());
    } catch (ClassNotFoundException cnfe) {
//      Logger.error(this, 1, cnfe.getMessage());
    }
  }

  public int rankHand(Hand h) {
    int size = h.size();
    if (size < 5) {
      return -1;
    }

    int p = 53;
    for (int pos = 1; pos <= size; pos++) {
      p = handRanks[p + h.getCard(pos).getIndex() + 1];
    }

    return (size < 7 ? handRanks[p] : p);
  }

  public int rankHand5(Hand h) {
    int p = 53;
    for (int pos = 1; pos <= 5; pos++) {
      p = handRanks[p + h.getCard(pos).getIndex() + 1];
    }

    return (handRanks[p]);
  }

  public int rankHand6(Hand h) {
    int p = 53;
    for (int pos = 1; pos <= 6; pos++) {
      p = handRanks[p + h.getCard(pos).getIndex() + 1];
    }

    return (handRanks[p]);
  }

  public int rankHand7(Hand h) {
    int p = 53;
    for (int pos = 1; pos <= 7; pos++) {
      p = handRanks[p + h.getCard(pos).getIndex() + 1];
    }
    return (p);
  }

  public int rankHand(Card c1, Card c2, Hand h) {
    h.addCard(c1);
    h.addCard(c2);

    int rank = rankHand(h);

    h.removeCard();
    h.removeCard();

    return rank;
  }
}
