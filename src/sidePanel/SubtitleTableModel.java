package sidePanel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class SubtitleTableModel extends AbstractTableModel {
	
	private String[] colNames = new String[3];
	private ArrayList<String[]> dataEntries = new ArrayList<String[]>();
	
	public SubtitleTableModel(){
		colNames[0] = "Start Time";
		colNames[1] = "Stop Time";
		colNames[2] = "Text";
	}

//	public Class getColumnClass(int c) {
//        return getValueAt(0, c).getClass();
//    }


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
		//return 10;
		return dataEntries.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		//return "poop";
		return dataEntries.get(rowIndex)[columnIndex];
	}


	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		dataEntries.get(rowIndex)[rowIndex] = (String) aValue;

	}
	
	public void addDataRow(String s, String t, String text){
		String[] obj = new String[3];
		obj[0] = s;
		obj[1] = t;
		obj[2] = text;
		
		dataEntries.add(obj);
		fireTableDataChanged();
	}
	
	public void deleteDataRow(int row){		
		dataEntries.remove(row);
		fireTableDataChanged();
	}
}
