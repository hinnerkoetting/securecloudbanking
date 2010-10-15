package de.mrx.client.admin.forms.shop;

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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class NewShop extends Composite implements Observable {

	private static NewShopUiBinder uiBinder = GWT.create(NewShopUiBinder.class);

	interface NewShopUiBinder extends UiBinder<Widget, NewShop> {
	}

	
	private List<Observer> observers = new ArrayList<Observer>();
	@UiField
	Label descName;
	
	@UiField
	Label descURL;
	
	@UiField
	TextBox name;
	
	@UiField
	TextBox url;

	@UiField
	Label title;
	
	@UiField
	Button submit;
	
	public static final int ADD_SHOP_SUCCEEDED = 9823;
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	

	
	
	public NewShop() {


		initWidget(uiBinder.createAndBindUi(this));
		title.setText(constants.addNewBank());
		descName.setText(constants.name());
		descURL.setText("URL");
		submit.setText(constants.submit());
	}
	
	@UiHandler("submit")
	public void onClickSubmit(ClickEvent e) {
		AdminServiceAsync service = GWT.create(AdminService.class);
		service.addShop(name.getText(), url.getText(), new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if (result.equals(Boolean.TRUE)) {
					notifyObservers(ADD_SHOP_SUCCEEDED, null);
				}
				else {
					Window.alert("Failed");
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}
		});
		
		
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


}
