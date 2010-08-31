package de.mrx.shared;

public class AccountNotExistException extends SCBException {
	
	 public AccountNotExistException(){
		 
	 }
	 
public AccountNotExistException(String message){
		 super(message);
	 }

}
