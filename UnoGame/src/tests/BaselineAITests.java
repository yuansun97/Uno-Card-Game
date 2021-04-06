package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import UnoGame.Card;
import UnoGame.ColoredCard;
import UnoGame.GameBoard;
import UnoGame.Player;
import UnoGame.BaselineAI;

class BaselineAITests {
	
	public final int NUM_OF_PLAYERS = 2;
	public final int NUM_OF_BASELINE_AI = 1;
	
	private final String ONE = "1";
	private final String TWO = "2";
	private final String RED = "Red";
	private final String GREEN = "Green";
	private Card twoRedCard = new ColoredCard(TWO, RED);
	private Card oneGreenCard = new ColoredCard(ONE, GREEN);
	
	private GameBoard game = new GameBoard(NUM_OF_PLAYERS, NUM_OF_BASELINE_AI, 0);
	private Player p0 = this.game.getPlayers().get(0);
	private Player p1 = this.game.getPlayers().get(1);
	private BaselineAI AI2 = (BaselineAI) this.game.getPlayers().get(2);

	@Test
	public void testLegalCardsPresent() {
		this.game.setCurrentPlayer(2);
		Card topCard = this.game.cardToFollow;
		assignACardToPlayer(AI2, topCard);
		assertTrue(AI2.randomPlay());
	}
	
	@Test
	public void testNoLegalCardsInHands() {
		this.game.setCurrentPlayer(2);
		clearPlayerCards(AI2);
		assertFalse(AI2.randomPlay());
		assert(AI2.cards.size() == 1);
	}
	
	@Test
	public void testRandomHands() {
		this.game.setCurrentPlayer(2);
		this.game.cardToFollow = oneGreenCard;
		clearPlayerCards(AI2);
		assignACardToPlayer(AI2, twoRedCard);
		assignACardToPlayer(AI2, twoRedCard);
		assertFalse(AI2.randomPlay());
	}
	
	
	private void assignACardToPlayer(Player player, Card card) {
		player.receiveACard(card);
		this.game.getPlayersCards().get(player).add(card);
	}
	
	private void clearPlayerCards(Player p) {
		p.cards.clear();
		this.game.getPlayersCards().get(p).clear();
	}
}
