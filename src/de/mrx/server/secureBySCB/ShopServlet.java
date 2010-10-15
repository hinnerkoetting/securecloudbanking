package de.mrx.server.secureBySCB;
import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.mrx.server.PMF;
import de.mrx.server.Shop;


public class ShopServlet extends HttpServlet {

	private class ShopNotFoundEception extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
		resp.setContentType("text/plain");  
			String action = req.getParameter("action");
			
			if (action.equals("addTransaction")) {
				String shopName = req.getParameter("shopName");
				Double amount;
				try {
					amount = new Double(req.getParameter("amount"));
				}
				catch (NumberFormatException e) {
					resp.getWriter().println("ERROR: Wrong format for amount");
					return;
				}
				String accountNr = req.getParameter("accountNr");
				int result;
				try {
					result = addTransaction(shopName, amount, accountNr);
				}
				catch (ShopNotFoundEception n) {
					resp.getWriter().println("Shop not found. Create it first in adminmenu.");
					return;
				}
				
				resp.getWriter().println("SUCCESS: Added transaction for shop: " + shopName + "/accountNr: " + accountNr + "/amount:" + amount+ " RESULT::::" + result);
				
				return;
			}
		  
			resp.getWriter().println("Unknown action");
	  }
	  
	  public int addTransaction(String shopName, Double amount, String accountNr) throws ShopNotFoundEception{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				Shop shop = Shop.getShop(shopName);
				if (shop == null)
					throw(new ShopNotFoundEception());
				Transaction3S t = new Transaction3S(amount, accountNr, shop);
			
				pm.makePersistent(t);
				
				//return id for confirmation
				return t.getID();
			}
			finally {
				pm.close();
			}
		}

}
