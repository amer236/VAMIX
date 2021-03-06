package operations;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Filterer is a SwingWorker which applies a filter to the selected video.
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
		}
	}

	/**
	 * Public method to begin adding filter
	 * @param mp4Input
	 * @param outFile
	 * @param filter
	 */
	public void applyFilter(String mp4Input, String outFile, String filter) {
		_processString = "avconv -i '" + mp4Input + "' -vf " + filter + " -strict experimental '" + outFile + "'";

		_isWorking = true;
		this.execute();
	}

	/**
	 * Perform operation
	 */
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

	/**
	 * Returns result of SwingWorker
	 * @return integer result
	 */
	public int getResult() {
		return _result;
	}

	/**
	 * Returns whether the SwingWorker is working
	 * @return boolean whether operation was in progress
	 */
	public boolean getWorking() {
		return _isWorking;
	}

	/**
	 * Cancel the operation
	 */
	public void cancel() {
		if (_process != null) {
			_process.destroy();
		}
	}
}