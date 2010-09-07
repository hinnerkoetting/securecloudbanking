package de.mrx.client.register;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Anchor;
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
		
		RootPanel r = RootPanel.get(SCB.PAGEID_CONTENT);
		Anchor a=new Anchor("LinkTest");
		RegistrationForm regForm=new RegistrationForm(a);		
		r.add(regForm);

	}

}
