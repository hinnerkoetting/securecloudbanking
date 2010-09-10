package de.mrx.client.admin.forms;



import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
	
	@UiField
	Button addBank;
	
	Admin adminpage;
	
	public AdminExternalBanks(Admin admin) {
		this.adminpage = admin;
		
		initWidget(uiBinder.createAndBindUi(this));
		title.setText("External Banks");
		addBank.setText("Add Bank");
	}
	
	public void setBanks(List<BankDTO> banks) {
		final Admin admin = adminpage;
		final int namePos = 0;
		final int blzPos =  1;
		final int viewPos = 2;
		final int editPos = 3;
		
		//add header
		
		table.setWidget(0, namePos, new Label("Name"));
		table.setWidget(0, blzPos, new Label("BLZ"));
		table.setWidget(0, viewPos, new Label("View accounts"));
		table.setWidget(0, editPos, new Label("Edit details"));
		TableStyler.setTableStyle(table);
		
		int row= 1;
		for (final BankDTO bank: banks) {
			if (bank.getName().equals("Secure Cloud Bank"))
				continue;
			table.setWidget(row, namePos, new Label(bank.getName()));
			table.setWidget(row, blzPos, new Label(bank.getBlz()));
			Button viewButton = new Button("View");
			
			viewButton.addClickHandler(new ClickHandler() {				
				@Override
				public void onClick(ClickEvent event) {
					admin.showExternalAccounts(bank.getBlz());					
				}
			});
			table.setWidget(row, viewPos, viewButton);
			table.setWidget(row, editPos, new Button("Edit"));
			row++;
		}
	}
	
	@UiHandler("addBank")
	public void onClickAddBank(ClickEvent event) {
		adminpage.showNewBank();
	}

}
