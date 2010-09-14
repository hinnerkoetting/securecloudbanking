//
//
// Administration Client page
//
//

package de.mrx.client.admin;




import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.BankDTO;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.client.SCBMenu;
import de.mrx.client.TransferHistoryForm;
import de.mrx.client.admin.forms.AccountDetails;
import de.mrx.client.admin.forms.AccountOverview;
import de.mrx.client.admin.forms.AdminExternalBanks;
import de.mrx.client.admin.forms.AdminTransfer;
import de.mrx.client.admin.forms.AdminWelcome;
import de.mrx.client.admin.forms.Adminmenu;
import de.mrx.client.admin.forms.EditBankDetails;
import de.mrx.client.admin.forms.ExternalAccountOverview;
import de.mrx.client.admin.forms.NewBank;


public class Admin extends Composite implements EntryPoint,Observer {

	private static AdminUiBinder uiBinder = GWT
		.create(AdminUiBinder.class);

	interface AdminUiBinder extends
		UiBinder<Widget, Admin> {
	}


	AdminConstants constants = GWT.create(AdminConstants.class);
	
	public final String ADMIN_PAGE_ID = "admin";
	
	

	SCBMenu scbMenu;
	
	@UiField
	HorizontalPanel topMenuPanel;
	
	String currentLanguage = "de";
	
	@UiField
	Anchor signIn;
	
	@UiField
	Anchor signOut;
	
	@UiField
	SimplePanel adminMenu;
	
	@UiField 
	SimplePanel content;

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
	
	public void showEditBankDetails(String name, String blz) {
		setContent(new EditBankDetails(this, blz, name));
	}
	
	/*
	 * change the language during runtime keeps the debug flag
	 */
	public static void changeToLocalisedVersion(String language) {
		

		String reloadURL;
		String debugFlag = Window.Location.getParameter("gwt.codesvr");

		if (debugFlag != null) {
			reloadURL = GWT.getHostPageBaseURL() +"Admin.html?gwt.codesvr="+ debugFlag + "&locale="
					+ language;
		} else {
			reloadURL = GWT.getHostPageBaseURL() + "Admin.html?locale=" + language;
		}

		GWT.log(reloadURL);

		Window.open(reloadURL, "_self", null);
	}
	
	public void showExternalBanks() {
		final AdminExternalBanks externalBanks = new AdminExternalBanks(this);
		
		AdminServiceAsync bankingService = GWT.create(AdminService.class);
		bankingService.getAllBanks(new AsyncCallback<List<BankDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}

			@Override
			public void onSuccess(List<BankDTO> result) {
				externalBanks.setBanks(result);
				setContent(externalBanks);				
			}

			
		});
		
		setContent(new AdminExternalBanks(null));
	}
	
	
	/**
	 * show information about all accounts
	 */
	public void showAccounts() {
		final AccountOverview accountOverview = new AccountOverview(this);
		accountOverview.addObserver(this);
		AdminServiceAsync bankingService = GWT.create(AdminService.class);
		bankingService.getAllInternalAccounts(new AsyncCallback<List<AccountDTO>>() {

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
	
	public void showNewBank() {
		setContent(new NewBank(this));
	}
	
	public void showExternalAccounts(String blz) {
		final ExternalAccountOverview accountOverview = new ExternalAccountOverview(this);
		AdminServiceAsync bankingService = GWT.create(AdminService.class);
		bankingService.getExternalAccounts(blz, new AsyncCallback<List<AccountDTO>>() {

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
		content.setWidget(widget);
	}
	
	private void loadAdminPage() {
		adminMenu.add(new Adminmenu(this));
		setContent(new AdminWelcome());
	}
	
	private void checkGoogleStatus() {
		AdminServiceAsync adminService = GWT.create(AdminService.class);

		adminService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<SCBIdentityDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						 GWT.log(caught.toString());
					}

					@Override
					public void onSuccess(SCBIdentityDTO result) {
						
						SCBIdentityDTO identityInfo = result;
						if (identityInfo.isLoggedIn()) {
							Log.info("User is logged in with email-adress "
									+ result.getEmail());
							scbMenu.getMenuUserInfo().setText(
									identityInfo.getEmail());
							String language = identityInfo.getLanguage();
							
							if (language != null
									&& !currentLanguage.equals(language)) {
								GWT.log("Language: " + language);
								changeToLocalisedVersion(language);
							}
//							if (identityInfo.isAdmin()) {								
								loadAdminPage();
//							}
							signOut.setHref(identityInfo.getLogoutUrl()+Window.Location.getQueryString());
							signOut.setText(constants.signOut());
							signIn.setVisible(false);
							signOut.setVisible(true);
							
							
							
						} else {
							Log.info("User is not yet logged in with his Google account");
					
							// Assemble login panel.
							signIn.setHref(identityInfo.getLoginUrl());
							signIn.setText(constants.signIn());
							signIn.setVisible(true);
							signOut.setVisible(false);
						}
						
					}
		
		});

	}
	
	@Override
	public void onModuleLoad() {
		RootLayoutPanel.get().add(uiBinder.createAndBindUi(this));
		scbMenu = new SCBMenu();
		scbMenu.addObserver(this);
		topMenuPanel.add(scbMenu);

		setContent(new Label("Administration privileges required."));
		checkGoogleStatus();
		
	}

	@Override
	public void update(Observable source, Object event, Object parameter) {
		if (source instanceof SCBMenu) {
			if (event == SCBMenu.EVENT_SHOW_REGISTRATION_MENU) {
//				doOpenRegisterMenu();
			}
			else if (event == SCBMenu.EVENT_CHANGE_LANGUAGE) {
				changeToLocalisedVersion((String)parameter);
			}
			else 
				Log.info("missing event");
		}
		else if (source instanceof AccountOverview) {
			if ((Integer)event == AccountDetails.ACCOUNT_DELETED) {
				showAccounts();
			}
			else if ((Integer)event == AdminTransfer.TRANSACTION_SUCCEEDED) {
				showAccounts();
			}
			else 
				Log.info("missing event");
		}
		else 
			Log.info("missing event");
		
	}

	@Override
	public void reportInfo(String info) {
		 GWT.log("test");
		
	}

	@Override
	public void reportError(String error) {
		 GWT.log("test");
		
	}

}
