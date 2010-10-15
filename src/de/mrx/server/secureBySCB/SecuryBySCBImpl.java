package de.mrx.server.secureBySCB;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import de.mrx.client.Transaction3SDTO;
import de.mrx.client.secureBySCB.SecuryBySCBService;
import de.mrx.server.BankServiceImpl;
import de.mrx.server.GeneralAccount;
import de.mrx.server.InternalSCBAccount;
import de.mrx.server.MoneyTransfer;
import de.mrx.server.PMF;
import de.mrx.server.SCBIdentity;
import de.mrx.server.Shop;


public class SecuryBySCBImpl extends BankServiceImpl implements SecuryBySCBService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(SecuryBySCBImpl.class.getName());
	
	
	
	
	private Transaction3S getTransaction(PersistenceManager pm, Integer id) {
		
		Extent<Transaction3S> extent = pm.getExtent(Transaction3S.class);
		Query query = pm.newQuery(extent);
	
		@SuppressWarnings("unchecked")
		List<Transaction3S> transactions = (List<Transaction3S>)query.execute();
		


 		for (Transaction3S trans: transactions) {
			

			if (id.intValue() == trans.getID()) {
				return trans;
			}
		}
		
 		
		return null;
	}

	@Override
	public Boolean confirmTransaction(Integer id) {
		log.setLevel(Level.INFO);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Transaction3S trans = getTransaction(pm, id);
			
			if (trans == null)
				return false;
			Shop shop = Shop.getShop(trans.getShopName());
			if (shop == null)
				return false;
			
			UserService userService = UserServiceFactory.getUserService();
			SCBIdentity identity = SCBIdentity.getIdentity(pm, userService.getCurrentUser());
			InternalSCBAccount account = InternalSCBAccount.getOwnByEmail(pm, identity.getEmail());
			if (!account.getAccountNr().equals(trans.accountNo))
				return false;
			
			GeneralAccount recAcc = GeneralAccount.getAccount(pm, shop.getAccountNo(), shop.getBlz());
			MoneyTransfer transfer = new MoneyTransfer(pm, account,
					recAcc, trans.getAmount(),recAcc.getOwner(),"test");
			transferMoney(pm, account, recAcc, transfer,  trans.getAmount(), "test");
			 URL url;
			try {
				
				url = new URL(shop.getUrl() + "/confirm_transfer.php?id=" + trans.getID());
				log.log(Level.INFO, "Calling URL: " + url);
				URLConnection conn = url.openConnection();
				conn.connect();
				pm.deletePersistent(trans);
				log.log(Level.INFO, conn.getContent().toString());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
	
			return true;
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	
	public Transaction3SDTO getTransactionData(Integer id) {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Transaction3S result = getTransaction(pm, id);
		if (result == null)
			return null;
		
		return result.getDTO();
	}


}
