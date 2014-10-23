package videoPanel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import net.miginfocom.swing.MigLayout;
import sidePanel.GeneralPanel;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * ControlsPanel holds all the controls for the media player and their functionality.
 * Taken from SE206 Assignment 3, paired prototype.
 */
@SuppressWarnings("serial")
public class ControlsPanel extends JPanel {
	private EmbeddedMediaPlayer _player;
	private GeneralPanel _generalPanel;
	private Timer _timer;
	private String _action = "forward";
	private boolean playPauseLoaded = true;
	private boolean muteUnmuteLoaded = true;

	JButton btnRewind;
	JButton btnPlay;
	JButton btnPause;
	JButton btnFastForward;;
	JButton btnMute;
	JButton btnStart;
	JButton btnStop;

	BufferedImage playImage = null;
	BufferedImage pauseImage = null;
	BufferedImage backImage = null;
	BufferedImage forwardImage = null;
	BufferedImage unmuteImage = null;
	BufferedImage muteImage = null;
	BufferedImage stopImage = null;
	
	ImageIcon backIcon;
	ImageIcon playIcon;
	ImageIcon forwardIcon;
	ImageIcon pauseIcon;
	ImageIcon unmuteIcon;
	ImageIcon muteIcon;
	ImageIcon stopIcon;

	public ControlsPanel(EmbeddedMediaPlayer embeddedMediaPlayer, GeneralPanel generalPanel) {
		this.setLayout(new MigLayout("center"));
		_player = embeddedMediaPlayer;
		_generalPanel = generalPanel;

		// Timer to perform repeat actions
		_timer = new Timer(16, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("tick")) {
					repeatAction();
				}
			}
		});
		_timer.setActionCommand("tick");
		createControls();
	}
	
	// Fast forward or rewind depending on _action
	protected void repeatAction() {
		if (_action.equals("forward")) {
			_player.skip(200);
		} else {
			_player.skip(-200);
		}

	}

	// Setup controls as icons if possible and add to panel
	public void createControls() {
		// All icons are from the basic application pack by Freepik, License CC BY 3.0
		// http://www.flaticon.com/packs/basic-application
		try {
			pauseImage = ImageIO.read(new File("resources/pause.png"));
			pauseIcon = new ImageIcon(pauseImage);
		} catch (IOException e1) {
			playPauseLoaded = false;
			e1.printStackTrace();
		}
		
		try {
			stopImage = ImageIO.read(new File("resources/stop.png"));
			stopIcon = new ImageIcon(stopImage);
			btnStop = new JButton(stopIcon);
		} catch (IOException e1) {
			btnStop = new JButton("Stop");
		}

		try {
			playImage = ImageIO.read(new File("resources/play.png"));
			playIcon = new ImageIcon(playImage);
			btnPlay = new JButton(playIcon);
		} catch (IOException e1) {
			btnPlay = new JButton("Play");
			playPauseLoaded = false;
			e1.printStackTrace();
		}

		try {
			backImage = ImageIO.read(new File("resources/back.png"));
			backIcon = new ImageIcon(backImage);
			btnRewind = new JButton(backIcon);
		} catch (IOException e1) {
			btnRewind = new JButton("Rewind");
			e1.printStackTrace();
		}

		try {
			forwardImage = ImageIO.read(new File("resources/forward.png"));
			forwardIcon = new ImageIcon(forwardImage);
			btnFastForward = new JButton(forwardIcon);
		} catch (IOException e1) {
			btnFastForward = new JButton("Fast-Forward");
			e1.printStackTrace();
		}

		try {
			unmuteImage = ImageIO.read(new File("resources/unmute.png"));
			unmuteIcon = new ImageIcon(unmuteImage);
			btnMute = new JButton(unmuteIcon);
		} catch (IOException e1) {
			btnMute = new JButton("Unmute");
			muteUnmuteLoaded = false;
			e1.printStackTrace();
		}

		try {
			muteImage = ImageIO.read(new File("resources/mute.png"));
			muteIcon = new ImageIcon(muteImage);
		} catch (IOException e1) {
			muteUnmuteLoaded = false;
			e1.printStackTrace();
		}

		// Add to panel and set up actions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.add(new MediaTime(_player));
		this.add(new TimeSlider(_player), "span 6, width " + (screenSize.width)	+ ", wrap");
		this.add(new JLabel(""));

		this.add(btnRewind);
		btnRewind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_action = "backward";
				if (_timer.isRunning()) {
					_timer.stop();
				} else {
					_timer.start();
				}
			}
		});

		this.add(btnPlay);
		btnPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_player.getMediaState().toString().equals("libvlc_Stopped") == false) {
					_player.pause();
					_timer.stop();
				} else {
					String file = _generalPanel.getInputField();
					if (file.equals("")) {
						// No source selected
					} else {
						_player.playMedia(file);
					}
				}
			}
		});
		
		this.add(btnStop);
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_player.getMediaState() != null) {
					_player.stop();
					_timer.stop();
					btnPlay.setIcon(playIcon);
				} else {
					String file = _generalPanel.getInputField();
					if (file.equals("")) {
						// No source selected
					} else {
						_player.playMedia(file);
					}
				}
			}
		});

		this.add(btnFastForward);
		btnFastForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_action = "forward";
				if (_timer.isRunning()) {
					_timer.stop();
				} else {
					_timer.start();
				}
			}
		});

		// Volume panel to be aligned to the right
		JPanel volControls = new JPanel();
		volControls.add(btnMute);
		btnMute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_player.mute();
				switchMuteIcon();
			}
		});

		VolumeSlider volSlider = new VolumeSlider(_player);
		volSlider.setMaximumSize(new Dimension(100, 20));
		volControls.add(volSlider);
		this.add(volControls, "wrap, align right");
	}

	// Switch play button between play and pause
	public void switchPlayIcon() {
		if (playPauseLoaded) {
			if (btnPlay.getIcon().equals(playIcon)) {
				btnPlay.setIcon(pauseIcon);
			} else {
				btnPlay.setIcon(playIcon);
			}
		} else {
			if (btnPlay.getText().equals("Play")) {
				btnPlay.setText("Pause");
			} else {
				btnPlay.setText("Play");
			}
		}
	}

	// Switch mute button between mute and unmute
	private void switchMuteIcon() {
		if (muteUnmuteLoaded) {
			if (btnMute.getIcon().equals(unmuteIcon)) {
				btnMute.setIcon(muteIcon);
			} else {
				btnMute.setIcon(unmuteIcon);
			}
		} else {
			if (btnMute.getText().equals("Mute")) {
				btnMute.setText("Unmute");
			} else {
				btnMute.setText("Mute");
			}
		}
	}
}
