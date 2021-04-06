package UnoGame;

import java.util.*;

/**
 * This class represents the player.
 */
public class Player {

	/**
	 * Holds the player's index.
	 */
	public final int playerIndex;
	/**
	 * Holds the cards the player currently has.
	 */
	public List<Card> cards;
	/**
	 * Holds the game the player is currently playing.
	 */
	protected final GameBoard game;
	
	
	/**
	 * Construct a player with given #playerIndex.
	 */
	public Player(int _playerIndex, GameBoard _game) {
		playerIndex = _playerIndex;
		cards = new ArrayList<Card>();
		game = _game;
	}
	
	/**
	 * Receive the dealt card from deck and add it to the list.
	 * 
	 */
	public void receiveACard(Card card) {
		cards.add(card);
	}
	
	public void removeACard(int cardIdx) {
		cards.remove(cardIdx);
	}
	
	/**
	 * Request to draw a card from the deck.
	 * 
	 */
	public boolean drawACard() {
		return game.requestACard(this);
	}
	
	/**
	 * 
	 * @return the new color
	 */
	public String specifyNewColor() {
		Random rand = new Random();
		int idx = rand.nextInt(ColoredCard.COLORS.length);
		return ColoredCard.COLORS[idx];
	}
	
	/**
	 * Play the specified card with given index.
	 * 
	 * @param cardIdx	the card to play
	 * @return true		if the card can be played
	 * 		   false	o.w.
	 */
	public boolean playACard(int cardIdx) {
		// TODO
		if (cardIdx < 0 || cardIdx >= cards.size()) {
			return false;
		}
		boolean validPlay = game.placeACard(this, cards.get(cardIdx));
		return validPlay;
	}
	
	@Override
	public String toString() {
		return "Player #" + playerIndex;
	}

}
