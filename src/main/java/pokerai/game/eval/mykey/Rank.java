package pokerai.game.eval.mykey;

public final class Rank
{

	private static final char[] RANKS = new char[]{'-', '-', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'};
	private static final String[] HANDTYPE = new String[]{"Bad", "High Card", "One Pair", "Two Pair", "Trips", "Straight",
		"Flush", "Full House", "Quads", "Straight Flush"};
	
	public static final String rankToString(int rank) {
		int type = rank >> 20;
		StringBuilder sb = new StringBuilder().append(HANDTYPE[type]);
		sb.append(" ").append(RANKS[((rank >> 16) & 0xf)])
			.append(" ").append(RANKS[((rank >> 12) & 0xf)])
			.append(" ").append(RANKS[((rank >> 8) & 0xf)])
			.append(" ").append(RANKS[((rank >> 4) & 0xf)])
			.append(" ").append(RANKS[(rank & 0xf)]);
		return sb.toString();
	}
	
	/*
	 * return value is an int; 32 bits = 0x0VTBKKKK
	 * where each letter refers to a 4-bit nybble:<pre>
	 * V nybble = category code
	 * T nybble = rank (2..14) of top pair for two pair, 0 otherwise
	 * B nybble = rank (2..14) of quads or trips (including full house trips),
	 *	or rank of high card (5..14) in straight or straight flush,
	 *	or rank of bottom pair for two pair (hence the symbol "B"),
	 *	or rank of pair for one pair,
	 *	or 0 otherwise
	 * KKKK mask = 16-bit mask with...
	 *	5 bits set for no pair or (non-straight-)flush
	 *	3 bits set for kickers with pair,
	 *	2 bits set for kickers with trips,
	 *	1 bit set for pair within full house or kicker with quads or kicker with two pair
	 *	or 0 otherwise</pre>
	 */
	public static final int rankToBrecher(int rank, boolean exp) {
		int c1, c2, c3, c4, c5;
		int type = rank >> 20;
		c1 = (rank >> 16) & 0xf;
		c2 = (rank >> 12) & 0xf;
		c3 = (rank >> 8) & 0xf;
		c4 = (rank >> 4) & 0xf;
		c5 = rank & 0xf;
		if (exp) {
		    switch (type) {
			case 1 : 
			case 6 : {
				return (1 << (c1-2)) | (1 << (c2-2)) | (1 << (c3-2)) | (1 << (c4-2)) | (1 << (c5-2)) | ((type - 1) << 24);
			}
			case 2 : {
				return (1 << (c3-2)) | (1 << (c4-2)) | (1 << (c5-2)) | (c2 << 16) | ((type - 1) << 24);
			}
			case 3 : {
				return (1 << (c5-2)) | (c4 << 16) | (c3 << 20) | ((type - 1) << 24);
			}
			case 4 : {
				return (1 << (c4-2)) | (1 << (c5-2)) | (c3 << 16) | ((type - 1) << 24);
			}
			case 5 : 
			case 9 : {
				return (c1 << 16) | ((type - 1) << 24);
			}
			case 7 : 
			case 8 : {
				return (1 << (c5-2)) | (c4 << 16) | ((type - 1) << 24);
			}
		    }
		} else {
		    switch (type) {
			case 1 : 
			case 6 : {
				return (1 << (c1-2)) | (1 << (c2-2)) | (1 << (c3-2)) | (1 << (c4-2)) | (1 << (c5-2)) | ((type - 1) << 24);
			}
			case 2 : {
				return (1 << (c2-2)) | (1 << (c3-2)) | (1 << (c4-2)) | (c1 << 16) | ((type - 1) << 24);
			}
			case 3 : {
				return (1 << (c3-2)) | (c2 << 16) | (c1 << 20) | ((type - 1) << 24);
			}
			case 4 : {
				return (1 << (c2-2)) | (1 << (c3-2)) | (c1 << 16) | ((type - 1) << 24);
			}
			case 5 : 
			case 9 : {
				return (c1 << 16) | ((type - 1) << 24);
			}
			case 7 : 
			case 8 : {
				return (1 << (c2-2)) | (c1 << 16) | ((type - 1) << 24);
			}
		    }
		}
		return 0;
	}
}