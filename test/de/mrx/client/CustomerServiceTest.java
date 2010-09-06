package de.mrx.client;

import java.util.List;

import javax.jdo.PersistenceManager;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import de.mrx.server.CustomerServiceImpl;
import de.mrx.server.PMF;
import de.mrx.server.RegisterServiceImpl;

public class CustomerServiceTest {
	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	PersistenceManager pmf;
	String emailAdress="JohnDoegooglemail.com";
	
	@Before 
	public void setUp() {
		helper.setUp();
		helper.setEnvIsLoggedIn(true);
		helper.setEnvEmail(emailAdress);
		helper.setEnvAuthDomain("testDomain");
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
	 
	 @Test
	 public void openAccountTest()throws Exception{
		 
			SCBIdentityDTO id=getStandardDummyUser(emailAdress);
			RegisterServiceImpl regService=new RegisterServiceImpl();
			regService.register(id);
			pmf=PMF.get().getPersistenceManager();
			CustomerServiceImpl customerService=new CustomerServiceImpl();
			customerService.openNewAccount();
			List<AccountDTO> accounts=customerService.getAccounts();
			Assert.assertEquals(1,accounts.size());
	 }
}
