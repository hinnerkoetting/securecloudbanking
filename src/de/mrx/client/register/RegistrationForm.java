package de.mrx.client.register;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.CustomerService;
import de.mrx.client.CustomerServiceAsync;
import de.mrx.client.GeneralConstants;
import de.mrx.client.RegisterService;
import de.mrx.client.RegisterServiceAsync;
import de.mrx.client.SCB;
import de.mrx.client.SCBConstants;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.client.SCBMessages;
import de.mrx.shared.SCBException;
import de.mrx.shared.UserAlreadyUsedException;

/**
 * Form for the customer registration process
 * uses UI-Binder
 * @see RegistrationForm.ui.xml
 *
 */
public class RegistrationForm extends Composite {
	interface MyUiBinder extends UiBinder<Widget, RegistrationForm> {
	}
	
	private SCBConstants constants = GWT.create(SCBConstants.class);
	private SCBMessages messages = GWT.create(SCBMessages.class);
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	private List<String> hints = new ArrayList<String>();
	
	RegisterServiceAsync registerSvc;
	
	SCBIdentityDTO identity;

	@UiField
	TextBox city;
	@UiField
	TextBox email;

	@UiField
	TextBox firstName;

	@UiField
	TextBox houseNr;

	@UiField
	ListBox language;

	@UiField
	TextBox lastName;

	@UiField
	TextBox plz;

	@UiField
	TextBox street;
	
	@UiField
	Anchor agblink;
	
	@UiField
	Button registerBtn;
	
	@UiField
	Label hintLogIn;
	
	@UiField
	VerticalPanel signInLinkWrapper;
	
	@UiField
	FlexTable validateErrorTable;
	
	@UiField
	CheckBox agb;
	
	@UiField
	HTMLPanel content;
	
	@UiField
	SimplePanel notLoggedIn;
	
	@UiField
	Label pleaseLogin;
	
	private SCBIdentityDTO id;

	private void displayRegistrationFormular() {
		content.setVisible(true);
		
	}

	
	public RegistrationForm(Anchor signInLink) {
		// sets listBox
		CustomerServiceAsync customerService = GWT.create(CustomerService.class);
		customerService.checkLogin(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result.booleanValue()) {
					displayRegistrationFormular();
					pleaseLogin.setVisible(false);
					notLoggedIn.setVisible(false);
				}
				else {
					notLoggedIn.setVisible(true);
					pleaseLogin.setVisible(true);
				}
			}
		});
		initWidget(uiBinder.createAndBindUi(this));
		content.setVisible(false);
		language.addItem(constants.languageGerman());
		language.addItem(constants.languageEnglish());
		agblink.setHref(GeneralConstants.AGB_LINK);
		signInLinkWrapper.add(signInLink);
		notLoggedIn.add(signInLink);
		pleaseLogin.setText(constants.pleaseLogin());
		
	}
	
	@UiHandler("registerBtn")
	void doRegister(ClickEvent e) {
		validateErrorTable.clear();
		boolean validForm=isRegisterFormValid();
		if (!validForm){
			createHintTable();
			return;
		}
		
		id = new SCBIdentityDTO(lastName.getText(), email.getText());
		id.setStreet(street.getText());
		id.setCity(city.getText());
		id.setPlz(plz.getText());		
		id.setHouseNr(houseNr.getText());
		id.setFirstName(firstName.getText());
		
		if (language.getSelectedIndex()==0){
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

				if (caught instanceof UserAlreadyUsedException) {
					Window.alert("User is already in use!. Please use a different email adress!");				
				}
				else if (caught instanceof SCBException) {					
					Window.alert(messages.scbError(caught.getMessage()));
				} else {
					caught.printStackTrace();
					Window.alert(messages.registrationError( caught.getMessage()));					
				}

			}

			public void onSuccess(Void result) {
				SCB.changeToLocalisedVersion(id.getLanguage());				
			}
		};

		registerSvc.register(id, callback);

		
	    
	  }

	public TextBox getCity() {
		return city;
	}

	public TextBox getEmail() {
		return email;
	}

	public TextBox getFirstName() {
		return firstName;
	}

	public TextBox getHouseNr() {
		return houseNr;
	}

	public ListBox getLanguage() {
		return language;
	}

	public TextBox getLastName() {
		return lastName;
	}
	public TextBox getPlz() {
		return plz;
	}
	public TextBox getStreet() {
		return street;
	}
	public void setCity(TextBox city) {
		this.city = city;
	}
	public void setEmail(TextBox email) {
		this.email = email;
	}
	public void setFirstName(TextBox firstName) {
		this.firstName = firstName;
	}

	public void setHouseNr(TextBox houseNr) {
		this.houseNr = houseNr;
	}

	public void setLanguage(ListBox language) {
		this.language = language;
	}

	public void setLastName(TextBox lastName) {
		this.lastName = lastName;
	}

	public void setPlz(TextBox plz) {
		this.plz = plz;
	}

	public void setStreet(TextBox street) {
		this.street = street;
	}
	
	public void setUser(SCBIdentityDTO identity){
		this.identity=identity;
		updateForm();
		
	}
	
	
	/**
	 * update the form. Should be called if state of registration form is changed
	 */
	public void updateForm(){
		if (identity==null || !identity.isLoggedIn()  ){
			hintLogIn.setVisible(true);
			signInLinkWrapper.setVisible(true);		
			Log.info("User logged in yet. This would ease the registration!");
		}
		else{
			hintLogIn.setVisible(false);
			signInLinkWrapper.setVisible(false);
			email.setText(identity.getEmail());
		}
			
	}
	
	

	private boolean isRegisterFormValid() {
		boolean result = true;

		hints.clear();
		
		//one upper case letter char then 1-infinite lower case chars (unicodes are ���, ��� and �)
		String oneWordRegExp = "([A-Z\u00C4\u00D6\u00DC][a-z\u00E4\u00F6\u00FC\u00DF.]+)";
		String spaceOrMinus = "([ -])";
		String wordRegExp   = oneWordRegExp + "(" + spaceOrMinus + oneWordRegExp + ")*";
		String streetRegExp = oneWordRegExp + "(" + spaceOrMinus + oneWordRegExp + ")*";
		
		if (!isFieldConfirmToExpresion(lastName, wordRegExp,
				constants.registerValidateName())) {
			result = false;
		}
		
		if (!isFieldConfirmToExpresion(firstName, wordRegExp,
		constants.registerValidateFirstName())) {
			result = false;
		}
		
		if (!isFieldConfirmToExpresion(email,
				"\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b",
				constants.registerValidateEmail())) {
			result = false;
		}
		if (!isFieldConfirmToExpresion(street, streetRegExp,
				constants.registerValidateStreet())) {
			result = false;
		}
		if (!isFieldConfirmToExpresion(plz, "[\\d]+",
				constants.registerValidatePLZ())) {
			result = false;
		}
		
		if (!isFieldConfirmToExpresion(houseNr, "[\\d]+",
				constants.registerValidateHouseNr())) {
			result = false;
		}

		if (!isFieldConfirmToExpresion(city, wordRegExp,
				constants.registerValidateCity())) {
			result = false;
		}
		
		if (agb.getValue()==false){
			hints.add(constants.registerValidateToS());
			result = false;			
		}

		return result;
	}
	
	private boolean isFieldConfirmToExpresion(TextBox input, String expression,
			String errorMessage) {
		if (input.getText().trim().matches(expression)) {
			input.setStyleName("");
			return true;
		} else {
			input.setStyleName(SCB.STYLE_VALUE_NOT_OKAY);
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
			hintLabel.setStyleName(SCB.STYLE_VALUE_NOT_OKAY);
			validateErrorTable.setWidget(i, 1, hintLabel);
		}
	}

}
