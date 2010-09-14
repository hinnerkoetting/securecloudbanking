package de.mrx.client.admin;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.mrx.client.AccountDTO;
import de.mrx.client.BankDTO;
import de.mrx.client.BankService;


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
	
	public String adminSendMoney(String senderAccountNr, String sendBLZ,
			String receiveraccountNr, double amount, String remark);
	
	public List<AccountDTO> getExternalAccounts(String blz);
	
	public String deleteBank(String blz);
	
	public String deleteInternalAccount(String accountNr);
	
	public String editBankDetails(String oldName, String oldBLZ, String newName, String newBLZ);
}
