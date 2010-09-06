//
//
// Administration Client page
//
//

package de.mrx.client.admin;




import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.SCB;
import de.mrx.client.TransferHistoryForm;
import de.mrx.client.admin.forms.AccountOverview;
import de.mrx.client.admin.forms.AdminTransfer;
import de.mrx.client.admin.forms.AdminWelcome;
import de.mrx.client.admin.forms.Adminmenu;
import de.mrx.client.moneytransfer.MoneyTransferForm;


public class Admin implements EntryPoint {

	AdminConstants constants = GWT.create(AdminConstants.class);
	
	public final String ADMIN_PAGE_ID = "admin";
	
	
	//test
	private VerticalPanel page;
	private Grid pageGrid;
	
    /**
     * show all transactions
     * 
     */
	public void showAccountTransfers(List<MoneyTransferDTO> transfers) {
		setContent(new TransferHistoryForm(transfers));
	}

	public void showNewTransaction() {
		setContent(new Label("PLaceholder1"));
	}
	
	public void showExternalBanks() {
		setContent(new Label("PLaceholder2"));
	}
	
	/**
	 * 
	 */
	public void showTransferMoney(String accNr, String accOwner) {
		setContent(new AdminTransfer(accNr, accOwner));
	}
	
	/**
	 * show information about all accounts
	 */
	public void showAccounts() {
		final AccountOverview accountOverview = new AccountOverview(this);
		
		AdminServiceAsync bankingService = GWT.create(AdminService.class);
		bankingService.getAllAccounts(new AsyncCallback<List<AccountDTO>>() {

			@Override
			public void onFailure(Throwable caught) {					
				GWT.log(caught.toString());
			}

			@Override
			public void onSuccess(List<AccountDTO> result) {
				
				accountOverview.setAccounts(result);
				setContent(accountOverview);
			}
			
		});
	}
	/**
	 * 
	 * @param widget
	 * set content of the admin page
	 */
	private void setContent(Widget widget) {
		pageGrid.setWidget(0, 1, widget);
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
		
		
		pageGrid.setWidget(0, 0, new Adminmenu(this));
		pageGrid.setWidget(0, 1, new AdminWelcome());
		
		//temp workaround for header
		SCB scb = new SCB();
		scb.createMainPanel();
		r = RootPanel.get(SCB.PAGEID_HEADER);
		r.add(scb.createMainPanel());
		
		

	}

}
