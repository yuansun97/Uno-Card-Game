package UnoGame;

import java.util.*;

public class StrategicAI extends Player {
	
	/**
	 * Map the color to the number of cards with this color in hands.
	 */
	private HashMap<String, Integer> colorCount;
	/**
	 * Map the type to the number of cards of this type in hands.
	 */
	private HashMap<String, Integer> typeCount;

	/**
	 * Construct a BaselineAI object.
	 * @param _playerIndex	the index for the AI player
	 * @param _game			the game the AI player is in
	 */
	public StrategicAI(int _playerIndex, GameBoard _game) {
		super(_playerIndex, _game);
	}
	
	/**
	 * Play a playable card with a popular color in hands,
	 *   or a playable card with a popular type in hands.
	 * Draw cards if no legal cards present.
	 *  
	 * @return 	true		if a legal card played
	 * 			false		no legal cards in hand and draw cards
	 */
	public boolean optimalPlay() {
		if (playTheBestMatched()) {
			return true;
		}
		
		if (playWildCard()) {
			return true;
		}
		
		this.drawACard();
		System.out.println("StratAI drawed a card.");
		return false;
	}
	
	/**
	 * Return the most popular color in hands,
	 *  or "Red" if no colored cards in hands.
	 *  
	 * // * built-in function in Collection. 
	 */
	@Override
	public String specifyNewColor() {
		int maxCount = -1;
		String newColor = "Red";
		for (String color : colorCount.keySet()) {
			int cnt = colorCount.get(color);
			if (cnt > maxCount) {
				maxCount = cnt;
				newColor = color;
			}
		}
		return newColor;
	}
	
	private boolean playTheBestMatched() {
		updateColorTypeCountMaps();
		String color = this.game.cardToFollow.getColor();
		String type = this.game.cardToFollow.type;
		
		if (colorCount.getOrDefault(color, 0) == 0 && typeCount.getOrDefault(type, 0) == 0) {
			return false;
		}
		
		if (colorCount.getOrDefault(color, 0) >= typeCount.getOrDefault(type, 0)) {
			// follow the color
			playTheMatched(color);
		} else {
			// follow the type
			playTheMatched(type);
		}
		
		return true;
	}
	
	private boolean playWildCard() {
		for (int i = 0; i < this.cards.size(); i++) {
			Card card = this.cards.get(i);
			if (card.isWild()) {
				this.playACard(i);
				System.out.println("StratAI played (" + card + ")");
				return true;
			}
		}
		return false;
	}
	
	private void playTheMatched(String colorOrType) {
		for (int i = 0; i < this.cards.size(); i++) {
			Card card = this.cards.get(i);
			if (card.getColor().compareTo(colorOrType) == 0
					|| card.type.compareTo(colorOrType) == 0) {
				System.out.println("StratAI played (" + card + ")");
				this.playACard(i);
				return;
			}
		}
	}
	
	private void updateColorTypeCountMaps() {
		colorCount = new HashMap<String, Integer>();
		typeCount = new HashMap<String, Integer>();
		for (Card card : this.cards) {
			if (card.isColored()) {
				String color = card.getColor();
				String type = card.type;
				colorCount.put(color, colorCount.getOrDefault(color, 0) + 1);
				typeCount.put(type, typeCount.getOrDefault(type, 0) + 1);
			}
		}
	}
	
}
