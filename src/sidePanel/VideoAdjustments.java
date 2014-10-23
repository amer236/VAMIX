package sidePanel;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import net.miginfocom.swing.MigLayout;

public class VideoAdjustments extends JFrame implements ChangeListener{
	EmbeddedMediaPlayer _player;
	JSlider hue = new JSlider(0,360);
	JSlider brightness = new JSlider(0,2000);
	JSlider saturation = new JSlider(0,3000);
	JSlider contrast = new JSlider(0,2000);

	
	public VideoAdjustments(EmbeddedMediaPlayer player) {
		_player = player;
		this.setTitle("Video Adjustments");
		JPanel panel = new JPanel(new MigLayout());

		_player.setAdjustVideo(true);

		hue.setValue(1000 * _player.getHue() / 360);
		brightness.setValue((int) (1000 * _player.getBrightness()));
		saturation.setValue((int) (1000 * _player.getSaturation()));
		contrast.setValue((int) (1000 * _player.getContrast()));


		hue.addChangeListener(this);
		brightness.addChangeListener(this);
		saturation.addChangeListener(this);
		contrast.addChangeListener(this);
		
		panel.add(new JLabel("Hue"), "grow");
		panel.add(hue, "wrap, grow");
		panel.add(new JLabel("Brightness"), "grow");
		panel.add(brightness, "wrap, grow");
		panel.add(new JLabel("Saturation"), "grow");
		panel.add(saturation, "wrap, grow");
		panel.add(new JLabel("Contrast"), "grow");
		panel.add(contrast, "wrap, grow");
		
		this.add(panel);
		this.setPreferredSize(new Dimension(380, 250));
		this.pack();
		this.setResizable(false);
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		_player.setHue(360*hue.getValue()/1000);
		_player.setBrightness(brightness.getValue()/1000.0f);
		_player.setSaturation(saturation.getValue()/1000.0f);
		_player.setContrast(contrast.getValue()/1000.0f);

	}
}
