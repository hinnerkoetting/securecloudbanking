package de.mrx.client.admin.forms;



import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.BankDTO;
import de.mrx.client.TableStyler;
import de.mrx.client.admin.Admin;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;


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
		final int deletePos = 4;
		
		//add header
		
		table.setWidget(0, namePos, new Label("Name"));
		table.setWidget(0, blzPos, new Label("BLZ"));
		table.setWidget(0, viewPos, new Label("View accounts"));
		table.setWidget(0, editPos, new Label("Edit details"));
		table.setWidget(0, deletePos, new Label("Delete"));
		TableStyler.setTableStyle(table);
		
		int row= 1;
		for (final BankDTO bank: banks) {
			if (bank.getName().equals("Secure Cloud Bank"))
				continue;
			//name
			table.setWidget(row, namePos, new Label(bank.getName()));
			
			//blz
			table.setWidget(row, blzPos, new Label(bank.getBlz()));
			
			//view
			Button viewButton = new Button("View");
			
			viewButton.addClickHandler(new ClickHandler() {				
				@Override
				public void onClick(ClickEvent event) {
					admin.showExternalAccounts(bank.getBlz());					
				}
			});
			table.setWidget(row, viewPos, viewButton);
			
			//edit
			Button editButton = new Button("Edit");
			editButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					adminpage.showEditBankDetails(bank.getName(), bank.getBlz());
					
				}
			});
			table.setWidget(row, editPos, editButton);
			
			
			//delete
			Button deleteButton = new Button("Delete");
			deleteButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (Window.confirm("Are you sure? This will delete this bank and all its accounts!")) {
						AdminServiceAsync adminService = GWT.create(AdminService.class);
						adminService.deleteBank(bank.getBlz(), new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								Window.alert(result);
								adminpage.showExternalBanks();
							}
							
							@Override
							public void onFailure(Throwable caught) {
								GWT.log(caught.toString());
								
							}
						});
					}
				}
				
			});
			table.setWidget(row, deletePos, deleteButton);
			
			row++;
		}
	}
	
	@UiHandler("addBank")
	public void onClickAddBank(ClickEvent event) {
		adminpage.showNewBank();
	}

}
