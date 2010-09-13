package de.mrx.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

import com.google.appengine.api.datastore.Key;

import de.mrx.client.BankDTO;

/**
 * represents a bank (SCB as well as external banks). A bank contains accounts.
 *
 */
@PersistenceCapable
public class Bank {

	private final static Logger log = Logger.getLogger(Bank.class.getName());

	public static final String SCB_BLZ = "1502222";
	public static final String SCB_NAME="Secure Cloud Bank";
	
	
	@Persistent	
	@Unique
	private String blz;

	@PrimaryKey
	private Key id;

	public void setId(Key id) {
		this.id = id;
	}

	public Key getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blz == null) ? 0 : blz.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Bank otherBank) {
		return (this.getBlz() == otherBank.getBlz() && this.getName() == otherBank.getName());
	}

	@Override
	public String toString() {
		return "Bank [blz=" + blz + ", id=" + id + ", accounts=" + accounts
				+ ", name=" + name + "]";
	}

	private List<Key> accounts = new ArrayList<Key>();

	public static Bank getByBLZ(PersistenceManager pm, String blz) {
		Extent<Bank> e = pm.getExtent(Bank.class);
		Query query = pm.newQuery(e);
		query.setFilter("blz == blzParam");
		query.declareParameters("String blzParam");
		query.setUnique(true);
		Bank result = (Bank) query.execute(blz);
		return result;
	}

	public void addAccount(GeneralAccount acc) {
		if (acc == null) {
			throw new RuntimeException("Keine Null Accounts");
		}
		log.info("Account ergänzt bei Bank: " + acc.toString());
		accounts.add(acc.getId());
	}

	public List<Key> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Key> accounts) {
		this.accounts = accounts;
	}

	@Persistent
	@Unique
	private String name;

	public Bank(String blz, String name) {
		this.blz = blz;
		this.name = name;
	}

	public Bank() {

	}

	public Bank(BankDTO dto) {
		setBlz(dto.getBlz());
		setName(dto.getName());
	}

	public String getBlz() {
		return blz;
	}

	public BankDTO getDTO() {
		BankDTO dto = new BankDTO();
		dto.setBlz(getBlz());
		dto.setName(getName());
		return dto;
	}

	public String getName() {
		return name;
	}

	public void setBlz(String blz) {
		this.blz = blz;
	}

	public void setName(String name) {
		this.name = name;
	}
}
