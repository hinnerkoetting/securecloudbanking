package de.mrx.client.admin;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.mrx.client.AccountDTO;
import de.mrx.client.BankDTO;
import de.mrx.client.BankService;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.ShopDTO;
import de.mrx.client.TansDTO;
import de.mrx.client.Transaction3SDTO;


/**
 * 
 * @author hinni
 * offers admin services
 */
@RemoteServiceRelativePath("admin")
public interface AdminService extends BankService {

	public List<AccountDTO> getAllInternalAccounts();
		
	public List<AccountDTO> searchInternalAccounts(String owner, String accountnr);
	
	public List<BankDTO> getAllBanks();
	
	public String addBank(BankDTO bank);
	
	public String generateTestData();
	
	public String deleteData();
	
	/**
	 * @throws NumberFormatException
	 * @param senderAccountNr
	 * @param sendBLZ
	 * @param receiveraccountNr
	 * @param amount
	 * @param remark
	 * @return
	 */
	public String adminSendMoney(String senderAccountNr, String sendBLZ,
			String receiveraccountNr, double amount, String remark) throws NumberFormatException;
	
	public List<AccountDTO> getExternalAccounts(String blz);
	
	public String deleteBank(String blz);
	
	public String deleteInternalAccount(String accountNr);
	
	public String resetInternalAccount(String accountNr);
	
	public String editBankDetails(String oldName, String oldBLZ, String newName, String newBLZ);
	
	public String editShopDetails(String oldName, String oldURL, String newName, String newURL);
	
	public TansDTO getTans(String accountNr);
	
	public List<MoneyTransferDTO> getTransfers(String accountNr, String blz);
	
	public Boolean addShop(String name, String url);
	
	public List<ShopDTO> getAllShops();
	
	public List<Transaction3SDTO> getAllOpenTransactions(String shopName);
	
	public Boolean deleteOpenTransactions();
}
