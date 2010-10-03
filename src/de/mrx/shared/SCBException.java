package de.mrx.shared;

import java.io.Serializable;

public class SCBException extends Exception implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3891185311480620990L;
	
	
	private String detailMessage;
	
	public SCBException(String message){
		super(message);
	
	}
	
	public SCBException(String message, Throwable t){
		super(message);
		detailMessage=t.getMessage();
	}
	
	public SCBException(String message, String detail){
		super(message);
		detailMessage=detail;
	}
	
	public SCBException(){
		
	}
	
	public String getDetailMessage() {
		return detailMessage;
	}
	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
