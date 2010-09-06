package de.mrx.client.admin.forms;



import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.BankDTO;
import de.mrx.client.TableStyler;
import de.mrx.client.admin.Admin;


public class AdminExternalBanks extends Composite {

	private static AdminExternalBanksUiBinder uiBinder = GWT
			.create(AdminExternalBanksUiBinder.class);

	interface AdminExternalBanksUiBinder extends
			UiBinder<Widget, AdminExternalBanks> {
	}

	@UiField
	Label title;
	
	@UiField
	FlexTable table;
	
	Admin adminpage;
	
	public AdminExternalBanks(Admin admin) {
		this.adminpage = admin;
		
		initWidget(uiBinder.createAndBindUi(this));
		title.setText("Manage external Banks");
	}
	
	public void setBanks(List<BankDTO> banks) {
		
		final int namePos = 0;
		final int blzPos =  1;
		final int editPos = 2;
		
		//add header
		
		table.setWidget(0, namePos, new Label("Name"));
		table.setWidget(0, blzPos, new Label("BLZ"));
		table.setWidget(0, editPos, new Label("Edit Details"));
		TableStyler.setTableStyle(table);
		
		int row= 1;
		for (BankDTO bank: banks) {
			table.setWidget(row, namePos, new Label(bank.getName()));
			table.setWidget(row, blzPos, new Label(bank.getBlz()));
			table.setWidget(row, editPos, new Button("Edit"));
			row++;
		}
	}

}
