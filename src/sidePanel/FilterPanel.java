package sidePanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Timer;

import operations.Filterer;

/**
 * FilterPanel allows the user to add filters to the current video.
 * Taken from SE206 Assignment 3, paired prototype.
 */
@SuppressWarnings("serial")
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
	
	JRadioButton _hflip = new JRadioButton("Horizontal Flip");
	JRadioButton _negate = new JRadioButton("Negate");
	JRadioButton _vflip = new JRadioButton("Vertical Flip");
	
	ButtonGroup _group = new ButtonGroup();

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
		
	    _group.add(_hflip);
	    _hflip.setSelected(true);
	    _group.add(_negate);
	    _group.add(_vflip);
	    
	    JPanel inner = new JPanel();
	    
	    inner.add(_hflip);
	    inner.add(_negate);
	    inner.add(_vflip);
	    
	    this.add(inner, "wrap");

		this.add(_outFileL,"wrap");
		this.add(_outFileT,"wrap, grow");

		this.add(_filter, "wrap, span, grow");
		this.add(_working, "wrap, grow");
		this.add(_cancel, "span, grow");
	}

	/**
	 * Setup button actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Add filter to video
		if (e.getActionCommand().equals("Filter")) {
			// Check fields, and then begin filtering
			if (_fsp.getInputField().equals("")) {
				JOptionPane.showMessageDialog(null,
						"No source file has been selected. Please select a file in the General tab.");
				return;
			}
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

	/**
	 * Checks that the output ends in .mp4
	 * @return boolean
	 */
	private boolean formatCheck() {
		if (_outFileT.getText().endsWith(".mp4")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Begin filtering
	 */
	private void beginApplyingFilter() {
		switchUsable();
		_filterer = new Filterer();
		_time.start();
		if(_hflip.isSelected()){
			_filterer.applyFilter(_fsp.getInputField(), _outFile, "hflip");
		}else if (_negate.isSelected()){
			_filterer.applyFilter(_fsp.getInputField(), _outFile, "negate");
		}else if(_vflip.isSelected()){
			_filterer.applyFilter(_fsp.getInputField(), _outFile, "vflip");
		}
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

	/**
	 * Switch the buttons' enabled characteristic
	 */
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
