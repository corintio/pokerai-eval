package pokerai.game.util;

import java.util.Set;
import java.util.Iterator;

public class Test {
	
	public static void main(String args[]) {
		testRangeHelper();
	}
	
	public static void testRangeHelper() {
		testRangeHelper("random");
		testRangeHelper("KcKd");
		testRangeHelper("AsQs,KcKd");
		testRangeHelper("KK");
		testRangeHelper("KK+");
		testRangeHelper("AKs");
		testRangeHelper("AKo");
		testRangeHelper("AK");
		testRangeHelper("A2s+");
		testRangeHelper("A2o+");
		testRangeHelper("A2+");
		testRangeHelper("J8s+");
		testRangeHelper("J8o+");
		testRangeHelper("J8+");
		testRangeHelper("KK-TT");
		testRangeHelper("A6o-A2o");
		testRangeHelper("A6-A2");
		testRangeHelper("QQ+,AQs+,KQs,AQo+,KQo");
		testRangeHelper("KK", "AcKc");
		testRangeHelper("A2s+", "AcKc");
		testRangeHelper("KK-TT", "KcQc");
	}
	
	public static void testRangeHelper(String range) {
		Set<String> result;
		System.out.println("\nTest: " + range);
		result = RangeHelper.rangeToCardCombinations(range);
		StringBuilder sb = new StringBuilder();
		for (String val : result) {
			sb.append(val).append(", ");
		}
		System.out.println("Result: " + sb.toString());
	}
	
	public static void testRangeHelper(String range, String remove) {
		Set<String> result;
		System.out.println("\nTest range: " + range + " removed: " + remove);
		result = RangeHelper.rangeToCardCombinations(range);
		result = RangeHelper.removeImpossibleCards(result, remove);
		StringBuilder sb = new StringBuilder();
		for (String val : result) {
			sb.append(val).append(", ");
		}
		System.out.println("Result: " + sb.toString());
	}
	
}
