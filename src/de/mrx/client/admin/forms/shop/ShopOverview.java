package de.mrx.client.admin.forms.shop;


import java.util.ArrayList;
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

import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.ShopDTO;
import de.mrx.client.TableStyler;
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class ShopOverview extends Composite implements Observable{

	private static SecuredBySCBUiBinder uiBinder = GWT
			.create(SecuredBySCBUiBinder.class);

	interface SecuredBySCBUiBinder extends UiBinder<Widget, ShopOverview> {
	}

	@UiField
	Label title;
	
	@UiField
	FlexTable table;
	
	@UiField
	Button addShop;

	@UiField
	Button deleteTransactions;
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	final int namePos = 0;
	final int urlPos = 1;
	final int accountNrPos = 2;
	final int blzPos = 3;
	final int editPos = 4;
	
	public static final int ADD_SHOP = 721;
	public static final int PROPERTIES_SHOP = 722;
	
	private List<Observer> observers = new ArrayList<Observer>();
	
	public ShopOverview() {
		initWidget(uiBinder.createAndBindUi(this));

		title.setText(constants.securedBySCB());
		addShop.setText(constants.addShop());
		deleteTransactions.setText(constants.deleteTransactions());
		
		initTable();
		
		AdminServiceAsync service = GWT.create(AdminService.class);
		service.getAllShops(new AsyncCallback<List<ShopDTO>>() {
			
			@Override
			public void onSuccess(List<ShopDTO> result) {
				setContent(result);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}
		});
		
		TableStyler.setTableStyle(table);
		
		
	}
	
	private void initTable() {
		table.setWidget(0, namePos		, new Label(constants.name()));
		table.setWidget(0, urlPos 		, new Label("URL"));
		table.setWidget(0, accountNrPos	, new Label(constants.accountNr()));
		table.setWidget(0, blzPos		, new Label(constants.blz()));
		table.setWidget(0, editPos		, new Label(constants.properties()));
	}

	private void setContent(List<ShopDTO> shops) {
		int row = 1;
		for (final ShopDTO shop: shops) {
			table.setWidget(row, namePos		, new Label(shop.getName()));
			table.setWidget(row, urlPos			, new Label(shop.getUrl()));
			table.setWidget(row, accountNrPos	, new Label(shop.getAccountNr()));
			table.setWidget(row, blzPos			, new Label(shop.getBlz()));
			Button b = new Button("x");
			b.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					notifyObservers(PROPERTIES_SHOP, shop);
					
				}
			});
			table.setWidget(row, editPos		, b);
			row++;
		}
		TableStyler.setTableStyle(table);
		
	}
	
	@UiHandler("addShop")
	public void onClickAddShop(ClickEvent e) {
		notifyObservers(ADD_SHOP, null);
	}

	@UiHandler("deleteTransactions")
	public void onClickDelete(ClickEvent e) {
		AdminServiceAsync service = GWT.create(AdminService.class);
		service.deleteOpenTransactions(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}

			@Override
			public void onSuccess(Boolean result) {
				Window.alert("Success");
				
			}
		});
	}
	
	@Override
	public void addObserver(Observer o) {
		observers.add(o);
		
	}

	@Override
	public void notifyObservers(Integer eventType, Object parameter) {
		for (Observer o: observers) {
			o.update(this, eventType, parameter);
		}
		
	}

}
