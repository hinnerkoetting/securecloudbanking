package de.mrx.client.admin.forms;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.TableStyler;
import de.mrx.client.admin.AdminConstants;


public class TanTable extends Composite {

	private static TanListUiBinder uiBinder = GWT.create(TanListUiBinder.class);

	interface TanListUiBinder extends UiBinder<Widget, TanTable> {
	}

	@UiField
	FlexTable tanTable;

	@UiField
	FlexTable selectPages;
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	@UiField 
	MyStyle style;
	
	interface MyStyle extends CssResource {
		    String active();
		    String nonActive();
	}
	
	
	private final static int NUMBER_TANS_PER_PAGE = 10;
	private List<String> tans;
	
	private int numerPages(int accountsNr) {
		int numberPages = accountsNr /  NUMBER_TANS_PER_PAGE;
		if (accountsNr %  NUMBER_TANS_PER_PAGE != 0)
			numberPages++;
		return numberPages;
	}
	
	public TanTable(List<String> list) {
		this.tans = list;
		initWidget(uiBinder.createAndBindUi(this));
		
		
		//setup pages
		int numberPages = numerPages(tans.size());
		for (int i = 1; i <= numberPages; i++) {
			Anchor pageLink = new Anchor(""+i);
			selectPages.setWidget(0, i, pageLink);
			pageLink.setStyleName(style.nonActive());
			final int j = i;
			pageLink.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					switchToPage(j);
					
				}
			});
		}
		
		//select page 1
		switchToPage(1);
	}
	
	public void switchToPage(int page) {
		tanTable.clear();
		//header
		//reset style for all pages
		int numberPages = numerPages(tans.size());
		for (int i = 1; i <= numberPages; i++) {
			Anchor pageLink = (Anchor)selectPages.getWidget(0, i);
			selectPages.setWidget(0, i, pageLink);
			if (page != i) { 
				pageLink.setStyleName(style.nonActive());
			}
			else { //page==i
				//set active anchor
				pageLink.setStyleName(style.active());
			}
		}
		tanTable.setWidget(0, 0, new Label(constants.nr()));
		tanTable.setWidget(0, 1, new Label(constants.tan()));
		
		
		//entries
		int row = 1;
		for (int i = 0; i < NUMBER_TANS_PER_PAGE; i++) {
			int index = (page-1) * NUMBER_TANS_PER_PAGE + i;
			if (index >= tans.size()) {
				if (tanTable.getRowCount() > row)
					tanTable.removeRow(row);
				//row++ is not needed because the last row was just removed
				continue;
			}
			String entry = tans.get(index);
			tanTable.setWidget(row , 0, new Label(""+(index+1)));
			tanTable.setWidget(row, 1, new Label(entry));
			row++;
		}
		TableStyler.setTableStyle(tanTable);
	}


}
