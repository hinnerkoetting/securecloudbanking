package de.mrx.server;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.PersistenceCapable;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.mrx.client.AccountDTO;

@PersistenceCapable
public class ExternalAccount extends GeneralAccount {

	public ExternalAccount(String owner, String accountNr, Bank bank) {
		super(owner, accountNr, bank);
		Key key = KeyFactory.createKey(ExternalAccount.class.getSimpleName(), accountNr);
		setId(key);

	}

	public static ExternalAccount getAccountByBLZAndAccountNr(Bank bank, String accountNr){
		PersistenceManager pm=PMF.get().getPersistenceManager();
	Extent<ExternalAccount> recAccExt = pm.getExtent(ExternalAccount.class, true);

	javax.jdo.Query q = pm.newQuery(recAccExt);
	
	 q.setFilter("accountNr == accountNrParam && this.bankID == bankIDPARAM");
	 
	 
	 q.declareParameters("String accountNrParam, javax.jdo.annotations.Key bankIDPARAM");
	 q.setUnique(true);
	 ExternalAccount acc = (ExternalAccount) q.execute(accountNr,bank.getId());
	 return acc;
	}


	

}
