package de.mrx.client.register;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.GeneralConstants;
import de.mrx.client.RegisterService;
import de.mrx.client.RegisterServiceAsync;
import de.mrx.client.SCB;
import de.mrx.client.SCBConstants;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.client.SCBMessages;
import de.mrx.shared.SCBException;

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
	
	RegisterServiceAsync registerSvc;

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

	public RegistrationForm() {
		// sets listBox
		initWidget(uiBinder.createAndBindUi(this));		
		language.addItem(constants.languageGerman());
		language.addItem(constants.languageEnglish());
		agblink.setHref(GeneralConstants.AGB_LINK);
		
	}
	
	@UiHandler("registerBtn")
	void doRegister(ClickEvent e) {
		
		SCBIdentityDTO id = new SCBIdentityDTO(lastName.getText());
		id.setStreet(street.getText());
		id.setCity(city.getText());
		id.setPlz(plz.getText());
		id.setEmail(email.getText());
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

				if (caught instanceof SCBException) {					
					Window.alert(messages.scbError(caught.getMessage()));
				} else {
					caught.printStackTrace();
					Window.alert(messages.registrationError( caught.getMessage()));					
				}

			}

			public void onSuccess(Void result) {				
				RootPanel.get(SCB.PAGEID_CONTENT).clear();
				
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

}
