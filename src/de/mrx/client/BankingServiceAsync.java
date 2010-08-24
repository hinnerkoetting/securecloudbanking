package de.mrx.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BankingServiceAsync {

	

	void getTransaction(String accountNr,AsyncCallback<List<MoneyTransferDTO>> callback);

	void  login(String requestUri,
			AsyncCallback<SCBIdentityDTO> callback);

	void getAccounts(AsyncCallback<List<AccountDTO>> callback);

	void openNewAccount(AsyncCallback<Void> callback);

	void getBalance(String accountNr, AsyncCallback<Double> callback);

	void sendMoney(String senderAccountNr, String blz, String accountNr, double amount, String remark,
			String receiverName,String bankName, String tan, AsyncCallback<Void> callback);

	void getAccountDetails(String accountNr,
			AsyncCallback<AccountDetailDTO> callback);

	void sendMoneyAskForConfirmationData(String senderAccountNr, String blz,
			String accountNr, double amount, String remark,
			String receiverName, String bankName,
			AsyncCallback<MoneyTransferDTO> callback);

	void sendMoneyAskForConfirmationDataWithEmail(String senderAccountNr,
			String email, double amount, String remark,
			AsyncCallback<MoneyTransferDTO> callback);

}
