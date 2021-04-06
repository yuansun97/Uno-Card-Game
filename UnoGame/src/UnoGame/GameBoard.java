package UnoGame;
import java.util.*;

/**
 * This class keeps track of the game state and rules.
 *
 * loop:
 * 	1. Penalty check (server side)
 * 		1.1. has penalty: jump to the next player
 * 		1.2. no penalty : continue
 * 	2. Make a move (client side)
 * 		- Place a card (server)
 * 			2.1. place a colored card to match
 * 				  and update the card to follow
 * 			2.2. place a Wild card
 * 		- Draw a card and play it if possible
 *  3. Winner check 
 *  	3.1. end the game if someone wins
 *  4. Move to the next player
 */
public class GameBoard {

	public final int NUM_OF_TOTAL_CARDS = 108;
	public final int PLAYER_INITIAL_CARDS = 7;
	public final int NUM_OF_PLAYERS;
	public final int NUM_OF_HUMAN_PLAYERS;
	public final int NUM_OF_BASELINE_AI;
	public final int NUM_OF_STRATEGIC_AI;
	
	/**
	 * Holds all the players in this game.
	 */
	private final List<Player> players;
	/**
	 * Holds the cards each player has.
	 */
	private Map<Player, List<Card>> playersCards;
	/**
	 * Holds the player of current turn, default to 0.
	 */
	private int currentPlayer = 0;
	/**
	 * Holds the turn direction.
	 *  1: increasing order (default)
	 * -1: decreasing order
	 */
	private int turnDirection = 1;
	/**
	 * Holds the penalty number of cards to draw from DrawTwo
	 */
	private int drawTwoPenalty = 0;
	/**
	 * Holds the penalty number of cards to draw from WildDrawFour
	 */
	private int wildDrawFourPenalty = 0;
	/**
	 * Holds the top card on the discard pile.
	 */
	public Card cardToFollow;
	/**
	 * Holds all the cards(e.g. 108).
	 * 
	 * The first #numOfCardsInDrawPile of cards are the draw pile,
	 *  and the rest are the discard pile.
	 */
	private List<Card> pile;
	/**
	 * Holds the count of cards in draw pile.
	 */
	private int numOfCardsInDrawPile;
	/**
	 * A random number generator used in dealing a card.
	 */
	private Random Rand;

	
	/**
	 * Construct a game with given #numOfPlayers, #numOfBaselineAI, #numOfStrategicAI
	 * 
	 * @param 	numOfPlayers		
	 * 			numOfBaselineAI
	 * 			numOfStrategicAI
	 */
	public GameBoard(int numOfPlayers, int numOfBaselineAI, int numOfStrategicAI) {
		// throw an exception if not enough players
		if (numOfPlayers + numOfBaselineAI + numOfStrategicAI < 2) {
			throw new IllegalArgumentException("Players should arrange in 2 ~ 10.");
		}
		
		// initialize total number of cards in the draw pile
		this.numOfCardsInDrawPile = NUM_OF_TOTAL_CARDS;
		this.NUM_OF_PLAYERS = numOfPlayers + numOfBaselineAI + numOfStrategicAI;
		this.NUM_OF_HUMAN_PLAYERS = numOfPlayers;
		this.NUM_OF_BASELINE_AI = numOfBaselineAI;
		this.NUM_OF_STRATEGIC_AI = numOfStrategicAI;
		this.pile = new ArrayList<Card>();
		this.cardToFollow = new ColoredCard("null", "null");
		this.Rand = new Random();
		this.players = new ArrayList<Player>();
		
		initializePlayers();
		initializeBaselineAI();
		initializeStrategicAI();
		initializeDrawPile();
		assignInitalCardsToPlayers();
		initializeCardToFollow();
	}
	
	/**
	 * Check whether there is a penalty for the current player.
	 * 
	 * Deal the penalty cards to the current player and clear the penalty,
	 * 	if the current player doesn't have the according action card.
	 * 
	 * Stack the penalty, otherwise. 
	 * 
	 * @return 	true	has penalty
	 * 			false	no penalty
	 */
	public boolean penaltyDrawCardsCheck() {
		// check for DrawTwo penalty
		// boolean drawTwo = penaltyCheck("DrawTwo", drawTwoPenalty, 2);
		boolean drawTwo = checkDrawTwo();
		
		// check for WildDrawFour penalty
		// boolean wildDrawFour = penaltyCheck("WildDrawFour", wildDrawFourPenalty, 4);
		boolean wildDrawFour = checkWildDrawFour();
		
		return drawTwo || wildDrawFour;
	}
	
	/**
	 * Try to place the play made by the player with given card.
	 * 
	 * @param player	the player intend to play
	 * @param card		the card intend to be played
	 * @return true		the play is valid,
	 * 			false	otherwise
	 */
	public boolean placeACard(Player player, Card card) {
		// player validity check: turn?
		if (!validPlayer(player)) {
			System.out.println("Wrong player turn: " + player);
			conductPenaltyTo(player);
			return false;
		}
		
		// card validity check: hasTheCard? 
		if (!hasTheCard(player, card)) {
			System.out.println(player + " does not have card " + card);
			conductPenaltyTo(player);
			return false;
		}
		
		int cardIndex = indexOf(playersCards.get(player), card);
		// wild card?
		if (card.isWild()) {
			removeCardFromPlayer(player, cardIndex);
			placeWildCard(card.type);
			return true;
		}
		
		// place colored card
		if (card.isMatchedWith(cardToFollow)) {
			removeCardFromPlayer(player, cardIndex);
			placeColoredCard(card);
			updateCardToFollow(card);
			return true;
		} 
		
		return false;
	}
	
	/**
	 * Player request to draw a card from the pile.
	 * 
	 * @param player
	 * @return	true	if valid, false o.w.
	 */
	public boolean requestACard(Player player) {
		if (validPlayer(player)) {
			dealCardsTo(player, 1);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determine whether the game is over.
	 */
	public boolean gameEnds() {
		Player player = players.get(currentPlayer);
		return playersCards.get(player).size() == 0;
	}
	
	/**
	 * Move to the next player.
	 */
	public void nextPlayer() {
		currentPlayer += turnDirection;
		if (currentPlayer < 0) {
			currentPlayer += NUM_OF_PLAYERS;
		}
		if (currentPlayer >= NUM_OF_PLAYERS) {
			currentPlayer -= NUM_OF_PLAYERS;
		}
	}
	
	/**
	 * Deal #n card to the given #player.
	 * 
	 * Randomly pick up a card, assign it to the player, 
	 *  and add it to the playersCards map as a record on the server side,
	 *  repeat n times.
	 * 
	 * @param player
	 */
	public void dealCardsTo(Player player, int n) {
		for (int i = 0; i < n; i++) {
			final Card card = pickACardFromDrawPile();
			player.receiveACard(card);
			playersCards.get(player).add(card);
		}
	}

	/**
	 * Initialize players.
	 */
	private void initializePlayers() {
		for (int cnt = 0; cnt < NUM_OF_HUMAN_PLAYERS; cnt++) {
			this.players.add(new Player(this.players.size(), this));
		}	
	}
	
	private void initializeBaselineAI() {
		for (int cnt = 0; cnt < NUM_OF_BASELINE_AI; cnt++) {
			this.players.add(new BaselineAI(this.players.size(), this));
		}
	}
	
	private void initializeStrategicAI() {
		for (int cnt = 0; cnt < NUM_OF_STRATEGIC_AI; cnt++) {
			this.players.add(new StrategicAI(this.players.size(), this));
		}
	}
	
	/**
	 * Initialize the draw pile with 108 cards.
	 */
	private void initializeDrawPile() {
		
		// initialize colored cards
		ColoredCard.addCardsToPile(pile);
		
		// initialize Wild cards
		WildCard.addCardsToPile(pile);
	}
	
	/**
	 * Assign the initial cards to each player in the beginning.
	 */
	private void assignInitalCardsToPlayers() {
		// the map that keeps track of each player's cards 
		playersCards = new HashMap<Player, List<Card>>();
		
		for (Player player : players) {
			playersCards.put(player, new ArrayList<Card>());
			// deal #PLAYER_INITIAL_CARDS cards to the player
			dealCardsTo(player, PLAYER_INITIAL_CARDS);
		}
	}
	
	/**
	 * Initialize the card to follow in the beginning.
	 */
	private void initializeCardToFollow() {
		Card card;
		do {
			card = pickACardFromDrawPile();
		} while (!card.isColored());
		updateCardToFollow(card);
	}
	
	/**
	 * Update the card to follow with the given card.
	 * 
	 * @param card	the card to copy from
	 */
	private void updateCardToFollow(Card card) {
		cardToFollow.type = card.type;
		cardToFollow.switchColor(card.getColor());
	}
	
	/**
	 * Randomly pick up a card from the draw pile.
	 * 
	 * Generate a random number in [0, numOfCardsInDrawPile) 
	 *  as the index of the card to be assigned, and switch this card 
	 *  with the last card in the draw pile (at index numOfCardsInDrawPile - 1).
	 * 
	 * @return a random card from the draw pile 
	 *  and shuffle if the draw pile is used up
	 */
	private Card pickACardFromDrawPile() {
		if (numOfCardsInDrawPile <= 0) {
			shuffle();
		}
		
		// generate a random index
		int cardIdx = Rand.nextInt(numOfCardsInDrawPile);
		Card toReturn = pile.get(cardIdx);
		
		// move the dealt card to the discard pile and decease the count in draw pile
		Collections.swap(pile, cardIdx, numOfCardsInDrawPile - 1);
		numOfCardsInDrawPile--;
		
		return toReturn;
	}
	
	/**
	 * Shuffle cards.
	 * 
	 * The top card in the discard pile is set aside and 
	 *  the rest of the discard pile is shuffled to a new deck.
	 */
	private void shuffle() {
		int topCardIdx = indexOf(pile, cardToFollow);
		Collections.swap(pile, topCardIdx, NUM_OF_TOTAL_CARDS - 1);
		numOfCardsInDrawPile = NUM_OF_TOTAL_CARDS - 1;
	}
	
	/**
	 * Find the index of the target card in the pile.
	 * 
	 * @param pile		the pile of cards
	 * @param target	the target card to search
	 * @return index of the target card, -1 if not existed
	 */
	private int indexOf(List<Card> pile, Card target) {
		for (int idx = 0; idx < pile.size(); idx++) {
			if (pile.get(idx).equalsTo(target)) {
				return idx;
			}
		}
		return -1;
	}
	
	private boolean checkDrawTwo() {
		if (drawTwoPenalty == 0) {
			return false;
		}
		
		Player player = players.get(currentPlayer);
		int idx = hasTheCardType(player, "DrawTwo");
		if (idx != -1) {
			// play the according card and stack penalty to the next player
			drawTwoPenalty += 2;
			removeCardFromPlayer(player, idx);
		} else {
			// receive penalty
			dealCardsTo(player, drawTwoPenalty);
			drawTwoPenalty = 0;
		}
		
		return true;
	}
	
	private boolean checkWildDrawFour() {
		// "WildDrawFour", wildDrawFourPenalty
		if (wildDrawFourPenalty == 0) {
			return false;
		}
		
		Player player = players.get(currentPlayer);
		int idx = hasTheCardType(player, "WildDrawFour");
		if (validWildDrawFour() && idx != -1) {
			// play the according card and stack penalty to the next player
			wildDrawFourPenalty += 4;
			wild();
			removeCardFromPlayer(player, idx);
		} else {
			// receive penalty
			dealCardsTo(player, wildDrawFourPenalty);
			wildDrawFourPenalty = 0;
		}
		return true;
	}
	
	private void removeCardFromPlayer(Player player, int cardIndex) {
		player.removeACard(cardIndex);
		playersCards.get(player).remove(cardIndex);
	}
	
	private void skip() {
		nextPlayer();
	}
	
	private void reverse() {
		turnDirection = 0 - turnDirection;
	}
	
	private void drawTwo() {
		drawTwoPenalty += 2;
	}
	
	private void wild() {
		String newColor = players.get(currentPlayer).specifyNewColor();
		cardToFollow.switchColor(newColor);
	}
	
	/**
	 * Place the Wild card and implement the effects .
	 * 
	 * @param wildType
	 */
	private void placeWildCard(String wildType) {
		wild();
		if (wildType.compareTo("WildDrawFour") == 0) {
			wildDrawFourPenalty += 4;
		}
	}
	
	private void placeColoredCard(Card card) {
		if (!card.isFunctional()) {
			// numbered cards
			return;
		}
		
		// action cards
		String type = card.type;
		if (type.compareTo("Skip") == 0) {
			skip();
		} else if (type.compareTo("Reverse") == 0) {
			reverse();
			// check Double Reverse custom rule
			if (cardToFollow.type.compareTo("Reverse") == 0) {
				switchHands();
			}
		} else if (type.compareTo("DrawTwo") == 0) {
			drawTwo();
		}
	}
	
	/**
	 * The effect of Double Reverse.
	 * 
	 * When two reverses are played in succession your hand is passed to 
	 *  the person next to you in the direction of the last reverse. 
	 * The entire table will then have switched hands.
	 */
	private void switchHands() {
		if (turnDirection == 1) {
			switchHandsIncOrderOnServerEnd();
		} else {
			switchHandsDecOrderOnServerEnd();
		}
	}
	
	// turnDirection == 1
	private void switchHandsIncOrderOnServerEnd() {
		Player tailPlayer = players.get(NUM_OF_PLAYERS - 1);
		List<Card> tailPlayerCardsOnServerEnd = playersCards.get(tailPlayer);
		List<Card> tailPlayerCardsOnClientEnd = tailPlayer.cards;
		for (int i = NUM_OF_PLAYERS - 1; i > 0; i--) {
			Player currPlayer = players.get(i);
			Player prevPlayer = players.get(i - 1);
			playersCards.put(currPlayer, playersCards.get(prevPlayer));
			currPlayer.cards = prevPlayer.cards;
		}
		playersCards.put(players.get(0), tailPlayerCardsOnServerEnd);
		players.get(0).cards = tailPlayerCardsOnClientEnd;
	}
	
	// turnDirection == -1
	private void switchHandsDecOrderOnServerEnd() {
		Player headPlayer = players.get(0);
		List<Card> headPlayerCardsOnServerEnd = playersCards.get(headPlayer);
		List<Card> headPlayerCardsOnClientEnd = headPlayer.cards;
		for (int i = 0; i < NUM_OF_PLAYERS - 1; i++) {
			Player currPlayer = players.get(i);
			Player prevPlayer = players.get(i + 1);
			playersCards.put(currPlayer, playersCards.get(prevPlayer));
			currPlayer.cards = prevPlayer.cards;
		}
		playersCards.put(players.get(NUM_OF_PLAYERS - 1), headPlayerCardsOnServerEnd);
		players.get(NUM_OF_PLAYERS - 1).cards = headPlayerCardsOnClientEnd;
	}
	
	/**
	 * Return whether it is the specified player's turn.
	 * 
	 * @param playerIdx		the player tend to play
	 */
	private boolean validPlayer(Player player) {
		return player.playerIndex == currentPlayer;
	}
	
	/**
	 * Check whether the player has the specified card.
	 * 
	 * @param player		
	 * @param cardToPlay	
	 * @return true 	if has such a card, false o.w.
	 */
	private boolean hasTheCard(Player player, Card cardToPlay) {
		for (Card card : playersCards.get(player)) {
			if (cardToPlay.equalsTo(card)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check whether the player has the specified type card.
	 * 
	 * @return the index of the target type card,
	 * 			-1 if not existed
	 */
	private int hasTheCardType(Player player, String type) {
		int idx = 0;
		for (Card card : playersCards.get(player)) {
			if (card.type.compareTo(type) == 0) {
				return idx;
			}
			idx++;
		}
		return -1;
	}
	
	/**
	 * Bonus point for additional rule of Wild Draw Four
	 * 
	 */
	private boolean validWildDrawFour() {
		Player player = players.get(currentPlayer);
		for (Card card : playersCards.get(player)) {
			if (card.isMatchedWith(cardToFollow)) {
				return false;
			}
		}
		return true;
	}
	
	private void conductPenaltyTo(Player player) {
		dealCardsTo(player, 1);
	}
	
	/**
	 * Getter for the list of players.
	 * 
	 * @return the list of players in the game
	 */
	public List<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Getter for current player.
	 * 
	 * @return the index of the current player
	 */
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Getter for the turn direction.
	 * 
	 * @return the current turn direction
	 */
	public int getTurnDirection() {
		return turnDirection;
	}
	
	/**
	 * Getter for the penalty from DrawTwo card.
	 * 
	 * @return the penalty of DrawTwo
	 */
	public int getDrawTwoPenalty() {
		return drawTwoPenalty;
	}
	
	/**
	 * Getter for the penalty from WildDrawFour card.
	 * 
	 * @return the penalty of WildDrawFour
	 */
	public int getWildDrawFourPenalty() {
		return wildDrawFourPenalty;
	}
	
	/**
	 * Getter for all the players' cards.
	 * 
	 * @return the map that store each player's cards
	 */
	public Map<Player, List<Card>> getPlayersCards() {
		return playersCards;
	}
	
	/**
	 * Getter for the cards pile.
	 * 
	 * @return the cards pile
	 */
	public List<Card> getPile() {
		return pile;
	}
	
	/**
	 * Getter for the number of cards in the draw pile.
	 * 
	 * @return the number of cards in the draw pile
	 */
	public int getNumOfCardsInDrawPile() {
		return numOfCardsInDrawPile;
	}
	
	/**
	 * Setter to set the current player.
	 * For testing only.
	 * 
	 * @param p		the player to be playing
	 */
	public void setCurrentPlayer(int p) {
		currentPlayer = p;
	}
	
	/**
	 * Setter to set the number of cards in the draw pile.
	 * For testing only.
	 * 
	 * @param setNum	the number of cards to set
	 */
	public void setNumOfCardsInDrawPile(int setNum) {
		numOfCardsInDrawPile = setNum;
	}
	
}


