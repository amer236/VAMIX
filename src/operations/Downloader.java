package operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Downloader is a SwingWorker which downloads media, given a url.
 * Taken from SE206 Assignment 3, paired prototype.
 */
public class Downloader extends SwingWorker<Void, Integer> {
	private String _downloadcmd = "wget";
	public int _percent = 0;
	private int _result;
	private String _url;
	private ProcessBuilder _builder;
	private Process _process;

	public Downloader() {
	}

	@Override
	protected Void doInBackground() throws Exception {
		downloadSW(_url);
		return null;
	}

	@Override
	protected void done() {
		if (getResult() == 0) {
			if (getPercentage() == 100) {
				JOptionPane.showMessageDialog(null, "Download Complete");
			} else {
				JOptionPane.showMessageDialog(null, "Download Incomplete");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Download Unsuccessful");
			_percent = 100;
		}
	}

	/**
	 * Public method to begin download
	 * @param url for download
	 */
	public void download(String url) {
		_url = url;
		this.execute();
	}

	/**
	 * Method to be run in background of swing worker
	 * @param url for download
	 */
	private void downloadSW(String url) {
		String fileSizeString = "";
		long fileSize = 1;
		_percent = 0;

		try {
			_builder = new ProcessBuilder(_downloadcmd, "-c", "--progress=bar",
					url);
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();
			InputStream stdout = _process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			// This block is in charge of getting progress through the download
			String line = null;
			Boolean calculatePercentage = false;
			while ((line = stdoutBuffered.readLine()) != null) {
				if (line.startsWith("Length:")) {
					int k = 8;
					while (line.charAt(k) != ' ') {
						k++;
					}
					fileSizeString = line.substring(8, k);
					fileSize = Integer.parseInt(fileSizeString);
					calculatePercentage = true;
				}
				if (calculatePercentage == true) {
					File urlFile = new File(url);
					File file = new File(urlFile.getName());
					_percent = (int) ((file.length() * 100) / fileSize);
				}
			}

			if (calculatePercentage == false) {
				// Fail- safe, if download continues, but is already complete
				_percent = 100;
			}

			_result = _process.waitFor();
			
			_process.destroy();

		} catch (IOException | InterruptedException e) {
			if (e.getMessage() == "Stream closed") {
				// This is expected behavior from cancel().
			} else {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns progress through download as a percentage
	 * @return integer progress through operation
	 */
	public int getPercentage() {
		return _percent;
	}

	/**
	 * Returns result of worker
	 * @return integer result
	 */
	public int getResult() {
		return _result;
	}

	/**
	 * Cancels the current process
	 */
	public void cancel() {
		if (_process != null) {
			_process.destroy();
		}
	}
}