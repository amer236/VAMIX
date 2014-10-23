package sidePanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import net.miginfocom.swing.MigLayout;

/**
 * ThemeSelector allows the user to change some colours of the current theme
 * Taken from SE206 Assignment 3, paired prototype.
 */
public class ThemeSelector extends JFrame {
	JSlider red = new JSlider(0,255);
	JSlider green = new JSlider(0,255);
	JSlider blue = new JSlider(0,255);
	
	public ThemeSelector(final JFrame frame) {
		this.setTitle("Customise Theme");
		JPanel panel = new JPanel(new MigLayout());

		panel.add(new JLabel("Red"), "grow");
		panel.add(red, "wrap, grow");
		panel.add(new JLabel("Green"), "grow");
		panel.add(green, "wrap, grow");
		panel.add(new JLabel("Blue"), "grow");
		panel.add(blue, "wrap, grow");
		
		//panel.add(new JLabel("NimbusBase"));
		//panel.add(new JLabel("Control"), "wrap");
		
		//This button is too bugged to be included in the final version
//		JButton base = new JButton("Set Base Colour");
//		base.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				UIManager.put("nimbusBase", new ColorUIResource(red.getValue(), green.getValue(), blue.getValue()));
//				SwingUtilities.updateComponentTreeUI(frame);
//				SwingUtilities.updateComponentTreeUI(ThemeSelector.this);
//			}
//		});
//		panel.add(base,"grow");
		
		JButton control = new JButton("Set Background Color");
		control.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("control", new ColorUIResource(red.getValue(), green.getValue(), blue.getValue()));
				SwingUtilities.updateComponentTreeUI(frame);
				SwingUtilities.updateComponentTreeUI(ThemeSelector.this);
			}
		});
		panel.add(control, "grow");
		
		JButton highlight = new JButton("Set Highlight Color");
		highlight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("nimbusBlueGrey", new ColorUIResource(red.getValue(), green.getValue(), blue.getValue()));
				UIManager.put("nimbusFocus", new ColorUIResource(red.getValue(), green.getValue(), blue.getValue()));
				SwingUtilities.updateComponentTreeUI(ThemeSelector.this);				
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		panel.add(highlight, "grow, wrap");
		
		JButton def = new JButton("Default All");
		def.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("control", new ColorUIResource(214, 217, 223));
				UIManager.put("ProgressBar.background", new ColorUIResource(214, 217, 223));
				UIManager.put("nimbusBase", new ColorUIResource(51,98,140));
				UIManager.put("nimbusBlueGrey", new ColorUIResource(169,176,190));
				UIManager.put("nimbusFocus", new ColorUIResource(115,164,209));
				SwingUtilities.updateComponentTreeUI(frame);
				SwingUtilities.updateComponentTreeUI(ThemeSelector.this);
			}
		});
		panel.add(def,"span, grow, wrap");
		
		JButton done = new JButton("Done");
		done.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.updateComponentTreeUI(frame);
				frame.setEnabled(true);
				dispose();
			}
		});
		panel.add(done,"span, grow");

		this.setPreferredSize(new Dimension(380, 250));
		this.pack();
		this.add(panel);
		this.setResizable(false);
	}	
}
