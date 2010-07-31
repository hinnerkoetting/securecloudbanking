package de.mrx.server;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

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
		// TODO Auto-generated method stub
		return null;
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
	    IdentityDTO identityInfo;// = new IdentityDTO();

	    if (user != null) {
	      
	      
	      GWT.log("Login: "+user);
	      String query = "select from " + Identity.class.getName()+" WHERE email=='"+user.getEmail()+"'";
	      GWT.log("Query: "+query);
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
	      
	      identityInfo.setLoggedIn(true);
	      identityInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
	      
	    } else {
	    	identityInfo=new IdentityDTO();
	      identityInfo.setLoggedIn(false);
	      identityInfo.setLoginUrl(userService.createLoginURL(requestUri));
	    }
	    return identityInfo;
	  }


}
