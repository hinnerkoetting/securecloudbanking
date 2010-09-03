package de.mrx.shared;

/**
 * Exception used when customers uses a wrong TAN
 */
public class WrongTANException extends SCBException{
	int trials;
	
	public int getTrials() {
		return trials;
	}

	public void setTrials(int trials) {
		this.trials = trials;
	}

	public WrongTANException(){
		
	}
	
	public WrongTANException(int trial){
		super("Wrong TAN");
		setTrials(trial);
		
		
	}

}
