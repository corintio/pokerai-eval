package pokerai.game.eval.jokipii;

import pokerai.game.eval.jokipii.cactuskev.CactusKev;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Generates LUTs
 * @author Antti Jokipii
 * @date 12th March 2010
 */
public final class LUTGenerator extends AbstractLUT {
	
	/* Card to integer conversions:
   		2c =  0    2d = 13    2h = 26    2s = 39
   		3c =  1    3d = 14    3h = 27    3s = 40
   		4c =  2    4d = 15    4h = 28    4s = 41
   		5c =  3    5d = 16    5h = 29    5s = 42
   		6c =  4    6d = 17    6h = 30    6s = 43
   		7c =  5    7d = 18    7h = 31    7s = 44
   		8c =  6    8d = 19    8h = 32    8s = 45
   		9c =  7    9d = 20    9h = 33    9s = 46
   		Tc =  8    Td = 21    Th = 34    Ts = 47
   		Jc =  9    Jd = 22    Jh = 35    Js = 48
   		Qc = 10    Qd = 23    Qh = 36    Qs = 49
   		Kc = 11    Kd = 23    Kh = 37    Ks = 50
   		Ac = 12    Ad = 25    Ah = 38    As = 51
	 */
	
	// converts new cards int values to original int values
	public static final int oldCards[] = new int[] {
		1, 5, 9, 13, 17, 21, 25, 29, 33, 37, 41, 45, 49, 
		2, 6, 10, 14, 18, 22, 26, 30, 34, 38, 42, 46, 50, 
		3, 7, 11, 15, 19, 23, 27, 31, 35, 39, 43, 47, 51, 
		4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 52,
	};
	
	private final static int NOSUIT		= 0;					// represent no suit
	private final static int CLUB		 	= 1;					// represent club suit
	private final static int DIAMOND		= 2;					// represent diamond suit
	private final static int HEART		= 3;					// represen heart suit
	private final static int SPADE		= 4;					// represent spade suit
	private final static int NUM_SUITS 	= 4;
	private final static int NUM_RANKS 	= 13;
	
	private final static int LEVEL1START	= 1;					// Start position of level1 states
	private final static int LEVEL2START	= LEVEL1START + 52;		// Start position of level2 states
	private final static int LEVEL3START	= LEVEL2START + 1326;	// Start position of level3 states
	private final static int LEVEL4START	= LEVEL3START + 22100;	// Start position of level4 states
	private final static int LEVEL5START	= LEVEL4START + 84448;	// Start position of level5 states
	private final static int LEVEL6START	= LEVEL5START + 152607;	// Start position of level6 states
	private static int LEVEL6END		= LEVEL6START + 279752;		// End position of level6 states
	private final static int SIZE			= 540287;//612978;			// lookup table size (states)
	private final static char[] RANKS = new char[]{'-', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A', 'X'};
	private final static char[] SUITS = new char[]{'x', 'c', 'd', 'h', 's'};
	
	private static int[]	hand			= new int[8];			// re-usable array to hold cards in a hand
	private static long[]	keys			= new long[SIZE];		// array to hold state key lookup table
	private static int[]	positions		= new int[SIZE];		// array to hold state positions
	private static int[]	splitPositions = new int[8];			// array to hold split positions
	private static HashMap<Long, Integer> ranks = new HashMap<Long, Integer>();	// cache for ranks
	private static int		numKeys		= 1;					// counter for number of defined keys in key array
	private static long		maxKey		= 0;					// holds current maximum key value
	private static int		numCards		= 0;					// re-usable counter for number of cards in a hand
	private static int		cardIndex		= 0;					// re-usable index for cards in a hands
	private static int		maxHandRankIndex = 0;
	
	
	/**
	 * Generates LUT and saves it to given file.
	 * @param file name of file where to save generated LUT
	 */
	public static void generateTables(String file) {
		long   startTimer = 0, stopTimer = 0;
		
		if (verbose) {
			System.out.print("\nGenerating and sorting keys...");
			startTimer = System.currentTimeMillis();
		}
		
		generateKeys();
		if (debug) {
			printKeyClasses();
		}
		
		if (verbose) {
			stopTimer = System.currentTimeMillis();
			System.out.printf("%35s %f seconds\n\n", "Time Required:", ((stopTimer - startTimer) / 1000.0));
			System.out.print("Generating position index...");
			startTimer = System.currentTimeMillis();
		}
		
		if(isCompressed()) {
			makeCompressedPositionIndex();
		}
		else {
			makeUncompressedPositionIndex();
		}
		
		if (verbose) {
			stopTimer = System.currentTimeMillis();
			System.out.printf("%35s %f seconds\n\n", "Time Required:", ((stopTimer - startTimer) / 1000.0));
			System.out.print("Generating hand ranks...");
			startTimer = System.currentTimeMillis();
		}
		
		if (isSplitted()) {
			makeSplitTables();
		}
		else {
			// we make one table
			makeHandRanksTable(0, LEVEL6END, handRanks);
		}
		
		if (verbose) {
			stopTimer = System.currentTimeMillis();
			System.out.printf("done.\n\n%35s %f seconds\n\n", "Time Required:", ((stopTimer - startTimer) / 1000.0));
		}
		
		
		if (isCompressed() && isSplitted() && debug){
			//searchEquivalenceOnCompressedSplittedHandranks();
		}
		
		saveTables(file);
		
	} // END generateTables method
	
	
	// fills keys[] table
	private static void generateKeys() {
		int keyIndex;
		// fill initial keys so binarysearch finds correct place for keys
		Arrays.fill(keys, 1, keys.length, Long.MAX_VALUE);
		// generate keys from 0 to LEVEL6START because we dont store last level keys
		for (keyIndex = 0; keyIndex < LEVEL4START; keyIndex++) {
			if (verbose) {
				if (keyIndex % (LEVEL6START / 100) == 0) System.out.println(keyIndex / (LEVEL6START / 100) + "% generated.");
			}
			for (int card = 1; card < 53; card++) {		  		// add a card to each previously calculated key
				long key = makeKey(keys[keyIndex], card);  		// create the new key
				insertKey(key);							// insert the new key into the key lookup table
			}
		}
		generateKeys(LEVEL4START, LEVEL5START);
		generateKeys(LEVEL5START, LEVEL6START);
		// we go and mark end position
		for (; keys[keyIndex] != Long.MAX_VALUE; keyIndex++);
		LEVEL6END = keyIndex;	// only needeed when we do new manual optimization
		if (verbose) System.out.printf("done.\n\n%35s %d\n", "Number of Keys Generated:", (keyIndex + 1));
	} // END generateKeys method
	
	
	// fills keys[] table but only call makeKey when necessary... work from level4 to level6
	private static void generateKeys(int start, int end) {
		int keyIndex, suitPosition, card;
		long key, tmpKey;
		for (keyIndex = start; keyIndex < end; keyIndex++) {
			if (verbose) {
				if (keyIndex % (LEVEL6START / 100) == 0) System.out.println(keyIndex / (LEVEL6START / 100) + "% generated.");
			}
			key = keys[keyIndex];
			// handle pure states
			if (isPure(key)) {
				// then we need 13 new states
				for (card = 1; card < 14; card++) {
					tmpKey = makeKey(key, card);
					insertKey(tmpKey);						// insert the new key into the key lookup table
				}
			}
			// this handles flushes that can fail
			else if (countSuit(key, NOSUIT) == 2) {
				// then we need 13 new states
				suitPosition = getSuit(key);
				for (card = 1 + suitPosition * 13; card < 14 + suitPosition * 13; card++) {
					tmpKey = makeKey(key, card);
					insertKey(tmpKey);						// insert the new key into the key lookup table
				}
			}
			// this handles flushes that can't fail with next card
			else {
				// then we need 26 new states
				suitPosition = getSuit(key);
				// 13 new with correct suit
				for (card = 1 + suitPosition * 13; card < 14 + suitPosition * 13; card++) {
					tmpKey = makeKey(key, card);
					insertKey(tmpKey);						// insert the new key into the key lookup table
				}
				// 13 new with other suit
				for (card = 1 + ((suitPosition == 0)? 1 : 0) * 13; card < 14 + ((suitPosition == 0)? 1 : 0) * 13; card++) {
					tmpKey = makeKey(key, card);
					insertKey(tmpKey);						// insert the new key into the key lookup table
				}
			}
		}	
	} // END generateKeys method
	
	
	// generates uncompressed position index
	private static void makeUncompressedPositionIndex() {
		if (isSplitted()) {
			handleUnhandledStates(53, 53, 0, LEVEL1START, 0);
			handleUnhandledStates(53, 53, LEVEL1START, LEVEL2START, 1);
			handleUnhandledStates(53, 53, LEVEL2START, LEVEL3START, 2);
			handleUnhandledStates(53, 53, LEVEL3START, LEVEL4START, 3);
			handleUnhandledStates(53, 53, LEVEL4START, LEVEL5START, 4);
			handleUnhandledStates(53, 53, LEVEL5START, LEVEL6START, 5);
			handleUnhandledStates(53, 53, LEVEL6START, LEVEL6END, 6);
		}
		else {
			// make positionIndex with 1-base values
			int currentIndex = handleUnhandledStates(53, 53, 0, LEVEL6END, 0);
			// we make normal size handRanks
			handRanks = new int[currentIndex];
			message(debug, "Level6 ends at: " + currentIndex);
		}
	} // END makeUncompressedPositionIndex method
	
	
	// generates compressed position index
	private static void makeCompressedPositionIndex() {
		int currentIndex;
		
		// if we are in split mode we need to go thru all levels and set indexes
		if (isSplitted()) {
			currentIndex = handleUnhandledStates(52, 52, 0, LEVEL1START, 0);
			currentIndex = handleUnhandledStates(52, 52, LEVEL1START, LEVEL2START, 1);
			currentIndex = handleUnhandledStates(52, 52, LEVEL2START, LEVEL3START, 2);
			currentIndex = handleUnhandledStates(52, 52, LEVEL3START, LEVEL4START, 3);
		}
		else {
			currentIndex = handleUnhandledStates(52, 52, 0, LEVEL4START, 0);
		}
		
		// compress level 4 and 5
		if (is2Step()) {
			currentIndex = compress2StepLevel(LEVEL4START, LEVEL5START, currentIndex, 4);
			currentIndex = compress2StepLevel(LEVEL5START, LEVEL6START, currentIndex, 5);
			currentIndex = compressLevel6(currentIndex);
		}
		else if (isNosuitCompression()) {
			currentIndex = compress2StepLevel(LEVEL4START, LEVEL5START, currentIndex, 4);
			currentIndex = compressNosuitLevel(LEVEL5START, LEVEL6START, currentIndex, 5);
			currentIndex = compressNosuitLevel(LEVEL6START, LEVEL6END, currentIndex, 6);
		}
		else {
			currentIndex = compressBasicLevel(LEVEL4START, LEVEL5START, currentIndex, 2, 4);
			currentIndex = compressBasicLevel(LEVEL5START, LEVEL6START, currentIndex, 3, 5);
			currentIndex = compressLevel6(currentIndex);
		}
		
		// create handRanks table of compressed size
		handRanks = new int[currentIndex];
	} // END makeUncompressedPositionIndex method
	
	
	// compress level 6
	private static int compressLevel6(int currentIndex) {
		int keyIndex;
		long key;

		// handle possible flush states
		for (keyIndex = LEVEL6START; keyIndex < LEVEL6END; keyIndex++) {
			key = keys[keyIndex];
			if (positions[keyIndex] == 0 && countSuit(key, DIAMOND) == 4) {
				// cdhsx as Xooo oXoo ooXo oooX oooo where o and X is 13 int sequence
				// now we can compress to oooXoooo where indexes are
				// s = s, h = s + 13, d = s + 26, c = s + 39, o = s + 52
				// currentIndex in not moved to the end of set because next set overlaps with current
				long tempKey = changeSuitToSuit(key, DIAMOND, SPADE);
				positions[findKey(tempKey)] = currentIndex;
				tempKey = changeSuitToSuit(key, DIAMOND, HEART);
				positions[findKey(tempKey)] = currentIndex + 13;
				positions[keyIndex] = currentIndex + 26;
				tempKey = changeSuitToSuit(key, DIAMOND, CLUB);
				positions[findKey(tempKey)] = currentIndex + 39;
				tempKey = changeSuitToSuit(key, DIAMOND, NOSUIT);
				positions[findKey(tempKey)] = currentIndex + 52;
				currentIndex += 65;
			}
		}
		// now we need to update currentIndex because inside loop we did not put it to correct position
		currentIndex += 39;
		// handle flush states
		for (keyIndex = LEVEL6START; keyIndex < LEVEL6END; keyIndex++) {
			key = keys[keyIndex];
			if (positions[keyIndex] == 0 && countSuit(key, DIAMOND) > 4) {
				// now we make following compression
				// cdhsx as Xooo oXoo ooXo oooX oooo where o and X is 13 int sequence
				// compressed to oXooXo where indexes are
				// d = d, c = d + 13, s = d + 26, h = d + 39
				// and update currentIndex = currentIndex + 91
				positions[keyIndex] = currentIndex;
				long tempKey = changeSuitToSuit(key, DIAMOND, CLUB);
				positions[findKey(tempKey)] = currentIndex + 13;
				tempKey = changeSuitToSuit(key, DIAMOND, SPADE);
				positions[findKey(tempKey)] = currentIndex + 26;
				tempKey = changeSuitToSuit(key, DIAMOND, HEART);
				positions[findKey(tempKey)] = currentIndex + 39;
				currentIndex += 91;
			}
		}
		currentIndex = handleUnhandledStates(currentIndex, 52, LEVEL6START, LEVEL6END, 6);
		// debug
		message(debug, "Level6 ends at: " + currentIndex);
		message(verbose, "done.\n\nNumber of positions Generated: " + (keyIndex + 1));
		return currentIndex;
	}
	
	
	// compresses level and returns currentIndex
	private static int compressNosuitLevel(int start, int end, int currentIndex, int position) {
		int keyIndex;
		long key;
		
		currentIndex = 26;
		for (keyIndex = start; keyIndex < end; keyIndex++) {
			key = keys[keyIndex];
			// handle pure states
			if (isPure(key)) {
				positions[keyIndex] = currentIndex;
				currentIndex += 26;
			}
			// handles flushes... any suit is ok
			else if (containsSuit(key, DIAMOND)) {
				positions[keyIndex] = currentIndex;
				currentIndex += 26;
			}
		}
		// save currentIndex to splitPositions
		splitPositions[position] = currentIndex;
		return currentIndex;
	}
	
	
	// compresses level and returns currentIndex
	private static int compress2StepLevel(int start, int end, int currentIndex, int position) {
		int keyIndex;
		long key;
		
		currentIndex = 17;
		for (keyIndex = start; keyIndex < end; keyIndex++) {
			key = keys[keyIndex];
			// handle pure states
			if (isPure(key)) {
				positions[keyIndex] = currentIndex;
				currentIndex += 17;
			}
			// handles flushes that can fail
			else if (countSuit(key, NOSUIT) == 2) {
				positions[keyIndex] = currentIndex;
				currentIndex += 17;
			}
			// handles flushes that can fail and contain 2 possible flush colors
			else if (countSuit(key, CLUB) == 2 || countSuit(key, DIAMOND) == 2 || countSuit(key, HEART) == 2 || countSuit(key, SPADE) == 2 ) {
				positions[keyIndex] = currentIndex;
				currentIndex += 4;
			}
			// handles flushes can't fail
			else {
				positions[keyIndex] = currentIndex;
				currentIndex += 30;
			}
		}
		// save currentIndex to splitPositions
		splitPositions[position] = currentIndex;
		if (isSplitted()) {
			currentIndex = 52;
		}
		return currentIndex;
	}
	
	
	// compresses level and returns currentIndex
	private static int compressBasicLevel(int start, int end, int currentIndex, int suitCount, int pos) {
		int keyIndex;
		long key;
		
		for (keyIndex = start; keyIndex < end; keyIndex++) {
			key = keys[keyIndex];
			// handle possible flush states where suit is diamond
			if (countSuit(key, DIAMOND) == suitCount) {
				if (noOtherSuits(key, DIAMOND)) {
					// now we make following compression cdhsx as Aooo oBoo ooCo oooD oooo
					// d and h compressed to oBooCo where indexes are
					// d = d, h = d + 26, and update currentIndex = currentIndex + 78
					positions[keyIndex] = currentIndex;
					long tempKey = changeSuitToSuit(key, DIAMOND, HEART);
					positions[findKey(tempKey)] = currentIndex + 26;
					currentIndex += 78;
					// c and s compressed to AoooD where indexes are
					// c = c, s = c + 13, and update currentIndex = currentIndex + 65
					tempKey = changeSuitToSuit(key, DIAMOND, CLUB);
					positions[findKey(tempKey)] = currentIndex;
					tempKey = changeSuitToSuit(key, DIAMOND, SPADE);
					positions[findKey(tempKey)] = currentIndex + 13;
					currentIndex += 65;
				}
				else if (containsSuit(key, CLUB)) {
					// now we make following compression cdhs as ABoo ABoo ooCD ooCD
					// d and h compressed to ABooCD where indexes are
					// d = d, h = d + 52, and update currentIndex = currentIndex + 78
					positions[keyIndex] = currentIndex;
					key = changeSuitToSuit(key, DIAMOND, HEART);
					key = changeSuitToSuit(key, CLUB, SPADE);
					positions[findKey(key)] = currentIndex + 26;
					currentIndex += 78;
				}
				else if (containsSuit(key, SPADE)) {
					// now we make following compression cdhs as AoCo oBoD AoCo oBoD
					// c and d compressed to AoCoBoD where indexes are
					// c = c, d = c + 39, and update currentIndex = currentIndex + 91
					positions[keyIndex] = currentIndex + 39;
					key = changeSuitToSuit(key, DIAMOND, CLUB);
					key = changeSuitToSuit(key, SPADE, HEART);
					positions[findKey(key)] = currentIndex;
					currentIndex += 91;
				}
			}
		}
		
		currentIndex = handleUnhandledStates(currentIndex, 52, start, end, pos);
		if (isSplitted()) {
			currentIndex = 52;
		}
		
		return currentIndex;
	} // END compressBasicLevel method
	
	
	// handles non previously handled states and save splitPosition
	private static int handleUnhandledStates(int index, int base, int start, int end, int position) {
 		int currentIndex = index;
		for (int keyIndex = start; keyIndex < end; keyIndex++) {
			if (positions[keyIndex] == 0) {
				positions[keyIndex] = currentIndex;
				currentIndex += base;
			}
		}
		// save currentIndex to splitPositions
		splitPositions[position] = currentIndex;
		return currentIndex;
	} // END handleUnhandledStates mathod
	
	
	// creates new slitted tables and generates content
	private static void makeSplitTables() {
		if (debug) {
			System.out.println("splitPositions");
			for (int i : splitPositions) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
		if (isNosuitCompression()) mode |= LUTEvaluator.MODE_ORIG_CARD_ORDER;
		handIndex1 = new int[splitPositions[0]];
		makeHandRanksTable(0, LEVEL1START, handIndex1);
		handIndex2 = new int[splitPositions[1]];
		makeHandRanksTable(LEVEL1START, LEVEL2START, handIndex2);
		handIndex3 = new int[splitPositions[2]];
		makeHandRanksTable(LEVEL2START, LEVEL3START, handIndex3);
		handIndex4 = new int[splitPositions[3]];
		makeHandRanksTable(LEVEL3START, LEVEL4START, handIndex4);
		if (isNosuitCompression()) mode &= ~LUTEvaluator.MODE_ORIG_CARD_ORDER;
		if (isOmaha()) {
			handRank5 = new char[splitPositions[4]];
			makeHandRanksTable(LEVEL4START, LEVEL5START, handRank5);
		}
		else {
			handIndex5 = new int[splitPositions[4]];
			handIndex6 = new int[splitPositions[5]];
			handRank7 = new char[splitPositions[6]];
			if (is2Step()) {
				make2StepHandRanksTable(LEVEL4START, LEVEL5START, handIndex5);
				make2StepHandRanksTable(LEVEL5START, LEVEL6START, handIndex6);
				makeHandRanksTable(LEVEL6START, LEVEL6END, handRank7);
			}
			else if (isNosuitCompression()) {
				make2StepHandRanksTable(LEVEL4START, LEVEL5START, handIndex5);
				makeNosuitHandRanksTable(LEVEL5START, LEVEL6START, handIndex6);
				makeNosuitHandRanksTable(LEVEL6START, LEVEL6END, handRank7);
			}
			else {
				makeHandRanksTable(LEVEL4START, LEVEL5START, handIndex5);
				makeHandRanksTable(LEVEL5START, LEVEL6START, handIndex6);
				makeHandRanksTable(LEVEL6START, LEVEL6END, handRank7);
				if (is56Included()) {
					handRank5 = new char[splitPositions[4]];
					makeHandRanksTable(LEVEL4START, LEVEL5START, handRank5);
					handRank6 = new char[splitPositions[5]];
					makeHandRanksTable(LEVEL5START, LEVEL6START, handRank6);
				}
			}
		}
	} // END makeSplitTables method
	
	
	// saves generated tables
	private static void saveTables(String file) {
		ObjectOutputStream outputStream = null;
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(file));
			outputStream.writeInt(mode);
			if (isSplitted()) {
				message(debug, "handIndex1 size " + handIndex1.length + " ints " + handIndex1.length * 4 + " bytes");
				message(debug, "handIndex2 size " + handIndex2.length + " ints " + handIndex2.length * 4 + " bytes");
				message(debug, "handIndex3 size " + handIndex3.length + " ints " + handIndex3.length * 4 + " bytes");
				message(debug, "handIndex4 size " + handIndex4.length + " ints " + handIndex4.length * 4 + " bytes");
				outputStream.writeObject(handIndex1);
				outputStream.writeObject(handIndex2);
				outputStream.writeObject(handIndex3);
				outputStream.writeObject(handIndex4);
				if (isOmaha()) {
					message(debug, "handRank5 size " + handRank5.length + " chars " + handRank5.length * 2 + " bytes");
					outputStream.writeObject(handRank5);
				}
				else {
					message(debug, "handIndex5 size " + handIndex5.length + " ints " + handIndex5.length * 4 + " bytes");
					message(debug, "handIndex6 size " + handIndex6.length + " ints " + handIndex6.length * 4 + " bytes");
					message(debug, "handRank7 size " + handRank7.length + " chars " + handRank7.length * 2 + " bytes");
					outputStream.writeObject(handIndex5);
					outputStream.writeObject(handIndex6);
					outputStream.writeObject(handRank7);
					if (is56Included()) {
						message(debug, "handRank6 size " + handRank6.length + " chars " + handRank6.length * 2 + " bytes");
						message(debug, "handRank5 size " + handRank5.length + " chars " + handRank5.length * 2 + " bytes");
						outputStream.writeObject(handRank6);
						outputStream.writeObject(handRank5);
					}
				}
			}
			else {
				message(debug, "handRanks size " + handRanks.length + " ints " + handRanks.length * 4 + " bytes");
				outputStream.writeObject(handRanks);
			}
			outputStream.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new Error(ioe);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				throw new Error(ioe);
			}
		}
	} // END makeSplitTables method
	
	
	// loops and fills given char table with actual rank values
	private static void makeHandRanksTable(int start, int end, char[] table) {
		int card, maxHandRankIndex, keyIndex;
		long key, tmpKey;
		for (keyIndex = start; keyIndex < end; keyIndex++) { 
			if (verbose) {
				if (keyIndex % (SIZE / 100) == 0) System.out.println(keyIndex / (SIZE / 100) + "% generated.");
			}
			key = keys[keyIndex];
			maxHandRankIndex = positions[keyIndex];							// get hand rank insertion index
			if (is1basedIndex()) {
				// we are not compress mode and index must be 1-based so we add it by 1
				maxHandRankIndex++;
			}
			for (card = 1; card < 53; card++) {
				tmpKey = makeKey(key, card);
				if (table[maxHandRankIndex] == 0) {
					table[maxHandRankIndex] = (char) getHandRank(tmpKey);		// populate hand rank lookup table with appropriate value
				}
				maxHandRankIndex++;
			}
		}
	}  // END makeHandRanksTable method
	
	
	// loops and fills given table
	private static void makeHandRanksTable(int start, int end, int[] table) {
		int card, maxHandRankIndex, keyIndex;
		long key, tmpKey;
		for (keyIndex = start; keyIndex < end; keyIndex++) { 
			if (verbose) {
				if (keyIndex % (SIZE / 100) == 0) System.out.println(keyIndex / (SIZE / 100) + "% generated.");
			}
			key = keys[keyIndex];
			maxHandRankIndex = positions[keyIndex];							// get hand rank insertion index
			if (is1basedIndex()) {
				// we are not compress mode and index must be 1-based so we add it by 1
				maxHandRankIndex++;
			}
			for (card = 1; card < 53; card++) {
				tmpKey = makeKey(key, card);
				if (numCards < 7) {
					table[maxHandRankIndex] = positions[findKey(tmpKey)];  	// if number of cards is < 7 insert key
				}
				else if (table[maxHandRankIndex] == 0) {
					table[maxHandRankIndex] = getHandRank(tmpKey);			// if number of cards is 7 insert hand rank
				}
				maxHandRankIndex++;
			}
			// we make uncompressed table so we add rank for 5 and 6 card hands on 0 position of state
			if ((isNotCompressed()) && (numCards == 6 || numCards == 7)) {
				// insert the hand rank into the hand rank lookup table
				table[positions[keyIndex]] = getHandRank(key);
			}
		}
	}  // END makeHandRanksTable method
	
	
	// loops and fills given table
	private static void make2StepHandRanksTable(int start, int end, int[] table) {
		int card, position, keyIndex, suitPosition;
		long key, tmpKey;
		for (keyIndex = start; keyIndex < end; keyIndex++) { 
			if (verbose) {
				if (keyIndex % (SIZE / 100) == 0) System.out.println(keyIndex / (SIZE / 100) + "% generated.");
			}
			key = keys[keyIndex];
			position = positions[keyIndex];
			// handle pure states
			if (isPure(key)) {
				// first 4-pointers
				table[position] = position + 4;
				table[position + 1] = position + 4;
				table[position + 2] = position + 4;
				table[position + 3] = position + 4;
				position += 4;
				// then 13-pointers
				for (card = 1; card < 14; card++) {
					tmpKey = makeKey(key, card);
					setHandRank(table, position, tmpKey, 5);
					position++;
				}
			}
			// this handles flushes that can fail
			else if (countSuit(key, NOSUIT) == 2) {
				// first 4-pointers
				suitPosition = getSuit(key);
				tmpKey = changeSuitToSuit(key, suitPosition + 1, NOSUIT);
				int noSuitPosition = positions[findKey(tmpKey)] + 4;
				table[position] = (suitPosition == 0)? position + 4 : noSuitPosition;
				table[position + 1] = (suitPosition == 1)? position + 4 : noSuitPosition;
				table[position + 2] = (suitPosition == 2)? position + 4 : noSuitPosition;
				table[position + 3] = (suitPosition == 3)? position + 4 : noSuitPosition;
				position += 4;
				// then 13-pointers
				for (card = 1 + suitPosition * 13; card < 14 + suitPosition * 13; card++) {
					tmpKey = makeKey(key, card);
					setHandRank(table, position, tmpKey, suitPosition);
					position++;
				}
			}
			// handles flushes that can fail and contain 2 possible flush colors
			else if (countSuit(key, CLUB) == 2 || countSuit(key, DIAMOND) == 2 || countSuit(key, HEART) == 2 || countSuit(key, SPADE) == 2 ){
				// first 4-pointers
				suitPosition = getSuit(key);
				int secondSuitPosition = getSecondSuit(key);
				tmpKey = changeSuitToSuit(key, suitPosition + 1, NOSUIT);
				int secondPosition = positions[findKey(tmpKey)] + 4;
				tmpKey = changeSuitToSuit(key, secondSuitPosition + 1, NOSUIT);
				int firstPosition = positions[findKey(tmpKey)] + 4;
				tmpKey = changeSuitToSuit(changeSuitToSuit(key, secondSuitPosition + 1, NOSUIT), suitPosition + 1, NOSUIT);
				int noSuitPosition = positions[findKey(tmpKey)] + 4;
				table[position] = (suitPosition == 0)? firstPosition : (secondSuitPosition == 0)? secondPosition : noSuitPosition;
				table[position + 1] = (suitPosition == 1)? firstPosition : (secondSuitPosition == 1)? secondPosition : noSuitPosition;
				table[position + 2] = (suitPosition == 2)? firstPosition : (secondSuitPosition == 2)? secondPosition : noSuitPosition;
				table[position + 3] = (suitPosition == 3)? firstPosition : (secondSuitPosition == 3)? secondPosition : noSuitPosition;
				position += 4;
			}
			// this handles flushes that can't fail with next card
			else {
				// first 4-pointers
				table[position] = position + 17;
				table[position + 1] = position + 17;
				table[position + 2] = position + 17;
				table[position + 3] = position + 17;
				suitPosition = getSuit(key);
				table[position + suitPosition] = position + 4;
				position += 4;
				// then 13-pointers with correct suit
				for (card = 1 + suitPosition * 13; card < 14 + suitPosition * 13; card++) {
					tmpKey = makeKey(key, card);
					setHandRank(table, position, tmpKey, suitPosition);
					position++;
				}
				// then 13-pointers with other suit
				for (card = 1 + ((suitPosition == 0)? 1 : 0) * 13; card < 14 + ((suitPosition == 0)? 1 : 0) * 13; card++) {
					tmpKey = makeKey(key, card);
					setHandRank(table, position, tmpKey, suitPosition);
					position++;
				}
			}
		}
	}  // END make2StepHandRanksTable method
	
	
	// method sets handRank to given table and alters value if nosuit compression is selected
	private static void setHandRank(int[] table, int position, long tmpKey, int value) {
		if (isNosuitCompression()) {
			// this corrects suited hands because only DIAMOND is used
			if (value != 5) {
				tmpKey = changeSuitToSuit(tmpKey, value + 1, DIAMOND);
			}
			int tmp = positions[findKey(tmpKey)];
			tmp = (tmp << 3) | value;
			table[position] = tmp;
		}
		else {
			table[position] = positions[findKey(tmpKey)];
		}
	} // END setHandRank method
	
	// return flush suit values from 0..3
	private static int getSuit(long key) {
		int suit = 0;
		if (containsSuit(key, CLUB)) {
			suit = CLUB - 1;
		}
		else if (containsSuit(key, DIAMOND)) {
			suit = DIAMOND - 1;
		} 
		else if (containsSuit(key, HEART)) {
			suit = HEART - 1;
		} 
		else if (containsSuit(key, SPADE)) {
			suit = SPADE - 1;
		} 
		return suit;
	}
	
	
	// return flush suit values from 0..3
	private static int getSecondSuit(long key) {
		int suit = 0;
		if (containsSuit(key, SPADE)) {
			suit = SPADE - 1;
		} 
		else if (containsSuit(key, HEART)) {
			suit = HEART - 1;
		} 
		else if (containsSuit(key, DIAMOND)) {
			suit = DIAMOND - 1;
		} 
		else if (containsSuit(key, CLUB)) {
			suit = CLUB - 1;
		}
		return suit;
	}
	
	
	// loops and fills given table
	private static void makeNosuitHandRanksTable(int start, int end, int[] table) {
		int card, position, keyIndex;
		long key, tmpKey;
		for (keyIndex = start; keyIndex < end; keyIndex++) { 
			if (verbose) {
				if (keyIndex % (SIZE / 100) == 0) System.out.println(keyIndex / (SIZE / 100) + "% generated.");
			}
			key = keys[keyIndex];
			position = positions[keyIndex];
			// handles all states that are needed
			if (isPure(key) || containsSuit(key, DIAMOND)) {
				for (card = 1; card < 27; card++) {
					tmpKey = makeKey(key, card);
					table[position] = positions[findKey(tmpKey)];
					position++;
				}
			}
		}
	}  // END makeNoSuitHandRanksTable method
	
	
	// loops and fills given table
	private static void makeNosuitHandRanksTable(int start, int end, char[] table) {
		int card, position, keyIndex;
		long key, tmpKey;
		for (keyIndex = start; keyIndex < end; keyIndex++) { 
			if (verbose) {
				if (keyIndex % (SIZE / 100) == 0) System.out.println(keyIndex / (SIZE / 100) + "% generated.");
			}
			key = keys[keyIndex];
			position = positions[keyIndex];
			// handles all states that are needed
			if (isPure(key) || containsSuit(key, DIAMOND)) {
				for (card = 1; card < 27; card++) {
					tmpKey = makeKey(key, card);
					table[position] = (char) getHandRank(tmpKey);
					position++;
				}
			}
		}
	}  // END makeNoSuitHandRanksTable method
	
	
	// Returns a key for the hand created by adding a new card to the hand 
	// represented by the given key. Returns 0 if new card already appears in hand.
	private static long makeKey(long baseKey, int newCard) {
		
		int[] suitCount = new int[NUM_SUITS + 1]; 	// number of times a suit appears in a hand
		int[] rankCount = new int[NUM_RANKS + 2];	// number of times a rank appears in a hand
		
		// extract the hand represented by the key value
		for (cardIndex = 0; cardIndex < 6; cardIndex++) {
			// hand[0] is used to hold the new card
			hand[cardIndex + 1] = (int)((baseKey >>> (cardIndex << 3)) & 0xFF);
		}
		hand[0] = formatCard8bit(newCard);
		
		// examine the hand to determine number of cards and rank/suit counts
		// and impossible hands
		for (numCards = 0; hand[numCards] != 0; numCards++) {
			// check to see if new card is already contained in hand (rank and suit considered)
			if (numCards != 0 && hand[0] == hand[numCards]) {
				return 0;
			}
			
			// check to see if we already have four of a particular rank
			if ((++rankCount[(hand[numCards] >>> 4) & 0xF]) > 4) {
				return 0;
			}
			
			suitCount[hand[numCards] & 0xF]++;
		}
		
		// determine the minimum number of suits required for a flush to be possible
		int minSuitCount = numCards - 2;
		
		// check to see if suit is significant
		// current version just removes insignificant ranks from ready made flushes
		if (minSuitCount > 1) {
			// examine each card in the hand
			for (cardIndex = 0; cardIndex < numCards; cardIndex++) {
				// if the suit is not significant then strip it from the card
				if (suitCount[hand[cardIndex] & 0xF] < minSuitCount) {
					hand[cardIndex] &= 0xF0;
				}
			}
		}
		
		checkKey(suitCount, rankCount);
		return encodeKey();
	} // END makeKey method
	
	
	// finds a key from the key array and returns it index.
	private static int findKey(long key) {
		int position = Arrays.binarySearch(keys, key);
		assert (position >= 0) : key; // this cannot happen if keys are correctly created
		return position;
	} // END findKey method
		
	
	// Inserts a key into the key array and returns the insertion index.
	private static int insertKey(long key) {
		int position = Arrays.binarySearch(keys, key);
		if (position < 0) {
			// key does not exist so must be inserted
			position = -position - 1;
			System.arraycopy(keys, position, keys, position + 1, numKeys - position);
			keys[position] = key;
			numKeys++;
		}
		return position;
	} // END insertKey method
	
	
	// checks and corrects insignificant ranks from hand[]
	// current version may not handle all cases
	private static void checkKey(int[] suitCount, int[] rankCount) {
		if (numCards == 6) {
			sortHand();
			int firstRank = (hand[0] >>> 4);
			int secondRank = (hand[1] >>> 4);
			// this removes insignificant ranks from ready made 5 card flushes
			if (suitCount[0] != 5  && ((suitCount[hand[0] & 0xF] == 5) || (suitCount[hand[1] & 0xF] == 5))) {
				// examine each card in the hand
				for (cardIndex = 0; cardIndex < numCards; cardIndex++) {
					// if the rank is not significant then change the card to Xx
					if (suitCount[hand[cardIndex] & 0xF] < 4) {
						hand[cardIndex] = (14 << 4);
						break;
					}
				}
			}
			// 4 of kind with low kicker
			else if (rankCount[secondRank] == 4) {
				hand[5] = (14 << 4);
			}
			// 4 of kind with high kicker
			else if (rankCount[(hand[5] >>> 4)] == 4) {
				hand[1] = (14 << 4);
			}
			// fullhouses with kicker lower than pair
			else if (rankCount[firstRank] == 3 && rankCount[(hand[3] >>> 4)] == 2) {
				hand[5] = (14 << 4);
			}
			// fullhouses with kicker lower than pair
			else if ((rankCount[firstRank] == 2) && (rankCount[(hand[2] >>> 4)] == 3)) {
				hand[5] = (14 << 4);
			}
			// fullhouses with kicker (middle) lower than pair
			else if ((rankCount[firstRank] == 2) && (rankCount[(hand[3] >>> 4)] == 3)) {
				hand[2] = (14 << 4);
			}
			// strait with top pair
			else if ((rankCount[firstRank] == 2) && (((firstRank - (hand[5] >>> 4)) == 4) && ((firstRank - (hand[4] >>> 4)) == 3) 
					&& ((firstRank - (hand[3] >>> 4)) == 2) && ((firstRank - (hand[2] >>> 4)) == 1))) {
				hand[1] = (14 << 4);
			}
			// strait AA5432
			else if ((rankCount[firstRank] == 2) && ((firstRank == 13) && ((hand[5] >>> 4)  == 1) && ((hand[4] >>> 4) == 2)
						&& ((hand[3] >>> 4) == 3) && ((hand[2] >>> 4) == 4))) {
				hand[1] = (14 << 4);
			}
			
			// staits with other than top pairs
			else if (((firstRank - (hand[5] >>> 4)) == 4) && ((firstRank - (hand[1] >>> 4)) == 1)) {
				// XYYZUT
				if (((firstRank - (hand[4] >>> 4)) == 3) && ((firstRank - (hand[3] >>> 4)) == 2) && ((firstRank - (hand[2] >>> 4)) == 1)) {
					hand[2] = (14 << 4);
				}
				// XYZZUT
				else if (((firstRank - (hand[4] >>> 4)) == 3) && ((firstRank - (hand[3] >>> 4)) == 2) && ((firstRank - (hand[2] >>> 4)) == 2)) {
					hand[3] = (14 << 4);
				}
				// XYZUUT
				else if (((firstRank - (hand[4] >>> 4)) == 3) && ((firstRank - (hand[3] >>> 4)) == 3) && ((firstRank - (hand[2] >>> 4)) == 2)) {
					hand[4] = (14 << 4);
				}
				// XYZUTT
				else if (((firstRank - (hand[4] >>> 4)) == 4) && ((firstRank - (hand[3] >>> 4)) == 3) && ((firstRank - (hand[2] >>> 4)) == 2)) {
					hand[5] = (14 << 4);
				}
			}
			// strait A5432 with pair lower than A
			else if ((firstRank == 13) && ((hand[5] >>> 4) == 1) && ((hand[1] >>> 4) == 4)) {
				// A55432
				if (((hand[4] >>> 4)  == 2) && ((hand[3] >>> 4) == 3) && ((hand[2] >>> 4) == 4)) {
					hand[2] = (14 << 4);
				}
				// A54432
				else if (((hand[4] >>> 4)  == 2) && ((hand[3] >>> 4) == 3) && ((hand[2] >>> 4) == 3)) {
					hand[3] = (14 << 4);
				}
				// A54332
				else if (((hand[4] >>> 4)  == 2) && ((hand[3] >>> 4) == 2) && ((hand[2] >>> 4) == 3)) {
					hand[4] = (14 << 4);
				}
				// A54322
				else if (((hand[4] >>> 4)  == 1) && ((hand[3] >>> 4) == 2) && ((hand[2] >>> 4) == 3)) {
					hand[5] = (14 << 4);
				}
			}
			// strait with high kicker that cannot make strait better (and not affect in possible flush)
			else if (((firstRank - secondRank) > 2) && ((secondRank - (hand[5] >>> 4)) == 4)
					&& ((secondRank - (hand[4] >>> 4)) == 3) && ((secondRank - (hand[3] >>> 4)) == 2)
					&& ((secondRank - (hand[2] >>> 4)) == 1) && ((hand[0] & 0xF) == 0)) {
				hand[0] = (14 << 4);
			}
			// strait A5432 with kicker that cannot make strait better or affect in possible flush
			else if ((firstRank == 13) && ((hand[5] >>> 4) == 1) && ((hand[4] >>> 4) == 2) && ((hand[3] >>> 4) == 3)
					&& ((hand[2] >>> 4) == 4) && ((hand[1] & 0xF) == 0) && ((secondRank - 4) > 2)) {
				hand[1] = (14 << 4);
			}
			// strait AKQJT with kicker that cannot affect in possible flush
			else if ((firstRank == 13) && ((hand[4] >>> 4) == 9) && ((hand[3] >>> 4) == 10) && ((hand[2] >>> 4) == 11)
					&& ((hand[1] >>> 4) == 12) && ((hand[5] & 0xF) == 0)) {
				hand[5] = (14 << 4);
			}

			// made 6 card flush, remove low kicker (if not contributing strait flush)
			else if (suitCount[0] == 0) {
				// check if not A2345 draw or lowest 4 cards in sequence (ie. 2345)
				if ((firstRank == 13) && ((hand[3] >>> 4) != 3) && ((hand[3] >>> 4) != 4)
						&& ((hand[2] >>> 4) - (hand[5] >>> 4) >= 4)) {
					hand[5] = (14 << 4);
				}
				// if not one gapper or lowest 4 cards in sequence (ie. 2345)
				else if ((firstRank - (hand[5] >>> 4)  > 5) && ((hand[2] >>> 4) - (hand[5] >>> 4)  >= 4)) {
					hand[5] = (14 << 4);
				}
			}
			
			// two pair in card 0-4
			else if ((rankCount[(hand[0] >>> 4)] == 2 && rankCount[(hand[2] >>> 4)] == 2) || 
					(rankCount[(hand[0] >>> 4)] == 2 && rankCount[(hand[3] >>> 4)] == 2) || 
					(rankCount[(hand[1] >>> 4)] == 2 && rankCount[(hand[3] >>> 4)] == 2)) {
				
				boolean reduce=true;
				// card5 needs gap to prevent straight
				// 887766 => 45 not allowed for card 5 (gap=2)
				// 997766 => 5 not allowed for card 5 (gap=1)
				int neededGap = Math.max((4 - ((hand[0] >>> 4) - (hand[4] >>> 4))), 0);
				
				if (((hand[4] >>> 4) - (hand[5] >>> 4)) <= neededGap) {
					reduce=false;
				}
				// possible A-high straights cannot be reduce
				// AA544, AA554, A5544, A4433
				if ((hand[0] >>> 4)==13 && (((hand[2] >>>4)==3) || ((hand[2] >>>4)==4))) {
					reduce=false;
				}
				// if card5 still has its suit its part of the flush-handrank
				if ( ((hand[5] & 0xF) != 0)) {
					reduce=false;
				}
				if (reduce) {
					hand[5] = (1 << 4);
				}
			}
		}
	} // END checkKey method
	
	
	// encodes hand[] to key
	private static long encodeKey() {
		sortHand();
		
		long key = 0;
		for (int i = 0; i < 7; i++) {
			key += (long)hand[i] << (i << 3);
		}
		
		return key;
	}
	
	
	// Formats and returns a card in 8-bit packed representation.
	private static int formatCard8bit(int card) {
		
		// 8-Bit Packed Card Representation
		// +--------+
		// |rrrr-sss|
		// +--------+
		// r = rank of card (deuce = 1, trey = 2, four = 3, five = 4,..., ace = 13) 14 means insignificant rank
		// s = suit of card (suits are arbitrary, can take value from 1 to 4) 0 means insignificant suit
		if (!isOriginalCardOrder()) {
			// we have new card order
			// so we need convert it back to old one because all code is desinged that way
			card = oldCards[card-1];
		}
		card--;
		return (((card >>> 2) + 1) << 4) + (card & 3) + 1;
		
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
	     int tmp;
		if (hand[i] < hand[j]) {
			tmp = hand[j];
			hand[j] = hand[i];
			hand[i] = tmp;
		}	
	} // END swapCard method
	
	
	// find pure states where all suits are 'x'
	private static boolean isPure(long key) {
		// extract the hand represented by the key value
		for (cardIndex = 0; cardIndex < 7; cardIndex++) {
			int card = (int)((key >>> (cardIndex << 3)) & 0xFF);
			if (card == 0) {
				break;
			}
			if ((card & 0xF) != NOSUIT) {
				return false;
			}
		}
		return true;
	} // END isPure method
	
	
	// check if keys hand contains given suit
	private static boolean containsSuit(long key, int suit) {
		// extract the hand represented by the key value
		for (cardIndex = 0; cardIndex < 7; cardIndex++) {
			int card = (int)((key >>> (cardIndex << 3)) & 0xFF);
			if (card == 0) {
				return false;
			}
			// if suit
			if ((card & 0xF) == suit) {
				return true;
			}
		}
		return false;
	} // END containsSuit method
	
	
	// count given suit from keys hand
	private static int countSuit(long key, int suit) {
		int count = 0;
		// extract the hand represented by the key value
		for (cardIndex = 0; cardIndex < 7; cardIndex++) {
			int card = (int)((key >>> (cardIndex << 3)) & 0xFF);
			if (card == 0) {
				break;
			}
			// if suit
			if ((card & 0xF) == suit) {
				count++;
			}
		}
		return count;
	} // END countSuit method
	
	
	// check that there are only given suit in key hand
	private static boolean noOtherSuits(long key, int suit) {
		// extract the hand represented by the key value
		for (cardIndex = 0; cardIndex < 7; cardIndex++) {
			int card = (int)((key >>> (cardIndex << 3)) & 0xFF);
			if (card == 0) {
				break;
			}
			// if suit
			if ((card & 0xF) != suit && (card & 0xF) != NOSUIT) {
				return false;
			}
		}
		return true;
	} // END countSuit method
	
	
	// changes keys hand suits to newSuits
	private static long changeSuitToSuit(long key, int suit, int newSuit) {
		int[] suitCount = new int[NUM_SUITS + 1]; 	// number of times a suit appears in a hand
		int[] rankCount = new int[NUM_RANKS + 2];	// number of times a rank appears in a hand
		
		// extract the hand represented by the key value
		for (cardIndex = 0; cardIndex < 7; cardIndex++) {
			hand[cardIndex] = (int)((key >>> (cardIndex << 3)) & 0xFF);
		}
		
		// examine the hand and change suit
		for (numCards = 0; hand[numCards] != 0; numCards++) {
			if ((hand[numCards] & 0xF) == suit) {
				// reset suit
				hand[numCards] &= 0xF0;
				// and set new
				hand[numCards] |= newSuit;
			}
			rankCount[(hand[numCards] >>> 4)]++;			
			suitCount[hand[numCards] & 0xF]++;
		}
		
		checkKey(suitCount, rankCount);
		return encodeKey();
	} // END changeSuitToSuit method
	
	
	// method for getting and caching handranks
	public static int getHandRank(long key) {
		int value;
		Long hashKey = new Long(key);
		if (ranks.containsKey(hashKey)) {
			return ranks.get(hashKey).intValue();
		}
		value = CactusKev.getHandRank(key);
		ranks.put(hashKey, new Integer(value));
		return value;
	}
	
	
	// for debugging
	private static String keyToString(long key) {
		String separator = "";
		int count = 0;
		StringBuilder sb = new StringBuilder().append("[");
		// extract the hand represented by the key value
		for (cardIndex = 0; cardIndex < 7; cardIndex++) {
			int card = (int)((key >>> (cardIndex << 3)) & 0xFF);
			if (card != 0) {
				sb.append(separator).append(RANKS[card >> 4]).append(SUITS[card & 0xF]);
				count++;
			}
			separator = ",";
		}
		if (count > 0) {
			return sb.append("]").toString();
		}
		return "";
	}
	
	
	// for debugging
	private static void printKey(long key) {
		System.out.println(keyToString(key));
	}
	
	// outputs messages
	private static void message(boolean test, String val) {
		if (test) System.out.println(val);
	}
	
	
	// bluegaspode's method to find unhandled compressions
	private static void searchEquivalenceOnCompressedSplittedHandranks() {
		int range = 13;
		
		// create an index (rank1*rank2 of 52) pointing to all starting
		// positions of (rank1*rank2 of 52)
		// this makes searching for duplicates faster later on
		Map<Integer, List<Integer>> handrankindex = new HashMap<Integer, List<Integer>>();
		System.out.println("creating handrank-index");
		for (int i = 0; i < handRank7.length; i += range) {
			Integer rank = Integer.valueOf(handRank7[i] * handRank7[i + 1]);
			List<Integer> position = handrankindex.get(rank);
			if (position == null) {
				position = new LinkedList<Integer>();
				handrankindex.put(rank, position);
			}
			position.add(Integer.valueOf(i));
		}
		
		System.out.println("starting pointer movement level7");
		int changed = 0;
		// work backwards (because we get faster in the end)
		Set<Integer> ignorePositions = new TreeSet<Integer>();
		for (int index6Position = handIndex6.length - 1; index6Position >= 0; index6Position--) {
		
			if (index6Position % 50000 == 0) {
				System.out.println(index6Position / (handIndex6.length / 100) + "% of " + handIndex6.length + " done. Changed:" + changed);
			}
			// start of first 52 block
			int compare1Start = handIndex6[index6Position];
			if (ignorePositions.contains(compare1Start)) {
				continue;
			}
			ignorePositions.add(compare1Start);
			
			List<Integer> equivalentPositions = new ArrayList<Integer>();
			
			// get all blocks starting with the same 2 ranks for deeper analysis
			List<Integer> startPositions = handrankindex.get(Integer.valueOf(handRank7[compare1Start] * handRank7[compare1Start + 1]));
			if (startPositions == null) {
				continue;
			}
			for (Integer compare2Start : startPositions) {
				if (compare2Start >= compare1Start)
					break;
					
				boolean match = true;
				// compare all ranks for exact matches
				for (int i = 0; i < 52; i++) {
					int rank1 = handRank7[compare1Start + i];
					int rank2 = handRank7[compare2Start + i];
					if (((rank1 >> 12) == 0) || ((rank2 >> 12) == 0)) {
						// we ignore error ranks as noone should get there
						continue;
					}
					if (rank1 != rank2) {
						match = false;
						break;
					}
				}
				// on a match we can move the pointer to the other block
				if (match) {
					changed++;
					// handIndex6[index6Position] = compare2Start;
					equivalentPositions.add(compare2Start);
					ignorePositions.add(compare2Start);
					// break;
				}
			}
			
			if (equivalentPositions.size() > 0) {
				equivalentPositions.add(compare1Start);
				System.out.print("Equivalent: [");
				for (int keyIndex = LEVEL6START; keyIndex < LEVEL6END; keyIndex++) {
					if (equivalentPositions.contains(Integer.valueOf(positions[keyIndex]))) {
						System.out.print(positions[keyIndex] + " ");
						System.out.print(keyToString(keys[keyIndex]) + " ");
					}
				
				}
				System.out.print(equivalentPositions);
				System.out.println("]");
			}
		}
	} // END searchEquivalenceOnCompressedSplittedHandranks method
	
	
	// prints level6 key classes
	public static void printKeyClasses() {
		long key;
		int pairPos, tripPos, card;
		int flush6 = 0, flush5 = 0, flush5pair = 0, flush4trips = 0, flush4_2pairs = 0,
			flush4pair = 0, flush4 = 0, other = 0, fourokpair = 0, fourok = 0, fullh3 = 0,
			fullh = 0, trips = 0, treepairs = 0, twopairs = 0, pair = 0;
		int[] sortedRanks = new int[NUM_RANKS + 3];
		
		for (int keyIndex = LEVEL6START; keyIndex < LEVEL6END; keyIndex++) {
			int[] suitCount = new int[NUM_SUITS + 1]; 	// number of times a suit appears in a hand
			int[] rankCount = new int[NUM_RANKS + 2];	// number of times a rank appears in a hand
			key = keys[keyIndex];
			
			// extract the hand represented by the key value
			for (cardIndex = 0; cardIndex < 6; cardIndex++) {
				card = (int)((key >>> (cardIndex << 3)) & 0xFF);
				hand[cardIndex] = card;
				rankCount[card >>> 4]++;
				suitCount[card & 0xF]++;
			}
			
			System.arraycopy(rankCount, 0, sortedRanks, 0, rankCount.length);
			Arrays.sort(sortedRanks);
			pairPos = Arrays.binarySearch(sortedRanks, 2);
			tripPos = Arrays.binarySearch(sortedRanks, 3);
			pairPos = Math.max(pairPos, 0);
			tripPos = Math.max(tripPos, 0);
			
			switch (suitCount[0]) {
			
				case 0 : 	flush6++; break;
				
				case 1 :	if (sortedRanks[pairPos] == 2) { flush5pair++; } else { flush5++; } break;
					
				case 2 :
					if (sortedRanks[tripPos] == 3) {
						flush4trips++;
					} else if (sortedRanks[pairPos] == 2) {
						if (sortedRanks[pairPos-1] == 2 || ((pairPos+1) < sortedRanks.length && sortedRanks[pairPos+1] == 2)) {
							flush4_2pairs++;
						} else {
							flush4pair++;
						}
					} else { flush4++; } break;
				
				default :
					if (sortedRanks[tripPos] == 3) {
						if (sortedRanks[tripPos-1] == 3 || ((tripPos+1) < sortedRanks.length && sortedRanks[tripPos+1] == 3)) {
							fullh3++;
						} else if (sortedRanks[pairPos] == 2) {
							fullh++;
						} else {
							trips++;
						}
					} else if (sortedRanks[pairPos] == 2) {
						if (sortedRanks[pairPos-1] == 2 && sortedRanks[pairPos+1] == 2) {
							treepairs++;
						} else  if (sortedRanks[pairPos-1] == 2 || sortedRanks[pairPos+1] == 2) {
							twopairs++;
						} else if (rankCount[hand[3] >>> 4] == 4) {
							fourokpair++;
						} else {
							pair++;
						}
					} else if (rankCount[hand[3] >>> 4] == 4) {
						fourok++;
					} else {
						other++;
					}
				
			}
		}
		
		StringBuilder sb = new StringBuilder().append("\n")
			.append("\nXaYaZaTaUaVa ").append(flush6)
			.append("\nXaYaZaTaUaX  ").append(flush5pair)
			.append("\nXaYaZaTaUaV  ").append(flush5)
			.append("\nXaYaZaTaXX   ").append(flush4trips)
			.append("\nXaYaZaTaXY   ").append(flush4_2pairs)
			.append("\nXaYaZaTaXV   ").append(flush4pair)
			.append("\nXaYaZaTaUV   ").append(flush4)
			.append("\nXXXXYY       ").append(fourokpair)
			.append("\nXXXXYZ       ").append(fourok)
			.append("\nXXXYYY       ").append(fullh3)
			.append("\nXXXYYZ       ").append(fullh)
			.append("\nXXXYZT       ").append(trips)
			.append("\nXXYYZZ       ").append(treepairs)
			.append("\nXXYYZT       ").append(twopairs)
			.append("\nXXYZTU       ").append(pair)
			.append("\nXYZTUV       ").append(other)
			.append("\n");
		
		System.out.println(sb.toString());
	} // END printKeyClasses method
	
	

} // END class Evaluator
