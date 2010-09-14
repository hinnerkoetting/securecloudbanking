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
import com.google.appengine.api.datastore.KeyFactory;

import de.mrx.shared.SCBData;

/**
 * Container for all banks (SCB and external.
 * Works as a root object for the persistence layer
 *
 */
@PersistenceCapable
public class AllBanks {

	private static int KEY_VALUE = 53353;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	public Key getId() {
		return id;
	}
	
	Set<Bank> allBanks;

	private void setID(Key id) {
		this.id = id;
	}

	public Set<Bank> getBanks() {
		return allBanks;
	}

	public void addBank(Bank bank) {
		allBanks.add(bank);
	}

	public Bank getSCBBank(PersistenceManager pm) {
		Extent<Bank> e=pm.getExtent(Bank.class);
		Query query=pm.newQuery(e);
		query.setUnique(true);
		query.setFilter("blz == param1 && name == param2");
		query.declareParameters("String param1, String param2");
		Bank result = (Bank)query.execute(SCBData.SCB_PLZ, SCBData.SCB_NAME);
		if (result == null) {
			
			result = new Bank(SCBData.SCB_PLZ, SCBData.SCB_NAME, this);
			pm.makePersistent(result);
		}
		return result;
	}


	private AllBanks() {
		allBanks = new HashSet<Bank>();
	}

	public Bank getBankByBlz(PersistenceManager pm, String blz) {
		Extent<Bank> e=pm.getExtent(Bank.class);
		Query query=pm.newQuery(e);
		query.setUnique(true);
		query.setFilter("blz == param");
		query.declareParameters("String param");
		query.setUnique(true);
		Bank result = (Bank)query.execute(blz);

		return result;
	}

	private static AllBanks singleton;
	
	/**
	 * gets the Root-Object of the JDO-Persistence
	 * @param pm
	 * @return
	 */
	public static AllBanks getSingleton(PersistenceManager pm){
		
		if (singleton == null) {
			//request singleton from storage
			Extent<AllBanks> e=pm.getExtent(AllBanks.class);
			Query query=pm.newQuery(e);
	
			query.setUnique(true);
			AllBanks result= (AllBanks) query.execute();
			
			if (result == null) {
				//create new if not stored
				result = new AllBanks();
				result.setID(KeyFactory.createKey(AllBanks.class.getSimpleName(), KEY_VALUE));
				pm.currentTransaction().begin();
				pm.makePersistent(result);
				pm.currentTransaction().commit();
			}
			return result;
		}
		else
			return singleton;
		
	}
}
