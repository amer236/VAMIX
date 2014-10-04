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
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;
import operations.StateOrganiser;
import sidePanel.ExtractPanel;
import sidePanel.GeneralPanel;
import sidePanel.MergeAudioPanel;
import sidePanel.OverlayPanel;
import sidePanel.VidEditingPanel;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 * MainGUI contains all of the GUI pieces that make up the VAMIX program.
 * Taken from SE206 Assignment 3, paired prototype.
 */
public class MainGUI {

	// Create and add all the necessary GUI components
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
		JFrame frame = new JFrame("VAMIX Prototype");
		frame.setLayout(new MigLayout());
		JPanel videoPanel = new JPanel(new MigLayout("center"));
		JPanel sidePanel = new JPanel();
		Canvas playerCanvas = new Canvas();
		JMenuBar menuBar = new JMenuBar();
		JMenu menu0 = new JMenu("Editor State");
		JMenu menu1 = new JMenu("Help");

		// Split pane between editor and player
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidePanel, videoPanel);
		frame.add(splitPane, "push, grow");

		// Media player
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayer.setRepeat(true);

		// Creates a surface to attach the mediaPlayer to the canvas
		CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(playerCanvas);
		mediaPlayer.setVideoSurface(videoSurface);
		videoPanel.add(playerCanvas, "grow, wrap");

		// Set up canvas to reflect screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		playerCanvas.setMinimumSize(new Dimension(10, 10));
		playerCanvas.setPreferredSize(screenSize);
		playerCanvas.setBackground(Color.black);

		// Setup side panel and add tabbedPane
		sidePanel.setLayout(new BorderLayout());
		sidePanel.setPreferredSize(new Dimension(screenSize.width / 4, screenSize.height / 4));
		JTabbedPane tabbedPane = new JTabbedPane();
		sidePanel.add(tabbedPane);

		// Create panes for tabbedPane and add them
		GeneralPanel generalPanel = new GeneralPanel("General", mediaPlayer);
		tabbedPane.addTab("General", generalPanel);
		ExtractPanel extractPanel = new ExtractPanel("Cut/Trim Audio, Video or Both", generalPanel);
		MergeAudioPanel mergePanel = new MergeAudioPanel("Merge Audio Tracks", generalPanel);
		OverlayPanel overlayPanel = new OverlayPanel("Replace audio track", generalPanel);

		// Wrap each audio pane into wrapAudio
		JPanel wrapAudio = new JPanel(new MigLayout());
		JScrollPane scrollPane = new JScrollPane(wrapAudio);
		wrapAudio.add(extractPanel, "wrap, grow");
		wrapAudio.add(mergePanel, "wrap, grow");
		wrapAudio.add(overlayPanel, "grow");
		tabbedPane.addTab("Audio", scrollPane);
		VidEditingPanel vPanel = new VidEditingPanel("Video Editing", generalPanel);
		tabbedPane.addTab("Video Editing", vPanel);
		videoPanel.add(new ControlsPanel(mediaPlayer, generalPanel), "");

		// Setup StateOrganiser
		final StateOrganiser _so = new StateOrganiser(System.getProperty("user.home") + "/VAMIX/.log.txt", vPanel);

		// Organise frame
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setJMenuBar(menuBar);
		
		//Don't think this is working
		Image icon = Toolkit.getDefaultToolkit().getImage("resources/simple.png");
		frame.setIconImage(icon);

		// Setup menu items
		menuBar.add(menu0);
		JMenuItem item0 = new JMenuItem("Save State");
		JMenuItem item1 = new JMenuItem("Load State");
		menu0.add(item0);
		menu0.add(item1);

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
				File file = new File("resources/SE206A3_ReadMe.txt");
				try {
					dt.open(file);
				} catch (IOException e1) {
				}
			}
		});

		menu1.add(item3);
		menuBar.add(menu1);
	}

}
