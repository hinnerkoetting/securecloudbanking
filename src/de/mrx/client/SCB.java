package de.mrx.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SCB implements EntryPoint {
	
	private static final String PAGEID_CLOUDBANKING = "cloudbanking";
	private static final String PAGEID_CONTENT = "content";
	
	RegisterServiceAsync registerSvc;
	private BankingServiceAsync bankingService;
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	
	private CheckBox agbBox;;
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	MenuBar generalMenu = new MenuBar(true);
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	
	private Button loginButton = new Button("Login");
//	private Label status = new Label("Welcome to Cloud Banking");
	private VerticalPanel mainPanel = new VerticalPanel();
	
	
	private TextBox name;

	private Label nameLbl;
	
	private PasswordTextBox password;
	private Label passwordLbl;
	
	private Button registerButton = new Button("Register");

	
	private VerticalPanel registrationPanel;

	HTMLTable registrationTable;

	
	 private LoginInfo loginInfo = null;
	  private VerticalPanel loginPanel = new VerticalPanel();
	  private Label loginLabel = new Label("Please sign in to your Google Account to access the banking application.");
	  
	  private Label logoutLabel = new Label("Sign out gefaellig.");
	  private Anchor signInLink = new Anchor("Sign In");
	  private Anchor signOutLink = new Anchor("Sign Out");

	
	private void doShowNoService() {
		
		
		
			Window.alert("Our business will soon offer service. Please return! Soon!");
			
	
	
	}

	
	private void doOpenRegisterMenu() {
		RootPanel.get(PAGEID_CONTENT).add(getRegistrationPanel());
	}

	public VerticalPanel getRegistrationPanel() {
		if (registrationPanel!=null){
			return registrationPanel;
		}
		registrationPanel=new VerticalPanel();
		registrationTable=new Grid(3,2);
		nameLbl=new Label("Your name: ");
		passwordLbl=new Label("Your desired password: ");
		name=new TextBox();
		password=new PasswordTextBox();
		agbBox=new CheckBox("I accept the Terms Of Services");
		registrationTable.setWidget(0,0,nameLbl);
		registrationTable.setWidget(0,1,name);
		registrationTable.setWidget(1,0,passwordLbl);
		registrationTable.setWidget(1,1,password);
		registrationTable.setWidget(2,0,agbBox);
		registrationTable.setWidget(2,1,registerButton);
		
		registerButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				register();
				
			}
		});
		registrationPanel.add(registrationTable);
		
		return registrationPanel;
	}

protected void register() {
		String n=name.getText();
		String pw=password.getText();
		IdentityDTO id=new IdentityDTO(n);
		if (registerSvc==null){
			registerSvc=GWT.create(RegisterService.class);
		}
		
		
		AsyncCallback<Void> callback=new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				Window.alert("Shit happens");
				GWT.log(caught.getMessage());
				
				
			}

			public void onSuccess(Void result) {
				Window.alert("Register succesful");
				
				
			}
		};
		registerSvc.register(id, callback);
		// TODO Auto-generated method stub
		
	}


/**
 * This is the entry point method.
 */
public void onModuleLoad() {
	GWT.log("On Module Load");
	RootPanel r=RootPanel.get(PAGEID_CLOUDBANKING);
    if (r==null){
    	GWT.log("Root nicht gefunden: '"+PAGEID_CLOUDBANKING+"'");
    	return ;
    }
    GWT.log("Root gefunden");
    
    
    
    
        
//	    mainPanel.add(buttonPanel);
    
//	    buttonPanel.add(registerButton);
//	    buttonPanel.add(loginButton);
//	    
//	    
//	    registerButton.addClickHandler(new ClickHandler() {
//			
//			public void onClick(ClickEvent event) {
//				doOpenRegisterMenu();
//				
//			}
//
//		});

    Command cmdShowNoService = new Command() {
        public void execute() {
        	GWT.log("Show No Service starts");
        	doShowNoService();
          
        }
      };

      Command cmdRegister = new Command() {
          public void execute() {
        	 GWT.log("Registration starts");
          	doOpenRegisterMenu();
            
          }
        };
    
        Command cmdLogin = new Command() {
            public void execute() {
          	 GWT.log("Login starts");
            	doLogin();
              
            }

			
          };

    MenuBar bankingMenu = new MenuBar(true);
    bankingMenu.addItem("Private Banking", cmdLogin);
    bankingMenu.addItem("Institutional Banking", cmdShowNoService);
    bankingMenu.addItem("Depot ", cmdShowNoService);

    MenuBar informationMenu = new MenuBar(true);
    informationMenu.addItem("Partner", cmdShowNoService);
    informationMenu.addItem("Impressum", cmdShowNoService);
    informationMenu.addItem("About Secure Cloud Computing", cmdShowNoService);
    

    // Make a new menu bar, adding a few cascading menus to it.
    MenuBar menu = new MenuBar();
    menu.addItem("Register", cmdRegister);
    menu.addItem("Banking", bankingMenu);
    menu.addItem("Information", informationMenu);
    
    mainPanel.add(menu);
    
    r.add(mainPanel);


}

private void doLogin() {
	
	if (bankingService==null){
		bankingService=GWT.create(BankingService.class);
	}
	bankingService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	    	  error.printStackTrace();
	    	  
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	          GWT.log("Eingeloggt");
	          loadLogout();
	        } else {
	          loadLogin();
	        }
	      }
	    });

		
	}
	


private void loadLogin() {
    // Assemble login panel.
    signInLink.setHref(loginInfo.getLoginUrl());
    
    loginPanel.add(loginLabel);
    loginPanel.add(signInLink);
    RootPanel.get(PAGEID_CONTENT).add(loginPanel);
  }

private void loadLogout() {
    // Assemble logout panel.
    
    signOutLink.setHref(loginInfo.getLogoutUrl());    
    loginPanel.add(logoutLabel);
    loginPanel.add(signOutLink);
    RootPanel.get(PAGEID_CONTENT).add(loginPanel);
  }



}