package de.mrx.client.admin.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AdminExternalBanks extends Composite {

	private static AdminExternalBanksUiBinder uiBinder = GWT
			.create(AdminExternalBanksUiBinder.class);

	interface AdminExternalBanksUiBinder extends
			UiBinder<Widget, AdminExternalBanks> {
	}

	public AdminExternalBanks() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
