package sidePanel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;

import operations.Extractor;

/**
 * ExtractPanel allows the user to strip out audio, video, or both from specific.
 * Taken from SE206 Assignment 3, paired prototype.
 * time slots
 */
@SuppressWarnings("serial")
public class ExtractPanel extends SidePanel implements ActionListener {
	GeneralPanel _fsp;
	Extractor _extractor;
	JFrame _frame;
	JButton _confirmAudio;
	JButton _confirmVideo;
	JButton _confirmBoth;

	// Labels and corresponding text fields
	JLabel sTimeL = new JLabel("Start Time: ");
	JTextField sTimeT = new JTextField();
	JLabel tTimeL = new JLabel("Total Time: ");
	JTextField tTimeT = new JTextField();
	JLabel outFileL = new JLabel("Output File Name: ");
	JTextField outFileT = new JTextField();

	JProgressBar _working = new JProgressBar();
	Timer _time = new Timer(1000, null);
	JButton _cancel = new JButton("Cancel");

	String _outFile;
	String _sTime;
	String _tTime;

	boolean isUsable = true;

	public ExtractPanel(String name, GeneralPanel fsPanel) {
		super(name);
		_fsp = fsPanel;
		super.setupPanel();
		setupProgress();

		_fsp = fsPanel;
		_extractor = new Extractor();

		_confirmAudio = new JButton("Only audio");
		_confirmAudio.addActionListener(this);
		_confirmAudio.setActionCommand("audio");

		_confirmVideo = new JButton("Only video");
		_confirmVideo.addActionListener(this);
		_confirmVideo.setActionCommand("video");

		_confirmBoth = new JButton("Both");
		_confirmBoth.addActionListener(this);
		_confirmBoth.setActionCommand("both");

		_cancel.setActionCommand("cancel");
		_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_extractor.cancel();
			}
		});

		JPanel inner = new JPanel();
		inner.setLayout(new GridLayout(3, 2));

		inner.add(sTimeL);
		inner.add(sTimeT);
		inner.add(tTimeL);
		inner.add(tTimeT);
		inner.add(outFileL);
		inner.add(outFileT);

		JPanel inner2 = new JPanel();
		inner2.add(_confirmAudio);
		inner2.add(_confirmVideo);

		_working.setIndeterminate(false);
		_working.setPreferredSize(new Dimension(250, 20));

		this.add(inner, "wrap, span");
		this.add(new JLabel("All times must be in seconds"), "span, wrap");
		this.add(inner2, "wrap, grow");
		this.add(_confirmBoth, "wrap, span, grow");
		this.add(_working, "wrap");
		this.add(_cancel, "span, grow");
	}
	
	protected void setupPanel() {
	}

	/**
	 * Setup button actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (_fsp.getInputField().equals("")) {
			JOptionPane.showMessageDialog(null,
					"No source file has been selected. Please select a file in the General tab.");
			return;
		}
		// Strip audio
		if (e.getActionCommand().equals("audio")) {
			// Check fields, and then begin extraction
			if (formatCheck(true) == true) {
				File f = new File(outFileT.getText());
				if (f.exists()) {
					Object[] options = { "Cancel", "Overwrite existing" };
					int result = JOptionPane.showOptionDialog(null, f.getName()
							+ " already exists. What would you like to do?",
							"Already Exists", JOptionPane.ERROR_MESSAGE,
							JOptionPane.WARNING_MESSAGE, null, options,
							options[1]);
					if (result == 0) {
						// Do nothing, cancelled
					} else {
						// Overwrite
						f.delete();
						_outFile = outFileT.getText();
						_sTime = sTimeT.getText();
						_tTime = tTimeT.getText();
						beginExtract(0);
					}
				} else {
					_outFile = outFileT.getText();
					_sTime = sTimeT.getText();
					_tTime = tTimeT.getText();
					beginExtract(0);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Only numbers are valid in the time fields, and the output name must end in .mp3");
			}
			// Strip video
		} else if (e.getActionCommand().equals("video")) {
			// Check fields, and then begin extraction
			if (formatCheck(false) == true) {
				File f = new File(outFileT.getText());
				if (f.exists()) {
					Object[] options = { "Cancel", "Overwrite existing" };
					int result = JOptionPane.showOptionDialog(null, f.getName()
							+ " already exists. What would you like to do?",
							"Already Exists", JOptionPane.ERROR_MESSAGE,
							JOptionPane.WARNING_MESSAGE, null, options,
							options[1]);
					if (result == 0) {
						// Do nothing, cancelled
					} else {
						f.delete();
						_outFile = outFileT.getText();
						_sTime = sTimeT.getText();
						_tTime = tTimeT.getText();
						beginExtract(1);
					}
				} else {
					_outFile = outFileT.getText();
					_sTime = sTimeT.getText();
					_tTime = tTimeT.getText();
					beginExtract(1);
				}
			} else {
				JOptionPane
						.showMessageDialog(null, "Only numbers are valid in the time fields, and the output name must end in .mp4");
			}
			// Strip both
		} else if (e.getActionCommand().equals("both")) {
			// Check fields, and then begin extraction
			if (formatCheck(false) == true) {
				File f = new File(outFileT.getText());
				if (f.exists()) {
					Object[] options = { "Cancel", "Overwrite existing" };
					int result = JOptionPane.showOptionDialog(null, f.getName()
							+ " already exists. What would you like to do?",
							"Already Exists", JOptionPane.ERROR_MESSAGE,
							JOptionPane.WARNING_MESSAGE, null, options,
							options[1]);
					if (result == 0) {
						// Do nothing, cancelled
					} else {
						f.delete();
						_outFile = outFileT.getText();
						_sTime = sTimeT.getText();
						_tTime = tTimeT.getText();
						beginExtract(2);
					}
				} else {
					_outFile = outFileT.getText();
					_sTime = sTimeT.getText();
					_tTime = tTimeT.getText();
					beginExtract(2);
				}
			} else {
				JOptionPane
						.showMessageDialog(null, "Only numbers are valid in the time fields, and the output name must end in .mp4");
			}
		}
	}

	/**
	 * Checks only the appropriate inputs are made
	 * Checks that the output ends in .mp3 or .mp4 depending on isAudio
	 * @param isAudio is the check for audio or video
	 * @return boolean whether inputs are appropriate
	 */
	private boolean formatCheck(boolean isAudio) {
		if (isAudio == true) {
			if (sTimeT.getText().matches("[0-9]+")
					&& tTimeT.getText().matches("[0-9]+")
					&& outFileT.getText().endsWith(".mp3")) {
				return true;
			} else {
				return false;
			}
		} else {
			if (sTimeT.getText().matches("[0-9]+")
					&& tTimeT.getText().matches("[0-9]+")
					&& outFileT.getText().endsWith(".mp4")) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Begin extraction
	 * @param isAudioVideoBoth
	 */
	private void beginExtract(int isAudioVideoBoth) {
		switchUsable();
		_extractor = new Extractor();
		_time.start();
		_extractor.extract(_fsp.getInputField(), _sTime, _tTime, _outFile, isAudioVideoBoth);
	}

	/**
	 * Setup progress bar
	 */
	public void setupProgress() {
		_time.setActionCommand("tick");
		_time.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("tick")) {
					if (_extractor.getWorking()) {
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

	/**
	 * Switch the buttons' enabled characteristic
	 */
	protected void switchUsable() {
		if (isUsable == true) {
			isUsable = false;
			_confirmAudio.setEnabled(false);
			_confirmVideo.setEnabled(false);
			_confirmBoth.setEnabled(false);
		} else {
			isUsable = true;
			_confirmAudio.setEnabled(true);
			_confirmVideo.setEnabled(true);
			_confirmBoth.setEnabled(true);
		}
	}
}
