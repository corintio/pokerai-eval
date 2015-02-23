package pokerai.game.eval.jokipii;

import java.io.IOException;
import java.io.EOFException;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

/**
 * Contains methods to load and generate LUT and some card handling methods
 * @author Antti Jokipii
 * @date 12th March 2010
 */
public class AbstractLUTEvaluator extends AbstractLUT  {
	
	/* Card to integer conversions:
   		2c =  0	2d = 13	2h = 26	2s = 39
   		3c =  1	3d = 14	3h = 27	3s = 40
   		4c =  2	4d = 15	4h = 28	4s = 41
   		5c =  3	5d = 16	5h = 29	5s = 42
   		6c =  4	6d = 17	6h = 30	6s = 43
   		7c =  5	7d = 18	7h = 31	7s = 44
   		8c =  6	8d = 19	8h = 32	8s = 45
   		9c =  7	9d = 20	9h = 33	9s = 46
   		Tc =  8	Td = 21	Th = 34	Ts = 47
   		Jc =  9	Jd = 22	Jh = 35	Js = 48
   		Qc = 10	Qd = 23	Qh = 36	Qs = 49
   		Kc = 11	Kd = 23	Kh = 37	Ks = 50
   		Ac = 12	Ad = 25	Ah = 38	As = 51
	 */
	public static final String[] cardString = new String[] {
		"2c", "3c", "4c", "5c", "6c", "7c", "8c", "9c", "Tc", "Jc", "Qc", "Kc", "Ac", 
		"2d", "3d", "4d", "5d", "6d", "7d", "8d", "9d", "Td", "Jd", "Qd", "Kd", "Ad", 
		"2h", "3h", "4h", "5h", "6h", "7h", "8h", "9h", "Th", "Jh", "Qh", "Kh", "Ah", 
		"2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "Ts", "Js", "Qs", "Ks", "As", 
	};
	
	protected static String handRanksFile = "jhandRanks.ser";
	
	
	/**
	 * Method initialises LUT from disk to memory or generates it if first time.
	 */
	public static void initialize() {
		ObjectInputStream inputStream = null;
		try {
			if (verbose) System.out.println("Loading evaluation tables ...");
			File f = new File(handRanksFile);
			if (!f.exists()) {
				generateNew();
			}
			else {
				long t = System.currentTimeMillis();
				inputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(handRanksFile), 100000));
				int i = inputStream.readInt();
				if (i != mode) {
					throw new RuntimeException("HandRanks file is generated with different mode. Remove it and run again to create it with selected mode.");
				}
				if (isSplitted()) {
					handIndex1 = (int[])inputStream.readObject();
					handIndex2 = (int[])inputStream.readObject();
					handIndex3 = (int[])inputStream.readObject();
					handIndex4 = (int[])inputStream.readObject();
					if (isOmaha()) {
						handRank5 = (char[])inputStream.readObject();
					}
					else
					{
						handIndex5 = (int[])inputStream.readObject();
						handIndex6 = (int[])inputStream.readObject();
						handRank7 = (char[])inputStream.readObject();
						if (is56Included()) {
							handRank6 = (char[])inputStream.readObject();
							handRank5 = (char[])inputStream.readObject();
						}
					}
				}
				else {
					handRanks = (int[])inputStream.readObject();
				}
				t = System.currentTimeMillis() - t;
				if (verbose) System.out.println("Evaluation tables loaded in " + t/1000.0 + " seconds" );
			}
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			throw new Error(cnfe);
		} catch (EOFException eofe) {
			System.out.println("Problem to read handRanks file it is probably generated with different mode.\nRemove it and run again! This will create it with selected mode.");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new Error(ioe);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				throw new Error(ioe);
			}
		}
	} // END method initialize
	
	
	private static void generateNew() {
		if (verbose) System.out.println("Evaluation tables do not exist, this is first time run. Generating them ...");
		LUTGenerator.debug = debug;
		LUTGenerator.verbose = verbose;
		LUTGenerator.mode = mode;
		LUTGenerator.generateTables(handRanksFile);
		if (isSplitted()) {
			handIndex1 = LUTGenerator.handIndex1;
			handIndex2 = LUTGenerator.handIndex2;
			handIndex3 = LUTGenerator.handIndex3;
			handIndex4 = LUTGenerator.handIndex4;
			handIndex5 = LUTGenerator.handIndex5;
			handIndex6 = LUTGenerator.handIndex6;
			handRank7 = LUTGenerator.handRank7;
			if (is56Included()) {
				handRank6 = LUTGenerator.handRank6;
				handRank5 = LUTGenerator.handRank5;
			}
		}
		else {
			handRanks = LUTGenerator.handRanks;
		}
	}
	
	
	/**
	 * Returns an encoded card value suitable for passing as an argument to {@code eval7} 
	 * method.
	 * The rank argument is an {@code int} from [0..12] representing the cards [2..A].
	 * The suit argument is an {@code int} from [0..3], chosen arbitrarily.
	 * The return value is an {@code int} from [0..51].
	 * <p>
	 * @param rank the rank of the card, a value from 0 to 12, inclusive
	 * @param suit the suit of the card, a value from 0 to 3, inclusive
	 * @return an encoded card value suitable for passing as an argument to the {@code eval7} method.
	 */
	public static int encode(int rank, int suit) {
		return rank + 13 * suit;
	}
	
	/**
	 * Returns an encoded card value as a String 
	 * <p>
	 * @param card an encoded card
	 * @return an encoded card value as a String.
	 */
	public static String cardToString(int card) {
		return cardString[card];
	}
	
	
} // END class Evaluator
