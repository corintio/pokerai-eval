package pokerai.game.eval.supersonic.generator;

import java.io.*;

import com.biotools.meerkat.Card;

public class StateTableGenerator {

  private static boolean verbose = true; // toggles verbose mode

  private static int[] hand; // re-usable array to hold cards in a hand
  private static int size = 612978; // lookup table size
  private static long[] keys = new long[size]; // array to hold key lookup
  // table
  private static int numKeys = 1; // counter for number of defined keys in key
  // array
  private static long maxKey = 0; // holds current maximum key value
  private static int numCards = 0; // re-usable counter for number of cards
  // in a hand
  private static int cardIndex = 0; // re-usable index for cards in a hands
  private static int maxHandRankIndex = 0;

  private static long startTimer;
  private static long stopTimer;

  // Inserts a key into the key array and returns the insertion index.
  private static int insertKey(long key) {

    // check to see if key is valid
    if (key == 0) { return 0; }

    // short circuit insertion for most common cases
    if (key >= maxKey) {
      if (key > maxKey) {
        keys[numKeys++] = key; // appends the new key to the key
        // array
        maxKey = key;
      }

      return numKeys - 1;
    }

    // use binary search to find insertion point for new key
    int low = -1;
    int high = numKeys;
    int pivot;
    long difference;

    while (high - low > 1) {
      pivot = (low + high) >> 1;
      difference = keys[pivot] - key;
      if (difference > 0) {
        high = pivot;
      } else if (difference < 0) {
        low = pivot;
      } else {
        return pivot; // key already exists
      }
    }

    // key does not exist so must be inserted
    System.arraycopy(keys, high, keys, high + 1, numKeys - high);
    keys[high] = key;

    numKeys++;
    return high;

  } // END insertKey method

  // Returns a key for the hand created by adding a new card to the hand
  // represented by the given key. Returns 0 if new card already appears in
  // hand.
  private static long makeKey(long baseKey, Card newCard) {

    int[] suitCount = new int[Card.NUM_SUITS + 1]; // number of times a
    // suit
    // appears in a hand
    int[] rankCount = new int[Card.NUM_RANKS + 1]; // number of times a
    // rank
    // appears in a hand
    hand = new int[8];

    // extract the hand represented by the key value
    for (cardIndex = 0; cardIndex < 6; cardIndex++) {

      // hand[0] is used to hold the new card
      hand[cardIndex + 1] = (int) ((baseKey >> (8 * cardIndex)) & 0xFF);
    }

    hand[0] = formatCard8bit(newCard);

    // examine the hand to determine number of cards and rank/suit counts
    for (numCards = 0; hand[numCards] != 0; numCards++) {
      suitCount[hand[numCards] & 0xF]++;
      rankCount[(hand[numCards] >> 4) & 0xF]++;

      // check to see if new card is already contained in hand (rank and
      // suit considered)
      if (numCards != 0 && hand[0] == hand[numCards]) { return 0; }
    }

    // check to see if we already have four of a particular rank
    if (numCards > 4) {
      for (int rank = 1; rank < 14; rank++) {
        if (rankCount[rank] > 4) return 0;
      }
    }

    // determine the minimum number of suits required for a flush to be
    // possible
    int minSuitCount = numCards - 2;

    // check to see if suit is significant
    if (minSuitCount > 1) {
      // examine each card in the hand
      for (cardIndex = 0; cardIndex < numCards; cardIndex++) {
        // if the suit is not significant then strip it from the card
        if (suitCount[hand[cardIndex] & 0xF] < minSuitCount) {
          hand[cardIndex] &= 0xF0;
        }
      }
    }

    sortHand();

    long key = 0;
    for (int i = 0; i < 7; i++) {
      key += (long) hand[i] << (i * 8);
    }

    return key;

  } // END makeKey method

  // Formats and returns a card in 8-bit packed representation.
  private static int formatCard8bit(Card card) {

    // 8-Bit Packed Card Representation
    // +--------+
    // |rrrr-sss|
    // +--------+
    // r = rank of card (deuce = 1, trey = 2, four = 3, five = 4,..., ace =
    // 13)
    // s = suit of card (suits are arbitrary, can take value from 1 to 4)

    return (((card.getRank() + 1) << 4) + card.getSuit() + 1);

  } // END formatCard8bit method

  // Sorts the hand using Bose-Nelson Sorting Algorithm (N = 7).
  private static void sortHand() {
    swapCard(0, 4);
    swapCard(1, 5);
    swapCard(2, 6);
    swapCard(0, 2);
    swapCard(1, 3);
    swapCard(4, 6);
    swapCard(2, 4);
    swapCard(3, 5);
    swapCard(0, 1);
    swapCard(2, 3);
    swapCard(4, 5);
    swapCard(1, 4);
    swapCard(3, 6);
    swapCard(1, 2);
    swapCard(3, 4);
    swapCard(5, 6);
  } // End sortHand method

  // Swaps card i with card j.
  private static void swapCard(int i, int j) {
    if (hand[i] < hand[j]) {
      hand[i] ^= hand[j];
      hand[j] ^= hand[i];
      hand[i] ^= hand[j];
    }
  } // END swapCard method

  // Determines the relative strength of a hand (the hand is given by its
  // unique key value).
  private static int getHandRank(long key) {

    // The following method implements a modified version of "Cactus Kev's
    // Five-Card
    // Poker Hand Evaluator" to determine the relative strength of two
    // five-card hands.
    // Reference: http://www.suffecool.net/poker/evaluator.html

    hand = new int[8];
    int currentCard;
    int rank;
    int handRank = 9999;
    int holdrank = 9999;
    int suit = 0;
    int numCards = 0;

    final int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41 };

    if (key != 0) {

      for (cardIndex = 0; cardIndex < 7; cardIndex++) {

        currentCard = (int) ((key >> (8 * cardIndex)) & 0xFF);
        if (currentCard == 0) break;
        numCards++;

        // Cactus Kev Card Representation
        // +--------+--------+--------+--------+
        // |xxxbbbbb|bbbbbbbb|cdhsrrrr|xxpppppp|
        // +--------+--------+--------+--------+
        // p = prime number of rank (deuce = 2, trey = 3, four = 5, five
        // = 7,..., ace = 41)
        // r = rank of card (deuce = 0, trey = 1, four = 2, five =
        // 3,..., ace = 12)
        // cdhs = suit of card
        // b = bit turned on depending on rank of card

        // extract suit and rank from 8-bit packed representation
        rank = (currentCard >> 4) - 1;
        suit = currentCard & 0xF;

        // change card representation to Cactus Kev Representation
        hand[cardIndex] = primes[rank] | (rank << 8) | (1 << (suit + 11)) | (1 << (16 + rank));
      }

      switch (numCards) {
      case 5:

        holdrank = eval_5hand(hand[0], hand[1], hand[2], hand[3], hand[4]);
        break;

      case 6:

        // Cactus Kev's Evaluator ranks hands from 1 (Royal Flush) to
        // 7462 (Seven High Card)
        holdrank = eval_5hand(hand[0], hand[1], hand[2], hand[3], hand[4]);
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[1], hand[2], hand[3], hand[5]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[1], hand[2], hand[4], hand[5]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[1], hand[3], hand[4], hand[5]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[2], hand[3], hand[4], hand[5]));
        holdrank = Math.min(holdrank, eval_5hand(hand[1], hand[2], hand[3], hand[4], hand[5]));
        break;

      case 7:

        holdrank = eval_5hand(hand[0], hand[1], hand[2], hand[3], hand[4]);
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[1], hand[2], hand[3], hand[5]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[1], hand[2], hand[3], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[1], hand[2], hand[4], hand[5]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[1], hand[2], hand[4], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[1], hand[2], hand[5], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[1], hand[3], hand[4], hand[5]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[1], hand[3], hand[4], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[1], hand[3], hand[5], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[1], hand[4], hand[5], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[2], hand[3], hand[4], hand[5]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[2], hand[3], hand[4], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[2], hand[3], hand[5], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[2], hand[4], hand[5], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[0], hand[3], hand[4], hand[5], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[1], hand[2], hand[3], hand[4], hand[5]));
        holdrank = Math.min(holdrank, eval_5hand(hand[1], hand[2], hand[3], hand[4], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[1], hand[2], hand[3], hand[5], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[1], hand[2], hand[4], hand[5], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[1], hand[3], hand[4], hand[5], hand[6]));
        holdrank = Math.min(holdrank, eval_5hand(hand[2], hand[3], hand[4], hand[5], hand[6]));
        break;

      default:

        System.out.println("ERROR: Invalid hand in GetRank method.");
        break;

      }

      // Hand Rank Representation
      // +--------+--------+
      // |hhhheeee|eeeeeeee|
      // +--------+--------+
      // h = poker hand (1 = High Card, 2 = One Pair, 3 = Two Pair,..., 9
      // = Straight Flush)
      // e = equivalency class (Rank of equivalency class relative to base
      // hand)

      // +-----------------------------------+----------------------------------+-----------------+
      // 5-Card Equivalency Classes 7-Card Equivalency Classes
      // +-----------------------------------+----------------------------------+-----------------+
      // 1277 407 High Card
      // 2860 1470 One Pair
      // 858 763 Two Pair
      // 858 575 Three of a Kind
      // 10 10 Straight
      // 1277 1277 Flush
      // 156 156 Full House
      // 156 156 Four of a Kind
      // 10 10 Straight Flush
      // +----------+------------------------+----------------------------------+-----------------+
      // Total: 7462 4824
      // +----------+------------------------+----------------------------------+-----------------+

      handRank = 7463 - holdrank; // Invert ranking metric (1 is now worst
      // hand)

      if (handRank < 1278) handRank = handRank - 0 + 4096 * 1; // High Card
      else if (handRank < 4138) handRank = handRank - 1277 + 4096 * 2; // One
                                                                       // Pair
      else if (handRank < 4996) handRank = handRank - 4137 + 4096 * 3; // Two
                                                                       // Pair
      else if (handRank < 5854) handRank = handRank - 4995 + 4096 * 4; // Three
                                                                       // of a
                                                                       // Kind
      else if (handRank < 5864) handRank = handRank - 5853 + 4096 * 5; // Straight
      else if (handRank < 7141) handRank = handRank - 5863 + 4096 * 6; // Flush
      else if (handRank < 7297) handRank = handRank - 7140 + 4096 * 7; // Full
                                                                       // House
      else if (handRank < 7453) handRank = handRank - 7296 + 4096 * 8; // Four
                                                                       // of a
                                                                       // Kind
      else handRank = handRank - 7452 + 4096 * 9; // Straight Flush

    }
    return handRank;

  } // END getHandRank method

  static int[] offsets = new int[] { 0, 1277, 4137, 4995, 5853, 5863, 7140, 7296, 7452 };

  private static int getIndex(int key) {

    // use binary search to find key
    int low = -1;
    int high = 4888;
    int pivot;

    while (high - low > 1) {
      pivot = (low + high) >>> 1;
      if (Products.table[pivot] > key) {
        high = pivot;
      } else if (Products.table[pivot] < key) {
        low = pivot;
      } else {
        return pivot;
      }
    }
    return -1;

  } // END getIndex method

  private static int eval_5hand(int c1, int c2, int c3, int c4, int c5) {
    int q = (c1 | c2 | c3 | c4 | c5) >> 16;
    short s;

    // check for Flushes and Straight Flushes
    if ((c1 & c2 & c3 & c4 & c5 & 0xF000) != 0) return Flushes.table[q];

    // check for Straights and High Card hands
    if ((s = Unique.table[q]) != 0) return s;

    q = (c1 & 0xFF) * (c2 & 0xFF) * (c3 & 0xFF) * (c4 & 0xFF) * (c5 & 0xFF);
    q = getIndex(q);

    return Values.table[q];

  } // END eval_5hand method

  public static void generateTables(int[] handRanks) {

    Card card;
    int handRank;
    int keyIndex;
    long key;

    if (verbose) {
      System.out.print("\nGenerating and sorting keys...");
      startTimer = System.currentTimeMillis();
    }

    for (keyIndex = 0; keys[keyIndex] != 0 || keyIndex == 0; keyIndex++) {
      if (keyIndex % (size / 100) == 0) System.out.println(keyIndex / (size / 100) + "% generated.");

      for (int ci = 0; ci < Card.NUM_CARDS; ci++) { // add a
        // card
        // to
        // each
        // previously
        // calculated
        // key
        card = new Card(ci);
        key = makeKey(keys[keyIndex], card); // create the new
        // key

        if (numCards < 7) insertKey(key); // insert the new key into the key
        // lookup table
      }
    }

    if (verbose) {
      stopTimer = System.currentTimeMillis();
      System.out.printf("done.\n\n%35s %d\n", "Number of Keys Generated:", (keyIndex + 1));
      System.out.printf("%35s %f seconds\n\n", "Time Required:", ((stopTimer - startTimer) / 1000.0));
      System.out.print("Generating hand ranks...");
      startTimer = System.currentTimeMillis();
    }

    for (keyIndex = 0; keys[keyIndex] != 0 || keyIndex == 0; keyIndex++) {
      if (keyIndex % (size / 100) == 0) System.out.println(keyIndex / (size / 100) + "% generated.");

      for (int ci = 0; ci < Card.NUM_CARDS; ci++) {
        card = new Card(ci);
        key = makeKey(keys[keyIndex], card);

        if (numCards < 7) {
          handRank = insertKey(key) * 53 + 53; // if number of
          // cards is < 7
          // insert key
        } else {
          handRank = getHandRank(key); // if number of cards is 7
          // insert hand rank
        }

        maxHandRankIndex = keyIndex * 53 + card.getIndex() + 54; // calculate
        // hand rank
        // insertion
        // index
        handRanks[maxHandRankIndex] = handRank; // populate hand
        // rank lookup table
        // with appropriate
        // value
        if (numCards == 6 || numCards == 7) {
          // insert the hand rank into the hand rank lookup table
          handRanks[keyIndex * 53 + 53] = getHandRank(keys[keyIndex]);
        }
      }
    }

    if (verbose) {
      stopTimer = System.currentTimeMillis();
      System.out.printf("done.\n\n%35s %f seconds\n\n", "Time Required:", ((stopTimer - startTimer) / 1000.0));
    }

  } // END generateTables method

  public static void saveTables(String fileName, int[] handRanks) throws FileNotFoundException, IOException {

    ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream(fileName));
    s.writeObject(handRanks);
  }
} // END class Evaluator

