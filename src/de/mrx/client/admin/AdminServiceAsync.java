package de.mrx.client.admin;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mrx.client.AccountDTO;
import de.mrx.client.BankDTO;
import de.mrx.client.BankServiceAsync;
import de.mrx.server.Bank;

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

	void getBankByBLZ(String blz, AsyncCallback<BankDTO> callback);



}
