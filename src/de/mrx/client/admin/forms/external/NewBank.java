package de.mrx.client.admin.forms.external;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.BankDTO;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class NewBank extends Composite implements Observable {

	private static NewBankUiBinder uiBinder = GWT.create(NewBankUiBinder.class);

	interface NewBankUiBinder extends UiBinder<Widget, NewBank> {
	}

	@UiField
	Label descName;
	
	@UiField
	Label descBlz;
	
	@UiField
	TextBox name;
	
	@UiField
	TextBox blz;

	@UiField
	Label title;
	
	@UiField
	Button submit;
	
	public static final int ADD_BANK_SUCCEEDED = 5223;
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	List<Observer> observer;
	
	
	public NewBank() {
		observer = new ArrayList<Observer>();

		initWidget(uiBinder.createAndBindUi(this));
		title.setText(constants.addNewBank());
		descName.setText(constants.name());
		descBlz.setText(constants.blz());
		submit.setText(constants.submit());
	}
	
	@UiHandler("submit")
	public void onClickSubmit(ClickEvent e) {
		BankDTO newBank = new BankDTO();
		newBank.setBlz(blz.getText());
		newBank.setName(name.getText());
		AdminServiceAsync adminService = GWT.create(AdminService.class);
		adminService.addBank(newBank, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				Window.alert(result);
				notifyObservers(ADD_BANK_SUCCEEDED, null);			
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());				
			}
		});
			
	}

	@Override
	public void addObserver(Observer o) {
		observer.add(o);
		
	}

	@Override
	public void notifyObservers(Integer eventType, Object parameter) {
		for (Observer o: observer) {
			o.update(this, eventType, parameter);
		}
		
	}

}
