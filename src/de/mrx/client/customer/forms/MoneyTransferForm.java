package de.mrx.client.customer.forms;

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
import de.mrx.client.TableStyler;
import de.mrx.shared.WrongTANException;

/**
 * GUI-Component to display the money transfer form
 * @see de.mrx.client.moneytransfer.MoneyTransferForm.ui.xml
 */
public class MoneyTransferForm extends Composite implements Observable{
	interface MyUiBinder extends UiBinder<Widget, MoneyTransferForm> {
	}
	
	public final Integer EVENT_RELOAD_ACCOUNT_DETAIL=1;
	//	private SCBMessages messages = GWT.create(SCBMessages.class);
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	@UiField
	TextBox amount;
	public TextBox getAmount() {
		return amount;
	}

	private CustomerServiceAsync bankingService;
	private boolean confirmation=false;
	
	private SCBConstants constants = GWT.create(SCBConstants.class);
	
	private String currentAccountNr;
	
	private List<String> hints = new ArrayList<String>();
	
	List<Observer>  observers=new ArrayList<Observer>();

	@UiField
	TextBox receiverAccountNr; 

	@UiField
	TextBox receiverBankName;

	@UiField
	TextBox receiverBLZ;
	
	@UiField
	TextBox receiverName;

	@UiField
	TextBox remark;
	
	@UiField
	Label requiredTanNr;
	
	@UiField
	Button sendMoney;
	
	@UiField
	Label wrongTAN;
	
	@UiField
	Button sendMoneyConfirm;
	
	
	
	public Button getSendMoneyConfirm() {
		return sendMoneyConfirm;
	}

	@UiField
	TextBox tan;
	
	public TextBox getTan() {
		return tan;
	}

	@UiField
	FlexTable validateErrorTable;
	public MoneyTransferForm(String accountNr){		
		initWidget(uiBinder.createAndBindUi(this));
		assert(accountNr!=null);	
		this.currentAccountNr=accountNr;		
		updateForm();
		wrongTAN.setVisible(false);
		wrongTAN.setText(constants.wrongTAN());
	}
	

	

	/**
	 * the MoneyTransferForm directly steps into the confirmation page asking for confirmation of a transaction
	 * @param currentAccountNr current Account
	 * @param confirmationData transaction to be confirmed
	 */
	public MoneyTransferForm(String currentAccountNr, MoneyTransferDTO dto){
		initWidget(uiBinder.createAndBindUi(this));
		assert(currentAccountNr!=null);	
		this.currentAccountNr=currentAccountNr;
		confirmation=true;
		requiredTanNr.setText(constants.confirmTanNr() +" "+ dto.getRequiredTan());
		receiverBLZ.setText(dto.getReceiverBankNr());
		amount.setText("" + dto.getAmount());
		remark.setText(dto.getRemark());
		receiverName.setText(dto.getReceiverName());
		receiverAccountNr.setText(dto.getReceiverAccountNr());
		//expand numbers to format like ??? ??? ???
		TableStyler.expandNumber(receiverBLZ);
		TableStyler.expandNumber(receiverAccountNr);
		receiverAccountNr.setEnabled(false);
		receiverBLZ.setEnabled(false);
		receiverBankName.setEnabled(false);
		amount.setEnabled(false);
		remark.setEnabled(false);
		receiverName.setEnabled(false);
		receiverAccountNr.setEnabled(false);
		receiverBankName.setText(dto.getReceiverBankName());
		updateForm();
		
	}
	
	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}
	
	
	
	private void createHintTable() {
		for (int i = 0; i < hints.size(); i++) {
			String currentMessage = hints.get(i);
			Label hintLabel = new Label(currentMessage);
			hintLabel.setStyleName(SCB.STYLE_VALUE_NOT_OKAY);
			validateErrorTable.setWidget(i, 1, hintLabel);
		}
	}
	
	public TextBox getReceiverAccountNr() {
		return receiverAccountNr;
	}
	
	public TextBox getReceiverBankName() {
		return receiverBankName;
	}
	
	public TextBox getReceiverBLZ() {
		return receiverBLZ;
	}
	
	public TextBox getReceiverName() {
		return receiverName;
	}
	
	
	public TextBox getRemark() {
		return remark;
	}

	public Button getSendMoney() {
		return sendMoney;
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

	private boolean isSendMoneyFormValid() {
		boolean result = true;
		Log.info("Text: " + receiverAccountNr.getText());
		hints.clear();
		if (!isFieldConfirmToExpresion(receiverAccountNr, "[\\s0-9]{1,15}$",
				constants.sendMoneyValidateaccount())) {
			result = false;
		}
		if (!isFieldConfirmToExpresion(receiverBLZ, "[\\s0-9]{1,15}$",
				constants.sendMoneyValidateBLZ())) {
			result = false;
		}
		if (!isFieldConfirmToExpresion(amount,
				"^[0-9]{1,5}[\\.]?[0-9]{0,2}$",
				constants.fastMoneyValidateAmount())) {
			result = false;
		}
		return result;
	}

	@Override
	public void notifyObservers(Integer eventType,Object parameter) {
		for (Observer o: observers){
			o.update(this,eventType,parameter);
		}		
	}

	
	
	public void reportError(String text) {
		for (Observer o: observers){
			o.reportError(text);
		}		
	}
	
	public void reportInfo(String text) {
		for (Observer o: observers){
			o.reportInfo(text);
		}		
	}
	
	
	
	public void setReceiverAccountNr(TextBox receiverAccountNr) {
		this.receiverAccountNr = receiverAccountNr;
	}
	
	public void setReceiverBankName(TextBox receiverBankName) {
		this.receiverBankName = receiverBankName;
	}
	
	public void setReceiverBLZ(TextBox receiverBLZ) {
		this.receiverBLZ = receiverBLZ;
	}

	public void setReceiverName(TextBox receiverName) {
		this.receiverName = receiverName;
	}

	public void setRemark(TextBox remark) {
		this.remark = remark;
	}
	
	/**
	 * update the form. Should be called if state of registration form is changed
	 * 
	 */
	public void updateForm(){
		if (confirmation){
			requiredTanNr.setVisible(true);
			tan.setVisible(true);
			sendMoneyConfirm.setVisible(true);
			sendMoney.setVisible(false);
		} 
		else{
			requiredTanNr.setVisible(false);
			tan.setVisible(false);
			sendMoney.setVisible(true);
			sendMoneyConfirm.setVisible(false);
		}
		
	}
	
	/**
	 * processes the 'submit' of the confirmation page (with TAN)
	 */
	@UiHandler("sendMoneyConfirm")
		public void sendMoneyConfirm(ClickEvent event) {
			validateErrorTable.clear();

			if (!isSendMoneyFormValid()) {
				createHintTable();
				return;
			}
		
			
			
			if (bankingService == null) {
				bankingService = GWT.create(CustomerService.class);
			}

			double amountValue = Double.parseDouble(amount.getText());

			bankingService.sendMoney(currentAccountNr,
					receiverBLZ.getText(), receiverAccountNr.getText(),
					amountValue, remark.getText(), receiverName.getText(), receiverBankName.getText(), tan.getText(),
					new AsyncCallback<Void>() {

						public void onFailure(Throwable caught) {
							if (caught instanceof WrongTANException) {
								//WrongTANException wte = (WrongTANException) caught;
								wrongTAN.setVisible(true);
							} 
							else if (caught instanceof NumberFormatException) { 

								Log.error("Sending money failed", caught);
								Window.alert("Invalid input :"
										+ caught.getMessage());
							}
							else {

								Log.error("Sending money failed", caught);
								Window.alert("Money sent failed :"
										+ caught.getMessage());
							}

						}

						public void onSuccess(Void result) {
							
							reportInfo(constants.sendMoneyHintSuccessful());
//							Window.alert(constants.sendMoneyHintSuccessful());
							notifyObservers(EVENT_RELOAD_ACCOUNT_DETAIL,currentAccountNr);

						}
					});


			
			
		}
	
	/**
	 * Processes first page of money transaction.  
	 * @author Jan
	 *
	 */
		@UiHandler("sendMoney")
		public void sendMoneyAskForConfirm(ClickEvent event) {
				validateErrorTable.clear();

				if (!isSendMoneyFormValid()){
					createHintTable();
					return;
				}
				
				
				if (bankingService == null) {
					bankingService = GWT.create(CustomerService.class);
				}

				Double amountValue = Double.parseDouble(amount.getText());
				assert(currentAccountNr!=null);				

				bankingService.sendMoneyAskForConfirmationData(currentAccountNr,
						receiverBLZ.getText(), receiverAccountNr.getText(),
						amountValue, remark.getText(), receiverName.getText(), receiverBankName.getText(),
						new AsyncCallback<MoneyTransferDTO>() {

							public void onFailure(Throwable caught) {
								if (caught instanceof NumberFormatException) {
									Window.alert("Invalid input " + caught.getMessage());
								}
								
								Log.error("Sending money failed", caught);

							}

							public void onSuccess(MoneyTransferDTO dto) {
								Log.debug("Show confirmation page");						
								confirmation=true;
								requiredTanNr.setText(constants.confirmTanNr() +" "+ dto.getRequiredTan());
								receiverBLZ.setText(dto.getReceiverBankNr());
								amount.setText("" + dto.getAmount());
								remark.setText(dto.getRemark());
								receiverName.setText(dto.getReceiverName());
								//expand numbers to format like ??? ??? ???
								TableStyler.expandNumber(receiverBLZ);
								TableStyler.expandNumber(receiverAccountNr);
								receiverAccountNr.setEnabled(false);
								receiverBLZ.setEnabled(false);
								receiverBankName.setEnabled(false);
								amount.setEnabled(false);
								remark.setEnabled(false);
								receiverName.setEnabled(false);
								receiverAccountNr.setEnabled(false);
								receiverBankName.setText(dto.getReceiverBankName());
								
								
								
								
								updateForm();

							}
						});

		
	}
}
