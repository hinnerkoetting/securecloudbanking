package de.mrx.client.register;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.GeneralConstants;
import de.mrx.client.SCBConstants;

public class RegistrationForm extends Composite {
	interface MyUiBinder extends UiBinder<Widget, RegistrationForm> {
	}
	
	private SCBConstants constants = GWT.create(SCBConstants.class);
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

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

	public RegistrationForm() {
		// sets listBox
		initWidget(uiBinder.createAndBindUi(this));
		language=new ListBox();
		language.addItem(constants.languageGerman());
		language.addItem(constants.languageEnglish());
		agblink.setHref(GeneralConstants.AGB_LINK);
		
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
