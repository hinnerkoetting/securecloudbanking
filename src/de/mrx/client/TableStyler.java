package de.mrx.client;

import com.google.gwt.user.client.ui.FlexTable;

/**
 * 
 * sets the CSS-style of a table
 *
 */
public class TableStyler {
	public static void setTableStyle(FlexTable table) {
		//set style of header
		for (int i = 0; i < table.getCellCount(0); i++)
			table.getCellFormatter().setStyleName(0, i, "TransfersHeader");
		
		for (int row = 1; row < table.getRowCount(); row++) {
			
			if (row %2 == 0) { //even row
				for (int i = 0; i < 4; i++)
					table.getCellFormatter().setStyleName(row, i, "TransfersEven");
			}
			else //odd row
				for (int i = 0; i < 4; i++)
					table.getCellFormatter().setStyleName(row, i, "TransfersOdd");
			
		}
	}
}
