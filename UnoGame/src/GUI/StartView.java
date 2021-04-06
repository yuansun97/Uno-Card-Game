package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import UnoGame.GameBoard;

public class StartView extends View {
	
	private final String PROMPT_1 = "Number of Players:     ";
	private final String PROMPT_2 = "Number of Baseline AI: ";
	private final String PROMPT_3 = "Number of Strategic AI:";
	private final String[] PROMPTS = {PROMPT_1, PROMPT_2, PROMPT_3};
	private final String BUTTON_TEXT = "PLAY";
	
	private final int LOGO_WIDTH = FRAME_WIDTH / 4;
	private final int LOGO_X = FRAME_WIDTH / 2 - LOGO_WIDTH / 2;
	private final int LOGO_Y = FRAME_HEIGHT / 2 - LOGO_WIDTH;
	
	private final int PROMPT_WIDTH = 360;
	private final int PROMPT_HEIGHT = 200;
	private final int PROMPT_X1 = FRAME_WIDTH / 2 - PROMPT_WIDTH + 150;
	private final int PROMPT_Y = 320;
	private final int FONT_SIZE = 20;
	private final int PROMPT_SPACE = 38;
	
	private final int INPUT_WIDTH = 100;
	private final int INPUT_HEIGHT = 20;
	private final int INPUT_X1 = FRAME_WIDTH / 2 + INPUT_WIDTH / 2 + 30;
	private final int INPUT_Y = 410;
	
	private final int BUTTON_WIDTH = 70;
	private final int BUTTON_HEIGHT = 40;
	private final int BUTTON_X = FRAME_WIDTH / 2 - BUTTON_WIDTH / 2;
	private final int BUTTON_Y = 570;
	
	private JTextField[] playersInputs = new JTextField[3];
	
	/**
	 * Construct a StartView object.
	 * 
	 * Display the initial view of the application.
	 */
    public StartView() {
    	super();
        addUnoLogo();
        int numOfPrompts = 3;
        for (int i = 0; i < numOfPrompts; i++) {
        	addPrompt(PROMPTS[i], PROMPT_X1, PROMPT_Y + PROMPT_SPACE * i);
        	addInputField(i, INPUT_X1, INPUT_Y + PROMPT_SPACE * i);
        }
        addButton();
        this.frame.setVisible(true);
    }
    
	public static void main(String[] args) {
		new StartView();
	}

	private void addUnoLogo() {
        try {
            BufferedImage unoLogo = ImageIO.read(new File("src/images/back.png"));
            JLabel label = new JLabel(new ImageIcon(unoLogo), SwingConstants.CENTER);
            label.setBounds(LOGO_X, LOGO_Y, LOGO_WIDTH, LOGO_WIDTH); 
            panel.add(label);
        } catch (IOException e) {
            System.out.print("Failed to load image.");
        }
    }
	
	private void addPrompt(String text, int X, int Y) {
		JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setBounds(X, Y, PROMPT_WIDTH, PROMPT_HEIGHT);
        label.setFont(new Font(Font.DIALOG_INPUT, Font.TRUETYPE_FONT, FONT_SIZE));
        this.panel.add(label);
	}
	
	private void addInputField(int i, int X, int Y) {
		this.playersInputs[i] = new JTextField();
		this.playersInputs[i].setBounds(X, Y, INPUT_WIDTH, INPUT_HEIGHT);
        panel.add(this.playersInputs[i]);
	}
	
	private void addButton() {
		JButton startButton = new JButton(BUTTON_TEXT);
        startButton.setBounds(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int a = Integer.valueOf(playersInputs[0].getText());
            	int b = Integer.valueOf(playersInputs[1].getText());
            	int c = Integer.valueOf(playersInputs[2].getText());
            	new GameView(new GameBoard(a, b, c));
            }
        });
        panel.add(startButton);
	}

}
