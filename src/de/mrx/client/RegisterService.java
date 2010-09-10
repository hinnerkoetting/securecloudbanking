package de.mrx.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.mrx.shared.SCBException;

/**
 * Service for registration of new users
 * activate is not yet fully implemented
 *
 */
@RemoteServiceRelativePath("register")
public interface RegisterService extends RemoteService{
	
	/**
	 * registers a new user. Sends confirmation link
	 * @param identity
	 * @throws SCBException
	 */
	public void register(SCBIdentityDTO identity) throws SCBException;
	
	/*
	 * entering the confirmation code from the registration email
	 */
	public void activate(String accountNr, String invitationCode) throws SCBException;

}
