# sp21-cs242-assignment1

Uno Game
=====

An implementation for the game Uno.


Table of Contents
-----------------

* [Requirements](#requirements)
* [Purpose](#purpose)
* [Usage](#usage)


Requirements
----

Uno requires the following to run:

* Java
* IDE to run Java applications (Eclipse, IntelliJ, etc.)

Purpose
----

The purpose of this project is to provide a working implementation of the game Uno.

Project Structure
-----
There are four main classes:
* Card
* Player
* GameBoard

The Card class is an abstract class with the subclasses:
* ColoredCard 
* WildCard

UnitTests have been created for each class.

Code Style
-----

The project uses Standard Java styling practices with Java doc comments, etc.

Code Examples
-----

###Method from GameBoard class that initialize players
```js
	/**
	 * Initialize players.
	 */
	private void initializePlayers() {
		players = new ArrayList<Player>();
		for (int p_idx = 0; p_idx < NUM_OF_PLAYERS; p_idx++) {
			players.add(new Player(p_idx, this));
		}	
	}
```

###Method from GameBoard class that deals cards to a specific players
```js
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

```
###Method from Card class that overrides the toString function
```js
	@Override
	public String toString() {
		return "[" + type + "#" + getColor() + "]";
	}
```
