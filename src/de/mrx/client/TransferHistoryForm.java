package de.mrx.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
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
	
	@UiField
	Label transferHistoryNoTransactionHint;
	
	@UiField
	FlexTable selectPages;
	
	@UiField MyStyle style;
	interface MyStyle extends CssResource {
			String active();
		    String nonActive();
	}
		
	private static int TRANSACTIONS_PER_PAGE = 8;
	
	List<MoneyTransferDTO> transfers;
	
	private int numberPages(int transferNr) {
		int numberPages = transferNr /  TRANSACTIONS_PER_PAGE;
		if (transferNr %  TRANSACTIONS_PER_PAGE != 0)
			numberPages++;
		return numberPages;
	}
	
	
	/**
	 * Creates a table of all transfers
	 * @param transfers to be shown
	 */
	public TransferHistoryForm(List<MoneyTransferDTO> transfers) {
		this.transfers = transfers;
		
		SCBConstants constants = GWT.create(SCBConstants.class);
		
		
		initWidget(uiBinder.createAndBindUi(this));
		
		
		if (transfers==null || transfers.size()==0){
			flexTable.setVisible(false);
			transferHistoryNoTransactionHint.setVisible(true);
		}
		
		//setup pages
		int numberPages = numberPages(transfers.size());
		
		for (int i = 1; i <= numberPages; i++) {
			Anchor pageLink = new Anchor(""+i);
			final int j = i;
			pageLink.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					switchToPage(j);
					
				}
			});
			selectPages.setWidget(0, i, pageLink);
			
		}
		
		
		//set header labels
		Label lbl = new Label(constants.accountDetailHeaderDate());
		flexTable.setWidget(0, 0, lbl);
		
		lbl = new Label(constants.accountDetailHeaderAccount());
		flexTable.setWidget(0, 1, lbl);
		
		lbl = new Label(constants.accountDetailHeaderReceiverBank());
		flexTable.setWidget(0, 2, lbl);
		
		
		lbl = new Label(constants.accountDetailHeaderComment());
		flexTable.setWidget(0, 3, lbl);
		
		lbl = new Label(constants.accountDetailHeaderAmount());
		flexTable.setWidget(0, 4, lbl);

		
		switchToPage(1);
		


	}
	
	private void switchToPage(int page) {
		
		//reset style for all pages
		int numberPages = numberPages(transfers.size());
		for (int i = 1; i <= numberPages; i++) {
			Anchor pageLink = (Anchor)selectPages.getWidget(0, i);
			selectPages.setWidget(0, i, pageLink);
			if (page != i) { 
				pageLink.setStyleName(style.nonActive());
			}
			else { //page==i
				//set active anchor
				pageLink.setStyleName(style.active());
			}
		}
		
		//table row
		int row = 1;
		
		NumberFormat fmt = NumberFormat.getFormat("#0.00");
		Label lbl;
		for (int i = 0; i < TRANSACTIONS_PER_PAGE; i++) {
			int index = (page-1) * TRANSACTIONS_PER_PAGE + i;
			if (index >= transfers.size()) {
				if (flexTable.getRowCount() > row)
					flexTable.removeRow(row);
				//row++ is not needed because the last row was just removed
				continue;
			}
			MoneyTransferDTO transfer = transfers.get(index);
			 
			//add each transfer
			lbl = new Label(DateTimeFormat.getMediumDateFormat().format(
											transfer.getTimestamp()));
			flexTable.setWidget(row, 0, lbl);
			
			lbl = new Label(transfer.getReceiverName()+" ("+ transfer.getReceiverAccountNr()+")");
			flexTable.setWidget(row, 1, lbl);

			lbl = new Label(transfer.getReceiverBankName()+" ("+ transfer.getReceiverBankNr()+")");
			flexTable.setWidget(row, 2, lbl);

			lbl = new Label(transfer.getRemark());
			flexTable.setWidget(row, 3, lbl);
			
			lbl = new Label(fmt.format(transfer.getAmount()));
			flexTable.setWidget(row, 4, lbl);

			if (transfer.getAmount() < 0)
				flexTable.getCellFormatter().addStyleName(row, 4, "negativeMoney");
			else //amount >= 0
				flexTable.getCellFormatter().addStyleName(row, 4, "positiveMoney");
			row++;
		}
		TableStyler.setTableStyle(flexTable);
	}

}
