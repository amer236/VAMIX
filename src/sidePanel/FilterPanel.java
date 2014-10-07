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
import operations.Filterer;

/**
 * ExtractPanel allows the user to strip out audio, video, or both from specific.
 * Taken from SE206 Assignment 3, paired prototype.
 * time slots
 */
public class FilterPanel extends SidePanel implements ActionListener {
	GeneralPanel _fsp;
	Filterer _filterer;
	JFrame _frame;
	JButton _filter;
	
	JLabel _outFileL = new JLabel("Output File Name: ");
	JTextField _outFileT = new JTextField();

	JProgressBar _working = new JProgressBar();
	Timer _time = new Timer(1000, null);
	JButton _cancel = new JButton("Cancel");

	String _outFile;

	boolean isUsable = true;

	public FilterPanel(String name, GeneralPanel fsPanel) {
		super(name);
		_fsp = fsPanel;
		super.setupPanel();
		setupProgress();

		_fsp = fsPanel;
		_filterer = new Filterer();

		_filter = new JButton("Filter");
		_filter.addActionListener(this);
		_filter.setActionCommand("Filter");

		_cancel.setActionCommand("cancel");
		_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_filterer.cancel();
			}
		});

		_working.setIndeterminate(false);
		_working.setPreferredSize(new Dimension(250, 20));

		this.add(_outFileL,"wrap");
		this.add(_outFileT,"wrap, grow");
		//this.add(new JLabel("All times must be in seconds"), "span, wrap");
		this.add(_filter, "wrap, span, grow");
		this.add(_working, "wrap");
		this.add(_cancel, "span, grow");
	}

	protected void setupPanel() {
	}

	// Setup button actions
	@Override
	public void actionPerformed(ActionEvent e) {
		// Add filter to video
		if (e.getActionCommand().equals("Filter")) {
			// Check fields, and then begin extraction
			if (formatCheck() == true) {
				File f = new File(_outFileT.getText());
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
						_outFile = _outFileT.getText();
						beginApplyingFilter();
					}
				} else {
					_outFile = _outFileT.getText();

					beginApplyingFilter();
				}
			} else {
				JOptionPane.showMessageDialog(null, "The output name must end in .mp4");
			}
		}
	}

	// Checks only the appropriate inputs are made
	// Checks that the output ends in or .mp4
	private boolean formatCheck() {
		if (_outFileT.getText().endsWith(".mp4")) {
			return true;
		} else {
			return false;
		}
	}

	// Begin filtering
	private void beginApplyingFilter() {
		switchUsable();
		_filterer = new Filterer();
		_time.start();
		_filterer.applyFilter(_fsp.getInputField(), _outFile, "hflip");
	}

	// Setup progress bar
	public void setupProgress() {
		_time.setActionCommand("tick");
		_time.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("tick")) {
					if (_filterer.getWorking()) {
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

	// Switch the buttons' enabled characteristic
	protected void switchUsable() {
		if (isUsable == true) {
			isUsable = false;
			_filter.setEnabled(false);
		} else {
			isUsable = true;
			_filter.setEnabled(true);
		}
	}
}
