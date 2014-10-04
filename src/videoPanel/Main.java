package videoPanel;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class to kick everything off.
 * Taken from SE206 Assignment 3, paired prototype.
 */
public class Main {
	public static void main(String[] args) {

		// Change look and feel
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
			// Oh well, worked at home
		}

		// Begin program
		MainGUI gui = new MainGUI();
		gui.createGUI();
	}
}
