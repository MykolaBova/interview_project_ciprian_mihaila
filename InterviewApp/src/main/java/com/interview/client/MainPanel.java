package com.interview.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextBox;
import com.interview.shared.Place;
import com.interview.shared.PlaceDetails;

public class MainPanel extends FlowPanel {
	public MainPanel() {
		initComponents();
	}

	private CellTable<Place> createTable() {
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

		ButtonCell deleteButton = new ButtonCell();
		Column<Place, String> delete = new Column<Place, String>(deleteButton) {
			@Override
			public String getValue(Place c) {
				return "Delete";
			}
		};
		delete.setFieldUpdater(new FieldUpdater<Place, String>() {

			@Override
			public void update(int index, Place object, String value) {
				PlacesClient placesClient = GWT.create(PlacesClient.class);
				placesClient.deletePlace(object.getPlaceId(), new MethodCallback<String>() {

					@Override
					public void onSuccess(Method method, String response) {
						Logger logger = Logger.getLogger(MainPanel.class.getName());
						logger.log(Level.SEVERE, "success delete");
					}

					@Override
					public void onFailure(Method method, Throwable exception) {
						Logger logger = Logger.getLogger(MainPanel.class.getName());
						logger.log(Level.SEVERE, "error" + exception.getMessage());
					}
				});
			}
		});
		table.addColumn(delete);

		ButtonCell viewButton = new ButtonCell();
		Column<Place, String> view = new Column<Place, String>(viewButton) {
			@Override
			public String getValue(Place c) {
				return "View";
			}
		};
		view.setFieldUpdater(new FieldUpdater<Place, String>() {

			@Override
			public void update(int index, Place object, String value) {
				PlacesClient placesClient = GWT.create(PlacesClient.class);
				final Place place = object;
				placesClient.getPlaceDetails(place.getPlaceId(), new MethodCallback<PlaceDetails>() {

					@Override
					public void onSuccess(Method method, PlaceDetails response) {
						final DialogBox dialogBox = new DetailsDialogBox(place, response, false);
						dialogBox.center();
						dialogBox.show();
					}

					@Override
					public void onFailure(Method method, Throwable exception) {
						Logger logger = Logger.getLogger(MainPanel.class.getName());
						logger.log(Level.SEVERE, "error" + exception.getMessage());
					}
				});

			}
		});
		table.addColumn(view);

		ButtonCell editButton = new ButtonCell();
		Column<Place, String> edit = new Column<Place, String>(editButton) {
			@Override
			public String getValue(Place c) {
				return "Edit";
			}
		};
		edit.setFieldUpdater(new FieldUpdater<Place, String>() {

			@Override
			public void update(int index, Place object, String value) {
				PlacesClient placesClient = GWT.create(PlacesClient.class);
				final Place place = object;
				placesClient.getPlaceDetails(place.getPlaceId(), new MethodCallback<PlaceDetails>() {

					@Override
					public void onSuccess(Method method, PlaceDetails response) {
						final DialogBox dialogBox = new DetailsDialogBox(place, response, true);
						dialogBox.center();
						dialogBox.show();
					}

					@Override
					public void onFailure(Method method, Throwable exception) {
						Logger logger = Logger.getLogger(MainPanel.class.getName());
						logger.log(Level.SEVERE, "error" + exception.getMessage());
					}
				});

			}
		});
		table.addColumn(edit);

		table.setVisible(false);

		return table;
	}

	private void initComponents() {
		FlowPanel flowPanel = new FlowPanel();
		final TextBox searchField = new TextBox();
		final CellTable<Place> placesTable = createTable();

		Button searchButton = new Button("Search");
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PlacesClient placesClient = GWT.create(PlacesClient.class);
				placesClient.getPlaces(searchField.getText(), new MethodCallback<List<Place>>() {

					@Override
					public void onSuccess(Method method, List<Place> response) {
						Logger logger = Logger.getLogger(MainPanel.class.getName());
						logger.log(Level.SEVERE, "success" + response.size());
						placesTable.setRowCount(response.size(), true);
						placesTable.setRowData(0, response);
						placesTable.setVisible(true);
					}

					@Override
					public void onFailure(Method method, Throwable exception) {
						Logger logger = Logger.getLogger(MainPanel.class.getName());
						logger.log(Level.SEVERE, "error " + exception.getMessage());
					}
				});
			}
		});

		flowPanel.add(searchField);
		flowPanel.add(new InlineHTML(" "));
		flowPanel.add(searchButton);
		add(flowPanel);
		add(placesTable);
	}
}
