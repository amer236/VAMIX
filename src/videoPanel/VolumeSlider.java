package videoPanel;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSlider;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * VolumeSlider is a slider which controls the volume of the embedded media player.
 * Taken from SE206 Assignment 3, paired prototype.
 */
@SuppressWarnings("serial")
public class VolumeSlider extends JSlider {
	private EmbeddedMediaPlayer _player;

	public VolumeSlider(EmbeddedMediaPlayer embeddedMediaPlayer) {
		_player = embeddedMediaPlayer;
		initSlider();
	}

	/**
	 * Initialize slider
	 */
	private void initSlider() {
		// Initialization
		this.setMinimum(0);
		this.setMaximum(100);
		_player.setVolume(50);
		this.setValue(50);

		// When slider is moved, set new volume
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e){
				Point p = VolumeSlider.this.getMousePosition();
				VolumeSlider.this.setValue(100 * p.x / VolumeSlider.this.getWidth());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				_player.setVolume(VolumeSlider.this.getValue());
			}
		});
	}
}
