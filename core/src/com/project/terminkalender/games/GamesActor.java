package com.project.terminkalender.games;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.project.terminkalender.Resources;
import com.project.terminkalender.TeacherMain;
import com.project.terminkalender.tools.ScrollWindow;

public class GamesActor extends Table {
	private Games games;
	private ScrollWindow gamesWindow;

	public GamesActor(Skin skin) {
		super(skin);
		games = new Games();
		
		gamesWindow = new ScrollWindow("Games", skin, games.getGamesTable());
		Table buttonsTable = new Table(skin);
		TextButton newGameButton = new TextButton("New Game", skin, "textButtonLarge");
		
		final CreateGameDialogActor createGameDialogActor = new CreateGameDialogActor(skin);
		
		setFillParent(true);
		
		add(gamesWindow).width(800).height(TeacherMain.HEIGHT - 16).expand().left().pad(8);
		buttonsTable.add(newGameButton).width(175).height(100).getTable().pad(8).row();
		add(buttonsTable);
		
		newGameButton.addListener(new ClickListener() {

			@Override 
			public void clicked(InputEvent event, float x, float y){
				createGameDialogActor.show(getStage());
			}
		});
		/*updateGamesButton.addListener(new ClickListener() {

			@Override 
			public void clicked(InputEvent event, float x, float y){
				games.askGames();
			}
		});*/
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(games.update()) {
			Array<TeacherGame> gamesArray = games.getGames();
			Array<TeacherGame> gamesOpenArray = games.getOpenGames();
			
			games.getGamesTable().clear();
			int column = createGamesButtons(gamesArray, Resources.skin.get("textButtonLarge", TextButtonStyle.class), "Game", 0);
			createGamesButtons(gamesOpenArray, Resources.skin.get("textButtonLargeGreen", TextButtonStyle.class), "OpenGame", column);
			
			games.finishUpdate();
		}
	}
	
	private int createGamesButtons(Array<TeacherGame> games, TextButtonStyle textButtonStyle, final String typeGame, int column) {
		Table gamesTable = this.games.getGamesTable();
		int actualColumn = column;
		
		for(final TeacherGame game : games) {
			float width = 200;
			float height = 125;
			
			final TextButton gameButton = new TextButton(game.getName(), textButtonStyle);
			gameButton.getLabel().setWrap(true);
			int widthDiference = (int) (gameButton.getLabel().getWidth() / (width * 2));
			if(widthDiference > 0) {
				++widthDiference;
				height = widthDiference * height;
			}
			gamesTable.add(gameButton).width(width).height(height).pad(30);
			
			++actualColumn;
			if(actualColumn % 3 == 0) {
				gamesTable.row();
			}
			
			gameButton.addListener(new ClickListener() {

				@Override 
				public void clicked(InputEvent event, float x, float y) {
					GameDialog dialogActor = null;
					if(typeGame.equals("Game")) {
						dialogActor = new GameDialogActor(Resources.skin, game, gameButton);
					}
					else if(typeGame.equals("OpenGame")) {
						dialogActor = new OpenGameDialogActor(Resources.skin, game, gameButton);
					}
					dialogActor.show(getStage());
				}
			});
		}
		
		return actualColumn;
	}
}
