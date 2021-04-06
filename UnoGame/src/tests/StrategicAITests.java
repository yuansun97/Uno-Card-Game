package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import UnoGame.BaselineAI;
import UnoGame.StrategicAI;
import UnoGame.WildCard;
import UnoGame.Card;
import UnoGame.ColoredCard;
import UnoGame.GameBoard;
import UnoGame.Player;

class StrategicAITests {
	
	public final int NUM_PLAYERS = 1;
	public final int NUM_BASELINE_AI = 1;
	public final int NUM_STRATEGY_AI = 1;
	
	private final String ONE = "1";
	private final String TWO = "2";
	private final String THREE = "3";
	private final String RED = "Red";
	private final String GREEN = "Green";
	private final String BLUE = "Blue";
	private final String WILD = "Wild";
	
	private Card oneRedCard = new ColoredCard(ONE, RED);
	private Card twoRedCard = new ColoredCard(TWO, RED);
	private Card threeRedCard = new ColoredCard(THREE, RED);
	private Card oneGreenCard = new ColoredCard(ONE, GREEN);
	private Card twoGreenCard = new ColoredCard(TWO, GREEN);
	private Card oneBlueCard = new ColoredCard(ONE, BLUE);
	private Card twoBlueCard = new ColoredCard(TWO, BLUE);
	private Card wildCard = new WildCard(WILD);
	
	private GameBoard game = new GameBoard(NUM_PLAYERS, NUM_BASELINE_AI, NUM_STRATEGY_AI);
	private Player p0 = this.game.getPlayers().get(0);
	private BaselineAI AI1 = (BaselineAI) this.game.getPlayers().get(1);
	private StrategicAI AI2 = (StrategicAI) this.game.getPlayers().get(2);

	@Test
	public void testNoCardsInHands() {
		this.game.setCurrentPlayer(2);
		clearPlayerCards(AI2);
		assertFalse(AI2.optimalPlay());
		assert(AI2.cards.size() == 1);
	}
	
	@Test
	public void testNoLegalCards() {
		this.game.setCurrentPlayer(2);
		this.game.cardToFollow = oneRedCard;
		clearPlayerCards(AI2);
		assignACardToPlayer(AI2, twoGreenCard, 3);
		assignACardToPlayer(AI2, twoBlueCard, 3);
		assertFalse(AI2.optimalPlay());
		assert(AI2.cards.size() == 6 + 1);
	}
	
	@Test
	public void testPlayTheMostPopularColor() {
		this.game.setCurrentPlayer(2);
		this.game.cardToFollow = oneRedCard;
		clearPlayerCards(AI2);
		assignACardToPlayer(AI2, twoRedCard, 3);
		assignACardToPlayer(AI2, oneBlueCard, 2);
		assertTrue(AI2.optimalPlay());
		assert(AI2.cards.size() == 5 - 1);
	}
	
	@Test
	public void testPlayTheMostPopularType() {
		this.game.setCurrentPlayer(2);
		this.game.cardToFollow = oneRedCard;
		clearPlayerCards(AI2);
		assignACardToPlayer(AI2, twoRedCard, 2);
		assignACardToPlayer(AI2, oneBlueCard, 3);
		assertTrue(AI2.optimalPlay());
		assert(AI2.cards.size() == 5 - 1);
	}
	
	@Test
	public void testPlayWildCard() {
		this.game.setCurrentPlayer(2);
		this.game.cardToFollow = oneRedCard;
		clearPlayerCards(AI2);
		assignACardToPlayer(AI2, wildCard, 1);
		assignACardToPlayer(AI2, twoBlueCard, 3);
		assertTrue(AI2.optimalPlay());
		assert(AI2.cards.size() == 4 - 1);
	}
	
	@Test
	public void testSpecifyTheMostPopularColor() {
		this.game.setCurrentPlayer(2);
		this.game.cardToFollow = threeRedCard;
		clearPlayerCards(AI2);
		assignACardToPlayer(AI2, wildCard, 1);
		assignACardToPlayer(AI2, oneBlueCard, 3);
		assignACardToPlayer(AI2, twoBlueCard, 3);
		assignACardToPlayer(AI2, oneGreenCard, 3);
		assertTrue(AI2.optimalPlay());
		assert(AI2.specifyNewColor().compareTo(BLUE) == 0);
	}
	
	
	private void assignACardToPlayer(Player player, Card card, int num) {
		for (int i = 0; i < num; i++) {
			player.receiveACard(card);
			this.game.getPlayersCards().get(player).add(card);
		}
	}
	
	private void clearPlayerCards(Player p) {
		p.cards.clear();
		this.game.getPlayersCards().get(p).clear();
	}
}




