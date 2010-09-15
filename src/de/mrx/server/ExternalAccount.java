package de.mrx.server;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceCapable;

/**
 * represents an external account. No extra information compared to GeneralAccount. Only necessary due to limitations in JDO.
 * @author IV#11C9
 *
 */
@PersistenceCapable
public class ExternalAccount extends GeneralAccount {

	public ExternalAccount(String owner, String accountNr, Bank bank) {
		super(owner, accountNr, bank, ExternalAccount.class.getSimpleName());
	}

	public static ExternalAccount getAccountByBLZAndAccountNr(PersistenceManager pm, Bank bank, String accountNr){
		return getExternalAccount(pm, accountNr, bank.getBlz());
	}

	@Override
	public String toString() {
		return "ExternalAccount [getBankID()=" 
				+ ", getAccountNr()=" + getAccountNr() + ", getId()=" + getId()
				+ ", getOwner()=" + getOwner() + ", getBalance()="
				+ "]";
	}


	public static ExternalAccount getExternalAccount(PersistenceManager pm, String accountNr, String blz){
		Extent<ExternalAccount> extent = pm.getExtent(ExternalAccount.class);
		Query query = pm.newQuery(extent, "accountNr == param1 && blz == param2");
		query.declareParameters("String param1, String param2");
		
		query.setUnique(true);
		return (ExternalAccount)query.execute(accountNr, blz);

	}
	
	/**
	 * not implemented as we do not know the amount of money in an external account
	 */
	public void changeMoney(double amount) {
		
	}

}
