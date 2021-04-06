package UnoGame;

import java.util.List;

/**
 * This class represents the Wild card and Wild Draw Four card, inherited from Card.
 * 
 */
public class WildCard extends Card {
	/**
	 * Holds all the types of wild cards.
	 */
	public static final String[] TYPES = {"Wild", "WildDrawFour"};
	/**
	 * Holds number of cards for each type.
	 */
	public static final int[] COUNT_OF_CARDS = {4, 4};
	
	/**
	 * Construct a Wild card with given type.
	 */
	public WildCard(String _type) {
		super(_type);
	}
	
	/**
	 * Initialize all the Wild cards and add it to the given list.
	 * 
	 * @param pile
	 */
	public static void addCardsToPile(List<Card> pile) {
		for (int typeIdx = 0; typeIdx < TYPES.length; typeIdx++) {
			String type = TYPES[typeIdx];
			for (int i = 0; i < COUNT_OF_CARDS[typeIdx]; i++) {
				pile.add(new WildCard(type));
			}
		}
	}
	
	public boolean isColored() {
		return false;
	}
	
	public boolean isWild() {
		return true;
	}
	
	public String getColor() {
		return "";
	}
	
	/**
	 * Always return false because Wild cards cannot match with any cards.
	 */
	public boolean isMatchedWith(Card other) {
		return other.isWild() && type.compareTo(other.type) == 0;
	}
	
	/**
	 * Wild cards don't have colors.
	 */
	public void switchColor(String newColor) {
		// Wild cards don't have colors.
		return;
	}
	
}



