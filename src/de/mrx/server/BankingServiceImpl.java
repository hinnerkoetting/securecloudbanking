package de.mrx.server;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mrx.client.AccountDTO;
import de.mrx.client.BankingService;
import de.mrx.client.LoginInfo;
import de.mrx.client.MoneyTransferDTO;

public class BankingServiceImpl extends RemoteServiceServlet implements
		BankingService {

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

	public LoginInfo login(String requestUri) {
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    LoginInfo loginInfo = new LoginInfo();

	    if (user != null) {
	      loginInfo.setLoggedIn(true);
	      loginInfo.setEmailAddress(user.getEmail());
	      loginInfo.setNickname(user.getNickname());
	      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
	      System.out.println("Login: "+loginInfo);
	      String query = "select from " + Identity.class.getName()+" WHERE email=='"+user.getEmail()+"'";
	      GWT.log("Query: "+query);
	      System.out.println("Query: "+query);
	      PersistenceManager pm = PMF.get().getPersistenceManager();
	      List<Identity> ids=(List<Identity>) pm.newQuery(query).execute();
	      System.out.println("RESULS  :"+ids.size());
	      for (Identity i:ids){
	    	  GWT.log("ID : "+i);
	    	  System.out.println("Gefundene ID"+i);
	      }
	      
	    } else {
	      loginInfo.setLoggedIn(false);
	      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
	    }
	    return loginInfo;
	  }


}
