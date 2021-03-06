package com.project.terminkalender.chat;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.project.terminkalender.AppMain;
import com.project.terminkalender.tools.ScrollWindow;

public class RoomActor extends Table {
	private final Room room = new Room();
	private final Chat defaultChat = new Chat("", room);
	private ChatActor chatActor;
	private ScrollWindow usersWindow;
	
	public RoomActor(Skin skin, ChatActor chatActor) {
		super(skin);
		this.chatActor = chatActor;
		
		usersWindow = new ScrollWindow("Benutzer", skin, room.getUsersTable());
		
		usersWindow.setMovable(false);
		add(usersWindow).width(210).height(AppMain.HEIGHT - 4);
		setBounds(2, 2, 210, AppMain.HEIGHT - 4);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(room.update()) {
			Table usersTable = room.getUsersTable();
			Array<Chat> chats = room.getChats();
			
			usersTable.clear();
			
			for(final Chat chat : chats) {
				float width = 175;
				float height = 100;
				
				final TextButton userButton = chat.getTextButton();//new TextButton(chat.getUser(), chat.getTextButton().getStyle());
				userButton.clearListeners();
				userButton.getLabel().setWrap(true);
				int widthDiference = (int) (userButton.getLabel().getWidth() / (width * 2));
				if(widthDiference > 0) {
					++widthDiference;
					height = widthDiference * height;
				}		
				usersTable.add(userButton).width(width).height(height).pad(5);
				usersTable.row();
				
				userButton.addListener(new ClickListener() {

					@Override 
					public void clicked(InputEvent event, float x, float y) {
						chat.setTextButton(userButton);
						setChatData(chat);
					}
				});
			}
			
			room.finishUpdate();
		}
		
		if(room.ScrollUp()) {
			usersWindow.getScrollTable().setScrollY(0);
			room.finishScrollUp();
		}
	 }
	
	public void setFirstChat() {
		Chat chat = room.getChats().first();
		setChatData(chat);
	}
	
	private void setChatData(Chat chat) {
		chatActor.setChat(chat);
		chatActor.updateScroll();
		AppMain.webSockets.askChatFromUser(chat.getUser(), chat.getMessagesSize());
	}
	
	public void setDefaultChat() {
		setChatData(defaultChat);
	}
}
