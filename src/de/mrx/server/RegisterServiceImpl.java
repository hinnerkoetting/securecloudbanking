package de.mrx.server;



import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mrx.client.SCBIdentityDTO;
import de.mrx.client.RegisterService;

public class RegisterServiceImpl extends RemoteServiceServlet implements RegisterService{
	
	private final Logger log=Logger.getLogger(RegisterServiceImpl.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -1791669577170197531L;
	public void register(SCBIdentityDTO identity){	
		SCBIdentity id=new SCBIdentity(identity);
		//At the moment, directly activate the user
		id.setActivated(true);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(id);
        } finally {
            pm.close();
        }
        log.info("Registration received: "+id);
	}
}
