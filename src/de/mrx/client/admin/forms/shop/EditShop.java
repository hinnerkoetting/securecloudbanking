package de.mrx.client.admin.forms.shop;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.BankDTO;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class EditShop extends Composite implements Observable {

	private static EditShopUiBinder uiBinder = GWT
			.create(EditShopUiBinder.class);

	interface EditShopUiBinder extends UiBinder<Widget, EditShop> {
	}

	@UiField
	Label descName;
	
	@UiField
	Label descURL;
	
	@UiField
	TextBox name;
	
	@UiField
	TextBox url;

	@UiField
	Button submit;
	
	
	String oldURL;
	String oldName;
	
	public static final int EDIT_SHOP_SUCCEED = 2455;
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	List<Observer> observer;
	
	public EditShop(String oldURL, String oldName) {
		observer = new ArrayList<Observer>();

		initWidget(uiBinder.createAndBindUi(this));
		
		descName.setText(constants.name());
		descURL.setText("URL");
		submit.setText(constants.submit());
		this.oldURL = oldURL;
		this.oldName = oldName;
		name.setText(oldName);
		url.setText(oldURL);
	}
	
	@UiHandler("submit")
	public void onClickSubmit(ClickEvent e) {
		BankDTO newBank = new BankDTO();
		newBank.setBlz(url.getText());
		newBank.setName(name.getText());
		AdminServiceAsync adminService = GWT.create(AdminService.class);
		adminService.editShopDetails(oldName, oldURL, name.getText(), url.getText(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				GWT.log(result);
				notifyObservers(EDIT_SHOP_SUCCEED, null);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());				
			}
		});
			
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
}
