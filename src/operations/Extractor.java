package operations;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Extractor is a SwingWorker which extracts audio, video or both from a given
 * input.
 */
public class Extractor extends SwingWorker<Void, Integer> {
	private int _result;

	private String _location;
	private String _stime;
	private String _ttime;
	private String _outname;

	private ProcessBuilder _builder;
	private Process _process;
	private String _processString;

	// This is for an external indeterminate progress bar
	private boolean _isWorking = false;

	public Extractor() {
	}

	@Override
	protected Void doInBackground() throws Exception {
		extractSW(_location, _stime, _ttime, _outname);
		return null;
	}

	@Override
	protected void done() {
		_isWorking = false;
		if (getResult() == 0) {
			JOptionPane.showMessageDialog(null, "Extraction Complete");
		} else {
			JOptionPane.showMessageDialog(null, "Extraction Incomplete");
		}
	}

	// Public method to begin extraction
	public void extract(String location, String stime, String ttime,
			String outname, int isAudioVideoBoth) {
		_location = location;
		_stime = stime;
		_ttime = ttime;
		_outname = outname;
		_isWorking = true;
		// Audio
		if (isAudioVideoBoth == 0) {
			_processString = "avconv -i '" + _location + "' -ss " + stime
					+ " -t " + ttime + " -strict experimental '" + outname
					+ "'";
		// Video
		} else if (isAudioVideoBoth == 1) {
			_processString = "avconv -i '" + _location + "' -ss " + stime
					+ " -t " + ttime + " -an '" + outname + "'";
		// Both
		} else {
			_processString = "avconv -i '" + _location + "' -ss " + stime
					+ " -t " + ttime + " -strict experimental '" + outname
					+ "'";
		}
		// Execute SwingWorker after process has been set
		this.execute();
	}

	// Method to be run in background of swing worker
	private void extractSW(String location, String stime, String ttime,
			String outname) {
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
