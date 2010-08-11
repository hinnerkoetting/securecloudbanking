package de.mrx.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import de.mrx.client.BankDTO;

@PersistenceCapable
public class Bank {

	private final static Logger log = Logger.getLogger(Bank.class.getName());

	@Persistent	
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bank other = (Bank) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Bank [blz=" + blz + ", id=" + id + ", accounts=" + accounts
				+ ", name=" + name + "]";
	}

	private List<Key> accounts = new ArrayList<Key>();

	public static Bank getByBLZ(PersistenceManager pm, String blz) {
		Extent e = pm.getExtent(Bank.class);
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
		log.info("Account erg�nzt bei Bank: " + acc.toString());
		accounts.add(acc.getId());
	}

	public List<Key> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Key> accounts) {
		this.accounts = accounts;
	}

	@Persistent
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
