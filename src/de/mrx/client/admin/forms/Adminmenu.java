package de.mrx.client.admin.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.admin.Admin;

public class Adminmenu extends Composite {

	private static AdminmenuUiBinder uiBinder = GWT
			.create(AdminmenuUiBinder.class);

	interface AdminmenuUiBinder extends UiBinder<Widget, Adminmenu> {
	}

	@UiField
	Button accounts;

	@UiField
	Button externalBanks;
	
	
	Admin adminpage;
	
	public Adminmenu(Admin admin) {
		adminpage = admin;
		initWidget(uiBinder.createAndBindUi(this));
		accounts.setText("Accounts");
		externalBanks.setText("Manage external Banks");
	}

	@UiHandler("accounts")
	void onClickAccounts(ClickEvent e) {
		adminpage.showAccounts();
	}
	

	@UiHandler("externalBanks")
	void onClickExternalBanks(ClickEvent e) {
		adminpage.showExternalBanks();
	}
}
