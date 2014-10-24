package sidePanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.text.NumberFormatter;

import operations.Texter;

/**
 * VidEditingPanel adds title and credit scene functionality. Taken from SE206
 * Assignment 3, paired prototype.
 */
@SuppressWarnings("serial")
public class VidEditingPanel extends SidePanel implements ActionListener {
	Texter _texter = new Texter();
	GeneralPanel _gPanel;
	Timer _time = new Timer(1000, null);

	JLabel _startTextLabel = new JLabel("Text to display at start of video: ");
	JTextArea _startText = new JTextArea(5, 20);
	JLabel _endTextLabel = new JLabel(
			"<html> Text to display at end of video: <html/>");
	JTextArea _endText = new JTextArea(5, 20);
	JLabel _outnameLabel = new JLabel("<html> Output file name: <html/>");
	JTextField _outnameText = new JTextField();
	JLabel _fontLabel = new JLabel("Select Font");
	JLabel _fontSizeLabel = new JLabel("Font Size");
	JLabel _colourLabel = new JLabel("Select Colour");
	JSpinner _fontSizeSpinner = null;

	// combo boxes that have drop down menu
	@SuppressWarnings({ "unchecked", "rawtypes" })
	JComboBox _fontBox = new JComboBox(new DefaultComboBoxModel(Font.values()));

	// array which stores colours
	String[] colours = { "white", "blue", "red", "green" };
	@SuppressWarnings({ "unchecked", "rawtypes" })
	JComboBox _fontColourBox = new JComboBox(colours);

	JButton _previewStart = new JButton("Preview Start");
	JButton _previewEnd = new JButton("Preview End");
	JButton _confirm = new JButton("Add text");

	JProgressBar _working = new JProgressBar();
	JButton _cancel = new JButton("Cancel");

	boolean isUsable = true;

	public VidEditingPanel(String name, GeneralPanel gPanel) {
		super(name);
		_gPanel = gPanel;
		super.setupPanel();
		setupProgress();
	}

	/**
	 * Setup this panel
	 */
	protected void setupPanel() {
		SpinnerModel inputSizeModel = new SpinnerNumberModel(10, 10, 72, 1);
		_fontSizeSpinner = new JSpinner(inputSizeModel);
		JFormattedTextField inputSizeText = ((JSpinner.NumberEditor) _fontSizeSpinner
				.getEditor()).getTextField();
		((NumberFormatter) inputSizeText.getFormatter())
				.setAllowsInvalid(false);

		// Add buttons
		this.add(_startTextLabel, "wrap");
		this.add(_startText, "width 500, wrap");
		this.add(_endTextLabel, "width 200, wrap");
		this.add(_endText, "width 500, wrap");
		this.add(_outnameLabel, "width 200, wrap");
		this.add(_outnameText, "width 500, wrap");
		this.add(_fontLabel, "split 2");
		this.add(_fontBox, "gapleft 32, wrap");
		this.add(_fontSizeLabel, "split 2");
		this.add(_fontSizeSpinner, "gapleft 47, wrap, width 50");
		this.add(_colourLabel, "split 2");
		this.add(_fontColourBox, "gapleft 20, wrap");

		this.add(_confirm, "split 3");
		this.add(_previewStart, "");
		this.add(_previewEnd, "wrap");

		_confirm.setActionCommand("confirm");
		_confirm.addActionListener(this);
		_previewStart.setActionCommand("start");
		_previewStart.addActionListener(this);
		_previewEnd.setActionCommand("end");
		_previewEnd.addActionListener(this);

		_working.setIndeterminate(false);
		_working.setPreferredSize(new Dimension(250, 20));

		_cancel.setActionCommand("cancel");
		_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_texter.cancel();
			}
		});

		this.add(_working, "span, grow");
		this.add(_cancel, "span, grow");
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
					if (_texter.getWorking()) {
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
	 * Enums that have path to each font
	 */
	public enum Font {
		UbuntuRegular(
				"/usr/share/fonts/truetype/ubuntu-font-family/Ubuntu-R.ttf"), UbuntuBold(
				"/usr/share/fonts/truetype/ubuntu-font-family/Ubuntu-B.ttf"), UbuntuMedium(
				"/usr/share/fonts/truetype/ubuntu-font-family/Ubuntu-M.ttf"), DejaVuSans(
				"/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf");

		public String getPath() {
			return _path;
		}

		private final String _path;

		Font(String path) {
			this._path = path;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (_gPanel.getInputField().equals("")) {
			JOptionPane
					.showMessageDialog(null,
							"No source file has been selected. Please select a file in the General tab.");
			return;
		}
		_texter = new Texter();
		// gets font path
		Font thisFont = (Font) _fontBox.getSelectedItem();
		String fontPath = thisFont.getPath();

		// creates preview file if it does not exist
		File file = new File(System.getProperty("user.home")
				+ "/VAMIX/preview.mp4");
		if (file.exists()) {
			file.delete();
		}
		_texter.createSizedPreview(_gPanel.getInputField());

		// checks that fontsize is vaild. If not, sets it to 12
		// add text command
		if (e.getActionCommand().equals("confirm")) {
			if (!_outnameText.getText().endsWith(".mp4")) {
				JOptionPane.showMessageDialog(null,
						"The output file must end with .mp4");
				return;
			} else {
				_texter.drawText(_gPanel.getInputField(), _startText.getText(),
						_endText.getText(), _outnameText.getText(), fontPath,
						"" + _fontSizeSpinner.getValue().toString(),
						_fontColourBox.getSelectedItem().toString());
				_time.start();
				switchUsable();
			}
			// preview start command
		} else if (e.getActionCommand().equals("start")) {
			_texter.preview(true, _startText.getText(), fontPath,
					_fontSizeSpinner.getValue().toString(), _fontColourBox
							.getSelectedItem().toString());
			_time.start();
			switchUsable();

			// preview end command
		} else if (e.getActionCommand().equals("end")) {
			_texter.preview(false, _endText.getText(), fontPath,
					_fontSizeSpinner.getValue().toString(), _fontColourBox
							.getSelectedItem().toString());
			_time.start();
			switchUsable();
		}
	}

	/**
	 * Gets all states into a list
	 * 
	 * @return list representing the state of this panel
	 */
	public ArrayList<String> returnState() {
		ArrayList<String> saveStateList = new ArrayList<String>();
		saveStateList.add(_gPanel.getInputField());
		saveStateList.add(_fontBox.getSelectedItem().toString());
		saveStateList.add(_fontSizeSpinner.getValue().toString());
		saveStateList.add(_fontColourBox.getSelectedItem().toString());
		saveStateList.add(_outnameText.getText());
		// the stars will be a flag to know when the text ends
		saveStateList.add(_startText.getText() + "\n*****");
		saveStateList.add(_endText.getText() + "\n*****");

		return saveStateList;
	}

	/**
	 * Loads states from a list
	 * 
	 * @param stateList
	 */
	public void loadState(ArrayList<String> stateList) {
		_startText.setText("");
		_endText.setText("");
		ArrayList<String> loadStateList = stateList;

		((JSpinner.NumberEditor) _fontSizeSpinner.getEditor()).getTextField()
				.setValue(Integer.parseInt(loadStateList.get(2)));

		_outnameText.setText(loadStateList.get(4));

		int linePos = 5;
		// StringBuilder sb = new StringBuilder();
		while (!loadStateList.get(linePos).equals("*****")) {
			_startText.append(loadStateList.get(linePos) + "\n");
			linePos++;
		}
		linePos++;
		while (!loadStateList.get(linePos).equals("*****")) {
			_endText.append(loadStateList.get(linePos) + "\n");
			linePos++;
		}
	}

	/**
	 * Switch the buttons' enabled characteristic
	 */
	protected void switchUsable() {
		if (isUsable == true) {
			isUsable = false;
			_confirm.setEnabled(false);
		} else {
			isUsable = true;
			_confirm.setEnabled(true);
		}
	}
}
