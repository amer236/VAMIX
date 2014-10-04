package operations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * This class holds some of the download logic
 */
public class GeneralOperations implements ActionListener {
	public Downloader _downloader;
	private Timer _time;
	private JProgressBar _prog;
	private EmbeddedMediaPlayer _player;

	public GeneralOperations(EmbeddedMediaPlayer player) {
		_downloader = new Downloader();
		_player = player;
	}

	// Select file action opens JFileChooser to select file
	// Sets textfield to the path
	public void selectAction(JTextField selectField) {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String absolutePath = selectedFile.getAbsolutePath();
			selectField.setText(absolutePath);
			_player.playMedia(absolutePath);
		}
	}

	// Runs checks then begins download
	public void downloadAction(String downloadString) {
		if (JOptionPane.showConfirmDialog(null, "Is this file open source?",
				"", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			File urlFile = new File(downloadString);
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
					beginDownload(downloadString);
				} else {
					beginDownload(downloadString);
				}
			} else {
				beginDownload(downloadString);
			}
		}
	}

	// Sets up the progress bar
	public void setupProgress(JProgressBar prog) {
		_prog = prog;
		_time = new Timer(200, null);
		_time.setActionCommand("tick");
		_time.addActionListener(this);
	}

	// Begin download through swing worker
	private void beginDownload(String downloadString) {
		_downloader = new Downloader();
		_downloader.download(downloadString);
	}

	// Cancel swing worker download
	public void cancel() {
		_downloader.cancel();
	}

	// Update progress bar each tick
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("tick")) {
			_prog.setValue(_downloader.getPercentage());
			if (_prog.getValue() == 100) {
				_time.stop();
				_downloader._percent = 0;
			}
		}
	}
}