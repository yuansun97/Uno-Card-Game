package tests;

import UnoGame.Card;
import UnoGame.ColoredCard;
import UnoGame.WildCard;
import UnoGame.Player;
import UnoGame.GameBoard;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class GameBoardTests {
	
	private final int NUM_OF_TOTAL_CARDS = 108;
	private final int PLAYER_INITIAL_CARDS = 7;
	private final int PLAYER_COUNT = 4;
	
	private GameBoard game = new GameBoard(PLAYER_COUNT, 0, 0);
	
	@Test
	public void testInitializePlayers() {
		assert(game.NUM_OF_PLAYERS == PLAYER_COUNT);
		assert(game.getPlayers().size() == PLAYER_COUNT);
	}
	
	@Test
	public void testInitializeDrawPile() {
		assert(game.getPile().size() == NUM_OF_TOTAL_CARDS);
		
		Map<String, Integer> cardDict = putCardsInMap(game.getPile());
		assert(cardDict.size() == 54);
	}

	@Test
	public void testAssignInitalCardsToPlayers() {
		assert(game.getPlayersCards().size() == PLAYER_COUNT);
		for (Player player : game.getPlayers()) {
			assert(game.getPlayersCards().get(player).size( )== PLAYER_INITIAL_CARDS);
		}
	}
	
	@Test
	public void testInitializeCardToFollow() {
		assertTrue(game.cardToFollow.isColored());
	}
	
	@Test
	public void testDealCardsTo() {
		int prevCount = game.getNumOfCardsInDrawPile();
		Player player = game.getPlayers().get(0);
		
		game.dealCardsTo(player, 1);
		assert(game.getNumOfCardsInDrawPile() == prevCount - 1);
		assert(player.cards.size() == PLAYER_INITIAL_CARDS + 1);
		
		game.dealCardsTo(player, 2);
		assert(game.getNumOfCardsInDrawPile() == prevCount - 1 - 2);
		assert(player.cards.size() == PLAYER_INITIAL_CARDS + 1 + 2);
	}
	
	@Test 
	public void testNextPlayer() {
		assert(game.getCurrentPlayer() == 0);
		game.nextPlayer();
		assert(game.getCurrentPlayer() == 1);
		game.nextPlayer();
		assert(game.getCurrentPlayer() == 2);
		game.nextPlayer();
		assert(game.getCurrentPlayer() == 3);
		game.nextPlayer();
		assert(game.getCurrentPlayer() == 0);	// test circle
	}
	
	@Test
	public void testGameEnds() {
		assertFalse(game.gameEnds());
		Player player = game.getPlayers().get(0);
		game.getPlayersCards().get(player).clear();
		assert(game.getCurrentPlayer() == 0);
		assertTrue(game.gameEnds());
	}
	
	@Test
	public void testValidCard() {
		Card oneCard = new ColoredCard("1", "Red");
		Player p1 = game.getPlayers().get(1);
		game.setCurrentPlayer(1);
		
		assertFalse(game.placeACard(p1, oneCard));
	}
	
	@Test
	public void testSkip() {
		Card skipCard = new ColoredCard("Skip", "Red");
		int player1 = 1;
		
		playTheCard(player1, skipCard);
		game.nextPlayer();
				
		assert(game.getCurrentPlayer() == 3);
	}
	
	@Test
	public void testReverse() {
		Card reverseCard = new ColoredCard("Reverse", "Red");
		int player1 = 1;
		
		playTheCard(player1, reverseCard);
		assert(game.getTurnDirection() == -1);
		
		// now direction changed to -1
		game.nextPlayer();
		assert(game.getCurrentPlayer() == 0);
		
		game.nextPlayer();
		assert(game.getCurrentPlayer() == 3);
		
		playTheCard(3, reverseCard);
		
		// now direction changed to 1
		game.nextPlayer();
		assert(game.getCurrentPlayer() == 0);
	}
	
	@Test
	public void testShuffle() {
		Player p0 = game.getPlayers().get(0);
		int cardsCount = p0.cards.size();
		
		game.setNumOfCardsInDrawPile(1);
		assert(game.getNumOfCardsInDrawPile() == 1);
		game.dealCardsTo(p0, 2);
		assert(p0.cards.size() == cardsCount + 2);
	}
	
	@Test
	public void testPlaceACard() {
		Player p = game.getPlayers().get(game.getCurrentPlayer());
		assignACardToPlayer(p, game.cardToFollow);
		assertTrue(game.placeACard(p, game.cardToFollow));
	}
	
	@Test
	public void testInitialPenalty() {
		assertFalse(game.penaltyDrawCardsCheck());
	}
	
	
	private Card drawTwoCard = new ColoredCard("DrawTwo", "Red");
	private Card oneRedCard = new ColoredCard("One", "Red");
	private Card wildDrawFourCard = new WildCard("WildDrawFour");
	private Card reverseCard = new ColoredCard("Reverse", "Red");
	
	@Test
	public void testDrawTwo() {
		int p1 = 1;
		Player p2 = game.getPlayers().get(2);
		game.getPlayersCards().get(p2).clear();
		
		playTheCard(p1, drawTwoCard);
		game.nextPlayer();
		
		assert(game.getCurrentPlayer() == 2);
		assert(game.getDrawTwoPenalty() == 2);
		
		game.penaltyDrawCardsCheck();
		
		// after draw two
		assert(game.getPlayersCards().get(p2).size() == 2);
	}
	
	@Test 
	public void testWild() {
		int p1 = 1;
		Player p2 = game.getPlayers().get(2);
		game.getPlayersCards().get(p2).clear();
		assignACardToPlayer(p2, oneRedCard);
		
		playTheCard(p1, wildDrawFourCard);
		game.nextPlayer();
		
		assert(game.getCurrentPlayer() == 2);
		assert(game.getWildDrawFourPenalty() == 4);
		
		game.penaltyDrawCardsCheck();
		
		// after draw four
		assert(game.getPlayersCards().get(p2).size() == 4 + 1);
	}

	@Test
	public void testValidWildDrawFour() {
		game.setCurrentPlayer(0);
		Player p1 = game.getPlayers().get(1);
		Player p2 = game.getPlayers().get(2);
		
		playTheCard(0, wildDrawFourCard);
		assert(game.getWildDrawFourPenalty() == 4);
		
		clearPlayerCards(p1);
		assignACardToPlayer(p1, wildDrawFourCard);
		game.nextPlayer();
		game.penaltyDrawCardsCheck();
		assert(game.getWildDrawFourPenalty() == 8);
		
		assignACardToPlayer(p2, wildDrawFourCard);
		assignACardToPlayer(p2, game.cardToFollow);
		game.nextPlayer();
		game.penaltyDrawCardsCheck();
		assert(game.getWildDrawFourPenalty() == 0);
	}
	
	@Test
	public void testDoubleReverse() {
		game.setCurrentPlayer(0);
		assert(game.getTurnDirection() == 1);
		
		playTheCard(0, reverseCard);
		assert(game.getTurnDirection() == -1);
		
		List<Card> playerCards0 = game.getPlayers().get(0).cards;
		
		playTheCard(1, reverseCard);
		assert(game.getTurnDirection() == 1);
		assert(playerCards0.equals(game.getPlayers().get(1).cards));
		
		playTheCard(2, reverseCard);
		assert(game.getTurnDirection() == -1);
		assert(playerCards0.equals(game.getPlayers().get(0).cards));
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
	
	private void playTheCard(int playerIdx, Card card) {
		Player player = game.getPlayers().get(playerIdx);
		assignACardToPlayer(player, card);
		
		game.cardToFollow.switchColor(card.getColor());
		game.setCurrentPlayer(playerIdx);
		game.placeACard(player, card);
	}
	
	private void assignACardToPlayer(Player player, Card card) {
		player.receiveACard(card);
		game.getPlayersCards().get(player).add(card);
	}
	
	private void clearPlayerCards(Player p) {
		p.cards.clear();
		game.getPlayersCards().get(p).clear();
	}
}


