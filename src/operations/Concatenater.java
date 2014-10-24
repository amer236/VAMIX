package operations;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Concatenater is a SwingWorker that allows the user to concatenate video or audio files together
 */
public class Concatenater extends SwingWorker<Void, Integer> {
	private String _location1;
	private String _location2;
	private String _outname;

	private ProcessBuilder _builder;
	private Process _process;
	private int _result;
	PrintWriter _writer;

	boolean _isWorking = false;

	public Concatenater() {
	}

	@Override
	protected Void doInBackground() throws Exception {
		clearTempFiles();
		createTempVids();
		
		return null;
	}

	/**
	 * Public method to begin worker
	 * @param location1 First file location
	 * @param location2 Second file location
	 * @param outname Output file name
	 */
	public void concat(String location1, String location2, String outname) {
		_isWorking = true;
		_location1 = location1;
		_location2 = location2;
		_outname = outname;

		this.execute();
	}

	/**
	 * Create temporary videos in mpeg format
	 */
	private void createTempVids() {
		createTemp1();
		if (_result == 0) {
			createTemp2();
		}
	}

	/**
	 * Creates temporary video 1
	 */
	private void createTemp1() {
		try {
			_builder = new ProcessBuilder(
					"/bin/bash",
					"-c",
					"avconv -i '" + _location1 + "' temp1.mpg");
			_builder.directory(new File(System.getProperty("user.home")
					+ "/VAMIX"));
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();
			_result = _process.waitFor();

			_process.destroy();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates temporary video 2
	 */
	private void createTemp2() {
		try {
			_builder = new ProcessBuilder(
					"/bin/bash",
					"-c",
					"avconv -i '" + _location2	+ "' temp2.mpg");
			_builder.directory(new File(System.getProperty("user.home")
					+ "/VAMIX"));
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();
			_result = _process.waitFor();
			if (_result == 0) {
				mergeVideos();
			}
			_process.destroy();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Merge the temporary videos
	 */
	private void mergeVideos() {
		try {
			String stringP = "cat temp1.mpg temp2.mpg > final.mpg";
			_builder = new ProcessBuilder("/bin/bash", "-c", stringP);
			_builder.directory(new File(System.getProperty("user.home")
					+ "/VAMIX"));
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();
			_result = _process.waitFor();
			
			if (_result == 0) {
				convertTomp4();
			}
			_process.destroy();
		} catch (IOException | InterruptedException e) {
		}
	}

	/**
	 * Convert the merged mpeg file to a file with an output name
	 */
	private void convertTomp4() {
		 try {
				String stringP = "avconv -i final.mpg -strict experimental '" + _outname + "'";
				_builder = new ProcessBuilder("/bin/bash", "-c", stringP);
				_builder.directory(new File(System.getProperty("user.home")
						+ "/VAMIX"));
				_builder = _builder.redirectErrorStream(true);
				_process = _builder.start();
				_result = _process.waitFor();
								
				_process.destroy();
			} catch (IOException | InterruptedException e) {
			}
	}

	/**
	 * Empties VAMIX folder for new operation
	 */
	private void clearTempFiles() {
		File dir = new File(System.getProperty("user.home") + "/VAMIX");
		for (File file : dir.listFiles()) {
			file.delete();
		}
	}

	@Override
	protected void done() {
		_isWorking = false;

		if (getResult() == 0) {
			JOptionPane.showMessageDialog(null, "Join Complete. Please copy your output file from the VAMIX folder in your home directory.");
		} else {
			JOptionPane.showMessageDialog(null,	"Operation incomplete");
		}
	}

	/**
	 * Returns result of SwingWorker
	 * @return integer result of the operation
	 */
	public int getResult() {
		return _result;
	}

	/**
	 * Returns whether the SwingWorker is working
	 * @return boolean representing whether the operation is in progress
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