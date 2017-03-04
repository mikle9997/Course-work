package com.maltsev.labyrinth.view.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.maltsev.labyrinth.view.Labyrinth;


public class SettingsScreen implements Screen {

    private Stage stage;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private ButtonGroup<CheckBox> boxButtonGroup;
    private CheckBox.CheckBoxStyle checkBoxStyle;

    private ImageTextButton.ImageTextButtonStyle buttonStyle;

    private BitmapFont font;
    private BitmapFont fontForLabel;
    private BitmapFont smallFont;

    private ClickListener backToMenuListener;

    private ImageTextButton backToMenu;

    private Table table;
    private Table subTable;

    private Texture checkBoxOn;
    private Texture checkBoxOff;

    private Labyrinth game;

    public SettingsScreen(final Labyrinth game) {

        this.game = game;

        checkBoxOn = new Texture("settings_ui/blue_boxTick.png");
        checkBoxOff = new Texture("settings_ui/grey_circle.png");

        font = game.fontGenerator.getFont();
        smallFont = game.fontGenerator.getSmallFont();

        fontForLabel = game.fontGenerator.getFontForLabel();

        checkBoxStyle = new CheckBox.CheckBoxStyle(new TextureRegionDrawable(new TextureRegion(checkBoxOff)),
                new TextureRegionDrawable(new TextureRegion(checkBoxOn)), smallFont, new Color(0,0,0,1));

        checkBox1 = new CheckBox("Touch",checkBoxStyle);
        checkBox2 = new CheckBox("Controller", checkBoxStyle);

        boxButtonGroup = new ButtonGroup<>(checkBox1,checkBox2);

        Label label = new Label("Select the type of control:", new Label.LabelStyle(fontForLabel,new Color(0,0,0,1)));

        checkBox1.getCells().get(0).size(100,100);
        checkBox1.getImage().setWidth(90);
        checkBox1.setChecked(true);
        checkBox2.getCells().get(0).size(200,200);

        buttonStyle = game.menuButtonStyle.getButtonStyle();

        backToMenu = new ImageTextButton("Back to menu", buttonStyle);

        registeredListenerAgain();

        table = new Table();
        subTable = new Table();

        table.top();
        table.setFillParent(true);

        table.add(backToMenu).padLeft(1300).padTop(20);
        table.row();

        subTable.add(label).padBottom(10);
        subTable.row();
        subTable.add(checkBox1).padRight(300);
        subTable.add(checkBox2).padRight(100);

        table.add(subTable).padTop(50);

        stage = new Stage(new ExtendViewport(Labyrinth.V_WIDTH, Labyrinth.V_HEIGHT));

        stage.addActor(table);
    }

    private void registeredListenerAgain(){

        backToMenuListener = new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                game.setMainMenuScreen();
                registeredListenerAgain();
            }
        };

        backToMenu.addListener(backToMenuListener);
    }

    public int getChoice() {

        return boxButtonGroup.getCheckedIndex();
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        font.dispose();
        fontForLabel.dispose();
        stage.dispose();
    }
}
