package pokerai.game.util;

import java.util.TreeSet;
import java.util.Set;
import java.util.Iterator;

/**
 * This class contains static helper methods to handle ranges
 */
public class RangeHelper {
	
	private static final String RANK_CHARS = "23456789TJQKA";
	private static final String RANDOM_RANGE = "22+,A2s+,K2s+,Q2s+,J2s+,T2s+,92s+,82s+,72s+,62s+,52s+,42s+,32s,A2o+,K2o+,Q2o+,J2o+,T2o+,92o+,82o+,72o+,62o+,52o+,42o+,32o";
	private static final String ALL_SUITED = "A2s+,K2s+,Q2s+,J2s+,T2s+,92s+,82s+,72s+,62s+,52s+,42s+,32s";
	private static final String BROADWAY = "TT+,ATs+,KTs+,QTs+,JTs,ATo+,KTo+,QTo+,JTo";
	
	/**
	 * Converts a range string in PokerStove format into a {@link TreeSet}<{@link String}>
	 * that contains all possible card combinations as String format.
	 * @param rangeString range, format "22+,A7s+,KTs+,ATo+,...."
	 * @return Set<String> set of all possible card combinations within range,
	 * 		strings in set can be parsed with {@link CardParser}
	 * @throws IllegalArgumentException if range contains unknown parts.
	 */
	public static Set<String> rangeToCardCombinations(String range) {
		TreeSet<String> result = new TreeSet<String>();
		for (String part : range.split(",")) {
			if (part.contains("-")) {
				addGap(result, part);
			}
			else if (part.contains("+")) {
				addMore(result, part);
			}
			else if (part.equals("random")) {
				result.addAll(rangeToCardCombinations(RANDOM_RANGE));
			}
			else if (part.length() == 4) {
				// form Ah9h
				result.add(part.substring(0,2) + " " + part.substring(2,4));
			}
			else if (part.contains("o")) {
				addOffsuited(result, part);
			}
			else if (part.contains("s")) {
				addSuited(result, part);
			}
			else if (part.charAt(0) == part.charAt(1)) {
				addPair(result, part);
			}
			else if (part.length() == 2) {
				// form 56 that means we want both offsuited and suited
				addOffsuited(result, part + "o");
				addSuited(result, part + "s");
			}
			else {
				throw new IllegalArgumentException("Unknown range part '" + part + "'");
			}
		}
		return result;
	}
	
	
	/**
	 * Takes card combinations set and set of impossible cards and return new set that doesn't
	 * contain impossible cards, example hole and board cards.
	 * @param rangeCards set of all range card combinations
	 * @param impossibleCards set of impossible range card combinations
	 * @return Set<String> new set that contains only possible card combinations within range
	 * @see #rangeToCardCombinations(String)
	 */
	public static Set<String> removeImpossibleCards(Set<String> rangeCards, String impossibleCards) {
		TreeSet<String> result = new TreeSet<String>();
		Iterator<String> iterator = rangeCards.iterator();
		while (iterator.hasNext()) {
			String cards = iterator.next();
			if (!(impossibleCards.contains(cards.subSequence(0,2)) || impossibleCards.contains(cards.subSequence(2,4)))) {
				result.add(cards);
			}
		}
		return result;
	}
	
	
	// add all possible combinations from gap. Example A9s-A6s
	private static void addGap(TreeSet<String> result, String part) {
		int start = CardParser.parseRank(part.charAt(1));
		if (part.contains("s")) {
			int end = CardParser.parseRank(part.charAt(5));
			for (int pos = start; pos >= end; pos--) {
				addSuited(result, String.valueOf(part.charAt(0)) + String.valueOf(RANK_CHARS.charAt(pos)) + "s");
			}
		}
		else if (part.contains("o")) {
			int end = CardParser.parseRank(part.charAt(5));
			for (int pos = start; pos >= end; pos--) {
				addOffsuited(result, String.valueOf(part.charAt(0)) + String.valueOf(RANK_CHARS.charAt(pos)) + "o");
			}
		}
		// form 99-77
		else if (part.length() == 5 && part.charAt(0) == part.charAt(1)) {
			int end = CardParser.parseRank(part.charAt(4));
			for (int pos = start; pos >= end; pos--) {
				addPair(result, String.valueOf(RANK_CHARS.charAt(pos)) + String.valueOf(RANK_CHARS.charAt(pos)));
			}
		}
		// form A9-A6 = A9o-A6o,A9s-A6s
		else if (part.length() == 5) {
			int end = CardParser.parseRank(part.charAt(4));
			for (int pos = start; pos >= end; pos--) {
				addSuited(result, String.valueOf(part.charAt(0)) + String.valueOf(RANK_CHARS.charAt(pos)) + "s");
				addOffsuited(result, String.valueOf(part.charAt(0)) + String.valueOf(RANK_CHARS.charAt(pos)) + "o");
			}
		}
		else {
			throw new IllegalArgumentException("Unknown gap '" + part + "'");
		}
	}
	
	
	// add all possible pair combinations
	private static void addPair(TreeSet<String> result, String part) {
		if (part.charAt(0) == part.charAt(1)) {
			// 6 pairs
			result.add(part.charAt(0)+"c "+part.charAt(1)+"d");
			result.add(part.charAt(0)+"c "+part.charAt(1)+"h");
			result.add(part.charAt(0)+"c "+part.charAt(1)+"s");
			result.add(part.charAt(0)+"d "+part.charAt(1)+"h");
			result.add(part.charAt(0)+"d "+part.charAt(1)+"s");
			result.add(part.charAt(0)+"h "+part.charAt(1)+"s");
		}
		else {
			throw new IllegalArgumentException("Unknown pair '" + part + "'");
		}
	}
	
	
	// add all possible combinations from more. Example KTs+
	private static void addMore(TreeSet<String> result, String part) {
		int start = CardParser.parseRank(part.charAt(1));
		if (part.contains("s")) {
			int end = CardParser.parseRank(part.charAt(0));
			for (int pos = start; pos < end; pos++) {
				addSuited(result, String.valueOf(part.charAt(0)) + String.valueOf(RANK_CHARS.charAt(pos)) + "s");
			}
		}
		else if (part.contains("o")) {
			int end = CardParser.parseRank(part.charAt(0));
			for (int pos = start; pos < end; pos++) {
				addOffsuited(result, String.valueOf(part.charAt(0)) + String.valueOf(RANK_CHARS.charAt(pos)) + "o");
			}
		}
		else if (part.charAt(0) == part.charAt(1)) {
			for (int pos = start; pos < RANK_CHARS.length(); pos++) {
				addPair(result, String.valueOf(RANK_CHARS.charAt(pos)) + String.valueOf(RANK_CHARS.charAt(pos)));
			}
		}
		else if (part.length() == 3) {
			int end = CardParser.parseRank(part.charAt(0));
			for (int pos = start; pos < end; pos++) {
				addSuited(result, String.valueOf(part.charAt(0)) + String.valueOf(RANK_CHARS.charAt(pos)) + "s");
				addOffsuited(result, String.valueOf(part.charAt(0)) + String.valueOf(RANK_CHARS.charAt(pos)) + "o");
			}
		}
		else {
			throw new IllegalArgumentException("Unknown more '" + part + "'");
		}
	}
	
	
	// add all possible combinations from offsuited. Example QJo
	private static void addOffsuited(TreeSet<String> result, String part) {
		if(part.charAt(2) == 'o') {
			// 12 offsuited
			result.add(part.charAt(0)+"c "+part.charAt(1)+"d");
			result.add(part.charAt(0)+"c "+part.charAt(1)+"h");
			result.add(part.charAt(0)+"c "+part.charAt(1)+"s");
			result.add(part.charAt(0)+"d "+part.charAt(1)+"c");
			result.add(part.charAt(0)+"d "+part.charAt(1)+"h");
			result.add(part.charAt(0)+"d "+part.charAt(1)+"s");
			result.add(part.charAt(0)+"h "+part.charAt(1)+"c");
			result.add(part.charAt(0)+"h "+part.charAt(1)+"d");
			result.add(part.charAt(0)+"h "+part.charAt(1)+"s");
			result.add(part.charAt(0)+"s "+part.charAt(1)+"c");
			result.add(part.charAt(0)+"s "+part.charAt(1)+"d");
			result.add(part.charAt(0)+"s "+part.charAt(1)+"h");
		}
		else {
			throw new IllegalArgumentException("Unknown offsuited '" + part + "'");
		}
	}
	
	
	// add all possible combinations from suited. Example T9s
	private static void addSuited(TreeSet<String> result, String part) {
		if(part.charAt(2) == 's') {
			// 4 suited
			result.add(part.charAt(0)+"c "+part.charAt(1)+"c");
			result.add(part.charAt(0)+"d "+part.charAt(1)+"d");
			result.add(part.charAt(0)+"h "+part.charAt(1)+"h");
			result.add(part.charAt(0)+"s "+part.charAt(1)+"s");
		}
		else {
			throw new IllegalArgumentException("Unknown suited '" + part + "'");
		}
	}
	
}
