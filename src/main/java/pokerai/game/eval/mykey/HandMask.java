package pokerai.game.eval.mykey;

public final class HandMask {

	public static final long[] HandMasksTable = new long[] { 
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

	public static final String[] CardStringTable = new String[] {
		"2C", "3C", "4C", "5C", "6C", "7C", "8C", "9C", "TC", "JC", "QC", "KC", "AC",
		"2D", "3D", "4D", "5D", "6D", "7D", "8D", "9D", "TD", "JD", "QD", "KD", "AD",
		"2H", "3H", "4H", "5H", "6H", "7H", "8H", "9H", "TH", "JH", "QH", "KH", "AH",
		"2S", "3S", "4S", "5S", "6S", "7S", "8S", "9S", "TS", "JS", "QS", "KS", "AS"
	};

	public static final String maskToString(long handMask) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		String delim = "";
		int i = 0;
		for(long mask : HandMasksTable) {
			if((handMask & mask) != 0) {
				sb.append(delim).append(CardStringTable[i]);
				delim = " ";
			}
			i++;
		}
		return sb.append("]").toString();
	}

	public static final long stringToMask(String hand) {
		long mask = 0;
		int i = 0;
		for(String card : CardStringTable) {
			if(hand.indexOf(card) != -1) {
				mask |= HandMasksTable[i];
			}
			i++;
		}
		return mask;
	}
}