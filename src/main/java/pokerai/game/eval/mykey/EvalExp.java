package pokerai.game.eval.mykey;

/**
 * Some experimental opitimizations. Only works with 7 cardhands
 */
public final class EvalExp
{
	public static final int HIGH_FLAG = 	0x100000;
	public static final int PAIR_FLAG = 	0x200000;
	public static final int TWOPAIR_FLAG = 	0x300000;
	public static final int TRIP_FLAG = 	0x400000;
	public static final int STRAIGHT_FLAG = 0x500000;
	public static final int FLUSH_FLAG = 	0x600000;
	public static final int HOUSE_FLAG = 	0x700000;
	public static final int QUAD_FLAG = 	0x800000;
	public static final int STRFLUSH_FLAG = 0x900000;
	
	// StraightTop return values for straights and higcard hands
	// for other hands it return number of cards in p1
	private static final int[] StraightTop = new int[8129];
	private static final int[] Flush = new int[8129];
	private static final char[] Top1 = new char[8129];
	private static final char[] Top1_4 = new char[8129];
	private static final char[] Top2 = new char[8129];
	private static final char[] Top2_4 = new char[8129];
	private static final char[] Top3 = new char[8129];
	private static final char[] Top1_12 = new char[8129];
	private static final char[] Top1_8 = new char[8129];
	private static final char[] Bit1 = new char[8129];
	private static final char[] Bit2 = new char[8129];
	
	public static final int rankHand(long hand)
	{
		int p1, p2, p3, p4;
		int s, h, d, c;
		int cards;
		
		c = (int)(hand & 0x1fff);
		d = (int)((hand >> 13) & 0x1fff);
		h = (int)((hand >> 26) & 0x1fff);
		s = (int)(hand >> 39);
		
		
		
		if ((Flush[s] | Flush[h] | Flush[d] | Flush[c]) != 0)
		{
			return Flush[s] | Flush[h] | Flush[d] | Flush[c];
		}
		
/*		
		p2 = s & h | ((s | h) & d);
		p3 = s & h & d | (p2 & c);
		p2 |= (p1 & c);
		p1 |= c;
*/		

		p1 = s | h;
		p2 = s & h;
		p3 = p2 & d; p2 |= (p1 & d); p1 |= d;
		p4 = p3 & c; p3 |= (p2 & c); p2 |= (p1 & c); p1 |= c;
		
		if ((cards = StraightTop[p1]) > 6)
		{
			return cards;
		}
		if (p3 == 0) // There are pairs but no triplets
		{
			if (cards == 6) {
				// we have exactly 1 card in p2
				return PAIR_FLAG | Top1_12[p2] | Top3[p1 ^ p2];
			}
			if (cards == 5) {
				// we have exactly 2 card in p2
				return TWOPAIR_FLAG | Top2_4[p2] | Top1[p1 ^ p2];
			}
			// we have 3 cards in p2 so we need to use Bit2 to get top 2 bits
			return TWOPAIR_FLAG | Top2_4[p2] | Top1[p1 ^ Bit2[p2]];
		}
		if (p4 == 0) // Deal with trips/sets/boats
		{
			if (cards == 5) {
				// p1 contains 5 cards so we have trips and p3 contains exactly 1 card
				return TRIP_FLAG | Top1_8[p3] | Top2[p1 ^ p3];
			}
			if (cards == 4) {
				// p1 contains 4 cards so we have 2 cards in p2 and 1 in p3
				return HOUSE_FLAG | Top1_4[p3] | Top1[p2 ^ p3];
			}
			// handle rest of full house hands
			return HOUSE_FLAG | Top1_4[p3] | Top1[p2 ^ Bit1[p3]];
		}
		
		// Only hands left are quads
		return QUAD_FLAG | Top1_4[p4] | Top1[p1 ^ p4];
	}
		
	static {
		int i, c1, c2, c3, c4, c5, c6, c7;
		
		for (c5 = 14; c5 > 4; c5--) {
			c4 = c5-1; 
			c3 = c4-1; 
			c2 = c3-1; 
			c1 = c2-1;
			if (c1 == 1)
			{
				c1 = 14;
			}
			for (c6 = 14; c6 > 1; c6--) {
				if (c6 != c5+1) {
					for (c7 = c6-1; c7 > 1; c7--) {
						if (c7 != c5+1)
						{
							i = ((1 << c1) | (1 << c2) | (1 << c3) | (1 << c4) | (1 << c5) | (1 << c6) | (1 << c7)) >> 2;
							Flush[i] = STRFLUSH_FLAG | (c5 << 16) | (c4 << 12) | (c3 << 8) | (c2 << 4) | c1;
							StraightTop[i] = STRAIGHT_FLAG | (c5 << 16) | (c4 << 12) | (c3 << 8) | (c2 << 4) | c1;
						}
					}
				}
			}
		}
		for (c1 = 14; c1 > 1; c1--) {
		  for (c2 = c1; c2 > 1; c2--) {
		    for (c3 = c2; c3 > 1; c3--) {
		      for (c4 = c3; c4 > 1; c4--) {
		        for (c5 = c4; c5 > 1; c5--) {
		          for (c6 = c5; c6 > 1; c6--) {
		            for (c7 = c6; c7 > 1; c7--) {
				i = ((1 << c1) | (1 << c2) | (1 << c3) | (1 << c4) | (1 << c5) | (1 << c6) | (1 << c7)) >> 2;
				if (c2 < c1) {
					if (c3 < c2) {
						if (c4 < c3 && c5 < c4) {
							if (c6 < c5 && c7 < c6 && StraightTop[i] == 0) {
								StraightTop[i] = HIGH_FLAG | (c1 << 16) | (c2 << 12) | (c3 << 8) | (c4 << 4) | c5;
							}
							if (Flush[i] == 0) {
								Flush[i] = FLUSH_FLAG | (c1 << 16) | (c2 << 12) | (c3 << 8) | (c4 << 4) | c5;
							}
						}
						Top3[i] = (char)((c1 << 8) | (c2 << 4) | c3);
					}
					Top2_4[i] = (char)((c1 << 8) | (c2 << 4));
					Top2[i] = (char)((c1 << 4) | c2);
					Bit2[i] = (char)((1 << (c1-2)) | (1 << (c2-2)));
				}
				Top1_12[i] = (char)(c1 << 12);
				Top1_8[i] = (char)(c1 << 8);
				Top1_4[i] = (char)(c1 << 4);
				Top1[i] = (char) c1;
				Bit1[i] = (char)(1 << (c1-2));
		            }
		          }
		        }
		      }
		    }
		  }
		}
		for (i = StraightTop.length-1; i >= 0; i--) {
			if (StraightTop[i] == 0 && (Integer.bitCount(i) < 7)) {
				StraightTop[i] = Integer.bitCount(i);
			}
		}
	}
	
}
