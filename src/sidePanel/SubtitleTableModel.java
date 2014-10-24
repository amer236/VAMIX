package sidePanel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * This is the model for the subtitle table in the subtitlespanel
 */
@SuppressWarnings("serial")
public class SubtitleTableModel extends AbstractTableModel {
	
	private String[] colNames = new String[3];
	private ArrayList<String[]> dataEntries = new ArrayList<String[]>();
	
	public SubtitleTableModel(){
		colNames[0] = "Start Time";
		colNames[1] = "Stop Time";
		colNames[2] = "Text";
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
		dataEntries.get(rowIndex)[rowIndex] = (String) aValue;
	}
	
	/**
	 * Add a new row
	 * @param s start time
	 * @param t stop time
	 * @param text
	 */
	public void addDataRow(String s, String t, String text){
		String[] obj = new String[3];
		obj[0] = s;
		obj[1] = t;
		obj[2] = text;
		
		dataEntries.add(obj);
		fireTableDataChanged();
	}
	
	/**
	 * Delete row from table
	 * @param row to delete
	 */
	public void deleteDataRow(int row){		
		dataEntries.remove(row);
		fireTableDataChanged();
	}
}