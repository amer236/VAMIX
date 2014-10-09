package sidePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

/**
 * ThemeSelector allows the user to change some colours of the current theme
 * Taken from SE206 Assignment 3, paired prototype.
 */
public class ThemeSelector extends JFrame {

	public ThemeSelector(final JFrame frame) {
		JPanel panel = new JPanel(new MigLayout());
		
		panel.add(new JLabel("NimbusBase"));
		panel.add(new JLabel("Control"), "wrap");
		
		JButton greenBase = new JButton("Green");
		greenBase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("nimbusBase", Color.green);
				frame.repaint();
				repaint();
			}
		});
		panel.add(greenBase,"grow");
		
		JButton greenControl = new JButton("Green");
		greenControl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("control", Color.green);
				frame.repaint();
				repaint();
			}
		});
		panel.add(greenControl, "wrap, grow");
		
		JButton redBase = new JButton("Red");
		redBase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("nimbusBase", Color.red);
				frame.repaint();
				repaint();
			}
		});
		panel.add(redBase,"grow");
		
		JButton redControl = new JButton("Red");
		redControl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("control", Color.red);
				frame.repaint();
				repaint();
			}
		});
		panel.add(redControl,"wrap, grow");
		
		JButton def = new JButton("Default All");
		def.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("control", new Color(214, 217, 223));
				UIManager.put("nimbusBase", new Color(51,98,140));
				repaint();
				frame.repaint();
			}
		});
		panel.add(def,"span, grow");
		
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
