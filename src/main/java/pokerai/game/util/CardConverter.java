package pokerai.game.util;

import java.util.Arrays;

/**
 * This class converts card formats used in evaluators to other card formats
 */
public class CardConverter {

	/*
	 * zeroBasedSuitFirst
	 * Format used: jokipii
	 * Card to integer conversions:
	 * 2c =  0	2d = 13	2h = 26	2s = 39
	 * 3c =  1	3d = 14	3h = 27	3s = 40
	 * 4c =  2	4d = 15	4h = 28	4s = 41
	 * 5c =  3	5d = 16	5h = 29	5s = 42
	 * 6c =  4	6d = 17	6h = 30	6s = 43
	 * 7c =  5	7d = 18	7h = 31	7s = 44
	 * 8c =  6	8d = 19	8h = 32	8s = 45
	 * 9c =  7	9d = 20	9h = 33	9s = 46
	 * Tc =  8	Td = 21	Th = 34	Ts = 47
	 * Jc =  9	Jd = 22	Jh = 35	Js = 48
	 * Qc = 10	Qd = 23	Qh = 36	Qs = 49
	 * Kc = 11	Kd = 23	Kh = 37	Ks = 50
	 * Ac = 12	Ad = 25	Ah = 38	As = 51
	 */
	
	/*
	 * oneBasedRankFirst
	 * Format used: jokipii, spears2p2, spears
	 * Card to integer conversions:
	 * 2c =  1	2d =  2	2h =  3	2s =  4
	 * 3c =  5	3d =  6	3h =  7	3s =  8
	 * 4c =  9	4d = 10	4h = 11	4s = 12
	 * 5c = 13	5d = 14	5h = 15	5s = 16
	 * 6c = 17	6d = 18	6h = 19	6s = 20
	 * 7c = 21	7d = 22	7h = 23	7s = 24
	 * 8c = 25	8d = 26	8h = 27	8s = 28
	 * 9c = 29	9d = 30	9h = 31	9s = 32
	 * Tc = 33	Td = 34	Th = 35	Ts = 36
	 * Jc = 37	Jd = 38	Jh = 39	Js = 40
	 * Qc = 41	Qd = 42	Qh = 43	Qs = 44
	 * Kc = 45	Kd = 46	Kh = 47	Ks = 48
	 * Ac = 49	Ad = 50	Ah = 51	As = 52
	 */

	/*
	 * zeroBasedRankFirst
	 * Format used: klaatu, indiana, alberta, supersonic, jpoker, hammer
	 * Card to integer conversions:
	 * 2c =  0	2d =  1	2h =  2	2s =  3
	 * 3c =  4	3d =  5	3h =  6	3s =  7
	 * 4c =  8	4d =  9	4h = 10	4s = 11
	 * 5c = 12	5d = 13	5h = 14	5s = 15
	 * 6c = 16	6d = 17	6h = 18	6s = 19
	 * 7c = 19	7d = 20	7h = 21	7s = 23
	 * 8c = 23	8d = 24	8h = 25	8s = 27
	 * 9c = 28	9d = 29	9h = 30	9s = 31
	 * Tc = 32	Td = 33	Th = 34	Ts = 35
	 * Jc = 36	Jd = 37	Jh = 38	Js = 39
	 * Qc = 40	Qd = 41	Qh = 42	Qs = 43
	 * Kc = 44	Kd = 45	Kh = 46	Ks = 47
	 * Ac = 48	Ad = 49	Ah = 50	As = 51
	 */
	
	/**
	 * bitSuitFirst
	 * Format used: brecher, mykey
	 */
	private static final long[] bitSuitFirst = new long[] { 
		0x0000000000000001L,
		0x0000000000000002L,
		0x0000000000000004L,
		0x0000000000000008L,
		0x0000000000000010L,
		0x0000000000000020L,
		0x0000000000000040L,
		0x0000000000000080L,
		0x0000000000000100L,
		0x0000000000000200L,
		0x0000000000000400L,
		0x0000000000000800L,
		0x0000000000001000L,
		0x0000000000002000L,
		0x0000000000004000L,
		0x0000000000008000L,
		0x0000000000010000L,
		0x0000000000020000L,
		0x0000000000040000L,
		0x0000000000080000L,
		0x0000000000100000L,
		0x0000000000200000L,
		0x0000000000400000L,
		0x0000000000800000L,
		0x0000000001000000L,
		0x0000000002000000L,
		0x0000000004000000L,
		0x0000000008000000L,
		0x0000000010000000L,
		0x0000000020000000L,
		0x0000000040000000L,
		0x0000000080000000L,
		0x0000000100000000L,
		0x0000000200000000L,
		0x0000000400000000L,
		0x0000000800000000L,
		0x0000001000000000L,
		0x0000002000000000L,
		0x0000004000000000L,
		0x0000008000000000L,
		0x0000010000000000L,
		0x0000020000000000L,
		0x0000040000000000L,
		0x0000080000000000L,
		0x0000100000000000L,
		0x0000200000000000L,
		0x0000400000000000L,
		0x0000800000000000L,
		0x0001000000000000L,
		0x0002000000000000L,
		0x0004000000000000L,
		0x0008000000000000L
	};
	
	/**
	 * conversion table from zeroBasedSuitFirst to oneBasedRankFirst 
	 */
	private static final int zeroBasedSuitFirstToOneBasedRankFirst[] = new int[] {
		1, 5, 9, 13, 17, 21, 25, 29, 33, 37, 41, 45, 49, 
		2, 6, 10, 14, 18, 22, 26, 30, 34, 38, 42, 46, 50, 
		3, 7, 11, 15, 19, 23, 27, 31, 35, 39, 43, 47, 51, 
		4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 52
	};
	
	/**
	 * conversion table from zeroBasedRankFirst to zeroBasedSuitFirst 
	 */
	private static final int zeroBasedRankFirstToZeroBasedSuitFirst[] = new int[] {
		0, 13, 26, 39,
		1, 14, 27, 40,
		2, 15, 28, 41,
		3, 16, 29, 42,
		4, 17, 30, 43,
		5, 18, 31, 44,
		6, 19, 32, 45,
		7, 20, 33, 46,
		8, 21, 34, 47,
		9, 22, 35, 48,
		10, 23, 36, 49,
		11, 24, 37, 50,
		12, 25, 38, 51
	};
	
	public static final int convertZeroBasedSuitFirstToZeroBasedRankFirst(int card) {
		return zeroBasedSuitFirstToOneBasedRankFirst[card] - 1;
	}
	
	public static final int convertZeroBasedSuitFirstToOneBasedRankFirst(int card) {
		return zeroBasedSuitFirstToOneBasedRankFirst[card];
	}
	
	public static final long convertZeroBasedSuitFirstToBitSuitFirst(int card) {
		return 0x1L << card;
	}
	
	public static final int convertOneBasedRankFirstToZeroBasedRankFirst(int card) {
		return card - 1;
	}
	
	public static final int convertOneBasedRankFirstToZeroBasedSuitFirst(int card) {
		return zeroBasedRankFirstToZeroBasedSuitFirst[card - 1];
	}
	
	public static final long convertOneBasedRankFirstToBitSuitFirst(int card) {
		return 0x1L << zeroBasedRankFirstToZeroBasedSuitFirst[card - 1];
	}
	
	public static final int convertZeroBasedRankFirstToZeroBasedSuitFirst(int card) {
		return zeroBasedRankFirstToZeroBasedSuitFirst[card];
	}
	
	public static final int convertZeroBasedRankFirstToOneBasedRankFirst(int card) {
		return card + 1;
	}
	
	public static final long convertZeroBasedRankFirstToBitSuitFirst(int card) {
		return 0x1L  << zeroBasedRankFirstToZeroBasedSuitFirst[card];
	}
	
	public static final int convertBitSuitFirstToZeroBasedSuitFirst(long card) {
		return Arrays.binarySearch(bitSuitFirst, card);
	}
	
	public static final int convertBitSuitFirstToZeroBasedRankFirst(long card) {
		return zeroBasedSuitFirstToOneBasedRankFirst[Arrays.binarySearch(bitSuitFirst, card)] - 1;
	}
	
	public static final int convertBitSuitFirstToOneBasedRankFirst(long card) {
		return zeroBasedSuitFirstToOneBasedRankFirst[Arrays.binarySearch(bitSuitFirst, card)];
	}
	
}
