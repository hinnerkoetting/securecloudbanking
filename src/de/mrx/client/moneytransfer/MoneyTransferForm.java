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
import de.mrx.shared.WrongTANException;

/**
 * GUI-Component to display the money transfer form
 * @see de.mrx.client.moneytransfer.MoneyTransferForm.ui.xml
 */
public class MoneyTransferForm extends Composite implements Observable{
	interface MyUiBinder extends UiBinder<Widget, MoneyTransferForm> {
	}
	
	private SCBConstants constants = GWT.create(SCBConstants.class);
//	private SCBMessages messages = GWT.create(SCBMessages.class);
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	private CustomerServiceAsync bankingService;
	private List<String> hints = new ArrayList<String>();
	
	@UiField
	TextBox receiverAccountNr;
	
	@UiField
	TextBox receiverName;
	
	@UiField
	TextBox receiverBLZ;
	
	@UiField
	TextBox receiverBankName;

	@UiField
	TextBox amount;

	@UiField
	TextBox remark;

	@UiField
	Button sendMoney;
	
	@UiField
	Button sendMoneyConfirm;
	
	@UiField
	FlexTable validateErrorTable;
	
	@UiField
	Label requiredTanNr;
	
	@UiField
	TextBox tan;
	
	
	private boolean confirmation=false;
	
	private String currentAccountNr;
	List<Observer>  observers=new ArrayList<Observer>(); 
	
	public MoneyTransferForm(String currentAccountNr){
		initWidget(uiBinder.createAndBindUi(this));
		this.currentAccountNr=currentAccountNr;
		updateForm();
		
	}
	
	/**
	 * the MoneyTransferForm directly steps into the confirmation page asking for confirmation of a transaction
	 * @param currentAccountNr current Account
	 * @param confirmationData transaction to be confirmed
	 */
	public MoneyTransferForm(String currentAccountNr, MoneyTransferDTO dto){
		initWidget(uiBinder.createAndBindUi(this));
		this.currentAccountNr=currentAccountNr;
		confirmation=true;
		requiredTanNr.setText(constants.confirmTanNr() +" "+ dto.getRequiredTan());
		receiverBLZ.setText(dto.getReceiverBankNr());
		amount.setText("" + dto.getAmount());
		remark.setText(dto.getRemark());
		receiverName.setText(dto.getReceiverName());
		receiverAccountNr.setText(dto.getReceiverAccountNr());
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
	
	@UiHandler("sendMoneyConfirm")
	public void sendMoneyConfirmation(ClickEvent e){
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
							WrongTANException wte = (WrongTANException) caught;
							Window.alert("Falsche TAN eingegeben: "
									+ wte.getTrials() + " x");
						} else {

							Log.error("Sending money failed", caught);
							Window.alert("Money sent failed :"
									+ caught.getMessage());
						}

					}

					public void onSuccess(Void result) {
						Window.alert(constants.sendMoneyHintSuccessful());
						notifyObservers(currentAccountNr);

					}
				});


		
		
	}
	
	@UiHandler("sendMoney")
	public void sendMoney(ClickEvent e){
		validateErrorTable.clear();

		if (!isSendMoneyFormValid()){
			createHintTable();
			return;
		}
		if (bankingService == null) {
			bankingService = GWT.create(CustomerService.class);
		}

		Double amountValue = Double.parseDouble(amount.getText());
		

		bankingService.sendMoneyAskForConfirmationData(currentAccountNr,
				receiverBLZ.getText(), receiverAccountNr.getText(),
				amountValue, remark.getText(), receiverName.getText(), receiverBankName.getText(),
				new AsyncCallback<MoneyTransferDTO>() {
					// bankingService.sendMoney(currentAccount,
					// receiverBankNrTxt.getText(),receiverAccountNrTxt.getText(),amount,remark,recipientTxt.getText(),
					// bankNameTxt.getText(), new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
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
	
	private void createHintTable() {
		for (int i = 0; i < hints.size(); i++) {
			String currentMessage = hints.get(i);
			Label hintLabel = new Label(currentMessage);
			hintLabel.setStyleName(SCB.STYLE_VALUE_NOT_OKAY);
			validateErrorTable.setWidget(i, 1, hintLabel);
		}
	}
	
	private boolean isSendMoneyFormValid() {
		boolean result = true;
		Log.info("Text: " + receiverAccountNr.getText());
		hints.clear();
		if (!isFieldConfirmToExpresion(receiverAccountNr, "^[0-9]{1,10}$",
				constants.sendMoneyValidateaccount())) {
			result = false;
		}
		if (!isFieldConfirmToExpresion(receiverBLZ, "^[0-9]{1,10}$",
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
}
