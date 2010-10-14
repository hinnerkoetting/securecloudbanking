package de.mrx.client.secureBySCB;



import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.mrx.client.BankService;
import de.mrx.client.TransactionDTO;

@RemoteServiceRelativePath("securybyscb")
public interface SecuryBySCBService extends BankService {
	public Long addTransaction(String shop, Double amount);
	
	public Boolean confirmTransaction(Long id);

	public TransactionDTO getTransactionData(Long id);
	

}
