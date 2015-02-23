/**
 *  Very fast hand evaluator
 *  @Author Indiana, http://pokerai.org/pf3
 *  @Copyright 2007
 */

#include <stdio.h>
#include <windows.h>

long value(int x1, int x2, int x3, int x4, int x5);

long defineHand(int *all, int n) {
	int max = 0, temp;
	// sort
	for (int i = 0; i<n; i++) for (int j = i+1; j<n; j++)
	  if (all[i] < all[j]) { int sw = all[i]; all[i] = all[j]; all[j] = sw; }    
	// find max
	temp = value(all[0], all[1], all[2], all[3], all[4]); if (temp > max) max = temp;
	temp = value(all[0], all[1], all[2], all[3], all[5]); if (temp > max) max = temp;
	temp = value(all[0], all[1], all[2], all[3], all[6]); if (temp > max) max = temp;
	temp = value(all[0], all[1], all[2], all[4], all[5]); if (temp > max) max = temp;
	temp = value(all[0], all[1], all[2], all[4], all[6]); if (temp > max) max = temp;
	temp = value(all[0], all[1], all[2], all[5], all[6]); if (temp > max) max = temp;
	temp = value(all[0], all[1], all[3], all[4], all[5]); if (temp > max) max = temp;
	temp = value(all[0], all[1], all[3], all[4], all[6]); if (temp > max) max = temp;
	temp = value(all[0], all[1], all[3], all[5], all[6]); if (temp > max) max = temp;
	temp = value(all[0], all[1], all[4], all[5], all[6]); if (temp > max) max = temp;
	temp = value(all[0], all[2], all[3], all[4], all[5]); if (temp > max) max = temp;
	temp = value(all[0], all[2], all[3], all[4], all[6]); if (temp > max) max = temp;
	temp = value(all[0], all[2], all[3], all[5], all[6]); if (temp > max) max = temp;
	temp = value(all[0], all[2], all[4], all[5], all[6]); if (temp > max) max = temp;
	temp = value(all[0], all[3], all[4], all[5], all[6]); if (temp > max) max = temp;
	temp = value(all[1], all[2], all[3], all[4], all[5]); if (temp > max) max = temp;
	temp = value(all[1], all[2], all[3], all[4], all[6]); if (temp > max) max = temp;
	temp = value(all[1], all[2], all[3], all[5], all[6]); if (temp > max) max = temp;
	temp = value(all[1], all[2], all[4], all[5], all[6]); if (temp > max) max = temp;
	temp = value(all[1], all[3], all[4], all[5], all[6]); if (temp > max) max = temp;
	temp = value(all[2], all[3], all[4], all[5], all[6]); if (temp > max) max = temp;
	return max;
}


  int div4[56] = {0,0,0,0, 1,1,1,1, 2,2,2,2, 3,3,3,3, 4,4,4,4, 5,5,5,5, 6,6,6,6, 7,7,7,7,
                  8,8,8,8, 9,9,9,9, 10,10,10,10, 11,11,11,11, 12,12,12,12, 13,13,13,13};

  int mod4[56] = {0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3,
                  0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3, 0,1,2,3};

  // static final int mul1 = 1, mul2 = mul1 * 15, mul3 = mul2 * 15, mul4 = mul3 * 15, mul5 = mul4 * 15;
  long mult[6][14];
  long mul[6] = {0, 1, 15, 225, 3375, 50625};

  // calculate the value of a hand according all rules
  // cards that are given as input here should be sorted in desceding order!
  long value(int x1, int x2, int x3, int x4, int x5) {
    int z0 = div4[x1], z1 = div4[x2], z2 = div4[x3], z3 = div4[x4], z4 = div4[x5];
    int c0 = mod4[x1], c1 = mod4[x2], c2 = mod4[x3], c3 = mod4[x4], c4 = mod4[x5];
    boolean flush = (c0 == c1 && c1 == c2 && c2 == c3 && c3 == c4);

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
      return 10000000 + z0; // only highest card matters, rest are the same
    }

    // 3. four of a kind
    if (diff == 1 && z1 == z2 && z2 == z3) {
        if (z0 != z1) return 8000000 + mult[5][z1] + z0;
           else return 8000000 + mult[5][z1] + z4;
    }

    // 4. full hause
    if (diff == 1) {
      if (z0 == z2) { // aaabb
        return 7000000 + mult[5][z2] + z4;
      } else {
        return 7000000 + mult[5][z2] + z0;
      }
    }

    // 5. Flush
    if (flush) {
      return 6000000 + mult[5][z0] + mult[4][z1] + mult[3][z2] + mult[2][z3] + mult[1][z4];     // all card matters
    }

    // 6. Straight
    if (straight) {
      if (z0 == 12 && z1 == 3) return 5000000 + z1; // this is 5-high straight A 5 4 3 2
      return 5000000 + z0; // Only the highest card matters, rest are ignored
    }

    // 7. Three of a kind
    if (diff == 2) {
      // aaabc, abbbc, abccc
      if (z2 == z0) return 4000000 + mult[5][z2] + mult[2][z3] + z4;
      if (z2 == z4) return 4000000 + mult[5][z2] + mult[2][z0] + z1;
      if (z1 == z3) return 4000000 + mult[5][z2] + mult[2][z0] + z4;
    }

    // 8. Two pairs
    if (diff == 2) {
      // aabbc, aabcc, abbcc
      if (z0 == z1) {
        if (z2 == z3) return 3000000 + mult[5][z0] + mult[3][z2];
        if (z3 == z4) return 3000000 + mult[5][z0] + mult[3][z3];
      }
      if (z1 == z2 && z3 == z4) return 3000000 + mult[5][z1] + mult[3][z3];
    }

   // 9. One pair
    if (diff == 3) {
      // aabcd, baacd, bcaad, bcdaa
      if (z0 == z1) return 2000000 + mult[5][z0] + mult[3][z2] + mult[2][z3] + mult[1][z4];
      if (z1 == z2) return 2000000 + mult[5][z1] + mult[3][z0] + mult[2][z3] + mult[1][z4];
      if (z2 == z3) return 2000000 + mult[5][z2] + mult[3][z0] + mult[2][z1] + mult[1][z4];
      if (z3 == z4) return 2000000 + mult[5][z3] + mult[3][z0] + mult[2][z1] + mult[1][z2];
    }

    // 10. High card
    return mult[5][z0] + mult[4][z1] + mult[3][z2] + mult[2][z3] + mult[1][z4];   // all card matters
  }

 int initialize() {
   for (int i = 0; i<6; i++) for (int z = 0; z<14; z++) mult[i][z] = mul[i] * z;
 }


__int64 start_count = 0;
__int64 freq = 0;

float time()
{
    __int64 end_count;
    QueryPerformanceCounter((LARGE_INTEGER*)&end_count);
    return (float)(end_count - start_count) / (float)freq;
}

int testPokerEval() {
	initialize();
    unsigned long int n = 0;
    int hand[7]; 
    QueryPerformanceFrequency((LARGE_INTEGER*)&freq);
    QueryPerformanceCounter((LARGE_INTEGER*)&start_count);
    float startTime = time();
    unsigned long int sum = 0, ss = 0;

    for (int h1 = 0; h1 < 13; h1++)
      for (int h2 = 0; h2 < 13; h2++) 
        for (int h3 = 0; h3 < 13; h3++)
          for (int h4 = 0; h4 < 13; h4++)
            for (int h5 = 0; h5 < 13; h5++)  if (h4 != h1)
              for (int h6 = 0; h6 < 13; h6++) if (h5 != h2)
                for (int h7 = 0; h7 < 13; h7++) if (h7 != h3) 
                                        {
										  hand[0] = h1*4;
										  hand[1] = h2*4+1;
										  hand[2] = h3*4+2;
										  hand[3] = h4*4+3;
										  hand[4] = h5*4+1;
										  hand[5] = h6*4+2;
										  hand[6] = h7*4+3;
										  n++;
										  ss = defineHand(hand, 7);
										  sum += ss;
                                          //printf("%ld \n", ss); 
                                        }

    printf("%ld\n", sum); 
    float totalTime = (time() - startTime);
    float handsPerSec = n / totalTime;
    printf("Poker Eval Test\n"); 
    printf("%ld hands in %f seconds\n", n, totalTime); 
    printf(" --- Hands per second: %f \n\n", handsPerSec);
}

int main() {
  testPokerEval();
}
