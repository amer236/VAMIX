package sidePanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.TableModel;

import operations.BashOperations;
import operations.Concatenater;

/**
 * ConcatPanel allows the user to concat two media files together.
 * Taken from SE206 Assignment 3, paired prototype.
 */
public class SubtitlesPanel extends SidePanel {
	GeneralPanel _generalPanel;
	BashOperations _bash;
	Concatenater _concater;
	JFrame _frame;
	
	JButton _saveVideo;
	
	TableModel subtitleData = new SubtitleTableModel();
	JTable subsTable = new JTable(subtitleData);
	
	JButton addButton = new JButton("Add Row");
	
	boolean isUsable = true;
	String _outFile;

	public SubtitlesPanel(String name, GeneralPanel generalPanel) {
		super(name);

		super.setupPanel();

		_generalPanel = generalPanel;
		

		this.add(subsTable);
		}

	protected void setupPanel() {
		this.add(addButton);
		addButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				subsTable.getModel().
				
			}
			
		});
	}

}