package UnoGame;

public class BaselineAI extends Player {

	/**
	 * Construct a BaselineAI object.
	 * @param _playerIndex	the index for the AI player
	 * @param _game			the game the AI player is in
	 */
	public BaselineAI(int _playerIndex, GameBoard _game) {
		super(_playerIndex, _game);
	}
	
	/**
	 * Iterate through all cards this player has, and play the first legal card,
	 *  draw cards if no legal cards present.
	 *  
	 * @return 	true		if a legal card played
	 * 			false		no legal cards in hand and draw cards
	 */
	public boolean randomPlay() {
		int numOfCards = this.cards.size();
		for (int i = 0; i < numOfCards; i++) {
			Card card = this.cards.get(i);
			if (this.playACard(i)) {
				System.out.println("BaseAI played (" + card + ")");
				return true;
			}
		}
		this.drawACard();
		System.out.println("BaseAI drawed a card.");
		return false;
	}

}
