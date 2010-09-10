package de.mrx.client.admin.forms;

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
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class NewBank extends Composite {

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
	
	public NewBank() {
		initWidget(uiBinder.createAndBindUi(this));
		title.setText("Add new Bank");
		descName.setText("Name");
		descBlz.setText("Blz");
		submit.setText("Submit");
	}
	
	@UiHandler("submit")
	public void onClickSubmit(ClickEvent e) {
		BankDTO newBank = new BankDTO();
		newBank.setBlz(blz.getText());
		newBank.setName(name.getText());
		AdminServiceAsync adminService = GWT.create(AdminService.class);
		adminService.addBank(newBank, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if (result.equals(Boolean.FALSE)) {
					Window.alert("Adding a new bank failed");
				}
				else { //result==true
					
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());				
			}
		});
			
	}

}
