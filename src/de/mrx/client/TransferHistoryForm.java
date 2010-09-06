package de.mrx.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TransferHistoryForm extends Composite {

	private static TransferHistoryFormUiBinder uiBinder = GWT
			.create(TransferHistoryFormUiBinder.class);

	interface TransferHistoryFormUiBinder extends
			UiBinder<Widget, TransferHistoryForm> {
	}

	
	@UiField
	FlexTable flexTable;
	
	/**
	 * Creates a table of all transfers
	 * @param transfers to be shown
	 */
	public TransferHistoryForm(List<MoneyTransferDTO> transfers) {
		//TODO change this
		SCBConstants constants = GWT.create(SCBConstants.class);
		
		
		initWidget(uiBinder.createAndBindUi(this));
		
		//set header labels
		Label lbl = new Label(constants.accountDetailHeaderDate());
		flexTable.setWidget(0, 0, lbl);
		
		lbl = new Label(constants.accountDetailHeaderAccount());
		flexTable.setWidget(0, 1, lbl);
		
		
		lbl = new Label(constants.accountDetailHeaderComment());
		flexTable.setWidget(0, 2, lbl);
		
		lbl = new Label(constants.accountDetailHeaderAmount());
		flexTable.setWidget(0, 3, lbl);

		int row = 1;
		for (MoneyTransferDTO transfer: transfers) {
			//add each transfer
			lbl = new Label(DateTimeFormat.getMediumDateFormat().format(
											transfer.getTimestamp()));
			flexTable.setWidget(row, 0, lbl);
			
			lbl = new Label(transfer.getReceiverAccountNr());
			flexTable.setWidget(row, 1, lbl);
			
			lbl = new Label(transfer.getRemark());
			flexTable.setWidget(row, 2, lbl);
			
			lbl = new Label(""+transfer.getAmount());
			flexTable.setWidget(row, 3, lbl);

			if (transfer.getAmount() < 0)
				flexTable.getCellFormatter().addStyleName(row, 3, "negativeMoney");
			else //amount >= 0
				flexTable.getCellFormatter().addStyleName(row, 3, "positiveMoney");
			row++;
		}
		
		TableStyler.setTableStyle(flexTable);


	}

}
