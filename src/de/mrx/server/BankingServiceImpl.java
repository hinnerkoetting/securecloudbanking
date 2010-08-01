package de.mrx.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.allen_sauer.gwt.log.client.Log;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mrx.client.AccountDTO;
import de.mrx.client.BankingService;
import de.mrx.client.IdentityDTO;
import de.mrx.client.MoneyTransferDTO;

@SuppressWarnings("serial")
public class BankingServiceImpl extends RemoteServiceServlet implements
		BankingService {
	
	Logger log=Logger.getLogger(BankingServiceImpl.class.getName());
	PersistenceManager pm = PMF.get().getPersistenceManager();

	public List<AccountDTO> getAccounts() {
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		PersistenceManager pm = PMF.get().getPersistenceManager();
	      String query=" SELECT FROM "+Account.class.getName()+" WHERE owner =='"+user.getEmail()+"'";
	      Log.info("geTAccounts Query: "+query);
	      List<Account> accounts=    (List<Account>)pm.newQuery(query).execute();
	      List<AccountDTO> accountDTOs=new ArrayList<AccountDTO>();
	      for (Account acc:accounts){
	    	  AccountDTO dto=acc.getDTO();
	    	  accountDTOs.add(dto);
	    	  log.info(dto.toString());
	      }
	      return accountDTOs;
	}

	public double getBalance(String accountNr) {
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		PersistenceManager pm = PMF.get().getPersistenceManager();
	      String query=" SELECT FROM "+Account.class.getName()+" WHERE owner =='"+user.getEmail()+"' && accountNr=='"+accountNr+"'";
	      log.info("geTAccounts Query: "+query);
	      List<Account> accounts=    (List<Account>)pm.newQuery(query).execute();
	      if(accounts.size()!=1){
	    	  throw new RuntimeException ("Anzahl Accounts mit Nr '"+accountNr+"' ist "+accounts.size() );
	      }
	      return accounts.get(0).getBalance();
	}

	public List<MoneyTransferDTO> getTransaction(String accountNr) {
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		PersistenceManager pm = PMF.get().getPersistenceManager();
	      String query=" SELECT FROM "+MoneyTransfer.class.getName() +" WHERE senderAccountNr =='"+accountNr+"'";
	    List<MoneyTransfer> moneyTransfers=(List<MoneyTransfer>) pm.newQuery(query).execute();
	    List<MoneyTransferDTO> mtDTOs=new ArrayList<MoneyTransferDTO>();
	      for (MoneyTransfer mtransfer:moneyTransfers){
	    	  MoneyTransferDTO dto=mtransfer.getDTO();
	    	  mtDTOs.add(dto);
	    	  log.info(dto.toString());
	      }
	      return mtDTOs;
	}

	public IdentityDTO login(String requestUri) {
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    IdentityDTO identityInfo;

	    if (user != null) {
	      
	      
	      log.fine("Login: "+user);
	      
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

	public void sendMoney(String senderAccountNr, String blz, String accountNr, double amount) {
		String accQuery=" SELECT FROM "+Account.class.getName()+" WHERE accountNr=='"+senderAccountNr+"'";
		Log.info(accQuery);
		List<Account> accounts= (List<Account>) pm.newQuery(accQuery).execute();
		if (accounts==null){
			throw new RuntimeException("No account. Bug!");
		}
		if (accounts.size()!=1){
			throw new RuntimeException("There must be exactly one account with the number: '"+senderAccountNr+"'. Instead there is : "+accounts.size());
		}
		Account senderAccount=accounts.get(0);
		try{
			pm.currentTransaction().begin();
		MoneyTransfer transfer=new MoneyTransfer(amount,senderAccount, accountNr, blz);
		senderAccount.addMoneyTransfer(transfer);
		log.warning("Sender Account bevor:"+senderAccount.toString());
//		pm.makePersistent(transfer);
		
		senderAccount.setBalance(senderAccount.getBalance()-amount);
			pm.currentTransaction().commit();
			log.warning("Sender Account after:"+senderAccount.toString());
			
		}
		catch (Exception e){
			e.printStackTrace();
			Log.error("error sending money ",e);			
		}
		
	}


}
