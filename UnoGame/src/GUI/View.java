package GUI;

import javax.swing.*;
import java.awt.*;

public class View {
	
	protected final int FRAME_WIDTH = 1000;
	protected final int FRAME_HEIGHT = 700;

	private final String TITLE = "Uno!";
	
	public JPanel panel;
	public JFrame frame;
	
	/**
	 * Construct a View object.
	 */
	public View() {
		this.panel = initializePanel();
        this.frame = initializeFrame();
	}
	
	/**
     * Initialize a black color panel
     *
     */
    public JPanel initializePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder());
        panel.setBackground(Color.BLACK);
        panel.setLayout(null);
        return panel;
    }

    /**
     * Add the given panel to a frame
     *
     */
    public JFrame initializeFrame() {
        JFrame frame = new JFrame();
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(TITLE);
        return frame;
    }
}
