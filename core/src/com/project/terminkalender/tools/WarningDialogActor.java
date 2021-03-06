package com.project.terminkalender.tools;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class WarningDialogActor extends Dialog {
	private Label messageLabel;

	public WarningDialogActor(Skin skin) {
		super("", skin, "windowDialog");
		
		messageLabel = new Label("", skin);
		TextButton closeButton = new TextButton("OK", skin, "textButtonLarge");
		
		center();
		setMovable(false);
		setResizable(false);
		setModal(true);
		getButtonTable().defaults().width(175).height(100);
		pad(20);
		
		getContentTable().padTop(40);
		getContentTable().add(messageLabel);
		getButtonTable().padTop(50);
		button(closeButton);
	}
	
	public Dialog show(String message, Stage stage) {
		messageLabel.setText(message);
		return super.show(stage);
	}
}
