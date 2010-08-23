package de.mrx.shared;

public class WrongTANException extends SCBException{
	int trials;
	
	public int getTrials() {
		return trials;
	}

	public void setTrials(int trials) {
		this.trials = trials;
	}

	public WrongTANException(int trial){
		super("Wrong TAN");
		setTrials(trial);
		
		
	}

}
