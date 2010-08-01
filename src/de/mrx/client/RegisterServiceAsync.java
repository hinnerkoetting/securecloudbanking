package de.mrx.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RegisterServiceAsync {

	void register(SCBIdentityDTO identity, AsyncCallback<Void> callback);

}
