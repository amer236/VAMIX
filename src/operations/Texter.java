package operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Texter is a SwingWorker that adds a title and credit scene to the source video.
 */
public class Texter extends SwingWorker<Void, Integer> {
	private String _location;
	private String _startText;
	private String _endText;
	private String _outname;
	private String _fontPath;
	private String _fontSize = "12";
	private String _colour;

	private ProcessBuilder _builder;
	private Process _process;
	private int _result;
	PrintWriter _writer;

	boolean _isWorking = false;

	public Texter() {
	}

	@Override
	protected Void doInBackground() throws Exception {
		clearTempFiles();
		createBlankVideo(_location);

		return null;
	}

	// Public method to begin worker
	public void drawText(String location, String startText, String endText,
			String outname, String fontPath, String fontSize, String colour) {
		_isWorking = true;
		_location = location;
		_startText = startText;
		_endText = endText;
		_outname = outname;
		_fontPath = fontPath;
		_fontSize = fontSize;
		_colour = colour;

		this.execute();
	}

	// Gets correct dimensions from target video
	private void createBlankVideo(String location) {
		try {
			_builder = new ProcessBuilder("/bin/bash", "-c", "avprobe "
					+ location);
			_builder.directory(new File(System.getProperty("user.home")
					+ "/VAMIX"));
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();
			InputStream stdout = _process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(
					new InputStreamReader(stdout));
			String line = null;
			String[] parts = null;
			while ((line = stdoutBuffered.readLine()) != null) {
				// Gets line that has needed information
				if (line.contains("Video")) {
					parts = line.split(",");
				}
			}

			// gets frame rate and frame size
			String[] tempArray = parts[2].split(" ");
			String frameSize = tempArray[1];
			tempArray = parts[4].split(" ");
			String frameRate = tempArray[1];

			_result = _process.waitFor();
			if (_result == 0) {
				createBlankVideo(frameSize, frameRate);
			}
			_process.destroy();

		} catch (IOException | InterruptedException e) {
			if (e.getMessage() == "Stream closed") {
				// This is expected behavior from cancel().
			} else {
				e.printStackTrace();
			}
		}
	}

	// Creates blank video with correct dimensions
	private void createBlankVideo(String frameSize, String frameRate) {
		try {
			// Gets absolute path for input image file
			File input = new File("input.jpg");
			_builder = new ProcessBuilder("/bin/bash", "-c",
					"avconv -loop 1 -i " + input.getAbsolutePath() + " -r "
							+ frameRate + " -t 10 -s " + frameSize
							+ " blankVideoStart.mp4 & " + "avconv -loop 1 -i "
							+ input.getAbsolutePath() + " -r " + frameRate
							+ " -t 30 -s " + frameSize + " blankVideoEnd.mp4");
			_builder.directory(new File(System.getProperty("user.home")
					+ "/VAMIX"));
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();
			_result = _process.waitFor();

			if (_result == 0) {
				drawTextStart();
				drawTextEnd();
			}

			_process.destroy();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Draws text on blank video start
	private void drawTextStart() {
		try {
			_builder = new ProcessBuilder("/bin/bash", "-c", "avconv -i "
					+ System.getProperty("user.home")
					+ "/VAMIX/blankVideoStart.mp4"
					+ " -strict experimental -vf \"drawtext=fontfile='"
					+ _fontPath + "':text='" + _startText + "':fontsize="
					+ _fontSize + ":fontcolor=" + _colour + "\" StartText.mp4");
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

	// Draws text on blank video start
	private void drawTextEnd() {
		try {
			_builder = new ProcessBuilder("/bin/bash", "-c", "avconv -i "
					+ System.getProperty("user.home")
					+ "/VAMIX/blankVideoEnd.mp4"
					+ " -strict experimental -vf \"drawtext=fontfile='"
					+ _fontPath + "':text='" + _endText + "':fontsize="
					+ _fontSize + ":fontcolor=" + _colour + "\" EndText.mp4");
			_builder.directory(new File(System.getProperty("user.home")
					+ "/VAMIX"));
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();
			_result = _process.waitFor();

			if (_result == 0) {
				createTempVids();
			}

			_process.destroy();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Create temp videos in mpegt format
	// https://trac.ffmpeg.org/wiki/How%20to%20concatenate%20%28join,%20merge%29%20media%20files
	private void createTempVids() {
		createTemp2();
		createTemp3();
		createTemp1();
		if (_result == 0) {
			overlaySound();	
		}

	}

	// Creates temporary video 1
	private void createTemp1() {
		try {
			_builder = new ProcessBuilder(
					"/bin/bash",
					"-c",
					"avconv -i StartText.mp4 temp1.mpg");
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

	// Creates temporary video 2
	private void createTemp2() {
		try {
			//_builder = new ProcessBuilder(
			//		"/bin/bash",
			//		"-c",
			//		"avconv -i "	+ _location	+ " -c copy -bsf:v h264_mp4toannexb -f mpegts temp2 2> /dev/null");
			_builder = new ProcessBuilder(
					"/bin/bash",
					"-c",
					"avconv -i " + _location	+ " temp2.mpg");
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
	
	// Creates temporary video 3
	private void createTemp3() {
		try {
//			_builder = new ProcessBuilder(
//					"/bin/bash",
//					"-c",
//					"avconv -i EndText.mp4 -c copy -bsf:v h264_mp4toannexb -f mpegts temp3 2> /dev/null");
			_builder = new ProcessBuilder(
					"/bin/bash",
					"-c",
					"avconv -i EndText.mp4 temp3.mpg");
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

	// Merge temporary videos. This removes sound
	private void mergeVideos() {
		try {
			String stringP = "cat temp1new.mpg temp2.mpg temp3.mpg > final.mpg";
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

	// Add sound back in to video in a number of steps
	// Strip source audio
	private void overlaySound() {
		try {
			File blank = new File("10sec.mp3");
			Path source = blank.toPath();
			File folder = new File(System.getProperty("user.home") + "/VAMIX");
			Path newdir = folder.toPath();

			Files.copy(source, newdir.resolve(source.getFileName()));
			
			String _processString = "avconv -i 10sec.mp3 -i temp1.mpg -map 0:0 -map 1:0 -c copy temp1new.mpg";
			_builder = new ProcessBuilder("/bin/bash", "-c", _processString);
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
		}
	}

	// Returns length of source
	public String getTimeLength(String location) {
		String length = "";
		try {
			_builder = new ProcessBuilder("/bin/bash", "-c", "avprobe "	+ location);
			_builder.directory(new File(System.getProperty("user.home")	+ "/VAMIX"));
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();
			InputStream stdout = _process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = null;
			String[] parts = null;
			while ((line = stdoutBuffered.readLine()) != null) {
				// Gets line that has needed information
				if (line.trim().startsWith("Duration")) {
					parts = line.split(",");
				}
			}
			String[] tempArray = parts[0].trim().split(" ");
			String[] timesArray = tempArray[1].split(":");
			length = ""	+ ((Integer.parseInt(timesArray[0]) * 60 * 60) + (Integer.parseInt(timesArray[1]) * 60) + (int) Double.parseDouble(timesArray[2]));
		} catch (IOException e) {
			if (e.getMessage() == "Stream closed") { 
				// This is expected behavior from cancel
			} else {
				e.printStackTrace();
			}
		}
		return length; 
	}

	
	public void preview(boolean start, String text, String fontPath,
			String fontSize, String colour) {
		String cmd = null;

		if (start) {
			cmd = "avplay -i preview.mp4 -strict experimental -vf \"drawtext=fontfile='"
					+ fontPath
					+ "':text='"
					+ text
					+ "':fontsize="
					+ fontSize
					+ ":fontcolor=" + colour + "\"";
		} else {
			cmd = "avplay -i preview.mp4 -strict experimental -vf \"drawtext=fontfile='"
					+ fontPath
					+ "':text='"
					+ text
					+ "':fontsize="
					+ fontSize
					+ ":fontcolor=" + colour + "\"";
		}
		try {
			_builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			_builder.directory(new File(System.getProperty("user.home")
					+ "/VAMIX"));
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();

			_result = _process.waitFor();

			_process.destroy();

		} catch (IOException | InterruptedException e) {
		}

	}

	// Creates a preview with the text 
	public void createPreview() {
		try {
			File input = new File("input.jpg");
			_builder = new ProcessBuilder("/bin/bash", "-c",
					"avconv -loop 1 -i " + input.getAbsolutePath()
							+ " -r 1 -t 1 -s 1280x720 preview.mp4");
			_builder.directory(new File(System.getProperty("user.home")
					+ "/VAMIX"));
			_builder = _builder.redirectErrorStream(true);
			_process = _builder.start();

			_result = _process.waitFor();
			if (_result == 0) {

			} else {

			}

			_process.destroy();

		} catch (IOException | InterruptedException e) {
		}

	}

	// Empties VAMIX folder for new operation
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
			JOptionPane.showMessageDialog(null, "Text Drawing Complete. Please copy your output file from the VAMIX folder in your home directory.");
		} else {
			JOptionPane.showMessageDialog(null,	"Operation incomplete");
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