package de.mrx.shared;

/**
 * Exception is thrown when an existing account is expected
 * Can be transferred to the GWT-Client
 *
 */
public class AccountNotExistException extends SCBException {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1216342195730542424L;

	private int accountNr;
	public int getAccountNr() {
		return accountNr;
	}

	public AccountNotExistException(){
		 
	 }
	 
public AccountNotExistException(String message){
		 super(message);
	 }

public AccountNotExistException(int accountNr){
	 this.accountNr=accountNr;
}

}
