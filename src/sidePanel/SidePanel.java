package sidePanel;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * SidePanel is the parent class to all panels on the editor side of the split pane.
 * Taken from SE206 Assignment 3, paired prototype.
 */
@SuppressWarnings("serial")
public class SidePanel extends JPanel{
	String _panelName;
	TitledBorder title;

	public SidePanel(String name) {
		_panelName = name;
		title = BorderFactory.createTitledBorder(name);
		this.setBorder(title);
	}

	protected void setupPanel() {
		this.setLayout(new MigLayout());
		this.setupPanel();
	}
}