package operations;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JPanel;

import sidePanel.GeneralPanel;
import sidePanel.VidEditingPanel;

public class StateOrganiser {
	String _loc;
	GeneralPanel _gPanel;
	JPanel _audioPanel;
	VidEditingPanel _vPanel;
	ArrayList<String> stateList = new ArrayList<String>();

	public StateOrganiser(String logFileLoc, VidEditingPanel vPanel) {
		_loc = logFileLoc;
		_vPanel = vPanel;
	}

	public void initialise() {

	}

	public void load() {
		BufferedReader br;
		ArrayList<String> stateList = new ArrayList<String>();
		try {

			br = new BufferedReader(new FileReader(System.getProperty("user.home") + "/Documents/VAMIX_STATE.txt"));
			String line;

			while ((line  = br.readLine()) != null) {
				stateList.add(line);
			}
			
			_vPanel.loadState(stateList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// gets states and saves to a file
	public void save() {
		ArrayList<String> saveStateList = new ArrayList<String>();
		saveStateList = _vPanel.returnState();
		try {
			PrintWriter writer = new PrintWriter(System.getProperty("user.home") + "/Documents/VAMIX_STATE.txt", "UTF-8");
			for (String s : saveStateList) {
				writer.println(s);
			}
			writer.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
