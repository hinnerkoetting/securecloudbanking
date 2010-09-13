package de.mrx.server;

import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceCapable;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * represents an external account. No extra information compared to GeneralAccount. Only necessary due to limitations in JDO.
 * @author IV#11C9
 *
 */
@PersistenceCapable
public class ExternalAccount extends GeneralAccount {

	public ExternalAccount(String owner, String accountNr, Bank bank) {
		super(owner, accountNr, bank);
		Key key = KeyFactory.createKey(ExternalAccount.class.getSimpleName(), accountNr);
		setId(key);

	}

	public static ExternalAccount getAccountByBLZAndAccountNr(PersistenceManager pm, Bank bank, String accountNr){
		
	Extent<ExternalAccount> recAccExt = pm.getExtent(ExternalAccount.class, true);

	javax.jdo.Query q = pm.newQuery(recAccExt);
	
	 q.setFilter("accountNr == accountNrParam && this.bankID == bankIDPARAM");
	 
	 
	 q.declareParameters("String accountNrParam, javax.jdo.annotations.Key bankIDPARAM");
	 q.setUnique(true);
	 ExternalAccount acc = (ExternalAccount) q.execute(accountNr,bank.getId());
	 return acc;
	}

	@Override
	public String toString() {
		return "ExternalAccount [getBankID()=" + getBankID()
				+ ", getAccountNr()=" + getAccountNr() + ", getId()=" + getId()
				+ ", getOwner()=" + getOwner() + ", getBalance()="
				+ "]";
	}


	public static GeneralAccount getExternalAccount(PersistenceManager pm, String accountNr, String blz){
		Extent<ExternalAccount> extent = pm.getExtent(ExternalAccount.class);
		Query query = pm.newQuery(extent, "accountNr == param");
		query.declareParameters("String param");
		
		List<ExternalAccount> result = (List<ExternalAccount>)query.execute(accountNr);
		//look for account with correct blz
		for (ExternalAccount account: result) {
			if (account.getBank(pm).getBlz().equals(blz))
				return account;
		}
		//not found
		return null;
	}
	
	/**
	 * not implemented as we do not know the amount of money in an external account
	 */
	public void changeMoney(double amount) {
		
	}

}
