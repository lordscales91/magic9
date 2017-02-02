package io.github.lordscales91.magic9;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Extracted from <a href="http://stackoverflow.com/a/13755155/3107765">this SO answer</a>
 */
@SuppressWarnings("serial")
public class ProgressCellRender extends JProgressBar implements TableCellRenderer {
	
		public ProgressCellRender() {
			super();
			setStringPainted(true);
		}

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int progress = 0;
            String text = null;
            if (value instanceof Float) {
                progress = Math.round((Float) value);
                text = String.format("%.02f%%", (Float)value);
            } else if (value instanceof Integer) {
                progress = (int) value;
                text = String.format("%d", progress);
            }
            if(text != null) {
            	setString(text);
            }
            setValue(progress);
            return this;
        }
    }