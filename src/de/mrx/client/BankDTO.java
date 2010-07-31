package de.mrx.client;

import java.io.Serializable;

public class BankDTO implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6828491949709673590L;
	private String blz;
	public String getBlz() {
		return blz;
	}
	public void setBlz(String blz) {
		this.blz = blz;
	}
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return "BankDTO [" + (blz != null ? "blz=" + blz + ", " : "")
				+ (name != null ? "name=" + name : "") + "]";
	}
	public void setName(String name) {
		this.name = name;
	}
	private String name;
}
