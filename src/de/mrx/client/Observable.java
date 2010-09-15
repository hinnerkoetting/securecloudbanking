package de.mrx.client;

/**
 * GWT don't offer this interface, so the relevant methods are copied
 * @see java.util.Observable
 */
public interface Observable {
	public void addObserver(Observer o) ;
	void notifyObservers(Integer eventType, Object parameter); 
}
