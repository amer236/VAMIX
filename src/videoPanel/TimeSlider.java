package videoPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSlider;
import javax.swing.Timer;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * TimeSlider is a slider which displays the progress of the media player and
 * enables controls of the media player.
 */
public class TimeSlider extends JSlider implements ActionListener {
	private EmbeddedMediaPlayer _player;
	Timer _timer;

	public TimeSlider(EmbeddedMediaPlayer embeddedMediaPlayer) {
		_player = embeddedMediaPlayer;

		// Timer for updating slider
		_timer = new Timer(500, this);
		_timer.setActionCommand("tick");
		_timer.start();

		initSlider();
	}

	private void initSlider() {
		// Initialization
		this.setMinimum(0);
		this.setMaximum(10000);

		// Only update slider when it is not in use
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				_timer.stop();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				_player.setPosition((float) TimeSlider.this.getValue() / 10000);
				_timer.start();
			}
		});
	}

	private void updateTime() {
		// Update slider to time of media
		if (_player.getLength() == 0) {
			this.setValue(0);
		} else {
			this.setValue((int) ((_player.getPosition() * 10000)));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("tick")) {
			// Update every timer tick
			updateTime();
		}
	}
}
