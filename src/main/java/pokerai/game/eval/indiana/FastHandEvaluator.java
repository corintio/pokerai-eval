
/**
 *  Very fast hand evaluator
 *  @Author Indiana, http://pokerai.org/pf3
 *  @Copyright 2007
 */

package pokerai.game.eval.indiana;

public class FastHandEvaluator {

  // 3-cards hand evaluator
  public static int evaluate5(int[] all) {
    int max = 0, temp;
    //sort(all);
    int a0 = all[0];
    int a1 = all[1];
    int a2 = all[2];
    int a3 = all[3];
    int a4 = all[4];
    /**/
    // sort
    int swap;
    if (a0 < a1) { swap = a0; a0 = a1; a1 = swap; }
    if (a0 < a2) { swap = a0; a0 = a2; a2 = swap; }
    if (a0 < a3) { swap = a0; a0 = a3; a3 = swap; }
    if (a0 < a4) { swap = a0; a0 = a4; a4 = swap; }
    if (a1 < a2) { swap = a1; a1 = a2; a2 = swap; }
    if (a1 < a3) { swap = a1; a1 = a3; a3 = swap; }
    if (a1 < a4) { swap = a1; a1 = a4; a4 = swap; }
    if (a2 < a3) { swap = a2; a2 = a3; a3 = swap; }
    if (a2 < a4) { swap = a2; a2 = a4; a4 = swap; }
    if (a3 < a4) { swap = a3; a3 = a4; a4 = swap; }

    // general check for flush
    boolean flush = false;

    int[] suits = new int[4];
    suits[mod4[a0]]++; suits[mod4[a1]]++; suits[mod4[a2]]++; suits[mod4[a3]]++; suits[mod4[a4]]++;
    if (suits[0] > 4 || suits[1] > 4 ||suits[2] > 4 ||suits[3] > 4) flush = true;

    // general check for straight
    boolean straight = false;

    int zz0 = div4[a0];
    int zz1 = div4[a1];
    int zz2 = div4[a2];
    int zz3 = div4[a3];
    int zz4 = div4[a4];
    z0 = zz0; z1 = zz1; z2 = zz2; z3 = zz3; z4 = zz4;
    temp = value2(a0, a1, a2, a3, a4, flush); if (temp > max) max = temp;
    /* */
    return max;
  }

  // 6-cards hand evaluator
  public static int evaluate6(int[] all) {
    int max = 0, temp;
    //sort(all);
    int a0 = all[0];
    int a1 = all[1];
    int a2 = all[2];
    int a3 = all[3];
    int a4 = all[4];
    int a5 = all[5];
    /**/
    // sort
    int swap;
    if (a0 < a1) { swap = a0; a0 = a1; a1 = swap; }
    if (a0 < a2) { swap = a0; a0 = a2; a2 = swap; }
    if (a0 < a3) { swap = a0; a0 = a3; a3 = swap; }
    if (a0 < a4) { swap = a0; a0 = a4; a4 = swap; }
    if (a0 < a5) { swap = a0; a0 = a5; a5 = swap; }
    if (a1 < a2) { swap = a1; a1 = a2; a2 = swap; }
    if (a1 < a3) { swap = a1; a1 = a3; a3 = swap; }
    if (a1 < a4) { swap = a1; a1 = a4; a4 = swap; }
    if (a1 < a5) { swap = a1; a1 = a5; a5 = swap; }
    if (a2 < a3) { swap = a2; a2 = a3; a3 = swap; }
    if (a2 < a4) { swap = a2; a2 = a4; a4 = swap; }
    if (a2 < a5) { swap = a2; a2 = a5; a5 = swap; }
    if (a3 < a4) { swap = a3; a3 = a4; a4 = swap; }
    if (a3 < a5) { swap = a3; a3 = a5; a5 = swap; }
    if (a4 < a5) { swap = a4; a4 = a5; a5 = swap; }

    // general check for flush
    boolean flush = false;

    int[] suits = new int[4];
    suits[mod4[a0]]++; suits[mod4[a1]]++; suits[mod4[a2]]++; suits[mod4[a3]]++;
    suits[mod4[a4]]++; suits[mod4[a5]]++;
    if (suits[0] > 4 || suits[1] > 4 ||suits[2] > 4 ||suits[3] > 4) flush = true;

    // general check for straight
    boolean straight = false;

    int zz0 = div4[a0];
    int zz1 = div4[a1];
    int zz2 = div4[a2];
    int zz3 = div4[a3];
    int zz4 = div4[a4];
    int zz5 = div4[a5];
    z0 = zz0; z1 = zz1; z2 = zz2; z3 = zz3; z4 = zz4;
    temp = value2(a0, a1, a2, a3, a4, flush); if (temp > max) max = temp;
    z4 = zz5;
    temp = value2(a0, a1, a2, a3, a5, flush); if (temp > max) max = temp;
    z3 = zz4; z4 = zz5;
    temp = value2(a0, a1, a2, a4, a5, flush); if (temp > max) max = temp;
    z2 = zz3; z3 = zz4; z4 = zz5;
    temp = value2(a0, a1, a3, a4, a5, flush); if (temp > max) max = temp;
    z1 = zz2; z2 = zz3; z3 = zz4; z4 = zz5;
    temp = value2(a0, a2, a3, a4, a5, flush); if (temp > max) max = temp;
    z0 = zz1; z1 = zz2; z2 = zz3; z3 = zz4; z4 = zz5;
    temp = value2(a1, a2, a3, a4, a5, flush); if (temp > max) max = temp;
     /* */
     return max;
  }

 // 7-cards hand evaluator
 public static int evaluate7(int[] all) {
   int max = 0, temp;
   //sort(all);
   int a0 = all[0];
   int a1 = all[1];
   int a2 = all[2];
   int a3 = all[3];
   int a4 = all[4];
   int a5 = all[5];
   int a6 = all[6];
   /**/
   // sort
   int swap;
   if (a0 < a1) { swap = a0; a0 = a1; a1 = swap; }
   if (a0 < a2) { swap = a0; a0 = a2; a2 = swap; }
   if (a0 < a3) { swap = a0; a0 = a3; a3 = swap; }
   if (a0 < a4) { swap = a0; a0 = a4; a4 = swap; }
   if (a0 < a5) { swap = a0; a0 = a5; a5 = swap; }
   if (a0 < a6) { swap = a0; a0 = a6; a6 = swap; }
   if (a1 < a2) { swap = a1; a1 = a2; a2 = swap; }
   if (a1 < a3) { swap = a1; a1 = a3; a3 = swap; }
   if (a1 < a4) { swap = a1; a1 = a4; a4 = swap; }
   if (a1 < a5) { swap = a1; a1 = a5; a5 = swap; }
   if (a1 < a6) { swap = a1; a1 = a6; a6 = swap; }
   if (a2 < a3) { swap = a2; a2 = a3; a3 = swap; }
   if (a2 < a4) { swap = a2; a2 = a4; a4 = swap; }
   if (a2 < a5) { swap = a2; a2 = a5; a5 = swap; }
   if (a2 < a6) { swap = a2; a2 = a6; a6 = swap; }
   if (a3 < a4) { swap = a3; a3 = a4; a4 = swap; }
   if (a3 < a5) { swap = a3; a3 = a5; a5 = swap; }
   if (a3 < a6) { swap = a3; a3 = a6; a6 = swap; }
   if (a4 < a5) { swap = a4; a4 = a5; a5 = swap; }
   if (a4 < a6) { swap = a4; a4 = a6; a6 = swap; }
   if (a5 < a6) { swap = a5; a5 = a6; a6 = swap; }

   // general check for flush
   boolean flush = false;

   int[] suits = new int[4];
   suits[mod4[a0]]++; suits[mod4[a1]]++; suits[mod4[a2]]++; suits[mod4[a3]]++;
   suits[mod4[a4]]++; suits[mod4[a5]]++; suits[mod4[a6]]++;
   if (suits[0] > 4 || suits[1] > 4 ||suits[2] > 4 ||suits[3] > 4) flush = true;
   
   // general check for straight
   boolean straight = false;

   int zz0 = div4[a0];
   int zz1 = div4[a1];
   int zz2 = div4[a2];
   int zz3 = div4[a3];
   int zz4 = div4[a4];
   int zz5 = div4[a5];
   int zz6 = div4[a6];
   z0 = zz0; z1 = zz1; z2 = zz2; z3 = zz3; z4 = zz4;
   temp = value2(a0, a1, a2, a3, a4, flush); if (temp > max) max = temp;
   z4 = zz5;
   temp = value2(a0, a1, a2, a3, a5, flush); if (temp > max) max = temp;
   z4 = zz6;
   temp = value2(a0, a1, a2, a3, a6, flush); if (temp > max) max = temp;
   z3 = zz4; z4 = zz5;
   temp = value2(a0, a1, a2, a4, a5, flush); if (temp > max) max = temp;
   z3 = zz4; z4 = zz6;
   temp = value2(a0, a1, a2, a4, a6, flush); if (temp > max) max = temp;
   z3 = zz5; z4 = zz6;
   temp = value2(a0, a1, a2, a5, a6, flush); if (temp > max) max = temp;
   z2 = zz3; z3 = zz4; z4 = zz5;
   temp = value2(a0, a1, a3, a4, a5, flush); if (temp > max) max = temp;
   z3 = zz4; z4 = zz6;
   temp = value2(a0, a1, a3, a4, a6, flush); if (temp > max) max = temp;
   z3 = zz5; z4 = zz6;
   temp = value2(a0, a1, a3, a5, a6, flush); if (temp > max) max = temp;
   z2 = zz4; 
   temp = value2(a0, a1, a4, a5, a6, flush); if (temp > max) max = temp;
   z1 = zz2; z2 = zz3; z3 = zz4; z4 = zz5;
   temp = value2(a0, a2, a3, a4, a5, flush); if (temp > max) max = temp;
   z4 = zz6;
   temp = value2(a0, a2, a3, a4, a6, flush); if (temp > max) max = temp;
   z3 = zz5; z4 = zz6;
   temp = value2(a0, a2, a3, a5, a6, flush); if (temp > max) max = temp;
   z2 = zz4; z3 = zz5; z4 = zz6;
   temp = value2(a0, a2, a4, a5, a6, flush); if (temp > max) max = temp;
   z1 = zz3; z2 = zz4; z3 = zz5; z4 = zz6;
   temp = value2(a0, a3, a4, a5, a6, flush); if (temp > max) max = temp;
   z0 = zz1; z1 = zz2; z2 = zz3; z3 = zz4; z4 = zz5;
   temp = value2(a1, a2, a3, a4, a5, flush); if (temp > max) max = temp;
   z4 = zz6;
   temp = value2(a1, a2, a3, a4, a6, flush); if (temp > max) max = temp;
   z3 = zz5; z4 = zz6;
   temp = value2(a1, a2, a3, a5, a6, flush); if (temp > max) max = temp;
   z2 = zz4; z3 = zz5; z4 = zz6;
   temp = value2(a1, a2, a4, a5, a6, flush); if (temp > max) max = temp;
   z1 = zz3; z2 = zz4; z3 = zz5; z4 = zz6;
   temp = value2(a1, a3, a4, a5, a6, flush); if (temp > max) max = temp;
   z0 = zz2; z1 = zz3; z2 = zz4; z3 = zz5; z4 = zz6;
   temp = value2(a2, a3, a4, a5, a6, flush); if (temp > max) max = temp;
    /* */
    return max;
  }

  static int[] div4 = {0,0,0,0, 1,1,1,1, 2,2,2,2, 3,3,3,3, 4,4,4,4, 5,5,5,5, 6,6,6,6, 7,7,7,7,
                       8,8,8,8, 9,9,9,9, 10,10,10,10, 11,11,11,11, 12,12,12,12, 13,13,13,13};
  static int[] mod4 = {0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3,
                       0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3};

  // static final int mul1 = 1, mul2 = mul1 * 15, mul3 = mul2 * 15, mul4 = mul3 * 15, mul5 = mul4 * 15;
   static int[] mult0 = new int[14];
   static int[] mult1 = new int[14];
   static int[] mult2 = new int[14];
   static int[] mult3 = new int[14];
   static int[] mult4 = new int[14];
   static int[] mult5 = new int[14];
   static final int[] mul = {0, 1, 15, 225, 3375, 50625};
   static {
     for (int z = 0; z<14; z++) mult0[z] = mul[0] * z;
     for (int z = 0; z<14; z++) mult1[z] = mul[1] * z;
     for (int z = 0; z<14; z++) mult2[z] = mul[2] * z;
     for (int z = 0; z<14; z++) mult3[z] = mul[3] * z;
     for (int z = 0; z<14; z++) mult4[z] = mul[4] * z;
     for (int z = 0; z<14; z++) mult5[z] = mul[5] * z;
   }

  static int z0, z1, z2, z3, z4;
  // calculate the value of a hand according all rules
  // cards that are given as input here should be sorted in desceding order!
  public static int value2( int x1, int x2, int x3, int x4, int x5, boolean flush ) {
    // int z0 = div4[x1], z1 = div4[x2], z2 = div4[x3], z3 = div4[x4], z4 = div4[x5];

    if (flush) {
      int c0 = mod4[x1], c1 = mod4[x2], c2 = mod4[x3], c3 = mod4[x4], c4 = mod4[x5];
      flush = (c0 == c1 && c1 == c2 && c2 == c3 && c3 == c4);
    }

    // Num of diff cards
    int diff = 0;
    if (z0 != z1) diff++; if (z1 != z2) diff++; if (z2 != z3) diff++; if (z3 != z4) diff++;

    boolean straight = false;
    //straight = (z4 == z0-4 && z1 == z0-1 && z2 == z1-1 && z3 == z2-1); // detect A straight?
    if (diff == 4) {
      if (z4 == z0 - 4) straight = true;
        else if (z0 == 12 && z1 == 3) straight = true;  // A 5 4 3 2
    }

    // 1. royal flush, 2. straight flush
    if (straight && flush) {
      if (z0 == 12 && z1 == 3) return 10000000 + z1; // this is 5-high straight flush A 5 4 3 2, return 5 instead of A
      return 10000000 + z0; // only highest card matters, rest are the same
    }

    // 3. four of a kind
    if (diff == 1) {
      if (z1 == z2 && z2 == z3) {
        if (z0 != z1)
          return 8000000 + mult5[z1] + z0;
          else return 8000000 + mult5[z1] + z4;
      }
      // diff == 1
      // 4. full hause
      if (z0 == z2) { // aaabb
        return 7000000 + mult5[z2] + z4;
      } else {
        return 7000000 + mult5[z2] + z0;
      }
    }

    
    // 5. Flush
    if (flush) {
      return 6000000 + mult5[z0] + mult4[z1] + mult3[z2] + mult2[z3] + mult1[z4];     // all card matters
    }

    // 6. Straight
    if (straight) {
      if (z0 == 12 && z1 == 3) return 5000000 + z1; // this is 5-high straight A 5 4 3 2, return 5 instead of A
      return 5000000 + z0; // Only the highest card matters, rest are ignored
    }

    // 7. Three of a kind
    if (diff == 2) {
      // aaabc, abbbc, abccc
      if (z2 == z0) return 4000000 + mult5[z2] + mult2[z3] + z4;
      if (z2 == z4) return 4000000 + mult5[z2] + mult2[z0] + z1;
      if (z1 == z3) return 4000000 + mult5[z2] + mult2[z0] + z4;
      // diff == 2
      // 8. Two pairs
      // aabbc, aabcc, abbcc
      if (z0 == z1) {
        if (z2 == z3) return 3000000 + mult5[z0] + mult3[z2] + mult2[z4];
        if (z3 == z4) return 3000000 + mult5[z0] + mult3[z3] + mult2[z2];
      }
      if (z1 == z2 && z3 == z4) return 3000000 + mult5[z1] + mult3[z3] + mult2[z0];
    }

   // 9. One pair
    if (diff == 3) {
      // aabcd, baacd, bcaad, bcdaa
      if (z0 == z1) return 2000000 + mult5[z0] + mult3[z2] + mult2[z3] + mult1[z4];
      if (z1 == z2) return 2000000 + mult5[z1] + mult3[z0] + mult2[z3] + mult1[z4];
      if (z2 == z3) return 2000000 + mult5[z2] + mult3[z0] + mult2[z1] + mult1[z4];
      if (z3 == z4) return 2000000 + mult5[z3] + mult3[z0] + mult2[z1] + mult1[z2];
    }

    // 10. High card
    return mult5[z0] + mult4[z1] + mult3[z2] + mult2[z3] + mult1[z4];   // all card matters
  }

}
