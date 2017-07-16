package com.interview.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.interview.shared.Place;
import com.interview.shared.PlaceDetails;

public class DetailsDialogBox extends DialogBox {

	private Place place;
	private PlaceDetails placeDetails;
	private boolean editable;

	private TextBox nameTextBox;
	private TextBox latlngTextBox;
	private TextBox addressTextBox;
	private TextBox phoneTextBox;

	private final DetailsDialogBox thisDialogBox = this;

	public DetailsDialogBox(Place place, PlaceDetails details, boolean editable) {
		this.place = place;
		this.placeDetails = details;
		this.editable = editable;
		initTextBoxes();
		initComponents();
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

	private void addPlaceDetailsData(VerticalPanel dialogContents) {
		dialogContents.add(createTextFieldEntry("Address: ", addressTextBox));
		dialogContents.add(createTextFieldEntry("Phone: ", phoneTextBox));
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

					PlacesClient placesClient = GWT.create(PlacesClient.class);
					placesClient.savePlace(place, new MethodCallback<Place>() {

						@Override
						public void onSuccess(Method method, Place response) {
							Logger logger = Logger.getLogger(MainPanel.class.getName());
							logger.log(Level.SEVERE, "success");
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
