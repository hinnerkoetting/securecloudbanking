package de.mrx.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import de.mrx.client.BankDTO;

@PersistenceCapable
public class Bank {

	@Persistent
	private String blz;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;

	@Persistent
	private String name;

	public Bank(){
		
	}

	public Bank(BankDTO dto){
			setBlz(dto.getBlz());
			setName(dto.getName());
		}

	public String getBlz() {
		return blz;
	}

	public BankDTO getDTO(){
		BankDTO dto=new BankDTO();
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
