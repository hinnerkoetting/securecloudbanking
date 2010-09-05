package de.mrx.client;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gwt.core.client.GWT;

import de.mrx.server.CustomerServiceImpl;
import de.mrx.server.RegisterServiceImpl;
import de.mrx.shared.UserAlreadyUsedException;

/**
 * Test cases for registration
 * @author Jan
 *
 */
public class RegisterTest {
	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
	
	@Before 
	public void setUp() {
		helper.setUp();
	}
	
	 @After
	    public void tearDown() {
	        helper.tearDown();
	    }
	
	 private SCBIdentityDTO getStandardDummyUser(String email){
		 SCBIdentityDTO id=new SCBIdentityDTO("Doe",email);
			
			id.setFirstName("John"); 
			id.setCity("Bremen");
			id.setHouseNr("10");
			id.setStreet("Teststreet");			
			return id;
	 }
	 
	 /**
	  * standard registration with a googlemail account. Expected to create a user
	  * @throws Exception
	  */
	@Test 
	public void simpleRegisterWithGoogleMail() throws Exception{
		SCBIdentityDTO id=getStandardDummyUser("testtesttest@googlemail.com");
		RegisterServiceImpl regService=new RegisterServiceImpl();
		regService.register(id);
	}
	
	
	 /**
	  * standard registration with a googlemail account. Expected to create a user, even though the user can not log in later
	  * @throws Exception
	  */
	@Test 
	public void simpleRegisterWithNotGoogleMail() throws Exception{		
		SCBIdentityDTO id=getStandardDummyUser("testtesttest@gmx.com");
		RegisterServiceImpl regService=new RegisterServiceImpl();
		regService.register(id);
	}
	
	/**
	  * register two times with same account. The second test should fail.
	  * @throws Exception
	  */
	@Test 
	public void registerTwiceWithSameEmail() throws Exception{		
		SCBIdentityDTO id=getStandardDummyUser("testtesttest@gmx.com");	
		RegisterServiceImpl regService=new RegisterServiceImpl();
		regService.register(id);
		try{
			regService.register(id);
		}
		catch (UserAlreadyUsedException e){
			//I want it to fail here
			return;
		}
		Assert.fail("Using same email twice should throw Exception");
	}
}
