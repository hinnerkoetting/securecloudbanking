package de.mrx.client;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mrx.shared.SCBException;


public class SCBTest extends GWTTestCase {
	 private static RegisterService rpcService =
			(RegisterService) SyncProxy.newProxyInstance(RegisterService.class,"http://localhost:8888/scb", "banking");

	@Override
	public String getModuleName() {
 
		return "de.mrx.SCB";
	}
	
	public void testSimple2() {
		
		
	};
	
	public void testSimple() throws SCBException {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				fail("registration fails "+caught);
			}
			
			public void onSuccess(Void result) {
				finishTest();
				
				System.out.println("Bestanden!");
			}
				
			
	};
	SCBIdentityDTO id = new SCBIdentityDTO("Mustermann");
	id.setStreet("Example Street");
	id.setCity("Nürnberg");
	id.setPlz("45312");
	id.setEmail("test@example.com");
	
	RegisterServiceAsync registerSvc = GWT.create(RegisterService.class);
//	registerSvc.register(id,callback);
//	delayTestFinish(1500);
	rpcService.register(id);
	}


}
