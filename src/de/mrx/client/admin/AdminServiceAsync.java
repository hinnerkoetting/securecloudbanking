package de.mrx.client.admin;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mrx.client.AccountDTO;
import de.mrx.client.BankDTO;
import de.mrx.client.BankServiceAsync;

/**
 * 
 * @author hinni
 * @see AdminService
 */

public interface AdminServiceAsync extends BankServiceAsync {

	void getAllAccounts(AsyncCallback<List<AccountDTO>> callback);

	void getAllBanks(AsyncCallback<List<BankDTO>> callback);

	void addBank(BankDTO bank, AsyncCallback<Boolean> callback);

	void generateTestData(AsyncCallback<String> callback);

	

	void adminSendMoney(String senderAccountNr, String senderBLZ,
			String receiveraccountNr, double amount, String remark,
			AsyncCallback<String> callback);

	void getExternalAccounts(String blz,
			AsyncCallback<List<AccountDTO>> callback);

	void deleteBank(String blz, AsyncCallback<String> callback);

	void deleteInternalAccount(String accountNr, AsyncCallback<String> callback);



}
