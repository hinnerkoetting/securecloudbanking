package de.mrx.client;

import java.util.List;

import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SCB implements EntryPoint {

	// private static final Logger log = Logger.getLogger(SCB.class.getName());
	Widget divLogger = Log.getLogger(DivLogger.class).getWidget();

	private static final String PAGEID_HEADER = "cloudbanking";
	private static final String PAGEID_CONTENT = "content";
	private static final String PAGEID_FEHLER = "fehler";
	private static final String PAGEID_SIGN = "signInOut";

	RegisterServiceAsync registerSvc;
	private BankingServiceAsync bankingService;
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private CheckBox agbBox;;
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	MenuBar generalMenu = new MenuBar(true);
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	private Button loginButton = new Button("Login");
	// private Label status = new Label("Welcome to Cloud Banking");
	private VerticalPanel mainPanel = new VerticalPanel();

	private HorizontalPanel accountOverviewPanel = new HorizontalPanel();
	private VerticalPanel accountsListPanel = new VerticalPanel();
	private VerticalPanel accountsDetailsPanel = new VerticalPanel();

	private TextBox nameTxt;
	private TextBox streetTxt;
	private TextBox plzTxt;
	private TextBox cityTxt;
	private TextBox emailTxt;

	private Label nameLbl;
	private Label streetLbl;
	private Label plzLbl;
	private Label cityLbl;
	private Label emailLbl;
	private Label onlyGoogleAllowedLabel = new Label(
			"As a bank we need to rely on highest security standards. Therefore we need to require you having an google account. ");

	private PasswordTextBox password;
	private Label passwordLbl;

	private Button registerButton = new Button("Register");

	private VerticalPanel registrationPanel;

	private boolean loggedInAtGoogle = false;

	private HTMLTable registrationTable;

	private IdentityDTO identityInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access the banking application.");

	// private Label logoutLabel = new Label("Sign out gefaellig.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private MenuBar informationMenu;

	private MenuBar menu;

	private MenuItem userInformationMenuItem;

	private MenuItem registerMItem;

	private void doShowNoService() {

		Window.alert("Our business will soon offer service. Please return! Soon!");

	}

	private void doOpenRegisterMenu() {
		RootPanel.get(PAGEID_CONTENT).remove(getRegistrationPanel());
		RootPanel.get(PAGEID_CONTENT).add(getRegistrationPanel());
	}

	public VerticalPanel getRegistrationPanel() {
		if (registrationPanel != null) {
			return registrationPanel;
		}

		registrationPanel = new VerticalPanel();
		registrationTable = new Grid(6, 4);
		nameLbl = new Label("Your name: ");
		streetLbl = new Label("Your street: ");
		plzLbl = new Label("Postal Code: ");
		cityLbl = new Label("City: ");
		emailLbl = new Label("Email: ");

		passwordLbl = new Label("Your desired password: ");
		nameTxt = new TextBox();
		streetTxt = new TextBox();
		plzTxt = new TextBox();
		cityTxt = new TextBox();
		emailTxt = new TextBox();
		password = new PasswordTextBox();
		agbBox = new CheckBox("I accept the Terms Of Services");
		registrationTable.setWidget(0, 0, nameLbl);
		registrationTable.setWidget(0, 1, nameTxt);
		registrationTable.setWidget(0, 2, emailLbl);
		registrationTable.setWidget(0, 3, emailTxt);
		registrationTable.setWidget(1, 0, streetLbl);
		registrationTable.setWidget(1, 1, streetTxt);
		registrationTable.setWidget(2, 0, plzLbl);
		registrationTable.setWidget(2, 1, plzTxt);
		registrationTable.setWidget(2, 2, cityLbl);
		registrationTable.setWidget(2, 3, cityTxt);
		registrationTable.setWidget(3, 0, passwordLbl);
		registrationTable.setWidget(3, 1, password);
		registrationTable.setWidget(4, 0, agbBox);
		registrationTable.setWidget(4, 1, registerButton);

		registerButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				register();

			}
		});
		registrationPanel.add(registrationTable);

		// if (identityInfo==null){
		// Window.alert("Logininformation not available!");
		//
		// }
		// if (!identityInfo.isLoggedIn()){
		// Label registrationOnlyWithLogin=new
		// Label("Remember: SCB only offers its services to Google customer. Please sign in!");
		// RootPanel.get(PAGEID_CONTENT).insert(registrationOnlyWithLogin, 0);
		//
		// }
		// else{
		// GWT.log("Eingeloggt");
		// }

		return registrationPanel;
	}

	protected void register() {

		String n = nameTxt.getText();
		String pw = password.getText();
		String str = streetTxt.getText();
		String city = cityTxt.getText();
		String plz = plzTxt.getText();
		String email = emailTxt.getText();

		IdentityDTO id = new IdentityDTO(n);
		id.setStreet(str);
		id.setCity(city);
		id.setPlz(plz);
		id.setEmail(email);
		if (registerSvc == null) {
			registerSvc = GWT.create(RegisterService.class);
		}

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				Window.alert("Shit happens");
				GWT.log(caught.getMessage());

			}

			public void onSuccess(Void result) {
				Window.alert("Register succesful");
				// RootPanel
				getRegistrationPanel().removeFromParent();

			}
		};
		Log.info("CLIENT: ID :" + id);
		registerSvc.register(id, callback);
		// TODO Auto-generated method stub

	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		try {
			GWT.log("On Module Load");
			RootPanel r = RootPanel.get(PAGEID_HEADER);
			if (r == null) {
				GWT.log("Root nicht gefunden: '" + PAGEID_HEADER + "'");
				return;
			}
			GWT.log("Root gefunden");

			RootPanel.get(PAGEID_FEHLER).add(divLogger);
			Log.setUncaughtExceptionHandler();

			// mainPanel.add(buttonPanel);

			// buttonPanel.add(registerButton);
			// buttonPanel.add(loginButton);
			//
			//
			// registerButton.addClickHandler(new ClickHandler() {
			//
			// public void onClick(ClickEvent event) {
			// doOpenRegisterMenu();
			//
			// }
			//
			// });

			Command cmdShowNoService = new Command() {
				public void execute() {
					GWT.log("Show No Service starts");
					doShowNoService();

				}
			};

			Command cmdRegister = new Command() {
				public void execute() {
					GWT.log("Registration starts");
					doOpenRegisterMenu();

				}
			};

			Command cmdLogin = new Command() {
				public void execute() {
					GWT.log("Login starts. TODO");
					// checkGoogleStatus();

				}

			};

			MenuBar bankingMenu = new MenuBar(true);
			bankingMenu.addItem("Private Banking", cmdLogin);
			// bankingMenu.addItem("Institutional Banking", cmdShowNoService);
			// bankingMenu.addItem("Depot ", cmdShowNoService);

			informationMenu = new MenuBar(true);
			// informationMenu.addItem("Partner", cmdShowNoService);
			informationMenu.addItem("Impressum", cmdShowNoService);
			informationMenu.addItem("About Secure Cloud Computing",
					cmdShowNoService);

			menu = new MenuBar();
			registerMItem = new MenuItem("Register", cmdRegister);
			menu.addItem(registerMItem);
			menu.addItem("Banking", bankingMenu);
			menu.addItem("Information", informationMenu);
			userInformationMenuItem = new MenuItem("UserInfo", cmdShowNoService);
			menu.addItem(userInformationMenuItem);

			mainPanel.add(menu);

			r.add(mainPanel);
			checkGoogleStatus();
			GWT.log("Module laden durchgefuehrt");

		} catch (Exception e) {
			GWT.log(e.getMessage());
			e.printStackTrace();
		}
	}

	private void showAccountOverview() {
		Log.info("Show Account Overview");
		accountOverviewPanel.add(accountsListPanel);
		accountOverviewPanel.add(accountsDetailsPanel);
		Log.info("Uebersicht zeigen");
		if (bankingService == null) {
			bankingService = GWT.create(BankingService.class);
		}
		bankingService.getAccounts(new AsyncCallback<List<AccountDTO>>() {

			public void onFailure(Throwable caught) {
				Log.error("Laden der Accountdaten gescheitert", caught);
				return;

			}

			public void onSuccess(List<AccountDTO> result) {
				Log.info("Account geladen ");
				if (result == null) {
					Log.error("Keine Accountdaten");

				} else {
					Log.info("Anzahl Accounts: " + result.size());
					accountsListPanel.add(new Label("Your accounts "));
					for (AccountDTO acc : result) {
						Log.info(acc.toString());
						
						Button btn=new Button(acc.getAccountNr());
						
						btn.addClickHandler(new ClickHandler() {
							
							public void onClick(ClickEvent event) {
								String accountNr=((Button)event.getSource()).getText();
								showAccountDetails(accountNr);
								
							}
						});
						accountsListPanel.add(btn);
					}
				}Log.info(".....Vor einbinden in oberpanel");
				
				
				Button neuerAccountBtn = new Button("Neuer Account");
				neuerAccountBtn.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						createAccount();
					}

				});
				Log.info("Vor einbinden in oberpanel");
				accountsListPanel.add(neuerAccountBtn);
//				RootPanel.get(PAGEID_CONTENT).remove(0);
//				RootPanel.get(PAGEID_CONTENT).remove(0);
//				RootPanel.get(PAGEID_CONTENT).remove(0);
//				RootPanel.get(PAGEID_CONTENT).remove(0);
				RootPanel.get(PAGEID_CONTENT).add(accountOverviewPanel);
				Log.info("Accountübersicht fertig geladen");

			}
		});

	}

	private void checkGoogleStatus() {

		if (bankingService == null) {
			bankingService = GWT.create(BankingService.class);
		}
		GWT.log(GWT.getHostPageBaseURL());
		bankingService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<IdentityDTO>() {
					public void onFailure(Throwable error) {
						error.printStackTrace();

					}

					public void onSuccess(IdentityDTO result) {
						identityInfo = result;
						if (identityInfo.isLoggedIn()) {
							Log.info("Eingeloggt");
							if (identityInfo.isActivated()) {
								Log.info("Valid Account " + identityInfo);
								String userInfo = identityInfo.getEmail();
								if (identityInfo.getNickName() != null) {
									userInfo = identityInfo.getNickName()
											+ " (" + userInfo + ")";
								}
								userInformationMenuItem.setText(userInfo);
								menu.removeItem(registerMItem);

								showAccountOverview();

							} else {
								Log.info("Account nicht aktiviert: "
										+ identityInfo);
								userInformationMenuItem.setText(identityInfo
										.getEmail());
							}
							loadLoggedInSetup();
						} else {
							Log.info("Nicht eingeloggt");
							loadLogin();
						}
					}
				});

	}

	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(identityInfo.getLoginUrl());

		// loginPanel.add(onlyGoogleAllowedLabel);
		// loginPanel.add(signInLink);
		RootPanel.get(PAGEID_SIGN).remove(signInLink);
		RootPanel.get(PAGEID_SIGN).remove(signOutLink);
		RootPanel.get(PAGEID_SIGN).add(signInLink);

		// RootPanel.get(PAGEID_HEADER).add(signInLink);
	}

	private void loadLoggedInSetup() {
		// Assemble logout panel.

		signOutLink.setHref(identityInfo.getLogoutUrl());
		// loginPanel.add(logoutLabel);
		loginPanel.add(signOutLink);
		RootPanel.get(PAGEID_SIGN).remove(signInLink);
		RootPanel.get(PAGEID_SIGN).remove(signOutLink);
		RootPanel.get(PAGEID_SIGN).add(signOutLink);
	}

	private void createAccount() {
		if (bankingService == null) {
			bankingService = GWT.create(BankingService.class);
		}
		bankingService.openNewAccount(new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				Log.error("Open new account failed", caught);
			}

			public void onSuccess(Void result) {
				Log.info("Account eröffnet");
				Window.alert("You have opened a new account");

			}
		});

	}
	
	private void showAccountDetails(String accNr){
		if (bankingService == null) {
			bankingService = GWT.create(BankingService.class);
		}
		accountsDetailsPanel.clear();
		bankingService.getBalance(new AsyncCallback<Double>() {

			public void onFailure(Throwable caught) {
				Log.error("Error loading balance", caught);
				
			}

			public void onSuccess(Double result) {
				Log.info("Balance: "+result);
				accountsDetailsPanel.add(new Label("Your balance: "+result+" Dollar"));
				
			}
			
		});
		bankingService.getTransaction(new AsyncCallback<List<MoneyTransferDTO>>() {

			public void onFailure(Throwable caught) {
				Log.error("Error loading transactions", caught);				
			}

			public void onSuccess(List<MoneyTransferDTO> result) {
				if (result==null){
					Log.error("No transactions");
					accountsDetailsPanel.add(new Label("No transactions"));
					return;
				}
				for (MoneyTransferDTO transfer: result){
					Log.info("Transfer: "+transfer);
					
				}

				
			}
		});
	}
}