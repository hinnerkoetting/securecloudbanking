package de.mrx.server.secureBySCB;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.mrx.client.TransactionDTO;
//transaction commited by some shop
//which is not yet confirmed
@PersistenceCapable
public class Transaction {
	
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

	public Long getId() {
		return id.getId();
	}


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	//internal account
	//unknown at creation
	@Persistent
	String accountNo = "-1";
	
	@Persistent
	Double amount;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Key id;
	
	@Persistent
	Date date;
	

	
	public Transaction(String shopName, Double amount) {
		this.shopName = shopName;
		this.amount = amount;

		date = new Date();
		id = KeyFactory.createKey(Transaction.class.getSimpleName(),
				date.toString() + shopName);
	}
	
	
	public TransactionDTO getDTO() {
		return new TransactionDTO(shopName, amount);
	}
	
}
