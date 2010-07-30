package de.mrx.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mrx.server.Identity;

public interface RegisterServiceAsync {

	void register(IdentityDTO identity, AsyncCallback<Void> callback);

}
