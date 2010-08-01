package de.mrx.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("register")
public interface RegisterService extends RemoteService{
	public void register(SCBIdentityDTO identity);

}
