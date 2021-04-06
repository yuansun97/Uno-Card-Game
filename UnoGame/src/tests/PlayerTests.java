package tests;

import UnoGame.Player;
import UnoGame.WildCard;
import UnoGame.Card;
import UnoGame.ColoredCard;
import UnoGame.GameBoard;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlayerTests {
	
	private final int ONE = 1;
	private final String ZERO = "0";
	private final String RED = "Red";
	private final String WILD = "Wild";
	
	private Player player = new Player(ONE, null);
	private Card zeroRedCard = new ColoredCard(ZERO, RED);
	private Card wildCard = new WildCard(WILD);
	
	private GameBoard game = new GameBoard(2, 0, 0);
	private Player p0 = game.getPlayers().get(0);
	private Player p1 = game.getPlayers().get(1);

	@Test
	public void testCreatePlayer() {
		assert(player.playerIndex == ONE);
	}
	
	@Test
	public void testReceiveACard() {
		assert(player.cards.size() == 0);
		player.receiveACard(zeroRedCard);
		assert(player.cards.size() == 1);
		assert(player.cards.get(0) == zeroRedCard);
	}
	
	@Test 
	public void testRemoveAcard() {
		player.receiveACard(zeroRedCard);
		assert(player.cards.size() == 1);
		player.removeACard(0);
		assert(player.cards.size() == 0);
	}
	
	@Test
	public void testDrawACard() {
		assert(game.getCurrentPlayer() == 0);
		int cardsCount = p0.cards.size();
		
		assertTrue(p0.drawACard());
		assert(p0.cards.size() == cardsCount + 1);
		
		assertFalse(p1.drawACard());
		assert(p1.cards.size() == cardsCount);
	}
	
	@Test
	public void testPlayACard() {
		assert(game.getCurrentPlayer() == 0);
		int cardsCount0 = p0.cards.size();
		int cardsCount1 = p1.cards.size();
		
		assertFalse(p1.playACard(0));
		assert(p1.cards.size() == cardsCount1 + 1);
		
		assertFalse(p0.playACard(cardsCount0));
				
		assignACardToPlayer(p0, wildCard);
		int cardIdx = validCardIndex(p0, game.cardToFollow);
		assertTrue(p0.playACard(cardIdx));
		assert(p0.cards.size() == cardsCount0 + 1 - 1);
	}
	
	private void assignACardToPlayer(Player player, Card card) {
		player.receiveACard(card);
		game.getPlayersCards().get(player).add(card);
	}
	
	private int validCardIndex(Player p, Card toMatch) {
		for (int i = 0; i < p.cards.size(); i++) {
			Card card = p.cards.get(i);
			if (card.isWild() || card.isMatchedWith(toMatch)) {
				return i;
			}
		}
		return -1;
	}
}




