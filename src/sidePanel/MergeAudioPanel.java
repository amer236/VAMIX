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
import javax.swing.JTextField;
import javax.swing.Timer;

import operations.BashOperations;
import operations.Merger;

/**
 * MergeAudioPanel allows the user to merge two different audio tracks.
 * Taken from SE206 Assignment 3, paired prototype.
 */
@SuppressWarnings("serial")
public class MergeAudioPanel extends SidePanel implements ActionListener {
	GeneralPanel _generalPanel;
	BashOperations _bash;
	Merger _merger;
	JFrame _frame;
	JButton _save;
	Timer _time = new Timer(1000, null);

	private JButton _btnSelect0 = new JButton("Select audio file");
	private JTextField _selectField0 = new JTextField();
	private JButton _btnSelect1 = new JButton("Select audio file");
	private JTextField _selectField1 = new JTextField();
	JLabel _outFileL = new JLabel("Output File Name: ");
	JTextField _outFileT = new JTextField();
	JProgressBar _working = new JProgressBar();
	JButton _cancel = new JButton("Cancel");

	boolean isUsable = true;
	String _outFile;

	public MergeAudioPanel(String name, GeneralPanel generalPanel) {
		super(name);
		super.setupPanel();

		_generalPanel = generalPanel;
		_merger = new Merger();
		_bash = new BashOperations();
		setupProgress();

		_save = new JButton("Merge");
		_save.addActionListener(this);
		_save.setActionCommand("audio");
		_save.setPreferredSize(new Dimension(250, 50));

		_selectField0.setEditable(false);
		_selectField1.setEditable(false);

		_outFileT.setPreferredSize(new Dimension(120, 20));

		_selectField0.setPreferredSize(new Dimension(120, 20));
		_selectField1.setPreferredSize(new Dimension(120, 20));

		_working.setIndeterminate(false);
		_working.setPreferredSize(new Dimension(250, 20));

		_cancel.setActionCommand("cancel");
		_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_merger.cancel();
			}
		});

		_btnSelect0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String absolutePath = selectedFile.getAbsolutePath();
					_selectField0.setText(absolutePath);
				}
			}
		});

		_btnSelect1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String absolutePath = selectedFile.getAbsolutePath();
					_selectField1.setText(absolutePath);
				}
			}
		});

		this.add(_btnSelect0);
		this.add(_selectField0, "wrap");
		this.add(_btnSelect1);
		this.add(_selectField1, "wrap");
		this.add(_outFileL);
		this.add(_outFileT, "wrap");
		this.add(_save, "span, wrap");
		this.add(_working, "span, grow");
		this.add(_cancel, "span, grow");
	}

	protected void setupPanel() {
	}

	// Setup buttons to perform appropriate actions
	@Override
	public void actionPerformed(ActionEvent e) {
		if (_selectField0.getText().equals("")
				|| _selectField1.getText().equals("")) {
			JOptionPane.showMessageDialog(null,
					"You must select two audio files to merge");
			return;
		}
		if (_bash.checkAudioFile(_selectField0.getText()) == false) {
			JOptionPane.showMessageDialog(null, "" + _selectField0.getText()
					+ " does not appear to be an audio file");
			return;
		}
		if (_bash.checkAudioFile(_selectField1.getText()) == false) {
			JOptionPane.showMessageDialog(null, "" + _selectField1.getText()
					+ " does not appear to be an audio file");
			return;
		}
		if (formatCheck(true) == true) {
			File f = new File(_outFileT.getText());
			if (f.exists()) {
				Object[] options = { "Cancel", "Overwrite existing" };
				int result = JOptionPane.showOptionDialog(null, f.getName()
						+ " already exists. What would you like to do?",
						"Already Exists", JOptionPane.ERROR_MESSAGE,
						JOptionPane.WARNING_MESSAGE, null, options, options[1]);
				if (result == 0) {
					// Do nothing
				} else {
					f.delete();
					_outFile = _outFileT.getText();
					beginMerge(true);
				}
			} else {
				_outFile = _outFileT.getText();
				beginMerge(true);
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"The output name must end in \".mp3\" when saving audio");
		}
	}

	// Begin merging audio
	private void beginMerge(boolean b) {
		switchUsable();
		_merger = new Merger();
		_merger.merge(_selectField0.getText(), _selectField1.getText(),
				_outFileT.getText());
		_time.start();
	}

	// Checks only the appropriate inputs are made
	// Checks that the output ends in .mp3 or .mp4 depending on isAudio
	private boolean formatCheck(boolean isAudio) {
		if (isAudio == true) {
			if (_outFileT.getText().endsWith(".mp3")) {
				return true;
			} else {
				return false;
			}
		} else {
			if (_outFileT.getText().endsWith(".mp4")) {
				return true;
			} else {
				return false;
			}
		}
	}

	// Sets up the progress bar
	public void setupProgress() {
		_time.setActionCommand("tick");
		_time.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("tick")) {
					if (_merger.getWorking()) {
						_working.setIndeterminate(true);
					} else {
						_working.setIndeterminate(false);
						switchUsable();
						_time.stop();
					}
				}
			}
		});
	}

	//Switch the buttons' enabled characteristic
	protected void switchUsable() {
		if (isUsable == true) {
			isUsable = false;
			_save.setEnabled(false);
		} else {
			isUsable = true;
			_save.setEnabled(true);
		}
	}
}
