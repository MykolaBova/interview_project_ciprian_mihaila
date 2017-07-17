package com.interview.client;

import java.util.ArrayList;
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
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.interview.shared.Place;
import com.interview.shared.PlaceDetails;

public class MainPanel extends VerticalPanel {

	private TextBox searchField;
	private CellTable<Place> placesTable;
	private HorizontalPanel filterArea;

	private PlacesClient placesClient = GWT.create(PlacesClient.class);

	private List<Place> placesModelList;

	public MainPanel() {
		initComponents();
	}

	private void addTableColumns(CellTable<Place> table) {
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
	}

	private Column<Place, String> createDeleteColumn() {
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
				placesClient.deletePlace(object.getPlaceId(), searchField.getValue(),
						new MethodCallback<List<Place>>() {

							@Override
							public void onSuccess(Method method, List<Place> response) {
								Logger logger = Logger.getLogger(MainPanel.class.getName());
								logger.log(Level.SEVERE, "success ");
								placesModelList = response;
								placesTable.setRowCount(placesModelList.size(), true);
								placesTable.setRowData(0, placesModelList);
							}

							@Override
							public void onFailure(Method method, Throwable exception) {
								Logger logger = Logger.getLogger(MainPanel.class.getName());
								logger.log(Level.SEVERE, "error " + exception.getMessage());
							}
						});
			}
		});

		return delete;
	}

	private Column<Place, String> createViewColumn() {
		ButtonCell viewButton = new ButtonCell();
		Column<Place, String> view = new Column<Place, String>(viewButton) {
			@Override
			public String getValue(Place c) {
				return "View";
			}
		};
		view.setFieldUpdater(new FieldUpdater<Place, String>() {

			@Override
			public void update(int index, final Place object, String value) {
				placesClient.getPlaceDetails(object.getPlaceId(), new MethodCallback<PlaceDetails>() {

					@Override
					public void onSuccess(Method method, PlaceDetails response) {
						final DialogBox dialogBox = new DetailsDialogBox(object, response, false);
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
		return view;
	}

	private Column<Place, String> createEditColumn() {
		ButtonCell editButton = new ButtonCell();
		Column<Place, String> edit = new Column<Place, String>(editButton) {
			@Override
			public String getValue(Place c) {
				return "Edit";
			}
		};
		edit.setFieldUpdater(new FieldUpdater<Place, String>() {

			@Override
			public void update(final int index, final Place object, String value) {
				placesClient.getPlaceDetails(object.getPlaceId(), new MethodCallback<PlaceDetails>() {

					@Override
					public void onSuccess(Method method, PlaceDetails response) {
						final DialogBox dialogBox = new DetailsDialogBox(object, response, true, placesTable, index);
						dialogBox.center();
						dialogBox.show();
						placesTable.redrawRow(index);
					}

					@Override
					public void onFailure(Method method, Throwable exception) {
						Logger logger = Logger.getLogger(MainPanel.class.getName());
						logger.log(Level.SEVERE, "error" + exception.getMessage());
					}
				});

			}
		});
		return edit;
	}

	private CellTable<Place> createTable() {
		CellTable<Place> table = new CellTable<Place>();

		addTableColumns(table);

		table.addColumn(createDeleteColumn());
		table.addColumn(createViewColumn());
		table.addColumn(createEditColumn());

		table.setVisible(false);

		return table;
	}

	private void initComponents() {
		setSpacing(6);
		FlowPanel flowPanel = new FlowPanel();
		searchField = new TextBox();
		placesTable = createTable();

		Button searchButton = new Button("Search");
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				placesClient.getPlaces(searchField.getText(), new MethodCallback<List<Place>>() {

					@Override
					public void onSuccess(Method method, List<Place> response) {
						Logger logger = Logger.getLogger(MainPanel.class.getName());
						logger.log(Level.SEVERE, "success" + response.size());
						placesModelList = response;
						placesTable.setRowCount(placesModelList.size(), true);
						placesTable.setRowData(0, placesModelList);
						placesTable.setVisible(true);
						filterArea.setVisible(true);
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

		filterArea = createFilerArea();
		add(filterArea);

		add(placesTable);
	}

	private HorizontalPanel createFilerArea() {
		HorizontalPanel filterArea = new HorizontalPanel();
		filterArea.setSpacing(6);
		filterArea.setVisible(false);
		Label label = new Label("Filter: ");
		filterArea.add(label);

		final TextBox filterBox = new TextBox();
		filterBox.setWidth("400");
		filterBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				filterTable(filterBox.getValue());
			}
		});

		filterArea.add(filterBox);
		return filterArea;
	}

	private void filterTable(final String searchString) {
		final List<Place> subList = getSubList(searchString.toLowerCase());
		AsyncDataProvider<Place> provider = new AsyncDataProvider<Place>() {
			@Override
			protected void onRangeChanged(HasData<Place> display) {
				updateRowData(0, subList);
			}
		};
		provider.addDataDisplay(placesTable);
		provider.updateRowCount(subList.size(), true);
	}

	private List<Place> getSubList(String searchString) {
		List<Place> filtered_list = null;
		if (searchString != null && !"".equals(searchString)) {
			filtered_list = new ArrayList<Place>();
			for (Place place : placesModelList) {
				if (place.getName().toLowerCase().contains(searchString)
						|| place.getLocation().toLowerCase().contains(searchString)) {
					filtered_list.add(place);
				}
			}
		}

		if (filtered_list == null || filtered_list.isEmpty()) {
			filtered_list = placesModelList;
		}

		return filtered_list;
	}
}
