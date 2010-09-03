package de.mrx.client.register;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import de.mrx.client.SCB;

/**
 * Registration page realised with UI-Binder technology
 * @author Jan
 *
 */
public class Registration implements EntryPoint {

	@Override
	public void onModuleLoad() {
		System.out.println("START Registration");
		RootPanel r = RootPanel.get(SCB.PAGEID_CONTENT);
		RegistrationForm regForm=new RegistrationForm();
		regForm.getFirstName().setText("Test");
		r.add(regForm);

	}

}
