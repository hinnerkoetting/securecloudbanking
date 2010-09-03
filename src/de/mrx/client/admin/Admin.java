//
//
// Administration Client page
//
//

package de.mrx.client.admin;



//checkintest
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


import de.mrx.client.AccountDTO;
import de.mrx.client.BankingService;
import de.mrx.client.BankingServiceAsync;
import de.mrx.client.SCB;


public class Admin implements EntryPoint {

	AdminConstants constants = GWT.create(AdminConstants.class);
	
	public final String ADMIN_PAGE_ID = "admin";
	
	
	//test
	private VerticalPanel page;
	private Grid pageGrid;
	
	
	/*
	 * create menu on the left 
	 */
	private VerticalPanel createAdminMenu() {
		
		ClickHandler clickOverview = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				
				BankingServiceAsync bankingService = GWT.create(BankingService.class);
				bankingService.getAccounts(new AsyncCallback<List<AccountDTO>>() {

					@Override
					public void onFailure(Throwable caught) {
						pageGrid.setWidget(0, 1, new Label(caught.toString()));	
						
					}

					@Override
					public void onSuccess(List<AccountDTO> result) {
						pageGrid.setWidget(0, 1, new Label("Accounts2"));	
						
					}
					
				});
				
			}	
		};
		
		ClickHandler clickNewTransaction = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageGrid.setWidget(0, 1, new Label("New Transaction"));				
			}	
		};
		
		ClickHandler clickExternalBanks = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageGrid.setWidget(0, 1, new Label("External Banks"));				
			}			
		};
		
		ClickHandler clickHistory = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageGrid.setWidget(0, 1, new Label("History"));
			}
		};
		
		VerticalPanel panel = new VerticalPanel();		
		
		
		//add all buttons
		Button btn = new Button(constants.overviewAccount(), clickOverview);
		panel.add(btn);
		
		btn = new Button(constants.newTransaction(), clickNewTransaction);
		panel.add(btn);
		
		btn = new Button(constants.externalBanks(), clickExternalBanks);
		panel.add(btn);
		
		btn = new Button(constants.history(), clickHistory);
		panel.add(btn);
		
		
		return panel;
	}
	
	/*
	 * create content for overview of admin page
	 */
	private VerticalPanel createContentOverview() {

		VerticalPanel content = new VerticalPanel();
		
		Label label = new Label("PLACEHOLDER");		
		content.add(label);
		
		return content;
	}
	
	@Override
	public void onModuleLoad() {
		
		RootPanel r = RootPanel.get(ADMIN_PAGE_ID);		
		if (r == null)
			return;
		page = new VerticalPanel();

		Label title = new Label("Administration");
		title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		
		r.add(title);
		
		pageGrid = new Grid(1, 2 );
		page.add(pageGrid);
		r.add(page);
		
		VerticalPanel adminMenu = createAdminMenu();
		adminMenu.setSpacing(10);
		
		pageGrid.setWidget(0, 0, adminMenu);
		pageGrid.setWidget(0, 1, createContentOverview());
		
		//temp workaround for header
		SCB scb = new SCB();
		scb.createMainPanel();
		r = RootPanel.get(SCB.PAGEID_HEADER);
		r.add(scb.createMainPanel());
		
		

	}

}
