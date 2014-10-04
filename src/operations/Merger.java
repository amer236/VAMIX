package operations;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Merger is a SwingWorker which merges two audio tracks into one.
 * Taken from SE206 Assignment 3, paired prototype.
 */
public class Merger extends SwingWorker<Void, Integer> {
	private int _result;
	private boolean _isWorking = false;

	private ProcessBuilder _builder;
	private Process _process;
	private String _processString;

	public Merger() {
	}

	@Override
	protected Void doInBackground() throws Exception {
		mergeSW();
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
	
	// Public method for beginning merge command
	public void merge(String mp3Input1, String mp3Input2, String outFile) {
		_processString = "avconv -i '" + mp3Input1 + "' -i '" + mp3Input2
				+ "' -filter_complex amix=inputs=2:duration=longest '"
				+ outFile + "'";
		_isWorking = true;
		this.execute();
	}

	private void mergeSW() {
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
