package videoPanel;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import operations.StateOrganiser;
import sidePanel.ConcatPanel;
import sidePanel.ExtractPanel;
import sidePanel.FilterPanel;
import sidePanel.GeneralPanel;
import sidePanel.MergeAudioPanel;
import sidePanel.ReplaceAudioPanel;
import sidePanel.SubtitlesPanel;
import sidePanel.ThemeSelector;
import sidePanel.VidEditingPanel;
import sidePanel.VideoAdjustments;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 * MainGUI contains all of the GUI pieces that make up the VAMIX program. Taken
 * from SE206 Assignment 3, paired prototype.
 */
public class MainGUI {
	GeneralPanel _generalPanel = null;
	EmbeddedMediaPlayer _mediaPlayer = null;
	ControlsPanel _controlPanel = null;
	JFrame _themeSelector = null;
	JFrame _videoAdjustment = null;
	JTabbedPane tabbedPane = null;

	/**
	 * Create and add all the necessary GUI components
	 */
	public void createGUI() {
		// Create VAMIX directory and save log file
		File dir = new File(System.getProperty("user.home") + "/VAMIX");
		dir.mkdir();
		File log = new File(System.getProperty("user.home") + "/VAMIX/.log.txt");
		try {
			log.createNewFile();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		// Create and initialise panels
		final JFrame frame = new JFrame("VAMIX");
		frame.setLayout(new MigLayout());
		JPanel videoPanel = new JPanel(new MigLayout("center"));
		JPanel sidePanel = new JPanel();
		Canvas playerCanvas = new Canvas();
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu saveMenu = new JMenu("Editor State");
		JMenu optionsSubMenu = new JMenu("Options");
		JMenu helpMenu = new JMenu("Help");

		// Split pane between editor and player
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				sidePanel, videoPanel);
		splitPane.setEnabled(false);
		frame.add(splitPane, "push, grow");

		// Media player
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		_mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();

		// Creates a surface to attach the mediaPlayer to the canvas
		CanvasVideoSurface videoSurface = mediaPlayerFactory
				.newVideoSurface(playerCanvas);
		_mediaPlayer.setVideoSurface(videoSurface);
		videoPanel.add(playerCanvas, "grow, wrap");

		// Set up canvas to reflect screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		playerCanvas.setMinimumSize(new Dimension(10, 10));
		playerCanvas.setPreferredSize(screenSize);
		playerCanvas.setBackground(Color.black);

		// Setup side panel and add tabbedPane
		sidePanel.setLayout(new BorderLayout());
		sidePanel.setPreferredSize(new Dimension(screenSize.width / 4,
				screenSize.height / 4));
		tabbedPane = new JTabbedPane();
		sidePanel.add(tabbedPane);

		// Create panes for tabbedPane and add them
		_generalPanel = new GeneralPanel("General", _mediaPlayer);
		tabbedPane.addTab("File Select", _generalPanel);
		ExtractPanel extractPanel = new ExtractPanel("Cut and Trim",
				_generalPanel);
		MergeAudioPanel mergePanel = new MergeAudioPanel("Merge Audio",
				_generalPanel);
		ReplaceAudioPanel overlayPanel = new ReplaceAudioPanel(
				"Replace Audio Track", _generalPanel);

		// Wrap each audio pane into wrapAudio
		JPanel wrapAudio = new JPanel(new MigLayout());
		JScrollPane scrollPane = new JScrollPane(wrapAudio);

		wrapAudio.add(extractPanel, "wrap, grow");
		wrapAudio.add(mergePanel, "wrap, grow");
		wrapAudio.add(overlayPanel, "grow, wrap");
		wrapAudio.add(new ConcatPanel("Join Media", _generalPanel), "grow");
		tabbedPane.addTab("Manipulation", scrollPane);

		JPanel wrapVideo = new JPanel(new MigLayout());
		VidEditingPanel vPanel = new VidEditingPanel("Title and Credits Scene",
				_generalPanel);
		FilterPanel fPanel = new FilterPanel("Video Filters", _generalPanel);

		wrapVideo.add(vPanel, "wrap");
		wrapVideo.add(fPanel, "grow");

		tabbedPane.addTab("Video", wrapVideo);

		// Initialise control panel
		_controlPanel = new ControlsPanel(_mediaPlayer, _generalPanel);
		videoPanel.add(_controlPanel, "");

		JPanel wrapSubtitle = new JPanel(new MigLayout());
		SubtitlesPanel sPanel = new SubtitlesPanel("Subtitles", _generalPanel,
				_controlPanel.getMediaTime());
		wrapSubtitle.add(sPanel, "grow");

		tabbedPane.addTab("Subtitles", wrapSubtitle);

		// Setup StateOrganiser
		final StateOrganiser _so = new StateOrganiser(
				System.getProperty("user.home") + "/VAMIX/.log.txt", vPanel,
				sPanel);

		// Organise frame
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setJMenuBar(menuBar);

		// Custom icon image
		Image icon = Toolkit.getDefaultToolkit().getImage(
				"resources/simple.png");
		frame.setIconImage(icon);

		// Setup menu items
		menuBar.add(fileMenu);
		JMenuItem quickOpen = new JMenuItem("Open Media");
		fileMenu.add(quickOpen);

		quickOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					_generalPanel.setInputField(selectedFile.getAbsolutePath());
					_mediaPlayer.playMedia(selectedFile.getAbsolutePath());
					_generalPanel.addToList(selectedFile.getAbsolutePath());
				}
			}
		});

		menuBar.add(saveMenu);
		JMenuItem item0 = new JMenuItem("Save State");
		JMenuItem item1 = new JMenuItem("Load State");
		saveMenu.add(item0);
		saveMenu.add(item1);

		// Setup menu actions
		item0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_so.save();
			}
		});

		item1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_so.load();
			}
		});

		JMenuItem item3 = new JMenuItem("Show ReadMe");
		item3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Desktop dt = Desktop.getDesktop();
				File file = new File("resources/SE206_VAMIX_ReadMe.txt");
				try {
					dt.open(file);
				} catch (IOException e1) {
				}
			}
		});

		fileMenu.add(optionsSubMenu);
		JMenuItem theme = new JMenuItem("Change Theme");
		optionsSubMenu.add(theme);

		// Implement custom theme feature
		theme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_themeSelector = new ThemeSelector(frame);
				_themeSelector.setVisible(true);
				frame.setEnabled(false);
				_themeSelector.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						SwingUtilities.updateComponentTreeUI(frame);
						SwingUtilities.updateComponentTreeUI(tabbedPane);
						frame.setEnabled(true);
					}
				});
			}
		});

		JMenuItem adjust = new JMenuItem("Playback Adjustment");
		optionsSubMenu.add(adjust);

		adjust.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_videoAdjustment = new VideoAdjustments(_mediaPlayer);
				_videoAdjustment.setVisible(true);
				_videoAdjustment.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						SwingUtilities.updateComponentTreeUI(frame);
						SwingUtilities.updateComponentTreeUI(tabbedPane);
						frame.setEnabled(true);
					}
				});
			}
		});

		helpMenu.add(item3);
		menuBar.add(helpMenu);

		// Add media player listener
		_mediaPlayer
				.addMediaPlayerEventListener(new MediaPlayerEventListenerAdapter() {
					@Override
					public void finished(MediaPlayer player) {
						_mediaPlayer.stop();
						_mediaPlayer.playMedia(_generalPanel.getInputField());
						_mediaPlayer.pause();
						_controlPanel.switchPlayIcon();
					}

					@Override
					public void paused(MediaPlayer arg0) {
						_controlPanel.switchPlayIcon();
					}

					@Override
					public void playing(MediaPlayer arg0) {
						_controlPanel.switchPlayIcon();
					}
				});
	}
}
