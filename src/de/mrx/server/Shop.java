package de.mrx.server;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.mrx.client.ShopDTO;
import de.mrx.shared.SCBData;

@PersistenceCapable
public class Shop {

	@PrimaryKey
	@Persistent
	Key id;
	
	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBlz() {
		return blz;
	}

	public void setBlz(String blz) {
		this.blz = blz;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Persistent
	String name;
	
	@Persistent
	String accountNo;
	
	@Persistent
	String blz;
	
	@Persistent
	String url;
	
	public Shop(String name, String accountNr, String url, Bank bank) {
		assert(blz.equals(SCBData.SCB_PLZ));
		this.name = name;
		this.accountNo = accountNr;
		this.url = url;
		this.blz = bank.getBlz();
		
		id = KeyFactory.createKey(bank.getId(),
				Shop.class.getSimpleName(), name);
	}
	
	public ShopDTO getDTO() {
		return new ShopDTO(name, blz, url, accountNo);
	}
	
	public static Shop getShop(String name) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
		
			Extent<Shop> extent = pm.getExtent(Shop.class);
			Query query = pm.newQuery(extent);
			query.setFilter("name == param");
			query.declareParameters("String param");
			query.setUnique(true);
			return (Shop)query.execute(name);
		}
		finally {
			pm.close();
		}
	}
	
}
