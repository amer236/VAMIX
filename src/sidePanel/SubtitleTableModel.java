package sidePanel;

import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class SubtitleTableModel extends DefaultTableModel {
	
	private String[] colNames = new String[3];
	private ArrayList<int[]> dataEntries = new ArrayList<int[]>();
	
	public SubtitleTableModel(){
		colNames[0] = "Start Time";
		colNames[1] = "Stop Time";
		colNames[2] = "Text";
	}
	
	public void addDataRow(){
		int[] obj = new int[3];
		dataEntries.add(obj);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return colNames[columnIndex];
	}

	@Override
	public int getRowCount() {
		return dataEntries.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return dataEntries.get(rowIndex)[columnIndex];
	}


	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		dataEntries.get(rowIndex)[rowIndex] = (int) aValue;

	}
}
