package de.mrx.client.admin.forms.shop;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.Transaction3SDTO;
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class ShopDetails extends Composite implements Observable, Observer {

	private static ShopDetailsUiBinder uiBinder = GWT
			.create(ShopDetailsUiBinder.class);

	interface ShopDetailsUiBinder extends UiBinder<Widget, ShopDetails> {
	}

	@UiField
	Label title;
	
	@UiField
	Label subTitle;
	
	@UiField
	MyStyle style;
	
	@UiField
	Button displayOpenTransactions;
	
	@UiField
	Button enableEdit;
	
	@UiField
	Button delete;
	
	@UiField
	SimplePanel content;

	interface MyStyle extends CssResource {
	    String active();
	    String nonActive();
	}
	
	private List<Observer> observers = new ArrayList<Observer>();
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	private void setActive(Button button) {
		//reset all buttons
		delete.setStyleName(style.nonActive());
		displayOpenTransactions.setStyleName(style.nonActive());
		enableEdit.setStyleName(style.nonActive());
		
		//activate the button
		button.setStyleName(style.active());
		
		
	}
	
	String name;
	String accountNr;
	String blz;
	String url;
	
	public ShopDetails(String name, String accountNr, String blz, String url) {
		this.name = name;
		this.accountNr = accountNr;
		this.blz = blz;
		this.url = url;
		
		initWidget(uiBinder.createAndBindUi(this));
		displayOpenTransactions.setText(constants.openTransactions());
		enableEdit.setText(constants.edit());
		delete.setText(constants.delete());
		onClickTransactions(null);
		
	}

	
	@UiHandler("displayOpenTransactions")
	public void onClickTransactions(ClickEvent e) {
		setActive(displayOpenTransactions);
		
		AdminServiceAsync service = GWT.create(AdminService.class);
		service.getAllOpenTransactions(name, new AsyncCallback<List<Transaction3SDTO>>() {
			
			@Override
			public void onSuccess(List<Transaction3SDTO> result) {
				content.setWidget(new OpenTransactions(result));
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}
		});
		
		
		
	}

	
	@UiHandler("enableEdit")
	public void onClickEdit(ClickEvent e){
		setActive(enableEdit);
		EditShop s =new EditShop(url, name);
		s.addObserver(this);
		content.setWidget(s);
	}
	
	@UiHandler("delete")
	public void onClickDelete(ClickEvent e) {
		Window.alert("Not implemented");
	}
	
	@Override
	public void addObserver(Observer o) {
		observers.add(o);
		
	}

	@Override
	public void notifyObservers(Integer eventType, Object parameter) {
		for (Observer o: observers) {
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
