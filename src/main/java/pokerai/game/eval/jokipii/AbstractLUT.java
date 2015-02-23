package pokerai.game.eval.jokipii;

/**
 * Basic functionality for all LUT classes
 * @author Antti Jokipii
 * @date 12th March 2010
 */
public abstract class AbstractLUT {

	/**
	 * If set generated LUT is compressed, if not set generated LUT is uncompressed.
	 */
	public final static int MODE_COMPRESS		= 1;
	/**
	 * If set card order is changed to original (2c,2d,2h,...Ah,As)
	 * If not set card order is (2c,3c,4c,...Ks,As)
	 * If set normal overlap compression doesn't work.
	 */
	public final static int MODE_ORIG_CARD_ORDER	= 2;
	/**
	 * If set tables are split to multiple tables, if not one big table is generated
	 */
	public final static int MODE_SPLIT			= 4;
	/**
	 * If set outputs 5 and 6 card hand ranktables.
	 */
	public final static int MODE_INCLUDE56		= 8;
	/**
	 * If set generated LUT is for omaha high evaluations
	 */
	public final static int MODE_OMAHA			= 16;
	/**
	 * if set compress handIndex5 and handIndex6 with suit and ranks separated
	 */
	public final static int MODE_2STEP			= 32;
	/**
	 * if set handIndex5 is compressed with 2step mode and handIndex6 and handRanks7 with suitinformation from handindex5
	 * Also original card order is used, but not 1 based index.
	 */
	public final static int MODE_NOSUIT_COMPRESS	= 64;
	/**
	 * if set card values starts from 1, if not card values start from 0.
	 * If set normal overlap compression doesn't work.
	 */
	public final static int MODE_1BASED_INDEX	= 128;
	

	protected static boolean verbose		= true;				// toggles verbose mode
	protected static boolean debug 		= true;				// toggles debug mode
	protected static int     mode			= MODE_COMPRESS;		// selects mode, default is MODE_COMPRESS
	
	public static int[]   handRanks; 							// array to hold hand rank lookup table
	public static int[]   handIndex1, handIndex2, handIndex3, handIndex4, handIndex5, handIndex6;  // split array
	public static char[]  handRank7, handRank6, handRank5;			// split array hand rank values
	
	
	/**
	 * Return true if generated splitted tables includes
	 * handRank6 and handRank5 tables.
	 */
	public static final boolean is56Included() {
		return ((mode & MODE_INCLUDE56) != 0);
	}
	
	/**
	 * Return true if generated keys are splitted to multipe tables:
	 * First level index to handIndex1,
	 * 2nd level index to handIndex2,
	 * 3rd level index to handIndex3,
	 * 4th level index to handIndex4,
	 * 5th level index to handIndex5,
	 * 6th level index to handIndex6, 
	 * and actual ranks to handRank7.
	 */
	public static final boolean isSplitted() {
		return ((mode & MODE_SPLIT) != 0);
	}
	
	/**
	 * Return true if compressed
	 */
	public static final boolean isCompressed() {
		return ((mode & MODE_COMPRESS) != 0);
	}
	
	/**
	 * Return true if not compressed
	 */
	public static final boolean isNotCompressed() {
		return ((mode & MODE_COMPRESS) == 0);
	}
	
	/**
	 * Return true if uses original card order ie.
	 * Card to integer conversions:
   	 * 2c =  1    2d =  2    2h =  3    2s =  4
   	 * 3c =  5    3d =  6    3h =  7    3s =  8
   	 * 4c =  9    4d = 10    4h = 11    4s = 12
   	 * 5c = 13    5d = 14    5h = 15    5s = 16
   	 * 6c = 17    6d = 18    6h = 19    6s = 20
   	 * 7c = 21    7d = 22    7h = 23    7s = 24
   	 * 8c = 25    8d = 26    8h = 27    8s = 28
   	 * 9c = 29    9d = 30    9h = 31    9s = 32
   	 * Tc = 33    Td = 34    Th = 35    Ts = 36
   	 * Jc = 37    Jd = 38    Jh = 39    Js = 40
   	 * Qc = 41    Qd = 42    Qh = 43    Qs = 44
   	 * Kc = 45    Kd = 46    Kh = 47    Ks = 48
   	 * Ac = 49    Ad = 50    Ah = 51    As = 52
	 * <p>
	 */
	public static final boolean isOriginalCardOrder() {
		return ((mode & MODE_ORIG_CARD_ORDER) != 0);
	}
	
	public static final boolean isOmaha() {
		return ((mode & MODE_OMAHA) != 0);
	}
	
	public static final boolean is2Step() {
		return ((mode & MODE_2STEP) != 0);
	}
	
	public static final boolean isNosuitCompression() {
		return ((mode & MODE_NOSUIT_COMPRESS) != 0);
	}
	
	public static final boolean is1basedIndex() {
		return ((mode & MODE_1BASED_INDEX) != 0);
	}
	
	/**
	 * Set debug mode (default off)
	 * @param value
	 */
	public static final void setDebug(boolean value) {
		debug = value;
	}
	
	/**
	 * Return current debug state
	 */
	public static final boolean isDebug() {
		return debug;
	}
	
	/**
	 * Set verbose mode (default on)
	 * @param value
	 */
	public static final void setVerbose(boolean value) {
		verbose = value;
	}
	
	/**
	 * Return current verbose state
	 */
	public static final boolean isVerbose() {
		return verbose;
	}
	
	/**
	 * Set mode flags. All combinations are not allowed so method returns flags that are actually set.
	 *
	 * @param value
	 * @return mode flags that are setted
	 */
	public static final int setMode(int value) {
		mode = value;
		// check if there are flags that cannot be set same time and set those correctly
		if (isCompressed() && is1basedIndex()) {
			if (isOriginalCardOrder()) {
				mode &= ~MODE_ORIG_CARD_ORDER;
			}
			mode &= ~MODE_1BASED_INDEX;
		}
		if (!isSplitted() && is56Included()) {
			mode &= ~MODE_INCLUDE56;
		}
		if (isOmaha()) {
			mode |= MODE_SPLIT;
			mode |= MODE_COMPRESS;
			mode &= ~MODE_INCLUDE56;
			mode &= ~MODE_1BASED_INDEX;
			mode &= ~MODE_ORIG_CARD_ORDER;
			mode &= ~MODE_NOSUIT_COMPRESS;
		}
		if (is2Step()) {
			mode |= MODE_SPLIT;
			mode |= MODE_COMPRESS;
		}
		if (isNosuitCompression()) {
			mode |= MODE_SPLIT;
			mode |= MODE_COMPRESS;
		}
		return mode;
	}
	
	/**
	 * Return current mode flags.
	 */
	public static final int getMode() {
		return mode;
	}
	
}