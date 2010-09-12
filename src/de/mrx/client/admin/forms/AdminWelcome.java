package de.mrx.client.admin.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.admin.AdminConstants;

public class AdminWelcome extends Composite {

	private static AdminWelcomeUiBinder uiBinder = GWT
			.create(AdminWelcomeUiBinder.class);

	interface AdminWelcomeUiBinder extends UiBinder<Widget, AdminWelcome> {
	}

	@UiField
	Label welcome;

	AdminConstants constants = GWT.create(AdminConstants.class);
	
	public AdminWelcome() {
		initWidget(uiBinder.createAndBindUi(this));
		welcome.setText(constants.adminWelcome());
	}



}
