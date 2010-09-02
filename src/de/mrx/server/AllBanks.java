package de.mrx.server;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Container for all banks (SCB and external.
 * Works as a root object for the persistence layer
 *
 */
@PersistenceCapable
public class AllBanks {

	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}



	public Set<Bank> getOtherBanks() {
		return otherBanks;
	}

	public void setOtherBanks(Set<Bank> otherBanks) {
		this.otherBanks = otherBanks;
	}

	public Bank getOwnBanks() {
		return ownBank;
	}

	public void setOwnBanks(Bank ownBanks) {
		this.ownBank = ownBanks;
	}



	@Persistent
	Set<Bank> otherBanks=new HashSet<Bank>();
	
	@Persistent
	Bank ownBank;
	
	/**
	 * gets the Root-Object of the JDO-Persistence
	 * @param pm
	 * @return
	 */
	public static AllBanks getBankWrapper(PersistenceManager pm){
		Extent e=pm.getExtent(AllBanks.class);
		Query query=pm.newQuery(e);
//		query.setFilter("accountNr == accountNrParam");
//		query.declareParameters("java.lang.String accountNrParam");
		query.setUnique(true);
		AllBanks result= (AllBanks) query.execute();
		
		return result;
	}
}
