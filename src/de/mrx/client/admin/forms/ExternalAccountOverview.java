package de.mrx.client.admin.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.BankDTO;

public class ExternalAccountOverview extends Composite {

	private static ExternalAccountOverviewUiBinder uiBinder = GWT
			.create(ExternalAccountOverviewUiBinder.class);

	interface ExternalAccountOverviewUiBinder extends
			UiBinder<Widget, ExternalAccountOverview> {
	}

	@UiField
	FlexTable table;

	public ExternalAccountOverview(BankDTO bank) {
		initWidget(uiBinder.createAndBindUi(this));
		table.setWidget(0,0, new Label(bank.getBlz()));
		table.setWidget(0,1, new Label(bank.getName()));
		
	}


}
