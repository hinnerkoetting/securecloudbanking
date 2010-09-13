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
import de.mrx.client.CustomerService;
import de.mrx.client.CustomerServiceAsync;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.client.SCBMenu;
import de.mrx.client.TransferHistoryForm;
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
		String queryPart = Window.Location.getQueryString();

		String reloadURL;
		String debugFlag = Window.Location.getParameter("gwt.codesvr");

		if (debugFlag != null) {
			reloadURL = GWT.getHostPageBaseURL() +"?gwt.codesvr="+ debugFlag + "&locale="
					+ language;
		} else {
			reloadURL = GWT.getHostPageBaseURL() + "?locale=" + language;
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
	 * 
	 */
	public void showTransferMoney(String accNr, String accOwner) {
		setContent(new AdminTransfer(this, accNr, accOwner));
	}
	
	/**
	 * show information about all accounts
	 */
	public void showAccounts() {
		final AccountOverview accountOverview = new AccountOverview(this);
		
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
	
	
	private void checkGoogleStatus() {
		CustomerServiceAsync bankingService = GWT.create(CustomerService.class);
		bankingService.login(GWT.getHostPageBaseURL(),
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
							if (identityInfo.isActivated()) {								
								doShowPageForActivatedUser();
							} else {
								Log.info("Account not yet activated in SCB: "
										+ identityInfo);
								scbMenu.getMenuUserInfo().setText(
										identityInfo.getEmail());
								
							}
//							loadLoggedInSetup();
							String language = identityInfo.getLanguage();
							GWT.log("Language: " + language);
							if (language != null
									&& !currentLanguage.equals(language)) {
								changeToLocalisedVersion(language);
							}
						} else {
							Log.info("User is not yet logged in with his Google account");
					
							loadLogin();
						}
						
					}

					private void loadLogin() {
						// Assemble login panel.
//						signIn.setHref(identityInfo.getLoginUrl());
//						signIn.setText(constants.signIn());
						signIn.setVisible(true);
						signOut.setVisible(false);
					}

					private void doShowPageForActivatedUser() {
						// TODO Auto-generated method stub
						
					}
		
		});
	}
	
	@Override
	public void onModuleLoad() {
		RootLayoutPanel.get().add(uiBinder.createAndBindUi(this));

		
		scbMenu = new SCBMenu();
		scbMenu.addObserver(this);
		topMenuPanel.add(scbMenu);
		adminMenu.add(new Adminmenu(this));
		content.add(new AdminWelcome());
//	
//		checkGoogleStatus();
		
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
		}
		
	}

	@Override
	public void reportInfo(String info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportError(String error) {
		// TODO Auto-generated method stub
		
	}

}
