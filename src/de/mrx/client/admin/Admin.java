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
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.client.SCBMenu;
import de.mrx.client.ShopDTO;
import de.mrx.client.admin.forms.AdminWelcome;
import de.mrx.client.admin.forms.Adminmenu;
import de.mrx.client.admin.forms.external.AdminExternalBanks;
import de.mrx.client.admin.forms.external.BankDetails;
import de.mrx.client.admin.forms.external.EditBank;
import de.mrx.client.admin.forms.external.NewBank;
import de.mrx.client.admin.forms.internalAccounts.AccountDetails;
import de.mrx.client.admin.forms.internalAccounts.AccountOverview;
import de.mrx.client.admin.forms.internalAccounts.AdminTransfer;
import de.mrx.client.admin.forms.shop.EditShop;
import de.mrx.client.admin.forms.shop.NewShop;
import de.mrx.client.admin.forms.shop.ShopDetails;
import de.mrx.client.admin.forms.shop.ShopOverview;



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


	
	private void showEditBankDetails(String name, String blz) {
		BankDetails bankDetails = new BankDetails(name, blz);
		bankDetails.addObserver(this);
		setContent(bankDetails);
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
	
	private void showExternalBanks() {
		final AdminExternalBanks externalBanks = new AdminExternalBanks();
		externalBanks.addObserver(this);
		
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
		
		setContent(externalBanks);
	}
	
	
	private void show3S() {
		ShopOverview s = new ShopOverview();
		s.addObserver(this);
		setContent(s);
	}
	
	private void showAddShop() {
		NewShop n = new NewShop();
		n.addObserver(this);
		setContent(n);
	}
	
	/**
	 * show information about all accounts
	 */
	private void showAccounts() {
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
	

	
	/**
	 * 
	 * @param widget
	 * set content of the admin page
	 */
	private void setContent(Widget widget) {
		content.setWidget(widget);
	}
	
	private void loadAdminPage() {
		Adminmenu menu = new Adminmenu();
		menu.addObserver(this);
		adminMenu.add(menu);
		setContent(new AdminWelcome());
	}

	
	private void checkGoogleStatus() {
		AdminServiceAsync adminService = GWT.create(AdminService.class);

		String debugFlag = Window.Location.getParameter("gwt.codesvr");
		String reloadURL = GWT.getHostPageBaseURL() + "Admin.html";
		if (debugFlag != null)
			reloadURL += "?gwt.codesvr=" + debugFlag;
		adminService.login(reloadURL,
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
							if (identityInfo.isAdmin()) {								
								loadAdminPage();
							}

							signOut.setHref(identityInfo.getLogoutUrl());
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

	private void showAddBank() {
		NewBank newBank = new NewBank();
		newBank.addObserver(this);
		setContent(newBank);
	}
	
	private void showShopDetails(ShopDTO shop) {
		ShopDetails d = new ShopDetails(shop.getName(), shop.getAccountNr(), shop.getBlz(), shop.getUrl());
		d.addObserver(this);
		setContent(d);
	}
	
	@Override
	public void update(Observable source, Object event, Object parameter) {
		if (source instanceof SCBMenu) {
			if (event == SCBMenu.EVENT_SHOW_REGISTRATION_MENU) {
//				doOpenRegisterMenu();
			}
			else if (event == SCBMenu.EVENT_CHANGE_LANGUAGE) {
				changeToLocalisedVersion((String)parameter);
				return;
			}
		}
		else if (source instanceof AccountOverview) {
			if ((Integer)event == AccountDetails.ACCOUNT_DELETED) {
				showAccounts();
				return;
			}
			else if ((Integer)event == AdminTransfer.TRANSACTION_SUCCEEDED) {
				showAccounts();
				return;
			}
			if ((Integer)event == AccountDetails.ACCOUNT_RESET) {				
				showAccounts();
				return;
			}
		}
		else if (source instanceof AdminExternalBanks) {
			if ((Integer)event == AdminExternalBanks.ADD_BANK) {
				showAddBank();
				return;
			}
			if ((Integer)event == AdminExternalBanks.EDIT_BANK) {
				BankDTO bank = (BankDTO)parameter;
				showEditBankDetails(bank.getName(), bank.getBlz());
				return;
			}
		}
		else if (source instanceof NewBank) {
			if ((Integer)event == NewBank.ADD_BANK_SUCCEEDED) {
				showExternalBanks();
				return;
			}
		}
		else if (source instanceof BankDetails) {
			if ((Integer)event == EditBank.EDIT_BANK_SUCCEED) {
				showExternalBanks();
				return;
			}
			if ((Integer)event == BankDetails.DELETE_BANK_SUCCEED) {
				showExternalBanks();
				return;
			} 
			 
		}
		else if (source instanceof Adminmenu) {
			if ((Integer)event == Adminmenu.SHOW_ACCOUNTS) {
				showAccounts();
				return;
			}
			else if ((Integer)event == Adminmenu.SHOW_EXTERNAL_BANKS) {
				showExternalBanks();
				return;
			}
			else if ((Integer)event == Adminmenu.SHOW_3S) {
				show3S();
				return;
			}
		}
		else if (source instanceof ShopOverview) {
			if ((Integer)event == ShopOverview.ADD_SHOP) {
				showAddShop();
				return;
			}
			else if ((Integer)event == ShopOverview.PROPERTIES_SHOP) {
				showShopDetails((ShopDTO)parameter);
				return;
			}
		}
		else if (source instanceof NewShop) {
			if ((Integer)event == NewShop.ADD_SHOP_SUCCEEDED) {
				show3S();
				return;
				
			}
		}
		else if (source instanceof ShopDetails) {
			if ((Integer)event == EditShop.EDIT_SHOP_SUCCEED) {
				show3S();
				return;
				
			}
		}
		Log.info("missing event" + source.getClass() + " - " + event.toString());
		
	}

	@Override
	public void reportInfo(String info) {
		
	}

	@Override
	public void reportError(String error) {
		
	}

}
