package de.mrx.client.admin;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mrx.client.AccountDTO;
import de.mrx.client.BankDTO;
import de.mrx.client.BankServiceAsync;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.ShopDTO;
import de.mrx.client.TansDTO;
import de.mrx.client.Transaction3SDTO;

/**
 * 
 * @author hinni
 * @see AdminService
 */

public interface AdminServiceAsync extends BankServiceAsync {

	void getAllInternalAccounts(AsyncCallback<List<AccountDTO>> callback);

	void getAllBanks(AsyncCallback<List<BankDTO>> callback);

	void addBank(BankDTO bank, AsyncCallback<String> callback);

	void generateTestData(AsyncCallback<String> callback);

	

	void adminSendMoney(String senderAccountNr, String senderBLZ,
			String receiveraccountNr, double amount, String remark,
			AsyncCallback<String> callback);

	void getExternalAccounts(String blz,
			AsyncCallback<List<AccountDTO>> callback);

	void deleteBank(String blz, AsyncCallback<String> callback);

	void deleteInternalAccount(String accountNr, AsyncCallback<String> callback);
	
	

	void searchInternalAccounts(String owner, String accountnr,
			AsyncCallback<List<AccountDTO>> callback);

	void editBankDetails(String oldName, String oldBLZ, String newName,
			String newBLZ, AsyncCallback<String> callback);

	void deleteData(AsyncCallback<String> callback);

	void getTans(String accountNr, AsyncCallback<TansDTO> callback);

	void getTransfers(String accountNr, String blz,
			AsyncCallback<List<MoneyTransferDTO>> callback);

	void resetInternalAccount(String accountNr, AsyncCallback<String> callback);

	void addShop(String name, String url, AsyncCallback<Boolean> callback);

	void getAllShops(AsyncCallback<List<ShopDTO>> callback);

	void getAllOpenTransactions(String shopName, AsyncCallback<List<Transaction3SDTO>> callback);

	void deleteOpenTransactions(AsyncCallback<Boolean> callback);

	void editShopDetails(String oldName, String oldURL, String newName,
			String newURL, AsyncCallback<String> callback);



}
