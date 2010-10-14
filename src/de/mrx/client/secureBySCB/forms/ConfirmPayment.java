package de.mrx.client.secureBySCB.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.secureBySCB.SecuryBySCBService;
import de.mrx.client.secureBySCB.SecuryBySCBServiceAsync;

public class ConfirmPayment extends Composite {

	private static ConfirmPaymentUiBinder uiBinder = GWT
			.create(ConfirmPaymentUiBinder.class);

	interface ConfirmPaymentUiBinder extends UiBinder<Widget, ConfirmPayment> {
	}

	@UiField
	TextBox shop;
	
	@UiField
	TextBox amount;

	Long id;
	public ConfirmPayment(String shop, Double amount, Long id) {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.shop.setText(shop);
		this.shop.setEnabled(false);
		this.amount.setText(amount.toString());
		this.amount.setEnabled(false);
		this.id = id;
	}
	
	native  public static void close()/*-{
	 $wnd.close();


	}-*/;
	@UiHandler("accept")	
	public void clickOnAccept(ClickEvent e) {
		
		SecuryBySCBServiceAsync serv = GWT.create(SecuryBySCBService.class);
		serv.confirmTransaction(id, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if (result.equals(true)) {
					close();
				}
				else {
					Window.alert("Failure");
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}
		});
		//TODO: close this window
	}
}
