package de.mrx.client.moneytransfer;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.CustomerService;
import de.mrx.client.CustomerServiceAsync;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.SCB;
import de.mrx.client.SCBConstants;
import de.mrx.shared.AccountNotExistException;

public class FastMoneyTransferForm  extends Composite implements Observable{

	
	
	  interface FastMoneySendBinder extends UiBinder<Widget, FastMoneyTransferForm> {}
	  
	  
		private SCBConstants constants = GWT.create(SCBConstants.class);
//		private SCBMessages messages = GWT.create(SCBMessages.class);
		
		private static FastMoneySendBinder moneytransferBinder = GWT.create(FastMoneySendBinder.class);
		private CustomerServiceAsync bankingService;
		private List<String> hints = new ArrayList<String>();
		 
		private String currentAccountNr;
		List<Observer>  observers=new ArrayList<Observer>(); 
	  
		@UiField
		TextBox amount;

		@UiField
		TextBox remark;


		@UiField
		TextBox receiverEmail;

		
		@UiField
		Button fastsendMoney;
		
		@UiField
		FlexTable validateErrorTable;
		
		public FastMoneyTransferForm(String currentAccountNr){
			initWidget(moneytransferBinder.createAndBindUi(this));
			this.currentAccountNr=currentAccountNr;
			updateForm();
			
		}
		
		@UiHandler("fastsendMoney")
		public void sendMoney(ClickEvent e){
			if (bankingService == null) {
				bankingService = GWT.create(CustomerService.class);
			}
			validateErrorTable.clear();

			if (!isSendFastMoneyFormValid()) {
				createHintTable();
				return;
			}
			
			double amountValue = Double.parseDouble(amount.getText());
			

			bankingService.sendMoneyAskForConfirmationDataWithEmail(currentAccountNr, receiverEmail.getText(), amountValue, remark.getText(),
					new AsyncCallback<MoneyTransferDTO>() {

						public void onFailure(Throwable caught) {
							if (caught instanceof AccountNotExistException){
								Window.alert(constants.sendMoneyErrorNoAccountInOurInstitute());	
							}
							else{						
								Log.error("Sending money failed", caught);
							}

						}

						public void onSuccess(MoneyTransferDTO result) {
							notifyObservers(result);
							Log.debug("Show confirmation page");
							

						}
					});


			
		}


			private boolean isSendFastMoneyFormValid() {
				boolean result = true;
				
				hints.clear();
				if (!isFieldConfirmToExpresion(receiverEmail,"\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b",
						constants.fastMoneyValidateEmail())) {
					result = false;
				}
				if (!isFieldConfirmToExpresion(amount,
						"^[0-9]{1,5}[\\.]?[0-9]{0,2}$",
						constants.fastMoneyValidateAmount())) {				
					result = false;
				}
				return result;
			}
		private void updateForm() {
			// TODO Auto-generated method stub
			
		}
		
		
		private boolean isFieldConfirmToExpresion(TextBox input, String expression,
				String errorMessage) {
			if (input.getText().trim().toUpperCase().matches(expression)) {
				input.setStyleName("");
				return true;
			} else {
				input.setStyleName(SCB.STYLE_VALUE_NOT_OKAY);
				Log.info("Text: '" + input.getText() + "'\tExpression: "
						+ expression);
				hints.add(errorMessage);
				return false;
			}

		}

		private void createHintTable() {
			for (int i = 0; i < hints.size(); i++) {
				String currentMessage = hints.get(i);
				Label hintLabel = new Label(currentMessage);
				hintLabel.setStyleName(SCB.STYLE_VALUE_NOT_OKAY);
				validateErrorTable.setWidget(i, 1, hintLabel);
			}
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
