package de.mrx.client.admin.forms;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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

public class EditBank extends Composite implements Observable {

	private static EditBankUiBinder uiBinder = GWT
			.create(EditBankUiBinder.class);

	interface EditBankUiBinder extends UiBinder<Widget, EditBank> {
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
	Button submit;
	
	
	String oldBLZ;
	String oldName;
	
	public static final int EDIT_BANK_SUCCEED = 23455;
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	List<Observer> observer;
	
	public EditBank(String oldBLZ, String oldName) {
		observer = new ArrayList<Observer>();

		initWidget(uiBinder.createAndBindUi(this));
		
		descName.setText(constants.name());
		descBlz.setText(constants.blz());
		submit.setText(constants.submit());
		this.oldBLZ = oldBLZ;
		this.oldName = oldName;
		name.setText(oldName);
		blz.setText(oldBLZ);
	}
	
	@UiHandler("submit")
	public void onClickSubmit(ClickEvent e) {
		BankDTO newBank = new BankDTO();
		newBank.setBlz(blz.getText());
		newBank.setName(name.getText());
		AdminServiceAsync adminService = GWT.create(AdminService.class);
		adminService.editBankDetails(oldName, oldBLZ, name.getText(), blz.getText(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				GWT.log(result);
				notifyObservers(EDIT_BANK_SUCCEED, null);
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
