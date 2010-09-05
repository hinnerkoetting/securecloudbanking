package de.mrx.client;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.mrx.client.moneytransfer.FastMoneyTransferForm;
import de.mrx.client.moneytransfer.MoneyTransferForm;
import de.mrx.client.register.RegistrationForm;

/**
 * Complete GUI for Secure Cloud Banking. Includes Registration process, general information, and online banking
 * Entry point classes define <code>onModuleLoad()</code>.
 * TODO: This class is too big! Split!
 * 
 */
public class SCB implements EntryPoint, Observer {

	// private static final Logger log = Logger.getLogger(SCB.class.getName());
//	Widget divLogger = Log.getLogger(DivLogger.class).getWidget();

	public static final String PAGEID_HEADER = "cloudbanking";
	public static final String PAGEID_CONTENT = "content";
	public static final String PAGEID_FEHLER = "fehler";
	public static final String PAGEID_SIGN = "signInOut";

	public final static String STYLE_VALUE_NOT_OKAY = "ValueNotOkay";
	String currentLanguage="en";
	
	RegistrationForm regForm;

	private SCBConstants constants = GWT.create(SCBConstants.class);
//	private SCBMessages messages = GWT.create(SCBMessages.class);
	RegisterServiceAsync registerSvc;
	private CustomerServiceAsync bankingService;
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
//	private static final String SERVER_ERROR = "An error occurred while "
//			+ "attempting to contact the server. Please check your network "
//			+ "connection and try again.";

	
	MenuBar generalMenu = new MenuBar(true);

	private VerticalPanel mainPanel = new VerticalPanel();

	private HorizontalPanel accountOverviewPanel = new HorizontalPanel();
	private VerticalPanel accountsListPanel = new VerticalPanel();
	private VerticalPanel accountsDetailsPanel = new VerticalPanel();

	FlexTable validateErrorTable = new FlexTable();



	


	private VerticalPanel informationPanel;



	private SCBIdentityDTO identityInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();

	private Anchor signInLink = new Anchor(constants.signIn());
	private Anchor signOutLink = new Anchor(constants.signOut());
	private MenuBar informationMenu;

	private MenuBar menu;

	private MenuItem userInformationMenuItem;
	private MenuBar languageMenubar;
	private MenuItem languageGermanMenuItem;
	private MenuItem languageEnglishMenuItem;

	private MenuItem registerMItem;
	private String currentAccount;


	private List<String> hints = new ArrayList<String>();

	
	
	private Image scbLogo;

	private void doShowNoService() {

		Window.alert(constants.outOfServiceNotice());

	}
	
	
	private void doShowAbout(boolean picture){
		RootPanel r = RootPanel.get(PAGEID_CONTENT);
		if (r == null)
			return;
		r.clear();
		informationPanel=new VerticalPanel();
		HTML text=new HTML(constants.registrationIntroductionText());
		text.setStyleName("centerAligned");
		scbLogo = new Image("images/banking.jpg");
		scbLogo.setStyleName("centerAligned");
		
		informationPanel.add(text);
		if (picture){
			informationPanel.add(scbLogo);
		}
		
		RootPanel.get(PAGEID_CONTENT).add(informationPanel);
	}

	private void doOpenRegisterMenu() {
		RootPanel.get(PAGEID_CONTENT).clear();
		doShowAbout(false);
		if (regForm==null){
			regForm=new RegistrationForm(signInLink);
		}
		
		RootPanel.get(PAGEID_CONTENT).add(regForm);
	}

	/**
	 * REMOVEME: temporal workaround until refactoring
	 */
	public VerticalPanel createMainPanel() {
		Command cmdShowImpressum = new Command() {
				public void execute() {
					GWT.log("Impressum follows");
					Window
							.alert(constants.impressumText());

				}
			};

			Command cmdShowInfoSCB = new Command() {
				public void execute() {
					GWT.log("SCB Info");
					Window
							.alert(constants.aboutText());

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


			informationMenu = new MenuBar(true);
			informationMenu.addItem("Impressum", cmdShowImpressum);
			informationMenu.addItem("About Secure Cloud Computing",
					cmdShowInfoSCB);

			menu = new MenuBar();
			registerMItem = new MenuItem(constants.menuRegister(), cmdRegister);
			menu.addItem(registerMItem);			
			menu.addItem(constants.menuInformation(), informationMenu);
			userInformationMenuItem = new MenuItem(constants.menuUserInformation(), cmdShowNoService);
			menu.addItem(userInformationMenuItem);
			languageMenubar =new MenuBar(true);
			Command cmdChangeToEnglish = new Command() {
				public void execute() {
					changeToLocalisedVersion("en");

				}
			};
			Command cmdChangeToGerman = new Command() {
				public void execute() {
					changeToLocalisedVersion("de");

				}
			};
			languageEnglishMenuItem=new MenuItem(constants.languageEnglish(), cmdChangeToEnglish);
			languageGermanMenuItem=new MenuItem(constants.languageGerman(), cmdChangeToGerman);
			languageMenubar.addItem(languageGermanMenuItem);
			languageMenubar.addItem(languageEnglishMenuItem);
			menu.addItem(constants.languageMenu(), languageMenubar);
			VerticalPanel tempMainpanel = new VerticalPanel();
			tempMainpanel.add(menu);
		return tempMainpanel;
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

//			RootPanel.get(PAGEID_FEHLER).add(divLogger);
			Log.setUncaughtExceptionHandler();

			

			mainPanel = createMainPanel();
			

			r.add(mainPanel);
			
			checkGoogleStatus();
			doShowAbout(true);
			GWT.log("Module loaded");

		} catch (Exception e) {
			GWT.log(e.getMessage());
			e.printStackTrace();
		}
	}

	private void showAccountOverview() {
		RootPanel.get(PAGEID_CONTENT).clear();
		accountOverviewPanel.clear();
		accountsDetailsPanel.clear();
		accountsListPanel.clear();
		Log.debug("Show Account Overview");
		accountOverviewPanel.add(accountsListPanel);
		accountOverviewPanel.add(accountsDetailsPanel);

		if (bankingService == null) {
			bankingService = GWT.create(CustomerService.class);
		}
		bankingService.getAccounts(new AsyncCallback<List<AccountDTO>>() {

			public void onFailure(Throwable caught) {
				Log.error("Loading Account data failed", caught);
				return;

			}

			public void onSuccess(List<AccountDTO> result) {				
				if (result == null) {
					Log.warn("No accounts");

				} else {
					Log.info("Number of Accounts: " + result.size());
					Button overviewBtn = new Button(constants.overViewBtnFinancialState());
					overviewBtn.setStyleName("OverViewButton");
					overviewBtn.addClickHandler(new ClickHandler() {

						public void onClick(ClickEvent event) {
							showAccountOverview();
						}
					});
					accountsListPanel.add(overviewBtn);
					showAccountOverviewInDetailPanel(result);
					// accountsListPanel.add(new Label("Your accounts "));
					for (AccountDTO acc : result) {
						Log.info(acc.toString());

						Button btn = new Button(acc.getAccountDescription()
								+ " (" + acc.getAccountNr() + ")");

						btn.setStyleName("OverViewButton");

						btn.addClickHandler(new AccountClickHandler(acc
								.getAccountNr()));
						accountsListPanel.add(btn);
					}

					if (result.size() == 0) {
						Button neuerAccountBtn = new Button(constants.overViewBtnOpenSavingAccount());
						neuerAccountBtn.setStyleName("OverViewButton");
						neuerAccountBtn.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {
								createAccount();
							}

						});
						Log.debug("Include 'open saving account button");
						accountsListPanel.add(neuerAccountBtn);

					}
				}

				RootPanel.get(PAGEID_CONTENT).add(accountOverviewPanel);
				Log.debug("Overview Page loaded");

			}
		});

	}

	protected void showAccountOverviewInDetailPanel(List<AccountDTO> result) {
		accountsDetailsPanel.clear();
		if (result.size()==0){
			Label noAccountHint=new Label(constants.accountOverviewHintNoAccount());
			accountsDetailsPanel.add(noAccountHint);
			return;
		}		
		Label accHeaderLbl = new Label(constants.accountOverviewLblTitel());
		accountsDetailsPanel.add(accHeaderLbl);
		Grid grid = new Grid(result.size() + 1, 2);
		accountsDetailsPanel.add(grid);
		Label accHeader = new Label(constants.accountOverviewLblHeaderAccount());
		Label amountHeader = new Label(constants.accountOverviewLblHeaderBalance());
		accHeader.setStyleName("TransfersHeader");
		amountHeader.setStyleName("TransfersHeader");
		grid.setWidget(0, 0, accHeader);
		grid.setWidget(0, 1, amountHeader);
		grid.setBorderWidth(1);

		int row = 1;
		for (AccountDTO dto : result) {
			Label accountElement = new Label(dto.getAccountNr());
			Label balanceElement = new Label(
					com.google.gwt.i18n.client.NumberFormat.getCurrencyFormat()
							.format(dto.getBalance()));
			if (dto.getBalance() >= 0) {
				balanceElement.setStyleName("positiveMoney");
			} else {
				balanceElement.setStyleName("negativeMoney");
			}
			grid.setWidget(row, 0, accountElement);
			grid.setWidget(row, 1, balanceElement);

			row++;
		}
	}

	/**
	 * Initialize personalization
	 */
	private void checkGoogleStatus() {

		if (bankingService == null) {
			bankingService = GWT.create(CustomerService.class);
		}
		GWT.log(GWT.getHostPageBaseURL());
		bankingService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<SCBIdentityDTO>() {
					public void onFailure(Throwable error) {
						error.printStackTrace();
						Log.error("Login state can not be retrieved! ", error);
						Window
								.alert(constants.loginFailedText());
					}

					public void onSuccess(SCBIdentityDTO result) {
						identityInfo = result;
						if (identityInfo.isLoggedIn()) {
							Log.info("User is logged in with email-adress "+result.getEmail());
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
								Log.info("Account not yet activated in SCB: "
										+ identityInfo);
								userInformationMenuItem.setText(identityInfo
										.getEmail());
							}
							loadLoggedInSetup();
							String language=identityInfo.getLanguage();
							GWT.log("Language: "+language);
							if (language!=null && !currentLanguage.equals(language)){
								changeToLocalisedVersion(language);								
							}
						} else {
							Log.info("User is not yet logged in with his Google account");
							loadLogin();
						}
					}
				});

	}

	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(identityInfo.getLoginUrl());
		signInLink.setStyleName("rightAligned");

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
			bankingService = GWT.create(CustomerService.class);
		}
		bankingService.openNewAccount(new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				Log.error("Open new account failed", caught);
			}

			public void onSuccess(Void result) {
				Log.info("Account created");
				Window.alert(constants.accountOpened());
				Window.Location.reload();

			}
		});

	}

	private void showAccountDetails(String accNr) {
		if (bankingService == null) {
			bankingService = GWT.create(CustomerService.class);
		}
		accountsDetailsPanel.clear();

		currentAccount = accNr;

		bankingService.getAccountDetails(accNr,
				new AsyncCallback<AccountDetailDTO>() {

					public void onFailure(Throwable caught) {
						Log.error("Error loading balance", caught);

					}

					

					public void onSuccess(AccountDetailDTO result) {
						Log.info("Balance: " + result);
						TransferHistoryForm transferHistoryForm=new TransferHistoryForm(result.getTransfers());
						Label accBalanceLbl = new Label(constants.accountDetailCurrentBalance()
								+ NumberFormat.getCurrencyFormat().format(
										result.getBalance()));
						if (result.getBalance() >= 0) {
							accBalanceLbl.setStyleName("positiveMoney");
						} else {
							accBalanceLbl.setStyleName("negativeMoney");
						}

						
						Button transferMoneyButton = new Button(constants.accountDetailSendMoneyBtn());
						transferMoneyButton.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {
								MoneyTransferForm mTransfer=new MoneyTransferForm(currentAccount);
								accountsDetailsPanel.clear();
								mTransfer.addObserver(SCB.this);
								accountsDetailsPanel.add(mTransfer);
								

							}
						});
						Button transferFastMoneyButton = new Button(constants.accountDetailSendMoneyFastBtn());
						transferFastMoneyButton.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {
								FastMoneyTransferForm mTransfer=new FastMoneyTransferForm(currentAccount);
								mTransfer.addObserver(SCB.this);
								accountsDetailsPanel.clear();
								accountsDetailsPanel.add(mTransfer);
								

							}
						});
						HorizontalPanel btnPanel=new HorizontalPanel();
						btnPanel.add(transferFastMoneyButton);
						btnPanel.add(transferMoneyButton);

						accountsDetailsPanel.insert(accBalanceLbl, 0);
						accountsDetailsPanel.insert(transferHistoryForm, 1);
						accountsDetailsPanel.insert(btnPanel,2);

					}
				});
	}

	private boolean isFieldConfirmToExpresion(TextBox input, String expression,
			String errorMessage) {
		if (input.getText().trim().toUpperCase().matches(expression)) {
			input.setStyleName("");
			return true;
		} else {
			input.setStyleName(STYLE_VALUE_NOT_OKAY);
			Log.info("Text: '" + input.getText() + "'\tExpression: "
					+ expression);
			hints.add(errorMessage);
			return false;
		}

	}

	
	
	
	private void createHintTable() {
		for (int i = 0; i < hints.size(); i++) {
			String currentMessage = hints.get(i);
			Label hintLabel = new Label(currentMessage);
			hintLabel.setStyleName(STYLE_VALUE_NOT_OKAY);
			validateErrorTable.setWidget(i, 1, hintLabel);
		}
	}

	class AccountClickHandler implements ClickHandler {
		String accNr;

		public AccountClickHandler(String accountNr) {
			accNr = accountNr;
		}

		public void onClick(ClickEvent event) {
			showAccountDetails(accNr);
		}

	}
	
	/*
	 * change the language during runtime
	 * keeps the debug flag
	 */	
	private void changeToLocalisedVersion(String language){
		String queryPart=Window.Location.getQueryString();
		
		
		String reloadURL;
		String debugFlag=Window.Location.getParameter("gwt.codesvr");
		
		if (debugFlag!=null){
			reloadURL=GWT.getHostPageBaseURL()+queryPart+"&locale="+language;	
		}
		else{
			reloadURL=GWT.getHostPageBaseURL()+"?locale="+language;
		}
		
		GWT.log(reloadURL);

		
		Window.open(reloadURL,"_self",null);
	}


	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Updated");
		if (o instanceof MoneyTransferForm){
			showAccountDetails((String) arg);
		}
		else if (o instanceof FastMoneyTransferForm){
			if (arg instanceof MoneyTransferDTO){
				MoneyTransferForm confirmPage=new MoneyTransferForm(currentAccount,(MoneyTransferDTO) arg);
				accountsDetailsPanel.clear();
				accountsDetailsPanel.add(confirmPage);
				
			}
			
			
		}

		
	}
}
