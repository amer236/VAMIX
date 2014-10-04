package sidePanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import operations.Downloader;
import operations.GeneralOperations;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * GeneralPanel is a SidePanel to allow file select and download functionality.
 * Taken from SE206 Assignment 3, paired prototype.
 */
public class GeneralPanel extends SidePanel implements ActionListener {
	private JTextField _selectField;
	private JButton _btnSelect;
	private JTextField _downloadField;
	private JButton _btnDownload;

	private JProgressBar _prog;
	private JButton _cancel;
	private EmbeddedMediaPlayer _player = null;
	private GeneralOperations _genOperations;

	public GeneralPanel(String name, EmbeddedMediaPlayer player) {
		super(name);
		_player = player;
		_genOperations = new GeneralOperations(_player);
		super.setupPanel();
		this.setBorder(null);
	}

	// Sets up the panel
	protected void setupPanel() {
		this.setLayout(new MigLayout());
		createFileSelect();
		createDownload();
		_genOperations.setupProgress(_prog);

		// Comprised of select panel and download panel
		JPanel selectPanel = new SidePanel("Select File");
		selectPanel.setLayout(new MigLayout());
		JPanel downloadPanel = new SidePanel("Download File");
		downloadPanel.setLayout(new MigLayout());

		selectPanel.add(_btnSelect);
		selectPanel.add(_selectField, "width 600, wrap");
		downloadPanel.add(_btnDownload);
		downloadPanel.add(_downloadField, "width 600, wrap");
		downloadPanel.add(_cancel);
		downloadPanel.add(_prog, "grow");

		this.add(selectPanel, "grow, wrap");
		this.add(downloadPanel);
	}

	// Adds functionality to file select JComponents
	private void createFileSelect() {
		_selectField = new JTextField("");
		_selectField.setEditable(false);
		_selectField.setPreferredSize(new Dimension(100, 20));

		_btnSelect = new JButton("Select File");
		_btnSelect.setActionCommand("select");
		_btnSelect.addActionListener(this);
	}

	// Adds functionality to download JComponents
	private void createDownload() {
		_btnDownload = new JButton("Download");
		_btnDownload.setActionCommand("download");
		_btnDownload.addActionListener(this);
		_downloadField = new JTextField("");

		_prog = new JProgressBar();
		_prog.setMinimum(0);
		_prog.setMaximum(100);

		_cancel = new JButton("Cancel");
		_cancel.addActionListener(this);
		_cancel.setActionCommand("cancel");
	}

	@Override
	// Perform actions through generalOperations based on action command
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("select")) {
			_genOperations.selectAction(_selectField);
		} else if (e.getActionCommand().equals("download")) {
			if (_downloadField.getText().equals("")) {
				JOptionPane.showMessageDialog(null,	"Please enter a url to download media");
			} else {
				_prog.setValue(0);
				_genOperations.downloadAction(_downloadField.getText());
			}
		} else if (e.getActionCommand().equals("cancel")) {
			_genOperations._downloader.cancel();
		}
	}

	// Returns the path of the selected file
	public String getInputField() {
		return _selectField.getText();
	}
	
	public void setInputField(String set){
		_selectField.setText(set);
	}
}