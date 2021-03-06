package videoPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * MediaTime displays progress through the media in the form hh:mm:ss. Taken
 * from SE206 Assignment 3, paired prototype.
 */
@SuppressWarnings("serial")
public class MediaTime extends JLabel implements ActionListener {
	private EmbeddedMediaPlayer _player;
	Timer _timer;
	private int _hours = 0;
	private int _minutes = 0;
	private int _seconds = 0;
	private int _hours10s = 0;
	private int _minutes10s = 0;
	private int _seconds10s = 0;

	public MediaTime(EmbeddedMediaPlayer embeddedMediaPlayer) {
		_player = embeddedMediaPlayer;

		// Timer for updating slider
		_timer = new Timer(10, this);
		_timer.setActionCommand("tick");
		_timer.start();
	}

	/**
	 * Recalculate progress in seconds, and update
	 */
	private void updateTime() {
		if (_player.getLength() == 0) {
			this.setTime(0);
		} else {
			this.setTime((int) ((_player.getPosition() * (_player.getLength() / 1000))));
		}
	}

	/**
	 * Set media time in hh:mm:ss from seconds
	 * @param totalSecs to convert to hh:mm:ss format
	 */
	private void setTime(int totalSecs) {
		this.setToolTipText("Seconds: " + totalSecs);
		// Convert seconds to hh:mm:ss
		_hours = (int) Math.floor(totalSecs / 3600);
		totalSecs = totalSecs - (_hours * 3600);
		_hours10s = (int) Math.floor(_hours / 10);
		_hours = _hours - (_hours10s * 10);

		_minutes = (int) Math.floor(totalSecs / 60);
		totalSecs = totalSecs - (_minutes * 60);
		_minutes10s = (int) Math.floor(_minutes / 10);
		_minutes = _minutes - (_minutes10s * 10);

		_seconds = (int) Math.floor(totalSecs);
		_seconds10s = (int) Math.floor(_seconds / 10);
		_seconds = _seconds - (_seconds10s * 10);

		this.setText("" + _hours10s + _hours + ":" + _minutes10s + _minutes
				+ ":" + _seconds10s + _seconds + "");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("tick")) {
			// Update every timer tick
			updateTime();
		}
	}
	
	/**
	 * Gets the current time strings
	 * @return string array of the current time
	 */
	public String[] getCurrentTime(){
		String[] times = new String[3];
		times[0] = "" + _hours10s + _hours;
		times[1] = "" + _minutes10s + _minutes;
		times[2] = "" + _seconds10s + _seconds;
		return times;
	}
}