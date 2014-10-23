package operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * BashOperations deals with bash commands that don't require a swing worker.
 * Taken from SE206 Assignment 3, paired prototype.
 */
public class BashOperations {

	public int _percent = 0;
	private int _result;

	private ProcessBuilder _builder;
	private Process _process;

	// Cancels the current process
	public void cancel() {
		_process.destroy();
	}

	// Returns completion percentage
	// Don't think this is used
	public int getPercentage() {
		return _percent;
	}

	// Returns result of SwingWorker
	public int getResult() {
		return _result;
	}

	// Check that a file is audio
	public boolean checkAudioFile(String loc) {
		File file = new File(loc);
		boolean returnValue = false;
		try {
			_builder = new ProcessBuilder("/bin/bash", "-c", "echo $(file '"
					+ loc + "')");
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();

			InputStream stdout = _process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			String line = null;

			while ((line = stdoutBuffered.readLine()) != null) {
				if (line.contains("Audio") || line.contains("audio")) {
					returnValue = true;
				}
			}

			_process.waitFor();
			_process.destroy();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	public boolean checkVideoFile(String loc) {
		File file = new File(loc);
		boolean returnValue = false;
		try {
			_builder = new ProcessBuilder("/bin/bash", "-c", "echo $(file '"
					+ loc + "')");
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();

			InputStream stdout = _process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			String line = null;

			while ((line = stdoutBuffered.readLine()) != null) {
				if (line.contains("Video") || line.contains("video")) {
					returnValue = true;
				}
			}

			_process.waitFor();
			_process.destroy();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return returnValue;
	}
}