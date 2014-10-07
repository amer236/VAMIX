package operations;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Filterer is a SwingWorker which replaces a video's audio stream.
 */
public class Filterer extends SwingWorker<Void, Integer> {
	private int _result;
	private boolean _isWorking = false;
	private ProcessBuilder _builder;
	private Process _process;
	private String _processString;

	public Filterer() {
	}

	@Override
	protected Void doInBackground() throws Exception {
		filterSW();
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
			System.out.println(_result + " " + _processString);
		}
	}

	// Public method to begin audio replacement
	public void applyFilter(String mp4Input, String outFile, String filter) {
		_processString = "avconv -i '" + mp4Input + "' -vf " + filter + " -strict experimental '" + outFile + "'";

		_isWorking = true;
		this.execute();
	}

	private void filterSW() {
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
