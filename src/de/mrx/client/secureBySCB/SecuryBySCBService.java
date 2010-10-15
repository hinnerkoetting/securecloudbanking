package de.mrx.client.secureBySCB;



import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.mrx.client.BankService;
import de.mrx.client.Transaction3SDTO;

@RemoteServiceRelativePath("securybyscb")
public interface SecuryBySCBService extends BankService {

	
	public Boolean confirmTransaction(Integer id);

	public Transaction3SDTO getTransactionData(Integer id);
	

}
