package sidePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import net.miginfocom.swing.MigLayout;

public class PlaybackPanel extends SidePanel{
	
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	JList list = new JList(listModel);
	JButton add = new JButton("Queue File");
	JButton delete = new JButton("Remove file from playlist");
	EmbeddedMediaPlayer _player = null;
	GeneralPanel _general = null;
	JLabel pl = null;

	public PlaybackPanel(String name, EmbeddedMediaPlayer player, GeneralPanel general) {
		super(name);
		_player = player;
		_general = general;

		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					listModel.addElement(selectedFile.getAbsolutePath());
				}
				delete.setEnabled(true);
				pl.setText("Playlist");
				
				if(_player.isPlayable()){
					// Do nothing
				}else{
					String mediapath = _general.getPlaybackPanel().getElementZero();
					_player.playMedia(mediapath);
					_general.setInputField(mediapath);
				}
			}
		});
		
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedIndex() == -1){
					//Nothing selected
				} else{
					listModel.remove(list.getSelectedIndex());
					if(listModel.getSize() == 0){
						delete.setEnabled(false);
						pl.setText("");
					}
				}
			}
		});
		
		
		this.setLayout(new MigLayout());
		pl = new JLabel("");
		this.add(pl, "wrap");
		this.add(list, "grow, wrap, span");
		this.add(add);
		this.add(delete);
		
		delete.setEnabled(false);
	}
	
	public boolean listHasNext(){
		if(listModel.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	public String getElementZero(){
		String path = listModel.elementAt(0);
		listModel.remove(0);
		if(listModel.getSize() == 0){
			delete.setEnabled(false);
			pl.setText("");
		}
		return path;
	}

}
