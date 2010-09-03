package de.mrx.shared;

/**
 * Exception is thrown when an existing account is expected
 * Can be transferred to the GWT-Client
 *
 */
public class AccountNotExistException extends SCBException {
	
	 public AccountNotExistException(){
		 
	 }
	 
public AccountNotExistException(String message){
		 super(message);
	 }

}
