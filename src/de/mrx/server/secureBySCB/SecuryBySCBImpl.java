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

import de.mrx.client.TransactionDTO;
import de.mrx.client.secureBySCB.SecuryBySCBService;
import de.mrx.server.BankServiceImpl;
import de.mrx.server.GeneralAccount;
import de.mrx.server.InternalSCBAccount;
import de.mrx.server.MoneyTransfer;
import de.mrx.server.PMF;
import de.mrx.server.SCBIdentity;


public class SecuryBySCBImpl extends BankServiceImpl implements SecuryBySCBService{

	Logger log = Logger.getLogger(SecuryBySCBImpl.class.getName());
	@Override
	public Long addTransaction(String shop, Double amount) {
				log.log(Level.INFO, "test");
		
		Transaction t = new Transaction(shop, amount);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		deleteAllTransactions(pm);
		pm.makePersistent(t);
		
		//return id for confirmation
		return t.getId();
		
	}
	
	private Transaction getTransaction(PersistenceManager pm, Long id) {
		
		Extent<Transaction> extent = pm.getExtent(Transaction.class);
		Query query = pm.newQuery(extent);
	
		List<Transaction> transactions = (List<Transaction>)query.execute();
		
 		Transaction result = null;

 		for (Transaction trans: transactions) {
			
 			return trans;
//			if (trans.getId().equals(id)) {
//				result = trans;
//				break;
//			}
		}
		
 		
		return result;
	}
	
	private void deleteAllTransactions(PersistenceManager pm) {
		Query query = pm.newQuery(Transaction.class);
		
		query.deletePersistentAll(query);
		
	}
	@Override
	public Boolean confirmTransaction(Long id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction trans = getTransaction(pm, id);
		if (trans == null)
			return false;
		//we know that client that requests this owns this transaction
		// set his accountno to this transaction
		UserService userService = UserServiceFactory.getUserService();
		SCBIdentity identity = SCBIdentity.getIdentity(pm, userService.getCurrentUser());
		InternalSCBAccount account = InternalSCBAccount.getOwnByEmail(pm, identity.getEmail());
		
		
//		GeneralAccount recAcc = GeneralAccount.getAccount(pm, "65", "999666999");
//		MoneyTransfer transfer = new MoneyTransfer(pm, account,
//				recAcc, trans.getAmount(),recAcc.getOwner(),"test");
//		transferMoney(pm, account, recAcc, transfer,  trans.getAmount(), "test");
		 URL url;
		try {
			url = new URL("http://www.stud.uni-karlsruhe.de/~urbhx/phpshop/confirm_transfer.php?id=" + trans.getId());
			URLConnection conn = url.openConnection();
			conn.connect();
			conn.getContent();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		pm.deletePersistent(trans);
		return true;
	}
	
	@Override
	
	public TransactionDTO getTransactionData(Long id) {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Transaction result = getTransaction(pm, id);
		if (result == null)
			return null;
		
		return result.getDTO();
	}


}
