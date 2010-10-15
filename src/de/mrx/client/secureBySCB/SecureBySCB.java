package de.mrx.client.secureBySCB;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

import de.mrx.client.CustomerService;
import de.mrx.client.CustomerServiceAsync;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.SCBConstants;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.client.SCBMenu;
import de.mrx.client.Transaction3SDTO;
import de.mrx.client.secureBySCB.forms.ConfirmPayment;


public class SecureBySCB extends Composite implements EntryPoint,Observer{

	private static SecureBySCBUiBinder uiBinder = GWT
	.create(SecureBySCBUiBinder.class);

	interface SecureBySCBUiBinder extends
		UiBinder<Widget, SecureBySCB> {
	}
	
	@UiField
	SimplePanel content;
	
	@UiField
	HorizontalPanel topMenuPanel;
	
	@UiField
	Anchor signIn;
	
	@UiField
	Anchor signOut;
	
	
	SCBMenu scbMenu;
	
	String currentLanguage = "de";
	
	SCBConstants constants = GWT.create(SCBConstants.class);
	
	

	
	@Override
	public void update(Observable source, Object event, Object parameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportInfo(String info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportError(String error) {
		// TODO Auto-generated method stub
		
	}
	

	private void setContent(Widget w) {
		content.setWidget(w);
	}
	
	private void checkGoogleStatus() {
		CustomerServiceAsync service = GWT.create(CustomerService.class);

		String debugFlag = Window.Location.getParameter("gwt.codesvr");
		String reloadURL = GWT.getHostPageBaseURL() + "3DSCB.html";
		if (debugFlag != null)
			reloadURL += "?gwt.codesvr=" + debugFlag;
		service.login(reloadURL,
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


							signOut.setHref(identityInfo.getLogoutUrl());
							signOut.setText(constants.signOut());
							signIn.setVisible(false);
							signOut.setVisible(true);
							createTransaction();
							
							
						} else {
							Log.info("User is not yet logged in with his Google account");
							
							// Assemble login panel.
							signIn.setHref(identityInfo.getLoginUrl());
							signIn.setText(constants.signIn());
							signIn.setVisible(true);
							signOut.setVisible(false);
							setContent(new Label("Please login first!"));
						}
						
					}
		
		});

	}
	
	
	/*
	 * change the language during runtime keeps the debug flag
	 */
	public static void changeToLocalisedVersion(String language) {
		

		String reloadURL;
		String debugFlag = Window.Location.getParameter("gwt.codesvr");

		if (debugFlag != null) {
			reloadURL = GWT.getHostPageBaseURL() +"3DSCB.html?gwt.codesvr="+ debugFlag + "&locale="
					+ language;
		} else {
			reloadURL = GWT.getHostPageBaseURL() + "3DSCB.html?locale=" + language;
		}
		
		GWT.log(reloadURL);
		
		Window.open(reloadURL, "_self", null);
	}

	private void deleteOldTransactions() {
		//TODO
	}
	
	@Override
	public void onModuleLoad() {
		 deleteOldTransactions();
		RootLayoutPanel.get().add(uiBinder.createAndBindUi(this));
		Map<String, List<String>> p = Window.Location.getParameterMap();
		Collection<List<String>> c = p.values();
		for (List<String> l: c) {
			for (String a: l) {
				Log.info(a);
			}
		}
		 String s = Window.Location.getQueryString();
		 Log.info(s);
		scbMenu = new SCBMenu();
		scbMenu.addObserver(this);
		topMenuPanel.add(scbMenu);

		checkGoogleStatus();
		setContent(new Label("Please login"));
		
		confirmID(1693);
	
	}

	public void createTransaction() {

//		SecuryBySCBServiceAsync serv = GWT.create(SecuryBySCBService.class);
//		serv.addTransaction("myShopTest", 60.0, new AsyncCallback<Long>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				GWT.log(caught.toString());
//				
//			}
//
//			@Override
//			public void onSuccess(Long result) {
//
//				confirmID(result);
//				
//			}
//		});
	}
	//prototype version
	public void confirmID(Integer id) {
		SecuryBySCBServiceAsync serv = GWT.create(SecuryBySCBService.class);
		final Integer fid = id;
		serv.getTransactionData(id, new AsyncCallback<Transaction3SDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}

			@Override
			public void onSuccess(Transaction3SDTO result) {
				content.setWidget(new ConfirmPayment(result.getShopName(), result.getAmount(), fid));

				
			}
		});
	}
	
}
