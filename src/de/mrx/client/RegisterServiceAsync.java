package de.mrx.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RegisterServiceAsync {

	public void register(SCBIdentityDTO identity, AsyncCallback<Void> callback);

	void activate(String accountNr, String invitationCode,
			AsyncCallback<Void> callback);

}
