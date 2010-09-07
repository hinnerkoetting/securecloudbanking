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
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.admin.Admin;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class Adminmenu extends Composite {

	private static AdminmenuUiBinder uiBinder = GWT
			.create(AdminmenuUiBinder.class);

	interface AdminmenuUiBinder extends UiBinder<Widget, Adminmenu> {
	}

	@UiField
	Button accounts;

	@UiField
	Button externalBanks;
	
	@UiField
	Button generateData;
	
	Admin adminpage;
	
	public Adminmenu(Admin admin) {
		adminpage = admin;
		initWidget(uiBinder.createAndBindUi(this));
		accounts.setText("Accounts");
		externalBanks.setText("External banks");
		
		generateData.setText("Generate data");
	}

	@UiHandler("accounts")
	void onClickAccounts(ClickEvent e) {
		adminpage.showAccounts();
	}
	

	@UiHandler("externalBanks")
	void onClickExternalBanks(ClickEvent e) {
		adminpage.showExternalBanks();
	}
	
	@UiHandler("generateData")
	void onClickGenerateData(ClickEvent e) {
		AdminServiceAsync bankingService = GWT.create(AdminService.class);
		bankingService.generateTestData(new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if (result.equals(Boolean.FALSE)) {
					Window.alert("Generating test data failed");
				}
				else { //result == true
					Window.alert("Success");
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}
		});
	}
}
