package sidePanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import operations.Downloader;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * GeneralPanel is a SidePanel to allow file select and download functionality.
 * It also allows the user to import files into an easy access list.
 * Taken from SE206 Assignment 3, paired prototype.
 */
@SuppressWarnings("serial")
public class GeneralPanel extends SidePanel implements ActionListener {
	private JTextField _selectField;
	private JTextField _downloadField;
	private JButton _btnDownload;

	private Timer _time;
	private Downloader _downloader = new Downloader();
	
	private JProgressBar _prog;
	private JButton _cancel;
	private EmbeddedMediaPlayer _player = null;
	
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	JList<String> list = new JList<String>(listModel);
	JButton add = new JButton("Import File");
	JButton delete = new JButton("Remove File");
	JButton play = new JButton("Load Selected Media");
	JLabel imported = new JLabel("Imported Files");

	public GeneralPanel(String name, EmbeddedMediaPlayer player) {
		super(name);
		_player = player;
		super.setupPanel();
		this.setBorder(null);
	}

	/**
	 * Sets up the panel
	 */
	protected void setupPanel() {
		this.setLayout(new MigLayout());
		createFileSelect();
		createDownload();

		// Comprised of select panel and download panel
		JPanel selectPanel = new SidePanel("Select File");
		selectPanel.setLayout(new MigLayout());
		JPanel downloadPanel = new SidePanel("Download File");
		downloadPanel.setLayout(new MigLayout());

		selectPanel.add(new JLabel("Current File"));
		selectPanel.add(_selectField, "width 400, wrap");
				
		downloadPanel.add(_btnDownload);
		downloadPanel.add(_downloadField, "width 400, wrap");
		downloadPanel.add(_cancel);
		downloadPanel.add(_prog, "grow");

		this.add(selectPanel, "grow, wrap");
		this.add(downloadPanel, "wrap");
		
		_time = new Timer(200, null);
		_time.setActionCommand("tick");
		_time.addActionListener(this);
		
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					addToList(selectedFile.getAbsolutePath());
				}
			}
		});
		
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedIndex() == -1){
					//Nothing selected
				} else{
					listModel.remove(list.getSelectedIndex());
					if(listModel.getSize() == 0){
						delete.setEnabled(false);
					}
				}
			}
		});
		
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedIndex() == -1){
					//Nothing selected
				} else{
					setInputField(listModel.elementAt(list.getSelectedIndex()));
					_player.playMedia(getInputField());
				}
			}
		});
		play.setEnabled(false);
		
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(list.getSelectedIndex() == -1){
					play.setEnabled(false);
				}else{
					play.setEnabled(true);
				}
				
			}
		});
		JPanel listPane = new JPanel();
		listPane.add(list);
		JScrollPane scrollPane = new JScrollPane(listPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(10,200));
		
		selectPanel.add(imported, "wrap");
		selectPanel.add(scrollPane, "grow, wrap, span");
		selectPanel.add(add);
		selectPanel.add(delete, "wrap");
		selectPanel.add(play,"span, grow");
		
		delete.setEnabled(false);
	}

	/**
	 * Adds functionality to file select JComponents
	 */
	private void createFileSelect() {
		_selectField = new JTextField("");
		_selectField.setEditable(false);
		_selectField.setPreferredSize(new Dimension(100, 20));
	}

	/**
	 * Adds functionality to download JComponents
	 */
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

	/**
	 * Add functionality to buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("tick")) {
			_prog.setValue(_downloader.getPercentage());
			if (_prog.getValue() == 100) {
				_time.stop();
				_downloader._percent = 0;
			}
		}else if (e.getActionCommand().equals("download")) {
			if (_downloadField.getText().equals("")) {
				JOptionPane.showMessageDialog(null,	"Please enter a url to download media");
			} else {
				_prog.setValue(0);
				if (JOptionPane.showConfirmDialog(null, "Is this file open source?",
						"", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					File urlFile = new File(_downloadField.getText());
					File file = new File(urlFile.getName());
					_time.start();

					if (file.exists()) {
						Object[] options = { "Cancel", "Overwrite existing",
								"Resume existing" };
						int result = JOptionPane.showOptionDialog(null, file.getName()
								+ " already exists. What would you like to do?",
								"Already Exists", JOptionPane.ERROR_MESSAGE,
								JOptionPane.WARNING_MESSAGE, null, options, options[0]);
						if (result == 0) {
							// Do nothing
						} else if (result == 1) {
							file.delete();
							beginDownload();
						} else {
							beginDownload();
						}
					} else {
						beginDownload();
					}
				}
			}
		} else if (e.getActionCommand().equals("cancel")) {
			_downloader.cancel();
		}
	}

	/**
	 * Returns the path of the selected file
	 * @return
	 */
	public String getInputField() {
		return _selectField.getText();
	}
	
	/**
	 * Sets the selected file
	 * @param set file path
	 */
	public void setInputField(String set){
		_selectField.setText(set);
	}
	
	/**
	 * Checks if the import list has another item
	 * @return boolean
	 */
	public boolean listHasNext(){
		if(listModel.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Get the first element on the list
	 * @return string of the first element
	 */
	public String getElementZero(){
		String path = listModel.elementAt(0);
		if(listModel.getSize() == 0){
			delete.setEnabled(false);
		}
		return path;
	}
	
	/**
	 * Add new item to import list
	 * @param input to add
	 */
	public void addToList(String input){
		listModel.addElement(input);
		delete.setEnabled(true);
	}
	
	/**
	 * Begin download through swing worker
	 */
	private void beginDownload() {
		_downloader = new Downloader();
		_downloader.download(_downloadField.getText());
	}
}