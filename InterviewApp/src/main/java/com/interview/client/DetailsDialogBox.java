package com.interview.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.interview.shared.PhotoReference;
import com.interview.shared.Place;
import com.interview.shared.PlaceDetails;

public class DetailsDialogBox extends DialogBox {

	private Place place;
	private PlaceDetails placeDetails;
	private boolean editable;

	private CellTable<Place> placesTable;
	private int rowIndex;

	private TextBox nameTextBox;
	private TextBox latlngTextBox;
	private TextBox addressTextBox;
	private TextBox phoneTextBox;

	PlacesClient placesClient = GWT.create(PlacesClient.class);

	private final DetailsDialogBox thisDialogBox = this;

	public DetailsDialogBox(Place place, PlaceDetails details, boolean editable) {
		this.place = place;
		this.placeDetails = details;
		this.editable = editable;
	}

	public DetailsDialogBox(Place place, PlaceDetails details, boolean editable, CellTable<Place> table, int row) {
		this(place, details, editable);
		this.placesTable = table;
		this.rowIndex = row;
	}

	@Override
	public void show() {
		initTextBoxes();
		initComponents();
		super.show();
	}

	private HorizontalPanel createTextFieldEntry(String name, TextBox textBox) {
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(4);
		Label nameLabel = new Label(name);
		hPanel.add(nameLabel);
		hPanel.add(textBox);
		return hPanel;
	}

	private void addPlaceData(VerticalPanel dialogContents) {
		dialogContents.add(createTextFieldEntry("Name: ", nameTextBox));
		dialogContents.add(createTextFieldEntry("LatLng: ", latlngTextBox));
	}

	private String createPhotoURL(String photoRef) {
		StringBuilder sb = new StringBuilder();
		sb.append("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&");
		sb.append("photoreference=");
		sb.append(photoRef);
		sb.append("&key=");
		sb.append("AIzaSyAQhlhowY8CxaIZHA4JrTVnOLuO1FrduI0");
		return sb.toString();

	}

	private void addPlaceDetailsData(final VerticalPanel dialogContents) {
		dialogContents.add(createTextFieldEntry("Address: ", addressTextBox));
		dialogContents.add(createTextFieldEntry("Phone: ", phoneTextBox));
		if (!editable) {// add image
			final Image image = new Image();
			image.setPixelSize(200, 200);
			dialogContents.add(image);
			placesClient.getPhotos(placeDetails.getPlaceId(), new MethodCallback<List<PhotoReference>>() {

				@Override
				public void onSuccess(Method method, List<PhotoReference> response) {
					if (response.size() > 0) {
						// just first image for now
						String photoRef = response.get(0).getPhotoReference();
						image.setUrl(createPhotoURL(photoRef));
					}
				}

				@Override
				public void onFailure(Method method, Throwable exception) {
					Logger logger = Logger.getLogger(MainPanel.class.getName());
					logger.log(Level.SEVERE, "onFailure photos");
				}
			});
		}
	}

	private void initTextBoxes() {
		nameTextBox = new TextBox();
		nameTextBox.setValue(place.getName());
		nameTextBox.setEnabled(editable);
		latlngTextBox = new TextBox();
		latlngTextBox.setValue(place.getLocation());
		latlngTextBox.setEnabled(editable);
		addressTextBox = new TextBox();
		addressTextBox.setValue(placeDetails.getAddress());
		addressTextBox.setEnabled(editable);
		phoneTextBox = new TextBox();
		phoneTextBox.setValue(placeDetails.getPhone());
		phoneTextBox.setEnabled(editable);
	}

	private void saveChanges() {
		place.setName(nameTextBox.getValue());
		place.setLocation(latlngTextBox.getValue());
		placeDetails.setAddress(addressTextBox.getValue());
		placeDetails.setPhone(phoneTextBox.getValue());
	}

	private void addButtons(VerticalPanel dialogContents) {
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.setCellHorizontalAlignment(buttonsPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		buttonsPanel.setSpacing(4);

		if (editable) {
			Button saveButton = new Button("Save", new ClickHandler() {
				public void onClick(ClickEvent event) {
					saveChanges();
					placesClient.savePlace(place, new MethodCallback<Place>() {

						@Override
						public void onSuccess(Method method, Place response) {
							Logger logger = Logger.getLogger(MainPanel.class.getName());
							logger.log(Level.SEVERE, "success");
							placesTable.redrawRow(rowIndex);
						}

						@Override
						public void onFailure(Method method, Throwable exception) {
							Logger logger = Logger.getLogger(MainPanel.class.getName());
							logger.log(Level.SEVERE, "error");
						}
					});

					placesClient.savePlaceDetails(placeDetails, new MethodCallback<PlaceDetails>() {

						@Override
						public void onSuccess(Method method, PlaceDetails response) {
							Logger logger = Logger.getLogger(MainPanel.class.getName());
							logger.log(Level.SEVERE, "success");
						}

						@Override
						public void onFailure(Method method, Throwable exception) {
							Logger logger = Logger.getLogger(MainPanel.class.getName());
							logger.log(Level.SEVERE, "error");
						}
					});
					thisDialogBox.hide();
				}
			});
			buttonsPanel.add(saveButton);
		}

		Button closeButton = new Button("Close", new ClickHandler() {
			public void onClick(ClickEvent event) {
				thisDialogBox.hide();
			}
		});
		buttonsPanel.add(closeButton);
		dialogContents.add(buttonsPanel);
	}

	private void initComponents() {
		if (editable) {
			setText("Edit");
		} else {
			setText("View");
		}

		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(4);

		addPlaceData(dialogContents);
		addPlaceDetailsData(dialogContents);
		addButtons(dialogContents);

		setWidget(dialogContents);
		setGlassEnabled(true);
		setAnimationEnabled(true);
	}

}
