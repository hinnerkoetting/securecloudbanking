package de.mrx.client.admin.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Overview extends Composite {

	private static OverviewUiBinder uiBinder = GWT
			.create(OverviewUiBinder.class);

	interface OverviewUiBinder extends UiBinder<Widget, Overview> {
	}

	@UiField
	Button button;
	
	@UiField
	Label test;

	public Overview(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(firstName);
		test.setText("bla");
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		Window.alert("Hello!");
	}

}
