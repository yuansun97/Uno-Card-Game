package GUI;

import UnoGame.BaselineAI;
import UnoGame.Card;
import UnoGame.Player;
import UnoGame.StrategicAI;
import UnoGame.GameBoard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameView extends View {
	
	private final String IMAGES_PATH = "src/images/";
	private final int FONT_SIZE = 18;
	private final int TEXT_HEIGHT = 120;
	private final int BUTTON_WIDTH_SMALL = 80;
	private final int BUTTON_HEIGHT_SMALL = 40;
	private final int SMALL_BUTTON_Y = 450;
	private final int CARDS_TEXT_WIDTH = 800;
	private final int INPUT_WIDTH = 100;
	private final int INPUT_HEIGHT = 25;
	
	private GameBoard game;
	private Player currentPlayer;
	
	private JLabel currentPlayerLabel;
	private JLabel currentDirectionLabel;
	private JLabel drawTwoLabel;
	private JLabel wildDrawFourLabel;
	private JLabel topCardLabel;
	private JLabel playerCardsLabel;
    
	private JButton skipButton;
	private JButton hideButton;
	private JButton revealButton;
	private JButton playButton;
	
	private JTextField playersInput;

	/**
	 * Construct a GameView class.
	 * 
	 * Create a game of three players.
	 */
	public GameView(GameBoard _game) {
		super();
		this.game = _game;
				
		addGameState();
		updatePlayerCardsDisplay();
		addInputField();
		addPlayButton();
		addTopCard();
        addDrawPile();
        addDrawButton();
        addSkipButton();
        addHideButton();
        addRevealButton();

        this.frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new GameView(new GameBoard(3, 0 , 0));
	}

	
	private void addGameState() {
        int playerIdx = this.game.getCurrentPlayer();
        addCurrentPlayer(this.game.getPlayers().get(playerIdx));
        addDrawTwo();
        addWildDrawFour();
        addTurnDirection();
	}
	
	private void updatePlayerCardsDisplay() {
		if (this.playerCardsLabel != null) {
			this.playerCardsLabel.setVisible(false);
		}
		ArrayList<String> cards = getPlayerCards();
		// System.out.println(cards);
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		for (int idx = 0; idx < cards.size(); idx++) {
			if (idx % 5 == 0) sb.append("<br/>");
			String cardName = cards.get(idx);
			sb.append(idx);
			sb.append(".(");
			sb.append(cardName);
			sb.append(") ");
		}
		sb.append("</html>");
		displayPlayerCards(sb.toString());
	}
	
	private ArrayList<String> getPlayerCards() {
		ArrayList<String> cards = new ArrayList<>();
		int currentIdx = this.game.getCurrentPlayer();
		Player player = this.game.getPlayers().get(currentIdx);
		for (Card card : player.cards) {
			cards.add(card.getColor() + "," + card.type);
		}
		return cards;
	}
	
	private void displayPlayerCards(String text) {
		this.playerCardsLabel = new JLabel(text);
		this.playerCardsLabel.setForeground(Color.WHITE);
		this.playerCardsLabel.setBounds(50, 500, CARDS_TEXT_WIDTH, TEXT_HEIGHT);
		this.playerCardsLabel.setFont(new Font(Font.DIALOG_INPUT, Font.TRUETYPE_FONT, FONT_SIZE));
		this.panel.add(this.playerCardsLabel);
	}
	
	private void addInputField() {
		this.playersInput = new JTextField();
        this.playersInput.setBounds(350, SMALL_BUTTON_Y, INPUT_WIDTH, INPUT_HEIGHT);
        panel.add(this.playersInput);
	}
	
	private void addTopCard() {
		Card topCard = this.game.cardToFollow;
		// System.out.println(topCard);
        ImageIcon upCardIcon = createIcon(IMAGES_PATH + topCard.toString() + ".png");
        if (this.topCardLabel != null) {
        	this.topCardLabel.setVisible(false);
        }
        this.topCardLabel = new JLabel(upCardIcon);
        this.topCardLabel.setBounds(300, 50, 300, 350);
        this.panel.add(this.topCardLabel);
	}
	
	private ImageIcon createIcon(String fileName) {
		ImageIcon icon = null;
        try {
            BufferedImage image = ImageIO.read(new File(fileName));
            icon = new ImageIcon(image);
        } catch (IOException e) {
            System.out.println("Failed to load image.");
        }
        return icon;
	}
	
	private void addDrawPile() {
        ImageIcon backCardIcon = createIcon(IMAGES_PATH + "back.png");
        JLabel drawPileLabel = new JLabel(backCardIcon);
        setCardLabel(drawPileLabel);
        this.panel.add(drawPileLabel);
    }
	
	private void setCardLabel(JLabel label) {
		label.setForeground(Color.WHITE);
		label.setBounds(600, 50, 250, 350);
		label.setFont(new Font(Font.DIALOG_INPUT, Font.TRUETYPE_FONT, FONT_SIZE));
	}
	
	private void addPlayButton() {
		this.playButton = new JButton("Play");
        this.playButton.setBounds(480, SMALL_BUTTON_Y, BUTTON_WIDTH_SMALL, INPUT_HEIGHT);
        this.currentPlayer = this.game.getPlayers().get(this.game.getCurrentPlayer());
        this.playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int cardIdx = Integer.valueOf(playersInput.getText());
            	currentPlayer.playACard(cardIdx);
            	updatePlayerCardsDisplay();
            	addTopCard();
            	playerCardsLabel.setVisible(true);
            }
        });
        this.panel.add(playButton);
    }
    
	private void addDrawButton() {
		this.skipButton = new JButton("Draw");
		this.skipButton.setBounds(10, SMALL_BUTTON_Y, BUTTON_WIDTH_SMALL, BUTTON_HEIGHT_SMALL);
		currentPlayer = this.game.getPlayers().get(this.game.getCurrentPlayer());
		this.skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	currentPlayer.drawACard();
            	updatePlayerCardsDisplay();
            }
        });
        this.panel.add(this.skipButton);
    }
	
	private void addSkipButton() {
		this.skipButton = new JButton("Skip");
		this.skipButton.setBounds(90, SMALL_BUTTON_Y, BUTTON_WIDTH_SMALL, BUTTON_HEIGHT_SMALL);
		currentPlayer = this.game.getPlayers().get(this.game.getCurrentPlayer());
		this.skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	moveToNextPlayer();
            }
        });
        this.panel.add(this.skipButton);
	}
	
	/**
	 * The controller of the game, pass the game to the next player 
	 * 	and update the according UI components. 
	 */
	private void moveToNextPlayer() {
		// winner check
		if (this.game.gameEnds()) {
			new EndView(this.game.getCurrentPlayer(), this.game);
		}
		
		// move to the next player
		game.nextPlayer();
		int idx = game.getCurrentPlayer();
		Player newPlayer = game.getPlayers().get(idx);
		this.currentPlayer = newPlayer;
		
		// update UI
		updateUI(newPlayer);
		
		// Penalty check
		this.game.penaltyDrawCardsCheck();
		
		// AI auto play
		if (this.currentPlayer instanceof BaselineAI) {
			BaselineAI baseAI = (BaselineAI) this.currentPlayer;
			this.playerCardsLabel.setVisible(true);
			baseAI.randomPlay();
			moveToNextPlayer();
		}
		
		if (this.currentPlayer instanceof StrategicAI) {
			StrategicAI stratAI = (StrategicAI) this.currentPlayer;
			this.playerCardsLabel.setVisible(true);
			stratAI.optimalPlay();
			moveToNextPlayer();
		}
		
	}
	
	/**
	 * Update the UI components.
	 * 
	 * @param newPlayer		the next player
	 */
	private void updateUI(Player newPlayer) {
		addTopCard();
		addCurrentPlayer(newPlayer);
		addDrawTwo();
		addWildDrawFour();
		addTurnDirection();
		updatePlayerCardsDisplay();
		playerCardsLabel.setVisible(false);
	}
	    
	private void addHideButton() {
		this.hideButton = new JButton("Hide");
		this.hideButton.setBounds(800, SMALL_BUTTON_Y, BUTTON_WIDTH_SMALL, BUTTON_HEIGHT_SMALL);
		this.hideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCards();
            }
        });
        this.panel.add(this.hideButton);
    }
    
	private void addRevealButton() {
		this.revealButton = new JButton("Reveal");
		this.revealButton.setBounds(875, SMALL_BUTTON_Y, BUTTON_WIDTH_SMALL, BUTTON_HEIGHT_SMALL);
		this.revealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                revealCards();
            }
        });
        this.panel.add(this.revealButton);
    }
	
	private void hideCards() {
		this.playerCardsLabel.setVisible(false);
    }
	
	private void revealCards() {
		this.playerCardsLabel.setVisible(true);
    }
	
	private void setGameStateLabel(JLabel label, int Y) {
		label.setForeground(Color.WHITE);
        label.setBounds(20, Y, 300, TEXT_HEIGHT);
        label.setFont(new Font(Font.DIALOG_INPUT, Font.TRUETYPE_FONT, FONT_SIZE));
	}
	
	private void addCurrentPlayer(Player player) {
		if (this.currentPlayerLabel != null) {
			this.currentPlayerLabel.setVisible(false);
		}
		this.currentPlayerLabel = new JLabel(player + "'s Turn");
		setGameStateLabel(this.currentPlayerLabel, 20);
		this.panel.add(this.currentPlayerLabel);
	}
	
	private void addDrawTwo() {
		int drawTwoPenalty = this.game.getDrawTwoPenalty();
		if (this.drawTwoLabel != null) {
			this.drawTwoLabel.setVisible(false);
		}
		this.drawTwoLabel = new JLabel("DrawTwo penalty: " + drawTwoPenalty);
		setGameStateLabel(this.drawTwoLabel, 50);
		this.panel.add(this.drawTwoLabel);
	}
	
	private void addWildDrawFour() {
		int wildDrawFourPenalty = this.game.getWildDrawFourPenalty();
		if (this.wildDrawFourLabel != null) {
			this.wildDrawFourLabel.setVisible(false);
		}
		this.wildDrawFourLabel = new JLabel("WildDrawFour penalty: " + wildDrawFourPenalty);
		setGameStateLabel(this.wildDrawFourLabel, 70);
		this.panel.add(this.wildDrawFourLabel);
	}
	
	private void addTurnDirection() {
		int turnDirection = this.game.getTurnDirection();
		String dir = (turnDirection == 1) ? "+1" : "-1";
		if (this.currentDirectionLabel != null) {
			this.currentDirectionLabel.setVisible(false);
		}
		this.currentDirectionLabel = new JLabel("Turn direction: " + dir);
		setGameStateLabel(this.currentDirectionLabel, 90);
		this.panel.add(this.currentDirectionLabel);
	}

}
