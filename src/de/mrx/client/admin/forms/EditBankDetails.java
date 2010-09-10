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
import de.mrx.client.admin.Admin;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class EditBankDetails extends Composite {

	private static EditBankDetailsUiBinder uiBinder = GWT
			.create(EditBankDetailsUiBinder.class);

	interface EditBankDetailsUiBinder extends UiBinder<Widget, EditBankDetails> {
	}

	@UiField
	Label title;

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
	
	Admin adminpage; 
	
	public EditBankDetails(Admin admin,String oldBLZ, String oldName) {
		adminpage = admin;
		initWidget(uiBinder.createAndBindUi(this));
		
		title.setText("Edit bank details");
		descName.setText("Name");
		descBlz.setText("Blz");
		submit.setText("Submit");
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
				Window.alert(result);
				adminpage.showExternalBanks();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());				
			}
		});
			
	}

}
