package de.mrx.client.admin.forms;



import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.BankDTO;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.TableStyler;
import de.mrx.client.admin.AdminConstants;


public class AdminExternalBanks extends Composite implements Observable, Observer {

	private static AdminExternalBanksUiBinder uiBinder = GWT
			.create(AdminExternalBanksUiBinder.class);

	interface AdminExternalBanksUiBinder extends
			UiBinder<Widget, AdminExternalBanks> {
	}

	@UiField
	Label title;
	
	@UiField
	FlexTable table;
	
	@UiField
	Button addBank;
	
	List<Observer> observer;
	
	public static final int EDIT_BANK = 2332;
	public static final int ADD_BANK = 2333;
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	public AdminExternalBanks() {
		observer = new ArrayList<Observer>();
		initWidget(uiBinder.createAndBindUi(this));
		title.setText(constants.externalBanks());
		addBank.setText(constants.addBank());
	}
	
	public void setBanks(List<BankDTO> banks) {
		final int namePos = 0;
		final int blzPos =  1;
		final int editPos = 2;
		
		//add header
		
		table.setWidget(0, namePos, new Label(constants.name()));
		table.setWidget(0, blzPos, new Label(constants.blz()));
		table.setWidget(0, editPos, new Label(constants.properties()));

		
		
		int row= 1;
		for (final BankDTO bank: banks) {
			//name
			table.setWidget(row, namePos, new Label(bank.getName()));
			
			//blz
			table.setWidget(row, blzPos, new Label(bank.getBlz()));
			
			//edit
			Button editButton = new Button("x");
			editButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					notifyObservers(EDIT_BANK, bank);
					
				}
			});
			table.setWidget(row, editPos, editButton);
			row++;
		}
		TableStyler.setTableStyle(table);
	}
	
	@UiHandler("addBank")
	public void onClickAddBank(ClickEvent event) {
		notifyObservers(ADD_BANK, null);
	}

	@Override
	public void addObserver(Observer o) {
		observer.add(o);
		
	}

	@Override
	public void notifyObservers(Integer eventType, Object parameter) {
		for (Observer o: observer) {
			o.update(this, eventType, parameter);
		}
		
	}

	@Override
	public void update(Observable source, Object event, Object parameter) {
		notifyObservers((Integer)event, parameter);
		
	}

	@Override
	public void reportInfo(String info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportError(String error) {
		// TODO Auto-generated method stub
		
	}

}
