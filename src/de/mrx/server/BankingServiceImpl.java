package de.mrx.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.apache.coyote.http11.filters.IdentityInputFilter;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mrx.client.AccountDTO;
import de.mrx.client.BankingService;
import de.mrx.client.IdentityDTO;
import de.mrx.client.LoginInfo;
import de.mrx.client.MoneyTransferDTO;

public class BankingServiceImpl extends RemoteServiceServlet implements
		BankingService {
	
	Logger log=Logger.getLogger(BankingServiceImpl.class.getName());
	

	public List<AccountDTO> getAccounts() {
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		PersistenceManager pm = PMF.get().getPersistenceManager();
	      String query=" SELECT FROM "+Account.class.getName()+" WHERE owner =='"+user.getEmail()+"'";
	      List<Account> accounts=    (List<Account>)pm.newQuery(query).execute();
	      List<AccountDTO> accountDTOs=new ArrayList<AccountDTO>();
	      for (Account acc:accounts){
	    	  AccountDTO dto=acc.getDTO();
	    	  accountDTOs.add(dto);
	    	  log.info(dto.toString());
	      }
	      return accountDTOs;
	}

	public double getBalance() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<MoneyTransferDTO> getTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	public IdentityDTO login(String requestUri) {
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    IdentityDTO identityInfo;

	    if (user != null) {
	      
	      
	      log.fine("Login: "+user);
	      String email=user.getEmail();
	      identityInfo = getIdentity(user);
	      
	      identityInfo.setLoggedIn(true);
	      identityInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
	      
	    } else {
	    	identityInfo=new IdentityDTO();
	      identityInfo.setLoggedIn(false);
	      identityInfo.setLoginUrl(userService.createLoginURL(requestUri));
	    }
	    return identityInfo;
	  }

	private IdentityDTO getIdentity(User user) {
		IdentityDTO identityInfo;
		String query = "select from " + Identity.class.getName()+" WHERE email=='"+user.getEmail()+"'";
	      log.info("Query: "+query);
	      System.out.println("Query: "+query);
	      PersistenceManager pm = PMF.get().getPersistenceManager();
	      List<Identity> ids=(List<Identity>) pm.newQuery(query).execute();
	      
	      System.out.println("RESULS  :"+ids.size());
	      log.info("RESULS  :"+ids.size());
	      for (Identity i:ids){
	    	  GWT.log("ID : "+i);
	    	  System.out.println("Gefundene ID"+i);
	      }
	      if (ids.size()==0){
	    	  identityInfo=new IdentityDTO();	    	  
		      identityInfo.setEmail(user.getEmail());
		      identityInfo.setNickName(user.getNickname());  
	      }
	      else if (ids.size()>1){
	    	  throw new RuntimeException("Für den User: '"+user.getEmail()+"' sind mehrere Identitaeten hinterlegt!");
	      }
	      else{
	    	  identityInfo=ids.get(0).getDTO();
	    	  
	      }
		return identityInfo;
	}

	public void openNewAccount() {
		 UserService userService = UserServiceFactory.getUserService();
		    User user = userService.getCurrentUser();
		    if (user==null){
		    	throw new RuntimeException("Nicht eingeloggt. Zugriff unterbunden");
		    }
		    IdentityDTO identityInfo=getIdentity(user);
		    Random rd=new Random();
		    int kontoNr=rd.nextInt(100000)+10000;
		    Account acc=new Account(identityInfo.getEmail(),""+kontoNr,5);
		    PersistenceManager pm = PMF.get().getPersistenceManager();
		    try {
	            pm.makePersistent(acc);
	        } finally {
	            pm.close();
	        }
	        log.info("account neu geoeffnet : "+acc);
		    
		
	}


}
