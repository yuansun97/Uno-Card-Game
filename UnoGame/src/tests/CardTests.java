package tests;

import java.util.*;
import UnoGame.Card;
import UnoGame.ColoredCard;
import UnoGame.WildCard;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CardTests {
	
	private final String WILD = "Wild";
	private final String WILDDRAW = "WildDrawFour";
	private final String ONE = "1";
	private final String TWO = "2";
	private final String RED = "Red";
	private final String GREEN = "Green";
	private final String ACTION = "Skip";
	
	private final int NUM_OF_COLORED_CARDS = 100;
	private final int NUM_OF_WILD_CARDS = 8;
	
	private Card wildCard = new WildCard(WILD);
	private Card wildDrawCard = new WildCard(WILDDRAW);
	private Card numberCard = new ColoredCard(ONE, RED);
	private Card actionCard = new ColoredCard(ACTION, GREEN);

	@Test
	public void testCreateWildCard() {
		assert(wildCard.type.compareTo(WILD) == 0);
	}
	
	@Test
	public void testCreateNumberCard() {
		assert(numberCard.type.compareTo(ONE) == 0);
		assert(numberCard.getColor().compareTo(RED) == 0);
	}
	
	@Test
	public void testCreateActionCard() {
		assert(actionCard.type.compareTo(ACTION) == 0);
		assert(actionCard.getColor().compareTo(GREEN) == 0);
	}
	
	@Test
	public void testIsColored() {
		assertFalse(wildCard.isColored());
		assertTrue(numberCard.isColored());
		assertTrue(actionCard.isColored());
	}
	
	@Test
	public void testIsWild() {
		assertTrue(wildCard.isWild());
		assertFalse(numberCard.isWild());
		assertFalse(actionCard.isWild());
	}
	
	@Test
	public void testIsFunctional() {
		assertTrue(wildCard.isFunctional());
		assertFalse(numberCard.isFunctional());
		assertTrue(actionCard.isFunctional());
	}
	
	private Card oneRedCard = new ColoredCard(ONE, RED);
	private Card twoRedCard = new ColoredCard(TWO, RED);
	private Card oneGreenCard = new ColoredCard(ONE, GREEN);
	
	@Test
	public void testSwitchColor() {
		oneRedCard.switchColor(GREEN);
		assert(oneRedCard.getColor().compareTo(GREEN) == 0);
		
		wildCard.switchColor(RED);
	}
	
	@Test
	public void testIsMatchedWith() {
		assertTrue(wildCard.isMatchedWith(wildCard));
		assertTrue(oneRedCard.isMatchedWith(twoRedCard));
		assertTrue(oneRedCard.isMatchedWith(oneGreenCard));
		
		assertFalse(twoRedCard.isMatchedWith(oneGreenCard));
		assertFalse(wildCard.isMatchedWith(wildDrawCard));
		assertFalse(wildCard.isMatchedWith(actionCard));
	}
	
	@Test
	public void testEqualsTo() {
		assertTrue(wildCard.equalsTo(wildCard));
		assertTrue(oneRedCard.equalsTo(oneRedCard));
		assertFalse(wildDrawCard.equalsTo(wildCard));
		assertFalse(wildDrawCard.equalsTo(oneGreenCard));
	}
	
	@Test
	public void testAddCardsToPileInColoredCard() {
		List<Card> coloredCards = new ArrayList<Card>();
		ColoredCard.addCardsToPile(coloredCards);
		assert(coloredCards.size() == NUM_OF_COLORED_CARDS);
		
		Map<String, Integer> cardDict = putCardsInMap(coloredCards);
		assert(cardDict.size() == 52);
	}
	
	@Test
	public void testAddCardsToPileInWildCard() {
		List<Card> wildCards = new ArrayList<Card>();
		WildCard.addCardsToPile(wildCards);
		assert(wildCards.size() == NUM_OF_WILD_CARDS);
		
		Map<String, Integer> cardDict = putCardsInMap(wildCards);
		assert(cardDict.size() == 2);
	}

	/**
	 * A helper function put cards into a dictionary,
	 *  with entries <card name, count>
	 *  card name = type + "#" + color
	 * 
	 * @param list		a list of cards
	 * @return a map mapping the card to its count
	 */
	private Map<String, Integer> putCardsInMap(List<Card> list) {
		Map<String, Integer> dict = new HashMap<String, Integer>();
		for (Card card : list) {
			String cardName = card.type + "#" + card.getColor();
			dict.put(cardName, dict.getOrDefault(card, 0) + 1);
		}
		return dict;
	}
	
}


