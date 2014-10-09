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
import operations.Replacer;

/**
 * ReplaceAudioPanel allows the user to replace a video's audio track.
 * Taken frReplaceAudioPanelom SE206 Assignment 3, paired prototype.
 */
public class ReplaceAudioPanel extends SidePanel implements ActionListener {
	GeneralPanel _generalPanel;
	BashOperations _bash;
	Replacer _overlayer;
	JFrame _frame;
	JButton _save;
	Timer _time = new Timer(1000, null);

	private JButton _btnSelect = new JButton("Select audio file");
	private JTextField _selectField = new JTextField();
	JLabel _outFileL = new JLabel("Output File Name: ");
	JTextField _outFileT = new JTextField();
	JProgressBar _working = new JProgressBar();
	JButton _cancel = new JButton("Cancel");

	boolean isUsable = true;
	String _outFile;

	public ReplaceAudioPanel(String name, GeneralPanel generalPanel) {
		super(name);
		_generalPanel = generalPanel;
		super.setupPanel();

		_generalPanel = generalPanel;
		_overlayer = new Replacer();
		_bash = new BashOperations();
		setupProgress();

		_save = new JButton("Replace");
		_save.addActionListener(this);
		_save.setActionCommand("audio");
		_save.setPreferredSize(new Dimension(250, 50));
		_selectField.setEditable(false);

		_outFileT.setPreferredSize(new Dimension(120, 20));
		_selectField.setPreferredSize(new Dimension(120, 20));

		_working.setIndeterminate(false);
		_working.setPreferredSize(new Dimension(250, 20));

		_cancel.setActionCommand("cancel");
		_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_overlayer.cancel();
			}
		});

		_btnSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String absolutePath = selectedFile.getAbsolutePath();
					_selectField.setText(absolutePath);
				}
			}
		});

		this.add(_btnSelect);
		this.add(_selectField, "wrap");
		this.add(_outFileL);
		this.add(_outFileT, "wrap");
		this.add(new JLabel("Audio from video is always overwritten"), "wrap, span");
		this.add(_save, "span, wrap");
		this.add(_working, "span, grow");
		this.add(_cancel, "span, grow");
	}

	protected void setupPanel() {
	}

	//Setup buttons to perform appropriate actions
	@Override
	public void actionPerformed(ActionEvent e) {
		if (_generalPanel.getInputField().equals("")) {
			JOptionPane.showMessageDialog(null, "No source file has been selected. Please select a file in the General tab.");
			return;
		}
		if (_selectField.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "You must select an audio file to overlay");
			return;
		}
		if (_bash.checkAudioFile(_selectField.getText()) == false) {
			JOptionPane.showMessageDialog(null, "" + _selectField.getText() + " does not appear to be an audio file");
			return;
		}
		if (formatCheck(false) == true) {
			File f = new File(_outFileT.getText());
			if (f.exists()) {
				Object[] options = { "Cancel", "Overwrite existing" };
				int result = JOptionPane.showOptionDialog(null, f.getName()
						+ " already exists. What would you like to do?",
						"Already Exists", JOptionPane.ERROR_MESSAGE,
						JOptionPane.WARNING_MESSAGE, null, options, options[1]);
				if (result == 0) {
					// Do nothing, cancelled
				} else {
					f.delete();
					_outFile = _outFileT.getText();
					beginOverlay(true);
				}
			} else {
				_outFile = _outFileT.getText();
				beginOverlay(true);
			}
		} else {
			JOptionPane.showMessageDialog(null,	"The output name must end in \".mp4\" when saving video");
		}
	}

	// Begins replacing audio stream
	private void beginOverlay(boolean b) {
		switchUsable();
		_overlayer = new Replacer();
		_overlayer.overlay(_selectField.getText(), _generalPanel.getInputField(), _outFileT.getText(), true);
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
					if (_overlayer.getWorking()) {
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
