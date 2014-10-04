package operations;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Overlayer is a SwingWorker which replaces a video's audio stream.
 * Taken from SE206 Assignment 3, paired prototype.
 */
public class Overlayer extends SwingWorker<Void, Integer> {
	private int _result;
	private boolean _isWorking = false;
	private ProcessBuilder _builder;
	private Process _process;
	private String _processString;

	public Overlayer() {
	}

	@Override
	protected Void doInBackground() throws Exception {
		overlaySW();
		return null;
	}

	@Override
	protected void done() {
		_isWorking = false;
		if (getResult() == 0) {
			JOptionPane.showMessageDialog(null,
					"Operation has been succesfully completed");
		} else {
			JOptionPane.showMessageDialog(null, "Operation Incomplete");
		}
	}

	// Public method to begin audio replacement
	public void overlay(String mp3Input, String mp4Input, String outFile,
			boolean isShortest) {
		if (isShortest == true) {
			_processString = "avconv -i '" + mp3Input + "' -i '" + mp4Input
					+ "' -map 0:0 -map 1:0 -c copy -shortest '" + outFile + "'";
		} else {
			// This command is not working :(
			_processString = "avconv -i '" + mp3Input + "' -i '" + mp4Input
					+ "' -map 0:0 -map 1:0 -acodec copy -vcodec copy '"
					+ outFile + "'";
		}

		_isWorking = true;
		this.execute();
	}

	private void overlaySW() {
		try {
			_builder = new ProcessBuilder("/bin/bash", "-c", _processString);
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();

			_result = _process.waitFor();

			_process.destroy();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Returns result of SwingWorker
	public int getResult() {
		return _result;
	}

	// Returns whether the SwingWorker is working
	public boolean getWorking() {
		return _isWorking;
	}

	// Cancel the Worker
	public void cancel() {
		if (_process != null) {
			_process.destroy();
		}
	}
}
