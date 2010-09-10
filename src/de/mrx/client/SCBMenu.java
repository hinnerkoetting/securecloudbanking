package de.mrx.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

public class SCBMenu extends Composite implements Observable{

	interface MyUiBinder extends UiBinder<Widget, SCBMenu> {
	}
	
	public final static Integer EVENT_SHOW_REGISTRATION_MENU=10; 
	
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	private SCBConstants constants = GWT.create(SCBConstants.class);
	
	List<Observer>  observers=new ArrayList<Observer>();
	
	@UiField
	MenuItem menuItemRegister;
	
	@UiField
	MenuItem menuAbout;
	
	@UiField
	MenuItem menuImpressum;
	
	@UiField
	MenuItem menuItemGerman;
	
	public MenuItem getMenuItemRegister() {
		return menuItemRegister;
	}

	@UiField 
	MenuItem menuItemEnglish;
	
	@UiField 
	MenuItem menuUserInfo;
	
	public MenuItem getMenuUserInfo() {
		return menuUserInfo;
	}

	public SCBMenu(){
		initWidget(uiBinder.createAndBindUi(this));
		menuAbout.setCommand(cmdShowInfoSCB);
		menuImpressum.setCommand(cmdShowImpressum);
		menuItemGerman.setCommand(cmdChangeToGerman);
		menuItemEnglish.setCommand(cmdChangeToEnglish);
		menuItemRegister.setCommand(cmdRegister);
		
	}
	
	
	
	
	
	
	
	Command cmdShowImpressum = new Command() {
		public void execute() {
			GWT.log("Impressum follows");
			Window.alert(constants.impressumText());

		}
	};

	Command cmdShowInfoSCB = new Command() {
		public void execute() {
			GWT.log("SCB Info");
			Window.alert(constants.aboutText());

		}
	};
	
	Command cmdRegister = new Command() {
		public void execute() {
			GWT.log("Registration starts");
			notifyObservers(EVENT_SHOW_REGISTRATION_MENU);
			

		}
	};
	
	
	Command cmdChangeToEnglish = new Command() {
		public void execute() {
			SCB.changeToLocalisedVersion("en");

		}
	};
	Command cmdChangeToGerman = new Command() {
		public void execute() {
			SCB.changeToLocalisedVersion("de");

		}
	};

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void notifyObservers(Object arg) {
		for (Observer o: observers){
			o.update(this,arg);
		}		
	}

}
