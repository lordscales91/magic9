package io.github.lordscales91.magic9.widget;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class UpdatableModel extends DefaultTableModel {
	Class<?>[] columnTypes = new Class[] {
		String.class, String.class, String.class, Float.class
	};

	public UpdatableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
	}
	
	private int findTag(String tag) {
		int result = -1;
		for(int i=0;i<getRowCount();i++) {
			if(tag.equals(getValueAt(i, 0))) {
				result = i;
				i = getRowCount();
			}
		}
		return result;
	}

	public void updateProgress(String tag, float progress) {
		int index = findTag(tag);
		if(index != -1)  {
			setValueAt(progress, index, 3);
		}
	}
	
	public void updateStatus(String tag, String status) {
		int index = findTag(tag);
		if(index != -1)  {
			setValueAt(status, index, 2);
		}
	}
	

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnTypes[columnIndex];
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}