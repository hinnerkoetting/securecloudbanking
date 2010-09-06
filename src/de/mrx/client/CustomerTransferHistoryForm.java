package de.mrx.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class CustomerTransferHistoryForm extends Composite implements Observable{
	interface MyUiBinder extends UiBinder<Widget, CustomerTransferHistoryForm> {
	}
	
	
	public final static Integer EVENT_SEND_MONEY=1;
	public final static Integer EVENT_SEND_FAST_MONEY=2;
	private SCBConstants constants = GWT.create(SCBConstants.class);
//	private SCBMessages messages = GWT.create(SCBMessages.class);
	private static MyUiBinder transactionsUiBinder = GWT.create(MyUiBinder.class);

	List<Observer>  observers=new ArrayList<Observer>();
	
	@UiField
	Label balance;
	
	@UiField
	VerticalPanel transferWrapper;

	TransferHistoryForm transfers;
	AccountDetailDTO account;
	
	public CustomerTransferHistoryForm(AccountDetailDTO acc ){
		initWidget(transactionsUiBinder.createAndBindUi(this));
		account=acc;
		transfers=new TransferHistoryForm(acc.getTransfers());
		transferWrapper.add(transfers);		
		String balanceValue=com.google.gwt.i18n.client.NumberFormat.getCurrencyFormat()
						.format(account.getBalance());
		if (account.getBalance() >= 0) {
			balance.setStyleName("positiveMoney");
		} else {
			balance.setStyleName("negativeMoney");
		}
		balance.setText(constants.accountDetailCurrentBalance()+ " "+balanceValue);
	}
	
	
	@UiHandler ("btnSendFastMoney")
	public void sendFastMoney(ClickEvent e){
		notifyObservers(EVENT_SEND_FAST_MONEY);
	}
	
	@UiHandler ("btnSendStandardMoney")
	public void sendStandardMoney(ClickEvent e){
		notifyObservers(EVENT_SEND_MONEY);
	}
	 
	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void notifyObservers(Object arg) {
		for (Observer o: observers){
			o.update(this,arg);
		}		
	}
}