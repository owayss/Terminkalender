package com.project.terminkalender.loginandregister;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.project.terminkalender.Resources;
import com.project.terminkalender.TeacherMain;
import com.project.terminkalender.websockets.TeacherWebSockets;

public class TeacherRegisterDialog {
	private TextField userText, passwordText, passwordRepeatText;
	

	public TeacherRegisterDialog() {
		Skin skin = Resources.skin;
		
		userText = new TextField("", skin);
		passwordText = new TextField("", skin);
		passwordRepeatText = new TextField("", skin);
	}
	
	public void registerTeacher() {
		if(userText.getText().equals("") || passwordText.getText().equals("") || passwordRepeatText.getText().equals("")) {
			Resources.warningDialog.show("You must fill the gaps", TeacherMain.teacherLoginRegisterScreen.getStage());
		}
		else if(userText.getText().contains(TeacherWebSockets.DATASPLIT) || userText.getText().contains(TeacherWebSockets.POINTSPLIT) || 
				userText.getText().contains(TeacherWebSockets.TASKSPLIT) || passwordText.getText().contains(TeacherWebSockets.DATASPLIT) || 
				passwordText.getText().contains(TeacherWebSockets.POINTSPLIT) || passwordText.getText().contains(TeacherWebSockets.TASKSPLIT) ||
				passwordRepeatText.getText().contains(TeacherWebSockets.DATASPLIT) || passwordRepeatText.getText().contains(TeacherWebSockets.POINTSPLIT) || 
				passwordRepeatText.getText().contains(TeacherWebSockets.TASKSPLIT)) {
			Resources.warningDialog.show("you musn't use ',', ';' or ':'", TeacherMain.teacherLoginRegisterScreen.getStage());
		}
		else if(!passwordText.getText().equals(passwordRepeatText.getText())) {
			Resources.warningDialog.show("Passwords must be the same", TeacherMain.teacherLoginRegisterScreen.getStage());
		}
		else {
			TeacherMain.teacherWebSockets.registerTeacher(userText.getText().toLowerCase(), passwordText.getText().toLowerCase());
		}
	}
	
	public void setEmpty() {
		userText.setText("");
		passwordText.setText("");
		passwordRepeatText.setText("");
	}
	
	public TextField getUserText() {
		return userText;
	}
	public TextField getPasswordText() {
		return passwordText;
	}
	public TextField getPasswordRepeatText() {
		return passwordRepeatText;
	}
}
