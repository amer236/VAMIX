package sidePanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
	
	/**
	 * Sorts the list of subtitles
	 */
	public void sortList(){
		Collections.sort(dataEntries, new Comparator<String[]>() {
			@Override
			public int compare(String[] o1, String[] o2) {
				String[] obj1 = o1[0].split(":");
				String[] obj2 = o2[0].split(":");

				if(Integer.parseInt(obj1[0]) > Integer.parseInt(obj2[0])){
					return 1;
				} else if(Integer.parseInt(obj1[0]) < Integer.parseInt(obj2[0])){
					return -1;
				}
				if(Integer.parseInt(obj1[1]) > Integer.parseInt(obj2[1])){
					return 1;
				} else if(Integer.parseInt(obj1[1]) < Integer.parseInt(obj2[1])){
					return -1;
				}
				if(Integer.parseInt(obj1[2]) > Integer.parseInt(obj2[2])){
					return 1;
				} else if(Integer.parseInt(obj1[2]) < Integer.parseInt(obj2[2])){
					return -1;
				}
				
				return 0;
				
				//return o1[0].compareTo(o2[0]);
			}
		});
	}
}