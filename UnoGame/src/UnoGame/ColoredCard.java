package UnoGame;

import java.util.*;
/**
 * This class represents the colored card, inherited from Card.
 *
 */
public class ColoredCard extends Card {

	/**
	 * Holds all the colors of colored cards.
	 */
	public static final String[] COLORS = {"Red", "Yellow", "Green", "Blue"};
	/**
	 * Holds all the types of colored cards.
	 */
	public static final String[] TYPES = 
		{	"0", "1", "2", "3", "4", 
			"5", "6", "7", "8", "9", 
			"Skip", "Reverse", "DrawTwo"};
	/**
	 * Holds the number of cards for each Color-Type.
	 */
	public static final int[] COUNT_OF_CARDS = {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
	
	/**
	 * Holds the color of the card.
	 */
	private String color;
	
	/**
	 * Construct a colored card with given color and type.
	 */
	public ColoredCard(String _type, String _color) {
		super(_type);
		color = _color;
	}
	
	public boolean isColored() {
		return true;
	}
	
	public boolean isWild() {
		return false;
	}
	
	public String getColor() {
		return color;
	}
	
	/**
	 * Initialize all the colored cards and add it to the given list.
	 * 
	 * @param pile
	 */
	public static void addCardsToPile(List<Card> pile) {
		for (String color : COLORS) {
			for (int type_idx = 0; type_idx < TYPES.length; type_idx++) {
				for (int i = 0; i < COUNT_OF_CARDS[type_idx]; i++) {
					pile.add(new ColoredCard(TYPES[type_idx], color));
				}
			}
		}
	}
	
	/**
	 * Return true if this card is matched with the given card,
	 *  that is, they have either the same color or the same type;
	 *  false otherwise.
	 */
	public boolean isMatchedWith(Card other) {
		return other.isColored() &&
			 (color.compareTo(other.getColor()) == 0 || type.compareTo(other.type) == 0);
	}
	
	/**
	 * Switch the card's color to the given newColor.
	 */
	public void switchColor(String newColor) {
		color = newColor;
	}

}





