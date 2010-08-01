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
	MenuBar generalMenu = new MenuBar(true);
	
	private VerticalPanel mainPanel=new VerticalPanel();

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

	private PasswordTextBox password;
	private Label passwordLbl;

	private Button registerButton = new Button("Register");

	private VerticalPanel registrationPanel;

	
	private HTMLTable registrationTable;

	private SCBIdentityDTO identityInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();

	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private MenuBar informationMenu;

	private MenuBar menu;

	private MenuItem userInformationMenuItem;

	private MenuItem registerMItem;
	private String currentAccount;

	private TextBox receiverAccountNrTxt;

	private TextBox receiverBankNrTxt;

	private TextBox amountTxt;

	private void doShowNoService() {

		Window.alert("Our business will soon offer service. Please try again the other day!");

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
		if (identityInfo!=null){
			emailTxt.setText(identityInfo.getEmail());
			emailTxt.setReadOnly(true);
		}
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
//		registrationTable.setWidget(3, 1, password);
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
//		String pw = password.getText();
		String str = streetTxt.getText();
		String city = cityTxt.getText();
		String plz = plzTxt.getText();
		String email = emailTxt.getText();

		SCBIdentityDTO id = new SCBIdentityDTO(n);
		id.setStreet(str);
		id.setCity(city);
		id.setPlz(plz);
		id.setEmail(email);
		if (registerSvc == null) {
			registerSvc = GWT.create(RegisterService.class);
		}

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				
				
				Window.alert("Registration fails! ");				

			}

			public void onSuccess(Void result) {
				Window.alert("Gratulation. You have registered! We will progress your request as soon as we are able to!");
				getRegistrationPanel().removeFromParent();
			}
		};
		
		registerSvc.register(id, callback);
		Window.Location.reload();


	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		try {
			GWT.log("On Module Load");
			RootPanel r = RootPanel.get(PAGEID_HEADER);
			if (r == null) {
				GWT.log("Root not found: '" + PAGEID_HEADER + "'");
				return;
			}
			
			RootPanel.get(PAGEID_FEHLER).add(divLogger);
			Log.setUncaughtExceptionHandler();


			Command cmdShowImpressum = new Command() {
				public void execute() {
					GWT.log("Impressum follows");
					Window.alert("This is a academic technology study in Cloud Computing. At the moment you see an internal development state." );
					

				}
			};

			Command cmdShowInfoSCB = new Command() {
				public void execute() {
					GWT.log("SCB Info");
					Window.alert("Demonstration of cloud and security technology ." );
					

				}
			};

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



			MenuBar bankingMenu = new MenuBar(true);

			informationMenu = new MenuBar(true);
			informationMenu.addItem("Impressum", cmdShowImpressum);
			informationMenu.addItem("About Secure Cloud Computing",
					cmdShowInfoSCB);

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

	/**
	 * Initialize personalization
	 */
	private void checkGoogleStatus() {

		if (bankingService == null) {
			bankingService = GWT.create(BankingService.class);
		}
		GWT.log(GWT.getHostPageBaseURL());
		bankingService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<SCBIdentityDTO>() {
					public void onFailure(Throwable error) {
						error.printStackTrace();
						Log.error("Login state can not be retrieved! ",error);
						Window.alert("Login state can not be retrieved! Detail: "+error.getMessage());
					}

					public void onSuccess(SCBIdentityDTO result) {
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
				Log.info("Account created");
				Window.alert("Your saving account is ready for service");
				Window.Location.reload();

			}
		});

	}
	
	private void showAccountDetails(String accNr){
		if (bankingService == null) {
			bankingService = GWT.create(BankingService.class);
		}
		accountsDetailsPanel.clear();
		Button transferMoneyButton=new Button("Send money");
		currentAccount=accNr;
		transferMoneyButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				sendMoney(currentAccount);
				
			}
		});
		accountsDetailsPanel.add(transferMoneyButton);
		bankingService.getBalance(accNr,new AsyncCallback<Double>() {

			public void onFailure(Throwable caught) {
				Log.error("Error loading balance", caught);
				
			}

			public void onSuccess(Double result) {
				Log.info("Balance: "+result);
				accountsDetailsPanel.add(new Label("Your balance: "+result+" Dollar"));
				
			}
			
		});
		bankingService.getTransaction(currentAccount, new AsyncCallback<List<MoneyTransferDTO>>() {

			private HTMLTable accountDetailTable;

			public void onFailure(Throwable caught) {
				Log.error("Error loading transactions", caught);				
			}

			public void onSuccess(List<MoneyTransferDTO> result) {
				if (result==null){
					Log.error("No transactions");
					accountsDetailsPanel.add(new Label("No transactions"));
					return;
				}
				if (accountDetailTable!=null){
					accountsDetailsPanel.remove(accountDetailTable);
				}
				accountDetailTable = new Grid(result.size(),4);
				int pos=0;
				Log.info("Einträge: "+result.size());
				for (MoneyTransferDTO transfer: result){
					Log.info("Transfer: "+transfer);
					accountDetailTable.setWidget(pos,0,new Label(transfer.getSenderAccountNr()));
					accountDetailTable.setWidget(pos,1,new Label(transfer.getReceiverAccountNr()));
					accountDetailTable.setWidget(pos,2,new Label(""+transfer.getAmount()));
					pos++;
					
				}
				accountsDetailsPanel.add(accountDetailTable);

				
			}
		});
	}

	protected void sendMoney(String accNr) {
		accountsDetailsPanel.clear();
		HTMLTable transferForm = new Grid(6, 4);
		Label receiverAccountNrLbl=new Label("Receiver Account Nr:");
		Label receiverBankNrLbl=new Label("Receiver Bank number");
		Label amountLbl=new Label("Amount");
		receiverAccountNrTxt = new TextBox();
		receiverBankNrTxt = new TextBox();
		amountTxt = new TextBox();
		Button sendMoneyBtn=new Button("Send Money");
		sendMoneyBtn.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				doSendMoney();
				
			}
		});
		transferForm.setWidget(0,0,receiverAccountNrLbl);
		transferForm.setWidget(0,1,receiverAccountNrTxt);
		transferForm.setWidget(1,0,receiverBankNrLbl);
		transferForm.setWidget(1,1,receiverBankNrTxt);
		transferForm.setWidget(2,0,amountLbl);
		transferForm.setWidget(2,1,amountTxt);
		transferForm.setWidget(3,1,sendMoneyBtn);
		
		accountsDetailsPanel.add(transferForm);
		
	}

	protected void doSendMoney() {
		if (bankingService == null) {
			bankingService = GWT.create(BankingService.class);
		}
		
		Double amount=Double.parseDouble( amountTxt.getText());
		bankingService.sendMoney(currentAccount, receiverBankNrTxt.getText(),receiverAccountNrTxt.getText(),amount,new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				Log.error("Sending money failed",caught);
				
			}

			
			public void onSuccess(Void result) {
				Window.alert("Money sent sucessfully");
				Window.Location.reload();
				
			}
		});
		
	}
}