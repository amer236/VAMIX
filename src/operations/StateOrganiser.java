package operations;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import sidePanel.GeneralPanel;
import sidePanel.SubtitlesPanel;
import sidePanel.VidEditingPanel;

/**
 * Allows the user to save and load the VidEditingPanel's fields.
 * Taken from SE206 Assignment 3, paired prototype.
 */
public class StateOrganiser {
	String _loc;
	GeneralPanel _gPanel;
	JPanel _audioPanel;
	VidEditingPanel _vPanel;
	SubtitlesPanel _sPanel;
	ArrayList<String> stateList = new ArrayList<String>();

	public StateOrganiser(String logFileLoc, VidEditingPanel vPanel, SubtitlesPanel sPanel) {
		_loc = logFileLoc;
		_vPanel = vPanel;
		_sPanel = sPanel;
	}

	/**
	 * Load the editing tab from the text file
	 */
	public void load() {
		BufferedReader br;
		ArrayList<String> stateList = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(System.getProperty("user.home") + "/Documents/VAMIX_STATE.txt"));
			String line;

			while ((line = br.readLine()) != null) {
				stateList.add(line);
			}
			
			_vPanel.loadState(stateList);
			JOptionPane.showMessageDialog(null, "Video tab has been loaded");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets states and saves to a file
	 */
	public void save() {
		_sPanel.saveSRTFile();
		ArrayList<String> saveStateList = new ArrayList<String>();
		saveStateList = _vPanel.returnState();
		try {
			PrintWriter writer = new PrintWriter(System.getProperty("user.home") + "/Documents/VAMIX_STATE.txt", "UTF-8");
			for (String s : saveStateList) {
				writer.println(s);
			}
			writer.close();
			JOptionPane.showMessageDialog(null, "Video tab has been saved");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
