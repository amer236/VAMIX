package sidePanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import operations.BashOperations;
import operations.Concatenater;

/**
 * ConcatPanel allows the user to join two media files together.
 * Taken from SE206 Assignment 3, paired prototype.
 */
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
	
	JSpinner startHour = new JSpinner(new SpinnerNumberModel(0,0,99,1));
	JSpinner startMin = new JSpinner(new SpinnerNumberModel(0,0,59,1));
	JSpinner startSec = new JSpinner(new SpinnerNumberModel(0,0,59,1));
	
	JSpinner endHour = new JSpinner(new SpinnerNumberModel(0,0,99,1));
	JSpinner endMin = new JSpinner(new SpinnerNumberModel(0,0,59,1));
	JSpinner endSec = new JSpinner(new SpinnerNumberModel(0,0,59,1));
	
	JLabel text = new JLabel("Text: ");
	JTextArea subtitle = new JTextArea();
	
	JButton save = new JButton("Save Subtitle File");

	
	boolean isUsable = true;
	String _outFile;

	public SubtitlesPanel(String name, GeneralPanel generalPanel) {
		super(name);
		super.setupPanel();

		_generalPanel = generalPanel;
		}

	protected void setupPanel() {
		subtitleData = new SubtitleTableModel();
		subsTable = new JTable(subtitleData);
		subsTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		subsTable.setFillsViewportHeight(true);
		
		JScrollPane scrollPane = new JScrollPane(subsTable);
        
		this.add(scrollPane, "grow, wrap, span");

		this.add(deleteButton, "span, wrap, grow");
		this.add(new JLabel(""));
		this.add(hours, "grow");
		this.add(mins, "grow");
		this.add(secs, "wrap, grow");
		this.add(start, "grow");
		this.add(startHour, "grow");
		this.add(startMin, "grow");
		this.add(startSec, "grow");
		this.add(new JLabel(""), "wrap");
		this.add(finish, "grow");
		this.add(endHour, "grow");
		this.add(endMin, "grow");
		this.add(endSec, "grow");
		this.add(new JLabel(""), "wrap");
		this.add(text, "grow");
		this.add(subtitle, "grow, wrap, span");

		this.add(addButton, "span, grow, wrap");
		this.add(save, "grow, span");
		
		addButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				subtitleData.addDataRow(startHour.getValue().toString() + ":" + startMin.getValue().toString() + ":" + startSec.getValue().toString(),
						endHour.getValue().toString() + ":" + endMin.getValue().toString() + ":" + endSec.getValue().toString(),
						subtitle.getText());
			}
		});
		
		deleteButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(subsTable.getSelectedRow() != -1){
					subtitleData.deleteDataRow(subsTable.getSelectedRow());
				}
			}
		});
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(_generalPanel.getInputField().equals("")){
					JOptionPane.showMessageDialog(null,
							"No source file has been selected. Please select a file in the General tab.");
					return;
				}
				saveSRTFile();
			}
		});
	}

	public void saveSRTFile() {
		String outtext;
		File inputMedia = new File(_generalPanel.getInputField());
		int location = inputMedia.getAbsolutePath().lastIndexOf(".");
		if(location == -1){
			outtext = inputMedia.getName();
		}else{
			outtext = _generalPanel.getInputField().substring(0, location);
		}
		try {
			File outSRT = new File(outtext + ".srt");
			outSRT.createNewFile();
			PrintWriter output = new PrintWriter(outtext + ".srt");
			System.out.println(outtext);
			for(int i = 0; i < subtitleData.getRowCount(); i++){
				output.println();
				output.println(i);
				output.println(subtitleData.getValueAt(i, 0) + ",000 --> " + subtitleData.getValueAt(i, 1) + ",000");
				output.println(subtitleData.getValueAt(i, 2));
			}
			output.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}