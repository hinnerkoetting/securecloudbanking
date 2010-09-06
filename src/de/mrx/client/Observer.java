package de.mrx.client;

/**
 * GWT don't offer this interface, so the relevant methods are copied
 * @see java.util.Observer
 */
public interface Observer {
	public void update(Observable o, Object arg); 
}
