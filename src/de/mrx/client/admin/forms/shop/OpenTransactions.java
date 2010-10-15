package de.mrx.client.admin.forms.shop;

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

import de.mrx.client.TableStyler;
import de.mrx.client.Transaction3SDTO;
import de.mrx.client.admin.AdminConstants;


public class OpenTransactions extends Composite {

	private static OpenTransactionsUiBinder uiBinder = GWT
			.create(OpenTransactionsUiBinder.class);

	interface OpenTransactionsUiBinder extends
			UiBinder<Widget, OpenTransactions> {
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
	
	final int idPos = 0;
	final int accountNrPos = 1;
	final int amountPos = 2;
	final int datePos = 3;
	
	List<Transaction3SDTO> transactions;
	
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
	public OpenTransactions(List<Transaction3SDTO> transactions) {
		this.transactions = transactions;
		
		AdminConstants constants = GWT.create(AdminConstants.class);
		
		
		initWidget(uiBinder.createAndBindUi(this));
		
		
		if (transactions==null || transactions.size()==0){
			flexTable.setVisible(false);
			transferHistoryNoTransactionHint.setVisible(true);
		}
		
		//setup pages
		int numberPages = numberPages(transactions.size());
		
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
		Label lbl = new Label("ID");
		flexTable.setWidget(0, idPos, lbl);
		
		lbl = new Label(constants.accountNr());
		flexTable.setWidget(0, accountNrPos, lbl);
		
		lbl = new Label(constants.amount());
		flexTable.setWidget(0, amountPos, lbl);
		
		
		lbl = new Label(constants.date());
		flexTable.setWidget(0, datePos, lbl);
		


		
		switchToPage(1);
		


	}
	
	private void switchToPage(int page) {
		
		//reset style for all pages
		int numberPages = numberPages(transactions.size());
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
			if (index >= transactions.size()) {
				if (flexTable.getRowCount() > row)
					flexTable.removeRow(row);
				//row++ is not needed because the last row was just removed
				continue;
			}
			Transaction3SDTO transaction = transactions.get(index);
			 
			//add each transfer
			
			
			lbl = new Label(transaction.getId() + "");
			flexTable.setWidget(row, idPos, lbl);

			lbl = new Label(transaction.getAccountNr());
			flexTable.setWidget(row, accountNrPos, lbl);


			lbl = new Label(fmt.format(transaction.getAmount()));
			flexTable.setWidget(row, amountPos, lbl);
			
			lbl = new Label(DateTimeFormat.getMediumDateFormat().format(
					transaction.getDate()));
			flexTable.setWidget(row, datePos, lbl);

			row++;
		}
		TableStyler.setTableStyle(flexTable);
	}
}
