package de.mrx.client.admin.forms;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.TableStyler;
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class SearchAccountsForm extends Composite implements UseSearchForm {

	private static SearchAccountsFormUiBinder uiBinder = GWT
			.create(SearchAccountsFormUiBinder.class);

	interface SearchAccountsFormUiBinder extends UiBinder<Widget, SearchAccountsForm> {
	}
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	@UiField
	Button search;
	
	@UiField
	FlexTable overviewTable;
	
	@UiField
	FlexTable selectPages;
	
	@UiField
	TextBox searchOwner;
	
	@UiField
	TextBox searchAccountNr;	
	
	private boolean displayName;
	private boolean displayAccountNr;
	private boolean displayButton;
	private boolean displayBalance;
	private String buttonName = "";
	
	private List<AccountDTO> accounts;
	
	private final int ACCOUNTS_PER_PAGE = 5;
	

	  @UiField MyStyle style;
		interface MyStyle extends CssResource {
			    String active();
			    String nonActive();
	 }
		
	
	//call this form if user clicks on button in searchform
	UseSearchForm callee;
	
	int posOwner;
	int posAccount;
	int posBalance;		
	int posButton;
	
	private int numerPages(int accountsNr) {
		int numberPages = accountsNr /  ACCOUNTS_PER_PAGE;
		if (accountsNr %  ACCOUNTS_PER_PAGE != 0)
			numberPages++;
		return numberPages;
	}
	public SearchAccountsForm(UseSearchForm callee) {
		this.callee = callee;
		initWidget(uiBinder.createAndBindUi(this));
		search.setText(constants.search());
		displayName = true;
		displayAccountNr = true;
		displayBalance = true;
		displayButton = false;
		
		posOwner 		= 0;
		posAccount 		= 1;
		posBalance 		= 2;		
		posButton 		= 3;
		
	}

	/**
	 * enables or displays columns
	 * @param name
	 * @param accountNr
	 * @param balance
	 */
	public void enableColumns(boolean name, boolean accountNr, boolean balance) {
		displayName = name;
		displayAccountNr = accountNr;
		displayBalance = balance;
		
		if (displayName)
			posOwner = 0;
		else
			posOwner = -1;
		if (displayAccountNr)
			posAccount = posOwner + 1;
		else
			posAccount = posOwner;
		if (displayBalance)
			posBalance = posAccount + 1;

		
	}
	
	public void enableButton(boolean enable, String name) {
		if (enable) {
			displayButton = true;
			buttonName = name;
			posButton = posBalance + 1;
		}
		else {
			displayButton = false;
		}
	
			
	}
	
	
	public void setAccounts(List<AccountDTO> accounts) {
		int numberPages = numerPages(accounts.size());
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
		
		
		this.accounts = accounts;
		
		this.accounts = accounts;
		
		overviewTable.clear();
		

		
		
		
		
		
		//add header
		if (displayName)
			overviewTable.setWidget(0, posOwner, new Label(constants.owner()));
		if (displayAccountNr)
			overviewTable.setWidget(0, posAccount, new Label(constants.accountNr()));
		if (displayBalance)
			overviewTable.setWidget(0, posBalance, new Label(constants.balance()));
		if (displayButton)
			overviewTable.setWidget(0, posButton, new Label(buttonName));
//		overviewTable.setWidget(0, posTransfer, new Label(constants.transferMoney()));
//		overviewTable.setWidget(0, posDelete, new Label(constants.deleteAccount()));
		
		
		switchToPage(1);
	}
	
	@Override
	public void clickedOnAccount(AccountDTO account) {
		callee.clickedOnAccount(account);	
	}

	private void switchToPage(int page) {
		//reset style for all pages
		int numberPages = numerPages(accounts.size());
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

		
		
		
		int row = 1;
		NumberFormat fmt = NumberFormat.getFormat("#0.00");
		for (int i = 0; i < ACCOUNTS_PER_PAGE; i++) {
			int index = (page-1) * ACCOUNTS_PER_PAGE + i;
			if (index >= accounts.size()) {
				if (overviewTable.getRowCount() > row)
					overviewTable.removeRow(row);
				//row++ is not needed because the last row was just removed
				continue;
			}
			//add all accounts to table
			final AccountDTO account = accounts.get(index);
			

			if (displayName)
				overviewTable.setWidget(row, posOwner, new Label(account.getOwner()));
			if (displayAccountNr)
				overviewTable.setWidget(row, posAccount, new Label(account.getAccountNr()));
			if (displayBalance) {
				String balance = fmt.format(account.getBalance());
				overviewTable.setWidget(row, posBalance, new Label(balance));
			}
			
			if (displayButton) {
				Button button = new Button("x");
				button.addClickHandler(new ClickHandler() {	
					public void onClick(ClickEvent event) {
						callee.clickedOnAccount(account);						
					}
				});
				overviewTable.setWidget(row, posButton, button);
			}
		
			row++;
		}
		if ((displayAccountNr | displayBalance | displayButton | displayName) == true) // at least one column enabled
			TableStyler.setTableStyle(overviewTable);
		
	}

	@UiHandler("search")
	public void onClickSearch(ClickEvent event) {
	final SearchAccountsForm overview = this;
	AdminServiceAsync adminService = GWT.create(AdminService.class);
	
	adminService.searchInternalAccounts(searchOwner.getText(), searchAccountNr.getText(), new AsyncCallback<List<AccountDTO>>() {

		@Override
		public void onFailure(Throwable caught) {
			GWT.log(caught.toString());
			
		}

		@Override
		public void onSuccess(List<AccountDTO> result) {
			overview.setAccounts(result);
			
		}
	});
}
}
