package de.mrx.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.CssResource;
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


	interface Style extends CssResource {
		String header();
		String transfersOdd();
		String transfersEven();
		String positiveMoney();
		String negativeMoney();
	}
	
	@UiField
	FlexTable flexTable;
	
	@UiField
	Style style;
	
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
		lbl.setStyleName(style.header());
		flexTable.setWidget(0, 0, lbl);
		
		lbl = new Label(constants.accountDetailHeaderAccount());
		flexTable.setWidget(0, 1, lbl);
		
		
		lbl = new Label(constants.accountDetailHeaderComment());
		flexTable.setWidget(0, 2, lbl);
		
		lbl = new Label(constants.accountDetailHeaderAmount());
		flexTable.setWidget(0, 3, lbl);

		//set style for header
		for (int i = 0; i < 4; i++)
			flexTable.getCellFormatter().setStyleName(0, i, style.header());
		
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

			//set style
			if (row %2 == 0) { //even row
				for (int i = 0; i < 4; i++)
					flexTable.getCellFormatter().setStyleName(row, i, style.transfersEven());
			}
			else //odd row
				for (int i = 0; i < 4; i++)
					flexTable.getCellFormatter().setStyleName(row, i, style.transfersOdd());
			
			if (transfer.getAmount() < 0)
				flexTable.getCellFormatter().addStyleName(row, 3, style.negativeMoney());
			else //amount >= 0
				flexTable.getCellFormatter().addStyleName(row, 3, style.positiveMoney());
			row++;
		}

	}

}
