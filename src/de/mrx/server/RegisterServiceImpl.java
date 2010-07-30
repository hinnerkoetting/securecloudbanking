package de.mrx.server;



import javax.jdo.PersistenceManager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mrx.client.IdentityDTO;
import de.mrx.client.RegisterService;

public class RegisterServiceImpl extends RemoteServiceServlet implements RegisterService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1791669577170197531L;
	public void register(IdentityDTO identity){
		Identity id=new Identity(identity.getName());
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(id);
        } finally {
            pm.close();
        }
		GWT.log("Registration received");
	}
}
