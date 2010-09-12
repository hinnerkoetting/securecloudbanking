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
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;
import de.mrx.shared.SCBData;


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
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	public AdminExternalBanks(Admin admin) {
		this.adminpage = admin;
		
		initWidget(uiBinder.createAndBindUi(this));
		title.setText(constants.externalBanks());
		addBank.setText(constants.addBank());
	}
	
	public void setBanks(List<BankDTO> banks) {
		final Admin admin = adminpage;
		final int namePos = 0;
		final int blzPos =  1;
		final int viewPos = 2;
		final int editPos = 3;
		final int deletePos = 4;
		
		//add header
		
		table.setWidget(0, namePos, new Label(constants.name()));
		table.setWidget(0, blzPos, new Label(constants.blz()));
		table.setWidget(0, viewPos, new Label(constants.viewAccounts()));
		table.setWidget(0, editPos, new Label(constants.editDetails()));
		table.setWidget(0, deletePos, new Label(constants.delete()));
		TableStyler.setTableStyle(table);
		
		int row= 1;
		for (final BankDTO bank: banks) {
			if (bank.getName().equals(SCBData.SCB_NAME))
				continue;
			//name
			table.setWidget(row, namePos, new Label(bank.getName()));
			
			//blz
			table.setWidget(row, blzPos, new Label(bank.getBlz()));
			
			//view
			Button viewButton = new Button(constants.display());
			
			viewButton.addClickHandler(new ClickHandler() {				
				@Override
				public void onClick(ClickEvent event) {
					admin.showExternalAccounts(bank.getBlz());					
				}
			});
			table.setWidget(row, viewPos, viewButton);
			
			//edit
			Button editButton = new Button(constants.edit());
			editButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					adminpage.showEditBankDetails(bank.getName(), bank.getBlz());
					
				}
			});
			table.setWidget(row, editPos, editButton);
			
			
			//delete
			Button deleteButton = new Button(constants.delete());
			deleteButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (Window.confirm(constants.deleteBankConfirm())) {
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
