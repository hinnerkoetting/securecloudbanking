package de.mrx.client;

import javax.jdo.PersistenceManager;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import de.mrx.server.PMF;
import de.mrx.server.RegisterServiceImpl;
import de.mrx.server.SCBIdentity;
import de.mrx.shared.EmailAdressNotAcceptedException;
import de.mrx.shared.UserAlreadyUsedException;

/**
 * Test cases for registration
 * @author Jan
 *
 */
public class RegisterTest {
	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	PersistenceManager pmf;
	
	@Before 
	public void setUp() {
		helper.setUp();
		pmf=PMF.get().getPersistenceManager();
	}
	
	 @After
	    public void tearDown() {
	        helper.tearDown();
	        pmf.close();
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
		String emailAdress="testtesttest@googlemail.com";
		SCBIdentityDTO id=getStandardDummyUser(emailAdress);
		RegisterServiceImpl regService=new RegisterServiceImpl();
		regService.register(id);
		PersistenceManager pmf=PMF.get().getPersistenceManager();
		SCBIdentity idInDB= SCBIdentity.getByEmail(pmf, emailAdress);
		Assert.assertEquals(emailAdress, idInDB.getEmail());		
	}
	
	
	 /**
	  * registration with a non googlemail account. Expected to fail
	  * @throws Exception
	  */
	@Test 
	public void simpleRegisterWithNotGoogleMail() throws Exception{		
		SCBIdentityDTO id=getStandardDummyUser("testtesttest@gmx.com");
		RegisterServiceImpl regService=new RegisterServiceImpl();
		try{
		regService.register(id);
		}
		catch (EmailAdressNotAcceptedException e){
			//this was expected
			return;
		}
		Assert.fail("A non Googlemail User was accepted");
	}
	
	/**
	  * register two times with same account. The second test should fail.
	  * @throws Exception
	  */
	@Test 
	public void registerTwiceWithSameEmail() throws Exception{		
		SCBIdentityDTO id=getStandardDummyUser("test@googlemail.com");	
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
