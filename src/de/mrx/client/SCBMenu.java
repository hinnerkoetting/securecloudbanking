package de.mrx.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SCBMenu extends Composite{

	interface MyUiBinder extends UiBinder<Widget, SCBMenu> {
	}
	
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	public SCBMenu(){
		initWidget(uiBinder.createAndBindUi(this));
		
		
	}
	
}
