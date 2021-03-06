package com.project.terminkalender.calendar;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.project.terminkalender.AppMain;
import com.project.terminkalender.Resources;
import com.project.terminkalender.screens.CalendarScreen;
import com.project.terminkalender.tools.TextFieldActor;
import com.project.terminkalender.userdata.Game;
import com.project.terminkalender.websockets.AppWebSockets;

public class SetTaskInCalendarDialog extends Dialog {
	public static final String OK = "Ok";
	public static final String CANCEL = "CANCEL";
	
	private TaskCalendar task;
	private Label partnerLabel, locationLabel, whatLabel, whereLabel;
	private Array<SelectBox<String>> partnertsboxes;
	private SelectBox<String> whatBox, whereBox;
	private TextFieldActor locationText;
	private Array<String> users;

	public SetTaskInCalendarDialog(String title, Skin skin) {
		super("", skin, "windowDialog");
		
		center();
		setMovable(false);
		setResizable(false);
		setModal(true);
		pad(20);
		padTop(35);
		
		task = new TaskCalendar();
		partnerLabel = new Label("Mit wem? ", skin);
		locationLabel = new Label("Wo? ", skin);
		whatLabel = new Label("Was konkret? ", skin);
		whereLabel = new Label("Wo konkret? ", skin);
		
		partnertsboxes = new Array<SelectBox<String>>();
		locationText = new TextFieldActor("", skin);
		whatBox = new SelectBox<String>(skin);
		whereBox = new SelectBox<String>(skin);
		TextButton acceptButton = new TextButton("OK", skin);
		TextButton cancelButton = new TextButton("Cancel", skin, "redTextButton");
		
		Game game = AppMain.user.getGame();
		users = new Array<String>(game.getUsers());
		users.removeValue(AppMain.user.getUserName(), false);
		
		getButtonTable().defaults().width(175).height(100);
		getContentTable().padTop(40);
		getContentTable().padBottom(40);
		button(acceptButton, OK);
		button(cancelButton, CANCEL);
		
		addListener(new InputListener() {

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					cancelTask();
					hide();
				}
				return true;
			}
		});
	}
	
	@Override
	public Dialog show(Stage stage) {
		construct();
		return super.show(stage);
	}
	
	private void construct() {
		getContentTable().clear();
		partnertsboxes.clear();
		
		getTitleLabel().setText(task.getDescription());
		int numberPartners = task.getNumberPartners();
		Array<String> partnerts = task.getPartners();
		if(numberPartners > 0) {
			getContentTable().add(partnerLabel);
			for(int index = 0; index < numberPartners; ++index) {
				SelectBox<String> partnertsBox = new SelectBox<String>(Resources.skin);
				partnertsBox.setItems(users);
				partnertsBox.setSelectedIndex(index);
				if(partnerts.size > index) {
					String partnerString = partnerts.get(index);
					partnertsBox.setSelected(partnerString);
				}
				if(index > 0) {
					Label otherPartnerLabel = new Label("und", Resources.skin);
					getContentTable().add(otherPartnerLabel);
				}
				if(index == 2) {
					getContentTable().row();
					getContentTable().add(new Label("", Resources.skin));
				}
				partnertsboxes.add(partnertsBox);
				getContentTable().add(partnertsBox).width(226).right();
			}
		}
		getContentTable().row();
		if(task.getWhatArray().size > 0) {
			getContentTable().add(whatLabel);
			whatBox.setItems(task.getWhatArray());
			if(!task.getWhat().equals("")) {
				whatBox.setSelected(task.getWhat());
			}
			getContentTable().add(whatBox).width(226).row();
		}
		locationText.setText(task.getLocation());
		getContentTable().add(locationLabel);
		getContentTable().add(locationText).width(226).row();
		if(task.getWhereArray().size > 0) {
			getContentTable().add(whereLabel);
			whereBox.setItems(task.getWhereArray());
			if(!task.getWhere().equals("")) {
				whereBox.setSelected(task.getWhere());
			}
			getContentTable().add(whereBox).width(226);
		}
	}
	
	protected void result(Object object) {
		if(object.equals(OK)) {
			sendTaskData();
		}
		else if(object.equals(CANCEL)) {
			cancelTask();
		}
	}
	private void sendTaskData() {
		String location = locationText.getText();
		if(location.contains(AppWebSockets.POINTSPLIT) || location.contains(AppWebSockets.DATASPLIT) || 
		   location.contains(AppWebSockets.TASKSPLIT)) {
			Resources.warningDialog.show("you musn't use ',', ';', or ':'", getStage());
			cancelTask();
		}
		else {
			task.setLocation(locationText.getText());
			int numberPartners = partnertsboxes.size;
			task.clearPartners();
			for(int index = 0; index < numberPartners; ++index) {
				SelectBox<String> partnertsBox = partnertsboxes.get(index);
				task.addPartner(partnertsBox.getSelected());
			}
			if(task.getWhatArray().size > 0) {
				task.setWhat(whatBox.getSelected());
			}
			if(task.getWhereArray().size > 0) {
				task.setWhere(whereBox.getSelected());
			}
			task.addDataServer();
		}
	}
	private void cancelTask() {
		CalendarScreen calendarScreen = (CalendarScreen) AppMain.calendarScreen;
		calendarScreen.setTaskInSlotEmpty(task);
	}

	public void setTask(TaskCalendar task) {
		this.task = task;
	}
}
