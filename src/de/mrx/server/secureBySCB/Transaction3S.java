package de.mrx.server.secureBySCB;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.mrx.client.Transaction3SDTO;
import de.mrx.server.PMF;
import de.mrx.server.Shop;
//transaction commited by some shop
//which is not yet confirmed
@PersistenceCapable
public class Transaction3S {
	
	@Persistent
	String shopName;
	
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Key getKey() {
		return key;
	}


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getID() {
		return myID;
	}
	//internal account
	//unknown at creation
	@Persistent
	String accountNo;
	
	@Persistent
	Double amount;
	
	@PrimaryKey
	@Persistent
	Key key;
	
	@Persistent
	Date date;
	
	@Persistent(valueStrategy = IdGeneratorStrategy.SEQUENCE)
	@Id
	Integer myID;
	
	public Transaction3S(Double amount, String accountNr, Shop shop) {
		this.shopName = shop.getName();
		this.amount = amount;
		this.accountNo = accountNr;
		date = new Date();
		key = KeyFactory.createKey(shop.getId(), Transaction3S.class.getSimpleName(),
				date.toString());
		
		
	}
	
	
	public Transaction3SDTO getDTO() {
		if (myID == null) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
//			pm.deletePersistent(this);
			return new Transaction3SDTO("----", -1.0, new Date(), "-1", -1);
		}
			
		return new Transaction3SDTO(shopName, amount, date, accountNo, myID);
	}
	
}
