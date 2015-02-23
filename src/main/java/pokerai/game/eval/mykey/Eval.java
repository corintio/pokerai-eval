package pokerai.game.eval.mykey;

public final class Eval 
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
	
	private static final int[] Flush = new int[8129];
	private static final int[] Straight = new int[8129];
	private static final int[] Top1_16 = new int[8129];
	private static final int[] Top1_12 = new int[8129];
	private static final int[] Top1_8 = new int[8129];
	private static final int[] Top2_12 = new int[8129];
	private static final int[] Top2_8 = new int[8129];
	private static final int[] Top3_4 = new int[8129];
	private static final int[] Top5 = new int[8129];
	private static final int[] Bit1 = new int[8129];
	private static final int[] Bit2 = new int[8129];
	
	public static final int rankHand(long hand)
	{
		int s, h, d, c;
		int p1, p2, p3, p4;
		
		s = (int)(hand & 0x1fffL);
		h = (int)((hand >> 13) & 0x1fffL);
		d = (int)((hand >> 26) & 0x1fffL);
		c = (int)((hand >> 39) & 0x1fffL);
		
		if ((Flush[s] | Flush[h] | Flush[d] | Flush[c]) != 0)
		{
			return Flush[s] | Flush[h] | Flush[d] | Flush[c];
		}
		
		p1 = s;
		p2 = p1 & h; p1 |=  h;
		p3 = p2 & d; p2 |= (p1 & d); p1 |= d;
		p4 = p3 & c; p3 |= (p2 & c); p2 |= (p1 & c); p1 |= c;
		
		if (Straight[p1] != 0)
		{
			return Straight[p1];
		}
		if (p2 == 0) // There are no pairs 
		{
			return Top5[p1];
		}
		if (p3 == 0) // There are pairs but no triplets
		{
			if (Bit2[p2] == 0)
			{
				return PAIR_FLAG | Top1_16[p2] | Top3_4[p1 ^ Bit1[p2]];
			}
			return TWOPAIR_FLAG | Top2_12[p2] | Top1_8[p1 ^ Bit2[p2]];
		}
		if (p4 == 0) // Deal with trips/sets/boats
		{
			if ((p2 > p3) || (p3 & (p3-1)) != 0)
			{
				return HOUSE_FLAG | Top1_16[p3] | Top1_12[p2 ^ Bit1[p3]];
			}
			return TRIP_FLAG | Top1_16[p3] | Top2_8[p1 ^ Bit1[p3]];
		}
		// Only hands left are quads
		return QUAD_FLAG | Top1_16[p4] | Top1_12[p1 ^ p4];
	}

	static {
		int i, c1, c2, c3, c4, c5, c6, c7;
		
		for (c1 = 14; c1 > 4; c1--) {
			c2 = c1-1;
			c3 = c2-1; 
			c4 = c3-1; 
			c5 = c4-1;
			if (c5 == 1)
			{
				c5 = 14;
			}
			for (c6 = 14; c6 > 1; c6--) {
				if (c6 != c1+1) {
					for (c7 = c6-1; c7 > 1; c7--) {
						if (c7 != c1+1)
						{
							i = (1 << c1) | (1 << c2) | (1 << c3) | (1 << c4) | (1 << c5) | (1 << c6) | (1 << c7);
							Flush[i >> 2] = STRFLUSH_FLAG | (c1 << 16) | (c2 << 12) | (c3 << 8) | (c4 << 4) | c5;
							Straight[i >> 2] = STRAIGHT_FLAG | (c1 << 16) | (c2 << 12) | (c3 << 8) | (c4 << 4) | c5;
						}
					}
				}
			}
		}
		for (c1 = 14; c1 > 5; c1--) {
			for (c2 = c1-1; c2 > 4; c2--) {
				for (c3 = c2-1; c3 > 3; c3--) {
					for (c4 = c3-1; c4 > 2; c4--) {
						for (c5 = c4-1; c5 > 1; c5--) {
							for (c6 = c5; c6 > 1; c6--) {
								for (c7 = c6; c7 > 1; c7--) {
									i = (1 << c1) | (1 << c2) | (1 << c3) | (1 << c4) | (1 << c5) | (1 << c6) | (1 << c7);
									if (Flush[i >> 2] == 0)
									{
										Flush[i >> 2] = FLUSH_FLAG | (c1 << 16) | (c2 << 12) | (c3 << 8) | (c4 << 4) | c5;
									}
									Top5[i >> 2] = HIGH_FLAG | (c1 << 16) | (c2 << 12) | (c3 << 8) | (c4 << 4) | c5;
								}
							}
						}
					}
				}
			}
		}
		
		for (c1 = 14; c1 > 3; c1--) {
			for (c2 = c1-1; c2 > 2; c2--) {
				for (c3 = c2-1; c3 > 1; c3--) {
					for (c4 = c3; c4 > 1; c4--) {
						for (c5 = c4; c5 > 1; c5--) {
							for (c6 = c5; c6 > 1; c6--) {
								for (c7 = c6; c7 > 1; c7--) {
									i = (1 << c1) | (1 << c2) | (1 << c3) | (1 << c4) | (1 << c5) | (1 << c6) | (1 << c7);
									Top3_4[i >> 2] = (c1 << 12) | (c2 << 8) | (c3 << 4);
								}
							}
						}
					}
				}
			}
		}
		
		for (c1 = 14; c1 > 2; c1--) {
			for (c2 = c1-1; c2 > 1; c2--) {
				for (c3 = c2; c3 > 1; c3--) {
					for (c4 = c3; c4 > 1; c4--) {
						for (c5 = c4; c5 > 1; c5--) {
							for (c6 = c5; c6 > 1; c6--) {
								for (c7 = c6; c7 > 1; c7--) {
									i = (1 << c1) | (1 << c2) | (1 << c3) | (1 << c4) | (1 << c5) | (1 << c6) | (1 << c7);
									Top2_12[i >> 2] = (c1 << 16) | (c2 << 12);
									Top2_8[i >> 2] = (c1 << 12) | (c2 << 8);
									Bit2[i >> 2] = (1 << (c1-2)) | (1 << (c2-2));
								}
							}
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
									i = (1 << c1) | (1 << c2) | (1 << c3) | (1 << c4) | (1 << c5) | (1 << c6) | (1 << c7);
									Top1_16[i >> 2] = (c1 << 16);
									Top1_12[i >> 2] = (c1 << 12);
									Top1_8[i >> 2] = (c1 << 8);
									Bit1[i >> 2] = (1 << (c1-2));
								}
							}
						}
					}
				}
			}
		}
	};
		
}
