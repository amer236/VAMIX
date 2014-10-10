package sidePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;

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
		
		JButton base = new JButton("Set Base Colour");
		base.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("nimbusBase", new Color(red.getValue(), blue.getValue(), green.getValue()));
				frame.repaint();
				repaint();
			}
		});
		panel.add(base,"grow");
		
		JButton control = new JButton("Set Control Color");
		control.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("control", new Color(red.getValue(), blue.getValue(), green.getValue()));
				frame.repaint();
				repaint();
			}
		});
		panel.add(control, "grow, wrap");
		
		JButton highlight = new JButton("Set Highlight Color");
		control.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("nimbusBlueGrey", new Color(red.getValue(), blue.getValue(), green.getValue()));
				UIManager.put("nimbusFocus", new Color(red.getValue(), blue.getValue(), green.getValue()));
				frame.repaint();
				repaint();
			}
		});
		panel.add(highlight, "grow, wrap, span");
		
		JButton def = new JButton("Default All");
		def.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("control", new Color(214, 217, 223));
				UIManager.put("nimbusBase", new Color(51,98,140));
				UIManager.put("nimbusBlueGrey", new Color(169,176,190));
				UIManager.put("nimbusFocus", new Color(115,164,209));
				repaint();
				frame.repaint();
			}
		});
		panel.add(def,"span, grow, wrap");
		
		JButton done = new JButton("Done");
		done.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.repaint();
				frame.setEnabled(true);
				dispose();
			}
		});
		panel.add(done,"span, grow");
		
		
		
		this.setPreferredSize(new Dimension(300, 300));
		this.pack();
		this.add(panel);
	}
	
}
