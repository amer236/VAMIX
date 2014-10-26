package sidePanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import operations.BashOperations;
import operations.Concatenater;
import videoPanel.MediaTime;

/**
 * SubtitlesPanel allows the user to create and add subtitles to the player
 */
@SuppressWarnings("serial")
public class SubtitlesPanel extends SidePanel {
	GeneralPanel _generalPanel;
	BashOperations _bash;
	Concatenater _concater;
	JFrame _frame;

	JButton _saveVideo;

	SubtitleTableModel subtitleData;
	JTable subsTable;

	JButton addButton = new JButton("Add Row");
	JButton deleteButton = new JButton("Delete Selected Row");

	JLabel hours = new JLabel("Hours");
	JLabel mins = new JLabel("Minutes");
	JLabel secs = new JLabel("Seconds");

	JLabel start = new JLabel("Start Time");
	JLabel finish = new JLabel("End Time");

	JSpinner startHour = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	JSpinner startMin = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
	JSpinner startSec = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
	
	JButton startTime = new JButton("Set");

	JSpinner endHour = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	JSpinner endMin = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
	JSpinner endSec = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

	JButton endTime = new JButton("Set");
	
	JLabel text = new JLabel("Text: ");
	JTextArea subtitle = new JTextArea();

	JButton save = new JButton("Save Subtitle File");

	JButton loadSRT = new JButton("Load Subtitle file");

	boolean isUsable = true;
	String _outFile;

	MediaTime mediaTime;
	
	public SubtitlesPanel(String name, GeneralPanel generalPanel, MediaTime currentTime) {
		super(name);
		
		_generalPanel = generalPanel;
		mediaTime = currentTime;
		super.setupPanel();

	}

	/**
	 * Setup this panel
	 */
	protected void setupPanel() {
		subtitleData = new SubtitleTableModel();
		subsTable = new JTable(subtitleData);
		subsTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		subsTable.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(subsTable);

		this.add(scrollPane, "grow, wrap, span");

		this.add(new JLabel(""));
		this.add(hours, "grow");
		this.add(mins, "grow");
		this.add(secs, "wrap, grow");
		this.add(start, "grow");
		this.add(startHour, "grow");
		this.add(startMin, "grow");
		this.add(startSec, "grow");
		this.add(startTime, "wrap");
		this.add(finish, "grow");
		this.add(endHour, "grow");
		this.add(endMin, "grow");
		this.add(endSec, "grow");
		this.add(endTime, "wrap");
		this.add(text, "grow");
		this.add(subtitle, "grow, wrap, span");

		this.add(addButton, "span, grow, wrap");
		this.add(deleteButton, "span, wrap, grow");

		this.add(new JSeparator(), "span, grow, wrap");
		this.add(save, "grow, span, wrap");
		this.add(loadSRT, "grow, span");

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				subtitleData.addDataRow(startHour.getValue().toString() + ":"
						+ startMin.getValue().toString() + ":"
						+ startSec.getValue().toString(), endHour.getValue()
						.toString()
						+ ":"
						+ endMin.getValue().toString()
						+ ":"
						+ endSec.getValue().toString(), subtitle.getText());
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (subsTable.getSelectedRow() != -1) {
					subtitleData.deleteDataRow(subsTable.getSelectedRow());
				}
			}
		});

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				saveSRTFile();
			}
		});

		loadSRT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String absolutePath = selectedFile.getAbsolutePath();
					loadSRTFile(absolutePath);
				}
			}

		});
		
		startTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startHour.setValue(Integer.parseInt(mediaTime.getCurrentTime()[0]));
				startMin.setValue(Integer.parseInt(mediaTime.getCurrentTime()[1]));
				startSec.setValue(Integer.parseInt(mediaTime.getCurrentTime()[2]));
			}
		});
		
		endTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				endHour.setValue(Integer.parseInt(mediaTime.getCurrentTime()[0]));
				endMin.setValue(Integer.parseInt(mediaTime.getCurrentTime()[1]));
				endSec.setValue(Integer.parseInt(mediaTime.getCurrentTime()[2]));
			}
		});
		
		ChangeListener chListener = new ChangeListener(){
			@SuppressWarnings("rawtypes")
			@Override
			public void stateChanged(ChangeEvent e) {
				int totalStart = ((int) startHour.getValue() * 3600) + ((int) startMin.getValue() * 60) + (int) startSec.getValue();
				int totalEnd = ((int) endHour.getValue() * 3600) + ((int) endMin.getValue() * 60) + (int) endSec.getValue();
				if(totalStart > totalEnd){
					endHour.setModel(new SpinnerNumberModel((Number) startHour.getValue(), (Comparable) startHour.getValue(), 99, 1));
					endMin.setModel(new SpinnerNumberModel((Number) startMin.getValue(), (Comparable) startMin.getValue(), 59, 1));
					endSec.setModel(new SpinnerNumberModel((Number) startSec.getValue(), (Comparable) startSec.getValue(), 59, 1));
				}else{
					endHour.setModel(new SpinnerNumberModel((Number) endHour.getValue(), 0, 99, 1));
					endMin.setModel(new SpinnerNumberModel((Number) endMin.getValue(), 0, 59, 1));
					endSec.setModel(new SpinnerNumberModel((Number) endSec.getValue(), 0, 59, 1));
				}
			}
		};
		
		startHour.addChangeListener(chListener);
		startMin.addChangeListener(chListener);
		startSec.addChangeListener(chListener);
		endHour.addChangeListener(chListener);
		endMin.addChangeListener(chListener);
		endSec.addChangeListener(chListener);
	}

	/**
	 * Save subtitle file from the table
	 */
	public void saveSRTFile() {
		if (_generalPanel.getInputField().equals("")) {
			JOptionPane
					.showMessageDialog(
							null,
							"No source file has been selected. Please select a file in the General tab to save SRT.");
			return;
		}
		String outtext;
		File inputMedia = new File(_generalPanel.getInputField());
		int location = inputMedia.getAbsolutePath().lastIndexOf(".");
		if (location == -1) {
			outtext = inputMedia.getName();
		} else {
			outtext = _generalPanel.getInputField().substring(0, location);
		}
		try {
			File outSRT = new File(outtext + ".srt");
			if (outSRT.exists()) {
				Object[] options = { "Cancel", "Overwrite existing" };
				int result = JOptionPane
						.showOptionDialog(
								null,
								outSRT.getName()
										+ " already exists. What would you like to do?",
								"Already Exists", JOptionPane.ERROR_MESSAGE,
								JOptionPane.WARNING_MESSAGE, null, options,
								options[1]);
				if (result == 0) {
					// Do nothing
				} else {
					outSRT.createNewFile();
					PrintWriter output = new PrintWriter(outtext + ".srt");
					for (int i = 0; i < subtitleData.getRowCount(); i++) {
						output.println();
						output.println(i);
						output.println(subtitleData.getValueAt(i, 0)
								+ ",000 --> " + subtitleData.getValueAt(i, 1)
								+ ",000");
						output.println(subtitleData.getValueAt(i, 2));
					}
					output.close();
					JOptionPane.showMessageDialog(null, "Subtitle file saved");
				}
			} else {
				outSRT.createNewFile();
				PrintWriter output = new PrintWriter(outtext + ".srt");
				for (int i = 0; i < subtitleData.getRowCount(); i++) {
					output.println();
					output.println(i);
					output.println(subtitleData.getValueAt(i, 0)
							+ ",000 --> " + subtitleData.getValueAt(i, 1)
							+ ",000");
					output.println(subtitleData.getValueAt(i, 2));
				}
				output.close();
				JOptionPane.showMessageDialog(null, "Subtitle file saved");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Load a subtitle file into the table
	 * @param absolutePath of the SRT file
	 */
	private void loadSRTFile(String absolutePath) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(absolutePath));
			String line;

			while ((line = br.readLine()) != null) {
				if (line.equals("")) {
				} else {
					line = br.readLine();
					String[] split = line.split("-->");
					String[] starts = split[0].split(",");
					String[] stops = split[1].split(",");
					String message = (line = br.readLine());
					while ((line = br.readLine()) != null) {
						if (line.equals("") == false) {
							message = message.concat("\n" + line);
						} else {
							break;
						}
					}
					subtitleData.addDataRow(starts[0].trim(), stops[0].trim(),
							message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}