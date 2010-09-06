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

	public List<AccountDTO> getAllAccounts();
		
	public List<BankDTO> getAllBanks();
	
	public boolean addBank(BankDTO bank);
}
