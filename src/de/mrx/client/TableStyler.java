package de.mrx.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * sets the CSS-style of a table
 *
 */
public class TableStyler {
	
	/**
	 * edit numbers to format like: ??? ??? ???
	 * @param w
	 * @throws NumberFormatException
	 */
	public static void expandNumber(HasText widget) {
		String number = widget.getText();
		number = number.replaceAll("\\s", "");
		new Long(number);
		//will throw exception now if is no Long

		//only edit number with length > 4
		if (number.length() <= 4)
			return;
		String formatedNumber = "";
		
		int numberSubgroups = number.length() / 3;
		if (number.length() % 3 != 0)
			numberSubgroups++;
		for (int c = 0; c <  numberSubgroups; c++) {
			int startIndex = c * 3;
			int endIndex = c * 3 + 3;
			if (endIndex >= number.length())
				endIndex = number.length() - 1;
			formatedNumber += number.substring(startIndex, endIndex)+ " ";
		}
		widget.setText(formatedNumber);


	}

	
	public static void setTableStyle(FlexTable table) {
		//set style of header
		for (int i = 0; i < table.getCellCount(0); i++)
			table.getCellFormatter().addStyleName(0, i, "TransfersHeader");
		
		//content
		for (int row = 1; row < table.getRowCount(); row++) {			
			if (row %2 == 0) { //even row
				for (int i = 0; i < table.getCellCount(row); i++) {
					table.getCellFormatter().addStyleName(row, i, "TransfersEven");
					try {
						Widget w = table.getWidget(row, i);
						if (w instanceof HasText)
							expandNumber((HasText)w);
					}
					catch (NumberFormatException n) { //no integer so just continue
						
					}
				}
			}
			else //odd row
				for (int i = 0; i < table.getCellCount(row); i++) {
					try {
						table.getCellFormatter().addStyleName(row, i, "TransfersOdd");
						Widget w = table.getWidget(row, i);
						if (w instanceof HasText)
							expandNumber((HasText)w);
					}
					catch (NumberFormatException n) { //no integer so just continue
					
					}
				}
			
		}
	}
}
