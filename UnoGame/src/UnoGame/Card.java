package UnoGame;

import java.util.*;
/**
 * This class represents the abstract card.
 *
 */
public abstract class Card {
	
	/**
	 * Holds all the functional cards' types (non-numbered cards).
	 */
	public static final Set<String> FUNCTIONAL_TYPES = new HashSet<>(
			Arrays.asList("Skip", "Reverse", "DrawTwo", "Wild", "WildDrawFour"));
	
	/**
	 * Holds the type of the card.
	 */
	public String type;
	
	/**
	 * Initialize the card type.
	 * @param _type
	 */
	public Card(String _type) {
		type = _type;
	}
	
	/**
	 * 
	 * @return	true, if this card is one of the functional type,
	 * 			false, if this card is a numbered card.
	 */
	public boolean isFunctional() {
		return FUNCTIONAL_TYPES.contains(type);
	}
	
	/**
	 * Determine whether the given card is the same to itself.
	 * 
	 * @param  other	card to compare with
	 * @return true		the card has the same color and type/number
	 * 		   false	o.w.
	 */
	public boolean equalsTo(Card other) {
		return toString().compareTo(other.toString()) == 0;
	}
	
	@Override
	public String toString() {
		return getColor().toLowerCase() + type.toLowerCase();
	}
	
	public abstract boolean isColored();
	
	public abstract boolean isWild();
	
	public abstract String getColor();
	
	public abstract boolean isMatchedWith(Card other);
	
	public abstract void switchColor(String newColor);
	
}



