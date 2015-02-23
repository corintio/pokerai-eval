package pokerai.game.util;

/**
 * This class converts Strings to ints that represent cards
 */
public class CardParser {
	
	public static final int CARD_TYPE_ZERO_BASED_SUIT_FIRST = 1;
	public static final int CARD_TYPE_ZERO_BASED_RANK_FIRST = 2;
	public static final int CARD_TYPE_ONE_BASED_RANK_FIRST = 3;
	public static final int CARD_TYPE_BIT_SUIT_FIRST = 4;
	
	public static final String RANK_CHARS = "23456789TJQKA";
	public static final String SUIT_CHARS = "cdhs";
	
	private int type;
	
	/**
	 * Constructs a CardParser of the specified card type.
	 * @param type
	 */
	public CardParser(int type) {
		this.type = type;
	}
	
	
	/**
	 * Returns int[] representing a given cards String.
	 * @param cards a {@link String} representing cards, format "Ah Qc 2d" (case insensitive).
	 * @return int[] presentation of cards denoted by cards String.
	 * @throws IllegalArgumentException on the first character in card which is not found in the respective string.
	 * @throws IllegalStateException if card type CARD_TYPE_BIT_SUIT_FIRST is selected
	 */
	public int[] parseCards(String cards) {
		int[] result = new int[cards.length() / 2];
		int pos = 0;
		for (String part : cards.split("\\s")) {
			result[pos++] = parseCard(part);
		}
		return result;
	}
	
	
	/**
	 * Returns long[] containing bit representation of a given cards String.
	 * @param cards a {@link String} representing cards, format "Ah Qc 2d" (case insensitive).
	 * @return long[] bit presentation of cards denoted by cards String.
	 * @throws IllegalArgumentException on the first character in card which is not found in the respective string.
	 * @throws IllegalStateException if card type CARD_TYPE_BIT_SUIT_FIRST is not selected
	 */
	public long[] parseBitCards(String cards) {
		int numberOfCards = cards.length() / 2;
		long[] result = new long[numberOfCards];
		for (int pos = 0; pos < numberOfCards; pos++) {
			result[pos] = parseBitCard(cards.substring(pos, pos + 2));
		}
		return result;
	}
	
	
	/**
	 * Returns int representing a card of the specified rank and suit.
	 * @param card a {@link String} of length 2, where the first character is in {@link Card.Rank#RANK_CHARS} and
	 * 			 the second is in {@link Card.Suit#SUIT_CHARS} (case insensitive).
	 * @return int presentation of card denoted by card String.
	 * @throws IllegalArgumentException on the first character in card which is not found in the respective string.
	 * @throws IllegalStateException if card type CARD_TYPE_BIT_SUIT_FIRST is selected
	 */
	public int parseCard(String card) {
		int result;
		if (type == CARD_TYPE_BIT_SUIT_FIRST) {
			throw new IllegalStateException("Method cannot return bit presentation use parseBitCard instead");
		}
		if (card.length() != 2) {
			throw new IllegalArgumentException("Card string presentaion does not contain 2 characters '" + card + "'");
		}
		int rank = parseRank(card.charAt(0));
		int suit = parseSuit(card.charAt(1));
		switch (type) {
			case CARD_TYPE_ZERO_BASED_SUIT_FIRST : result = rank + suit * 13;	break;
			case CARD_TYPE_ZERO_BASED_RANK_FIRST : result = rank * 4 + suit;		break;
			case CARD_TYPE_ONE_BASED_RANK_FIRST  : result = rank * 4 + suit + 1;	break;
			default : throw new IllegalStateException("Unknown card type");
		}
		return result;
	}
	
	
	/**
	 * Returns bit representing a card of the specified rank and suit.
	 * @param card a {@link String} of length 2, where the first character is in {@link Card.Rank#RANK_CHARS} and
	 * 			 the second is in {@link Card.Suit#SUIT_CHARS} (case insensitive).
	 * @return long bit presentation of card denoted by card String.
	 * @throws IllegalArgumentException on the first character in card which is not found in the respective string.
	 * @throws IllegalStateException if card type CARD_TYPE_BIT_SUIT_FIRST is not selected
	 */
	public long parseBitCard(String card) {
		if (type != CARD_TYPE_BIT_SUIT_FIRST) {
			throw new IllegalStateException("Method returns bit presentation use parseCard instead");
		}
		if (card.length() != 2) {
			throw new IllegalArgumentException("Card string presentaion does not contain 2 characters '" + card + "'");
		}
		int rank = parseRank(card.charAt(0));
		int suit = parseSuit(card.charAt(1));
		return 0x1L << (rank + suit * 13);
	}
	
	
	/**
	 * @param rank a character present in {@link #RANK_CHARS} (case insensitive)
	 * @return int presentation of rank denoted by character.
	 * @throws IllegalArgumentException if rank not in {@link #RANK_CHARS}
	 */
	public static int parseRank(char rank) {
		int index = RANK_CHARS.indexOf(Character.toUpperCase(rank));
		if (index == -1) {
			throw new IllegalArgumentException("Illegal rank character '" + rank + "'");
		}
		return  index;
	}
	
	
	/**
	 * @param suit a character present in {@link #SUIT_CHARS} (case insensitive)
	 * @return int presentation of suit denoted by the character.
	 * @throws IllegalArgumentException if suit not in {@link #SUIT_CHARS}
	 */
	public static int parseSuit(char suit) {
		int index = SUIT_CHARS.indexOf(Character.toLowerCase(suit));
		if (index == -1) {
			throw new IllegalArgumentException("Illegal suit character '" + suit + "'");
		}
		return  index;
	}
	
	
}
