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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.mrx.shared.AccountNotExistException;
import de.mrx.shared.SCBException;
import de.mrx.shared.WrongTANException;

/**
 * Complete GUI for Secure Cloud Banking. Includes Registration process, general information, and online banking
 * Entry point classes define <code>onModuleLoad()</code>.
 * TODO: This class is too big! Split!
 * 
 */
public class SCB implements EntryPoint {

	// private static final Logger log = Logger.getLogger(SCB.class.getName());
//	Widget divLogger = Log.getLogger(DivLogger.class).getWidget();

	public static final String PAGEID_HEADER = "cloudbanking";
	public static final String PAGEID_CONTENT = "content";
	public static final String PAGEID_FEHLER = "fehler";
	public static final String PAGEID_SIGN = "signInOut";

	private final static String STYLE_VALUE_NOT_OKAY = "ValueNotOkay";
	String currentLanguage="en";
	

	private SCBConstants constants = GWT.create(SCBConstants.class);
	private SCBMessages messages = GWT.create(SCBMessages.class);
	RegisterServiceAsync registerSvc;
	private CustomerServiceAsync bankingService;
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private CheckBox agbBox;;
	MenuBar generalMenu = new MenuBar(true);

	private VerticalPanel mainPanel = new VerticalPanel();

	private HorizontalPanel accountOverviewPanel = new HorizontalPanel();
	private VerticalPanel accountsListPanel = new VerticalPanel();
	private VerticalPanel accountsDetailsPanel = new VerticalPanel();

	FlexTable validateErrorTable = new FlexTable();

	private TextBox nameTxt;
	private TextBox firstNameTxt;
	private TextBox streetTxt;
	private TextBox houseTxt;
	private TextBox plzTxt;
	private TextBox cityTxt;
	private TextBox emailTxt;

	
	RadioButton languageGerman;
	RadioButton languageEnglish;

	private Label nameLbl;
	private Label firstNameLbl;
	private Label streetLbl;
	private Label houseLbl;
	private Label plzLbl;
	private Label cityLbl;
	private Label emailLbl;
	private Label languageLbl;

	
	private Button registerButton = new Button(constants.menuRegister());

	private VerticalPanel registrationPanel;
	private VerticalPanel informationPanel;

	private HTMLTable registrationTable;

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

	private TextBox receiverAccountNrTxt;
	private TextBox tanConfirmationTxt;
	private Button tanConfirmationBtn;
	private TextBox receiverEmailTxt;

	private TextBox receiverBankNrTxt;

	private TextBox amountTxt;
	private TextBox remarkTxt;
	private TextBox recipientTxt;
	private TextBox bankNameTxt;

	private List<String> hints = new ArrayList<String>();

	private Button sendMoneyBtn;

	private HTMLTable transferForm;

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
		
		RootPanel.get(PAGEID_CONTENT).add(createRegistrationPanel());
	}

	/**
	 * creates the registration panel
	 * @return registration panel
	 */
	public VerticalPanel createRegistrationPanel() {		

		registrationPanel = new VerticalPanel();
		
		if (identityInfo==null || !identityInfo.isLoggedIn()  ){
			Label hint=new Label(constants.hintRegistration());
			hint.setStyleName("hint");
			registrationPanel.add(hint);
			registrationPanel.add(signInLink);
		}
		registrationTable = new Grid(7, 6);
		nameLbl = new Label(constants.registrationName());
		firstNameLbl=new Label(constants.registrationFirstName());
		streetLbl = new Label(constants.registrationStreet());
		houseLbl=new Label(constants.registrationHouseNr());
		plzLbl = new Label(constants.registrationPostalCode());
		cityLbl = new Label(constants.registrationCity());
		emailLbl = new Label(constants.registrationEmail());
		languageLbl = new Label(constants.registrationLanguage());
		nameTxt = new TextBox();
		firstNameTxt=new TextBox();
		streetTxt = new TextBox();
		plzTxt = new TextBox();
		cityTxt = new TextBox();
		emailTxt = new TextBox();		
		languageGerman=new RadioButton("language",constants.languageGerman());
		languageEnglish=new RadioButton("language",constants.languageEnglish());
		languageGerman.setValue(true);
		houseTxt=new TextBox();
		if (identityInfo != null && (identityInfo.getEmail() != null)) {
			emailTxt.setText(identityInfo.getEmail());
			emailTxt.setReadOnly(true);
		} else {
			Log.info("User logged in yet. He should enter a gmail-adress to be able to use this service");
		}
		
		agbBox = new CheckBox(constants.registrationtoSAGBHint());
		Anchor toSLink=new Anchor(constants.registrationtoAGBLink(), "doc/ToS.pdf");
		toSLink.setTarget("_blank");
		registrationTable.setWidget(0, 0, firstNameLbl);
		registrationTable.setWidget(0, 1, firstNameTxt);
		registrationTable.setWidget(0, 2, nameLbl);
		registrationTable.setWidget(0, 3, nameTxt);
		registrationTable.setWidget(0, 4, emailLbl);
		registrationTable.setWidget(0, 5, emailTxt);
		registrationTable.setWidget(1, 0, streetLbl);
		registrationTable.setWidget(1, 1, streetTxt);
		registrationTable.setWidget(1, 2, houseLbl);
		registrationTable.setWidget(1, 3, houseTxt);		
		registrationTable.setWidget(2, 0, plzLbl);
		registrationTable.setWidget(2, 1, plzTxt);
		registrationTable.setWidget(2, 2, cityLbl);
		registrationTable.setWidget(2, 3, cityTxt);
		
		registrationTable.setWidget(3, 0, languageLbl);
		registrationTable.setWidget(3, 1, languageGerman);
		registrationTable.setWidget(4, 1, languageEnglish);
		 registrationTable.setWidget(6, 0, agbBox);
		 registrationTable.setWidget(6, 1, toSLink);
		registrationTable.setWidget(6, 3, registerButton);

		registerButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				validateErrorTable.clear();

				if (isRegisterFormValid()) {
					doRegister();
				} else {

					createHintTable();
					registrationPanel.add(validateErrorTable);
				}

			}
		});
		registrationPanel.add(registrationTable);

		return registrationPanel;
	}

	/**
	 * performes the registration
	 */
	protected void doRegister() {
		

		String n = nameTxt.getText();
		String str = streetTxt.getText();
		String city = cityTxt.getText();
		String plz = plzTxt.getText();
		String email = emailTxt.getText();
		String houseNr=houseTxt.getText();
		String firstName=firstNameTxt.getText();

		SCBIdentityDTO id = new SCBIdentityDTO(n);
		id.setStreet(str);
		id.setCity(city);
		id.setPlz(plz);
		id.setEmail(email);
		id.setHouseNr(houseNr);
		id.setFirstName(firstName);
		if (languageGerman.getValue().equals(Boolean.TRUE)){
			id.setLanguage("de");
		}
		else{
			id.setLanguage("en");
		}
		
		if (registerSvc == null) {
			registerSvc = GWT.create(RegisterService.class);
		}

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {

				if (caught instanceof SCBException) {					
					Window.alert(messages.scbError(caught.getMessage()));
				} else {
					caught.printStackTrace();
					Window.alert(messages.registrationError( caught.getMessage()));					
				}

			}

			public void onSuccess(Void result) {
				Window.alert(constants.registrationSuccessMessage());
				RootPanel.get(PAGEID_CONTENT).clear();
				checkGoogleStatus();
			}
		};

		registerSvc.register(id, callback);

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

					private HTMLTable accountDetailTable;

					public void onSuccess(AccountDetailDTO result) {
						Log.info("Balance: " + result);
						Label accBalanceLbl = new Label(constants.accountDetailCurrentBalance()
								+ NumberFormat.getCurrencyFormat().format(
										result.getBalance()));
						if (result.getBalance() >= 0) {
							accBalanceLbl.setStyleName("positiveMoney");
						} else {
							accBalanceLbl.setStyleName("negativeMoney");
						}

						if (accountDetailTable != null) {
							accountsDetailsPanel.remove(accountDetailTable);
						}
						List<MoneyTransferDTO> transfers = result
								.getTransfers();
						
						accountDetailTable = new Grid(transfers.size() + 2, 4);
						if (transfers.size()==0){
							Label noTransferHint=new Label(constants.accountDetailHintNoTransaction());
							accountDetailTable.setWidget(0,0,noTransferHint);
						}
						else{
						
						
						}
						Button transferMoneyButton = new Button(constants.accountDetailSendMoneyBtn());
						transferMoneyButton.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {
								sendMoney(currentAccount);

							}
						});
						Button transferFastMoneyButton = new Button(constants.accountDetailSendMoneyFastBtn());
						transferFastMoneyButton.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {
								sendFastMailMoney(currentAccount);

							}
						});
						accountDetailTable.setWidget(accountDetailTable
								.getRowCount() - 1, 2, transferFastMoneyButton);


						accountDetailTable.setWidget(accountDetailTable
								.getRowCount() - 1, 3, transferMoneyButton);

						accountsDetailsPanel.insert(accBalanceLbl, 0);
						accountsDetailsPanel.insert(accountDetailTable, 1);

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

	private boolean isSendFastMoneyFormValid() {
		boolean result = true;
		
		hints.clear();
		if (!isFieldConfirmToExpresion(receiverEmailTxt,"\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b",
				constants.fastMoneyValidateEmail())) {
			result = false;
		}
		if (!isFieldConfirmToExpresion(amountTxt,
				"^[0-9]{1,5}[\\.]?[0-9]{0,2}$",
				constants.fastMoneyValidateAmount())) {				
			result = false;
		}
		return result;
	}
	
	private boolean isSendMoneyFormValid() {
		boolean result = true;
		Log.info("Text: " + receiverAccountNrTxt.getText());
		hints.clear();
		if (!isFieldConfirmToExpresion(receiverAccountNrTxt, "^[0-9]{1,10}$",
				constants.sendMoneyValidateaccount())) {
			result = false;
		}
		if (!isFieldConfirmToExpresion(receiverBankNrTxt, "^[0-9]{1,10}$",
				constants.sendMoneyValidateBLZ())) {
			result = false;
		}
		if (!isFieldConfirmToExpresion(amountTxt,
				"^[0-9]{1,5}[\\.]?[0-9]{0,2}$",
				constants.fastMoneyValidateAmount())) {
			result = false;
		}
		return result;
	}

	private boolean isRegisterFormValid() {
		boolean result = true;

		hints.clear();

		if (!isFieldConfirmToExpresion(nameTxt, "[\\w]+",
				constants.registerValidateName())) {
			result = false;
		}
		
		if (!isFieldConfirmToExpresion(firstNameTxt, "[\\w]+",
		constants.registerValidateFirstName())) {
			result = false;
		}
		
		if (!isFieldConfirmToExpresion(emailTxt,
				"\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b",
				constants.registerValidateEmail())) {
			result = false;
		}
		if (!isFieldConfirmToExpresion(streetTxt, "[\\w \\d������]+",
				constants.registerValidateStreet())) {
			result = false;
		}
		if (!isFieldConfirmToExpresion(plzTxt, "[\\d]+",
				constants.registerValidatePLZ())) {
			result = false;
		}
		
		if (!isFieldConfirmToExpresion(houseTxt, "[\\d]+",
				constants.registerValidateHouseNr())) {
	result = false;
}

		if (!isFieldConfirmToExpresion(cityTxt, "[\\w]+",
				constants.registerValidateCity())) {
			result = false;
		}
		
		if (agbBox.getValue()==false){
			hints.add(constants.registerValidateToS());
			result = false;			
		}

		return result;
	}

	private void sendFastMailMoney(String accNr){
		accountsDetailsPanel.clear();
		Label howToLbl=new HTML(constants.sendMoneyHint());
		transferForm = new Grid(6, 4);
		
		Label amountLbl = new Label(constants.sendMoneyFormAmount());
		Label remarkLbl = new Label(constants.sendMoneyFormRemark());
		Label emailLbl=new Label(constants.sendMoneyFormEmailRecipient());
		receiverAccountNrTxt = new TextBox();
		amountTxt = new TextBox();
		remarkTxt = new TextBox();
		receiverEmailTxt=new TextBox();
		sendMoneyBtn = new Button(constants.sendMoneyFormEmailBtn());
		sendMoneyBtn.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				validateErrorTable.clear();

				if (isSendFastMoneyFormValid()) {
					doSendMoneyFast();
				} else {

					createHintTable();
					accountsDetailsPanel.add(validateErrorTable);
				}

			}});
			
//		transferForm.setWidget(0, 0, receiverAccountNrLbl);
//		transferForm.setWidget(0, 1, receiverAccountNrTxt);
		transferForm.setWidget(0, 0, emailLbl);
		transferForm.setWidget(0, 1, receiverEmailTxt);
		transferForm.setWidget(1, 0, amountLbl);
		transferForm.setWidget(1, 1, amountTxt);
		transferForm.setWidget(1, 2, remarkLbl);
		transferForm.setWidget(1, 3, remarkTxt);
		transferForm.setWidget(2, 3, sendMoneyBtn);
		accountsDetailsPanel.add(howToLbl);
		accountsDetailsPanel.add(transferForm);
	}
	
	protected void sendMoney(String accNr) {
		accountsDetailsPanel.clear();
		transferForm = new Grid(6, 4);
		Label receiverAccountNrLbl = new Label(constants.sendMoneyFormReceiverAccountNr());
		Label receiverBankNrLbl = new Label(constants.sendMoneyFormReceiverBankNr());
		Label amountLbl = new Label(constants.sendMoneyFormAmount());
		Label remarkLbl = new Label(constants.sendMoneyFormRemark());
		Label recipientName = new Label(constants.sendMoneyFormReceiverName());
		Label bankName = new Label(constants.sendMoneyFormReceiverBankName());
		receiverAccountNrTxt = new TextBox();
		receiverBankNrTxt = new TextBox();
		amountTxt = new TextBox();
		remarkTxt = new TextBox();
		recipientTxt = new TextBox();
		bankNameTxt = new TextBox();
		sendMoneyBtn = new Button(constants.sendMoneyFormEmailBtn());
		sendMoneyBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				validateErrorTable.clear();

				if (isSendMoneyFormValid()) {
					doSendMoney();
				} else {

					createHintTable();
					accountsDetailsPanel.add(validateErrorTable);
				}

			}

		});
		transferForm.setWidget(0, 0, receiverAccountNrLbl);
		transferForm.setWidget(0, 1, receiverAccountNrTxt);
		transferForm.setWidget(0, 2, recipientName);
		transferForm.setWidget(0, 3, recipientTxt);
		transferForm.setWidget(1, 0, receiverBankNrLbl);
		transferForm.setWidget(1, 1, receiverBankNrTxt);
		transferForm.setWidget(1, 2, bankName);
		transferForm.setWidget(1, 3, bankNameTxt);
		transferForm.setWidget(2, 0, amountLbl);
		transferForm.setWidget(2, 1, amountTxt);
		transferForm.setWidget(3, 0, remarkLbl);
		transferForm.setWidget(3, 1, remarkTxt);
		transferForm.setWidget(4, 1, sendMoneyBtn);

		accountsDetailsPanel.add(transferForm);

	}

	private void doSendMoneyFast(){
		if (bankingService == null) {
			bankingService = GWT.create(CustomerService.class);
		}

		Double amount = Double.parseDouble(amountTxt.getText());
		String remark = remarkTxt.getText();
		String email=receiverEmailTxt.getText();
		
		bankingService.sendMoneyAskForConfirmationDataWithEmail(currentAccount, email, amount, remark,
				new AsyncCallback<MoneyTransferDTO>() {
					// bankingService.sendMoney(currentAccount,
					// receiverBankNrTxt.getText(),receiverAccountNrTxt.getText(),amount,remark,recipientTxt.getText(),
					// bankNameTxt.getText(), new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						if (caught instanceof AccountNotExistException){
							Window.alert(constants.sendMoneyErrorNoAccountInOurInstitute());	
						}
						else{						
							Log.error("Sending money failed", caught);
						}

					}

					public void onSuccess(MoneyTransferDTO result) {
						Log.debug("Show confirmation page");
						showConfirmationPage(result);

					}
				});

	}
	
	protected void doSendMoney() {
		if (bankingService == null) {
			bankingService = GWT.create(CustomerService.class);
		}

		Double amount = Double.parseDouble(amountTxt.getText());
		String remark = remarkTxt.getText();

		bankingService.sendMoneyAskForConfirmationData(currentAccount,
				receiverBankNrTxt.getText(), receiverAccountNrTxt.getText(),
				amount, remark, recipientTxt.getText(), bankNameTxt.getText(),
				new AsyncCallback<MoneyTransferDTO>() {
					// bankingService.sendMoney(currentAccount,
					// receiverBankNrTxt.getText(),receiverAccountNrTxt.getText(),amount,remark,recipientTxt.getText(),
					// bankNameTxt.getText(), new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						Log.error("Sending money failed", caught);

					}

					public void onSuccess(MoneyTransferDTO result) {
						Log.debug("Show confirmation page");
						showConfirmationPage(result);

					}
				});

	}

	protected void doConfirmSendMoney() {
		if (bankingService == null) {
			bankingService = GWT.create(CustomerService.class);
		}

		Double amount = Double.parseDouble(amountTxt.getText());
		String remark = remarkTxt.getText();
		String tan = tanConfirmationTxt.getText();

		bankingService.sendMoney(currentAccount, receiverBankNrTxt.getText(),
				receiverAccountNrTxt.getText(), amount, remark, recipientTxt
						.getText(), bankNameTxt.getText(), tan,
				new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						if (caught instanceof WrongTANException) {
							WrongTANException wte = (WrongTANException) caught;
							Window.alert("Falsche TAN eingegeben: "
									+ wte.getTrials() + " x");
						} else {

							Log.error("Sending money failed", caught);
							Window.alert("Money sent failed :"
									+ caught.getMessage());
						}

					}

					public void onSuccess(Void result) {
						Window.alert(constants.sendMoneyHintSuccessful());

					}
				});

	}

	private void showConfirmationPage(MoneyTransferDTO dto) {
		// accountsDetailsPanel.clear();
		
		accountsDetailsPanel.clear();
		transferForm = new Grid(6, 4);
		Label receiverAccountNrLbl = new Label(constants.sendMoneyFormReceiverAccountNr());
		Label receiverBankNrLbl = new Label(constants.sendMoneyFormReceiverBankNr());
		Label amountLbl = new Label(constants.sendMoneyFormAmount());
		Label remarkLbl = new Label(constants.sendMoneyFormRemark());
		Label recipientName = new Label(constants.sendMoneyFormReceiverName());
		Label bankName = new Label(constants.sendMoneyFormReceiverBankName());
		receiverAccountNrTxt = new TextBox();
		receiverBankNrTxt = new TextBox();
		amountTxt = new TextBox();
		remarkTxt = new TextBox();
		recipientTxt = new TextBox();
		bankNameTxt = new TextBox();
		transferForm.setWidget(0, 0, receiverAccountNrLbl);
		transferForm.setWidget(0, 1, receiverAccountNrTxt);
		transferForm.setWidget(0, 2, recipientName);
		transferForm.setWidget(0, 3, recipientTxt);
		transferForm.setWidget(1, 0, receiverBankNrLbl);
		transferForm.setWidget(1, 1, receiverBankNrTxt);
		transferForm.setWidget(1, 2, bankName);
		transferForm.setWidget(1, 3, bankNameTxt);
		transferForm.setWidget(2, 0, amountLbl);
		transferForm.setWidget(2, 1, amountTxt);
		transferForm.setWidget(3, 0, remarkLbl);
		transferForm.setWidget(3, 1, remarkTxt);
		transferForm.setWidget(4, 1, sendMoneyBtn);

		accountsDetailsPanel.add(transferForm);
		

		receiverAccountNrTxt.setText(dto.getReceiverAccountNr());

		receiverBankNrTxt.setText(dto.getReceiverBankNr());
		amountTxt.setText("" + dto.getAmount());
		remarkTxt.setText(dto.getRemark());
		recipientTxt.setText(dto.getReceiverName());
		receiverAccountNrTxt.setEnabled(false);
		receiverBankNrTxt.setEnabled(false);
		bankNameTxt.setEnabled(false);
		amountTxt.setEnabled(false);
		remarkTxt.setEnabled(false);
		recipientTxt.setEnabled(false);
		receiverAccountNrTxt.setEnabled(false);
		bankNameTxt.setText(dto.getReceiverBankName());
		tanConfirmationTxt = new TextBox();
		tanConfirmationBtn = new Button(constants.confirmTan());
		transferForm
				.setWidget(3, 2, new Label(constants.confirmTanNr() + dto.getRequiredTan()));
		transferForm.setWidget(3, 3, tanConfirmationTxt);
		transferForm.remove( sendMoneyBtn);
		transferForm.setWidget(4, 3, tanConfirmationBtn);

		tanConfirmationBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				validateErrorTable.clear();

				if (isSendMoneyFormValid()) {
					doConfirmSendMoney();
					showAccountDetails(currentAccount);
				} else {

					createHintTable();
					accountsDetailsPanel.add(validateErrorTable);
				}
			}
		});
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
}
