package de.mrx.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mrx.shared.SCBException;

/**
 * asnychron interface for registration service
 * @see de.mrx.client.RegisterServiceAsync
 *
 */
public interface RegisterServiceAsync {

	/**
	 * registers a new user. Sends confirmation link
	 * @param identity
	 * @throws SCBException
	 */
	public void register(SCBIdentityDTO identity, AsyncCallback<Void> callback);

	/*
	 * entering the confirmation code from the registration email
	 */
	void activate(String accountNr, String invitationCode,
			AsyncCallback<Void> callback);

}
