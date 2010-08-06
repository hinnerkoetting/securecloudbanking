package de.mrx.server;



import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mrx.client.SCBIdentityDTO;
import de.mrx.client.RegisterService;
import de.mrx.shared.SCBException;

public class RegisterServiceImpl extends RemoteServiceServlet implements RegisterService{
	
	private final Logger log=Logger.getLogger(RegisterServiceImpl.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -1791669577170197531L;
	public void register(SCBIdentityDTO identity) throws SCBException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
		SCBIdentity id=new SCBIdentity(identity);
		//At the moment, directly activate the user
		id.setActivated(true);
		Properties props = new Properties();

		 Session session = Session.getDefaultInstance(props, null);

		Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("activation@securecloudbanking.appspotmail.com", "support@securecloudbanking.appspotmail.com"));
        msg.addRecipient(Message.RecipientType.TO,
                         new InternetAddress(id.getEmail(),id.getName()));
        msg.setSubject("Your Example.com account has been activated");
        msg.setText("Congratulations. You have activated your account at Secure Cloud Banking");
        Transport.send(msg);
        
        pm.makePersistent(id);
        log.info("Registration received: "+id);
        
		}
		catch (MessagingException e){
			throw new SCBException("Registration currently not possible",e);
		}
		
		catch (UnsupportedEncodingException e){
			throw new SCBException("Registration currently not possible",e);
		}
		
		  finally {
            pm.close();
        }
        
	}
}
