package GUI;

import UnoGame.Player;
import UnoGame.GameBoard;

import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

public class EndView extends View {
	
	private final int TEXT_WIDTH = 300;
	private final int TEXT_HEIGHT = 100;
	private final int TEXT_X = FRAME_WIDTH / 2 - TEXT_WIDTH / 2;
	private final int TEXT_Y = FRAME_HEIGHT / 2;
	private final int FONT_SIZE = 30;
	
	private int winnerIdx;
	private GameBoard game;

	/**
	 * Construct an EndView object.
	 * 
	 * Display the winner on the view.
	 * 
	 * @param _winner	the index of the winner
	 * @param _game		the game being played
	 */
	public EndView(int _winner, GameBoard _game) {
		super();
		winnerIdx = _winner;
		game = _game;
		addWinnerText();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new EndView(1, new GameBoard(3, 0, 0));
	}

	private void addWinnerText() {        
        JLabel label = new JLabel("Player " + winnerIdx + " Wins!");
        label.setForeground(Color.WHITE);
        label.setBounds(TEXT_X, TEXT_Y, TEXT_WIDTH, TEXT_HEIGHT);
        label.setFont(new Font(Font.DIALOG_INPUT, Font.TRUETYPE_FONT, FONT_SIZE));
        panel.add(label);
	}
}
