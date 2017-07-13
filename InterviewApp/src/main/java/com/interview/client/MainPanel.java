package com.interview.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.interview.shared.Place;


public class MainPanel extends FlowPanel {
	public MainPanel() {
		initComponents();
	}
	
	private CellTable createTable(List<Place> places){
		CellTable<Place> table = new CellTable<Place>();
		TextColumn<Place> nameColumn = new TextColumn<Place>() {
			@Override
			public String getValue(Place object) {
				return object.getName();
			}
		};
		table.addColumn(nameColumn, "Name");
		
		TextColumn<Place> locationColumn = new TextColumn<Place>() {
			@Override
			public String getValue(Place object) {
				return object.getLocation();
			}
		};
		table.addColumn(locationColumn, "Location");
		table.setRowCount(places.size(), true);

	    table.setRowData(0, places);
	    return table;
	}
	
	private void initComponents(){
		FlowPanel flowPanel = new FlowPanel();
		final TextBox searchField = new TextBox(); 
//	    List<String> data = getData();
//	    for (int i = 0; i < data.size(); i++) {
//	      System.out.println(data.get(i));
//	      dropBox.addItem(data.get(i), data.get(i));
//	    }
		Button searchButton = new Button("Search");
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				PlacesClient placesClient = GWT.create(PlacesClient.class);
				placesClient.getPlaces(searchField.getText(), new MethodCallback<List<Place>>() {
					
					@Override
					public void onSuccess(Method method, List<Place> response) {
						Logger logger = Logger.getLogger(MainPanel.class.getName());
						logger.log(Level.FINE, "success" + response.size());
						add(createTable(response));
					}
					
					@Override
					public void onFailure(Method method, Throwable exception) {
						Logger logger = Logger.getLogger(MainPanel.class.getName());
						logger.log(Level.SEVERE, "error");
					}
				});
			}
		});
		
		flowPanel.add(searchField);
		flowPanel.add(new InlineHTML(" "));
		flowPanel.add(searchButton);
		add(flowPanel);
	}
}
