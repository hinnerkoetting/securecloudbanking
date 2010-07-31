package de.mrx.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RegisterServiceAsync {

	void register(IdentityDTO identity, AsyncCallback<Void> callback);

}
