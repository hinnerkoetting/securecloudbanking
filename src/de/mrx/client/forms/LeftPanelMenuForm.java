package de.mrx.client.forms;

import java.util.ArrayList;
import java.util.List;

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

import de.mrx.client.AccountDetailDTO;
import de.mrx.client.CustomerService;
import de.mrx.client.CustomerServiceAsync;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.SCBConstants;

public class LeftPanelMenuForm extends Composite implements Observable{
	interface MyUiBinder extends UiBinder<Widget, LeftPanelMenuForm> {
	}
	
	public final static Integer EVENT_SHOW_OVERVIEW=1;
	public final static Integer EVENT_SHOW_SAVING_ACCOUNT=2;
	public final static Integer EVENT_SEND_MONEY=3;
	public final static Integer EVENT_SEND_MONEY_FAST=4;
	private SCBConstants constants = GWT.create(SCBConstants.class);

	private static MyUiBinder menuUiBinder = GWT.create(MyUiBinder.class);
	CustomerServiceAsync  customerService=GWT.create(CustomerService.class);

	List<Observer>  observers=new ArrayList<Observer>();
		
	@UiField
	Button overviewBtn;
	
	@UiField
	Button savingAccDetailBtn;
	
	@UiField
	Button sendMoneyBtn;
	
	@UiField
	Button sendFastMoneyBtn;
	
	@UiField
	Button openNewAccount;
	
	
 	
	
	public LeftPanelMenuForm(){
		initWidget(menuUiBinder.createAndBindUi(this));
	}
	
	public void setStateOpenedSavingAccount(){
		overviewBtn.setVisible(true);
		savingAccDetailBtn.setVisible(true);
		sendMoneyBtn.setVisible(true);
		sendFastMoneyBtn.setVisible(true);
		openNewAccount.setVisible(false);		
	}
	
	public void setStateRegisteredButNoSavingAccount(){
		overviewBtn.setVisible(false);
		savingAccDetailBtn.setVisible(false);
		sendMoneyBtn.setVisible(false);
		sendFastMoneyBtn.setVisible(false);
		openNewAccount.setVisible(true);		
	}
	
	public void setStateNotRegistered(){
		overviewBtn.setVisible(false);
		savingAccDetailBtn.setVisible(false);
		sendMoneyBtn.setVisible(false);
		sendFastMoneyBtn.setVisible(false);
		openNewAccount.setVisible(false);		
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void notifyObservers(Integer eventType,Object parameter) {
		for (Observer o: observers){
			o.update(this,eventType,parameter);
		}		
	}
	
	@UiHandler("overviewBtn")	
	public void showOverview(ClickEvent e){
		notifyObservers(EVENT_SHOW_OVERVIEW,null);
		
	}
	
	@UiHandler("savingAccDetailBtn")	
	public void showSavingAccount(ClickEvent e){
		customerService.getSavingAccount(new AsyncCallback<AccountDetailDTO>() {
			
			@Override
			public void onSuccess(AccountDetailDTO result) {
				notifyObservers(EVENT_SHOW_SAVING_ACCOUNT,result);

				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("showOverviewErro",caught);
				Window.alert("Fehler: "+caught.getMessage());
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	@UiHandler("sendMoneyBtn")	
	public void showStandardMoneyTransform(ClickEvent e){
		notifyObservers(EVENT_SEND_MONEY,null);
	}
	
	@UiHandler("sendFastMoneyBtn")	
	public void showFastTransform(ClickEvent e){
		notifyObservers(EVENT_SEND_MONEY_FAST,null);
	}

}
