package com.maltsev.labyrinth.view.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.maltsev.labyrinth.presenter.IPresenter;
import com.maltsev.labyrinth.presenter.Presenter;
import com.maltsev.labyrinth.presenter.interfaces.IProtagonistDrawer;
import com.maltsev.labyrinth.presenter.interfaces.IGameScreen;
import com.maltsev.labyrinth.presenter.tempdata.PointOnTheScreen;
import com.maltsev.labyrinth.view.Labyrinth;
import com.maltsev.labyrinth.view.game.drawers.FieldDrawer;
import com.maltsev.labyrinth.view.game.drawers.ProtagonistDrawer;
import com.maltsev.labyrinth.view.game.scenes.Fon;
import com.maltsev.labyrinth.presenter.tempdata.SizeOfTexture;
import com.maltsev.labyrinth.view.game.scenes.Hud;

/**
 * Игровой экран
 *
 * Здесь происходит основное действо
 *
 * Контактирует с классами Fon  и Hud, используя из для удобной отрисовки, с классом Presenter, выступает в качестве
 * IGameScreen для него и как следствие обладает методами для отрисовки по запросу, а так же может обратиться к Labyrinth для того,
 * чтобы закончить игру и перевести управление к другому экрану, в частности к MainMenuScreen
 *
 * Общее определение IGameScreen из шаблона проектирования MVP:
 * Представление, как правило, реализуется в Activity,
 * которая содержит ссылку на презентер.
 * Единственное, что делает представление,
 * это вызывает методы презентера при каком-либо действии пользователя
 */
public class GameScreen implements Screen, IGameScreen {

    private Labyrinth labyrinth;
    private IPresenter presenter;

    private Hud hud;
    private Fon fonGameScreen;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Texture exit;
    private Texture doorClose;
    private Texture doorOpen;
    private Texture key;
    private Texture infoGameEnd;

    private Vector3 touchPos;

    private boolean lockInput = false;
    private boolean isGameEnd = false;
    private boolean isItPause = false;
    private boolean isInMotion = false;

    private float defaultWidth = Gdx.graphics.getWidth();
    private float defaultHeight = Gdx.graphics.getHeight();

    private Stage stage;

    private boolean typeOfControl = false;

    private ProtagonistDrawer protagonistDrawer;
    private FieldDrawer fieldDrawer;


    public GameScreen(final Labyrinth labyrinth) {

        this.labyrinth = labyrinth;

        batch  = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Labyrinth.V_WIDTH, Labyrinth.V_HEIGHT);
        loadingOfTextures();

        typeOfControl = labyrinth.infoAboutSettings.getTypeOfTheControl();
        touchPos = new Vector3();

        fieldDrawer = new FieldDrawer(batch);
        protagonistDrawer = new ProtagonistDrawer(batch);

        presenter = new Presenter(this, labyrinth.infoAboutSettings.getGameField());
        hud = new Hud(labyrinth,this, presenter);
        fonGameScreen = new Fon(batch);

        Gdx.input.setInputProcessor(hud.hudStage);
        camera.position.set(protagonistDrawer.getPositionOfProtagonist());
        camera.update();

        stage = new Stage();
    }

    /**
     * Загрузка текстур
     * В это методе происходи создание объектов класса Texture, т.е. после завершения работы этого метода
     * каждой ссылке на объект будет поставленно в соответствие определённое изображение
     *
     * Здесь фиксируется размер Блока, из которого будет конструироваться игровое поле (дорога, по которой идёт протагонист)
     */
    private  void loadingOfTextures() {
        exit = new Texture("game_ui/exit.png");
        doorClose = new Texture("game_ui/doorClose.png");
        doorOpen = new Texture("game_ui/doorOpen.png");
        key = new Texture("game_ui/key.png");
        infoGameEnd = new Texture("game_ui/grey_panel.png");
    }

    @Override
    public void dispose () {

        hud.dispose();
        fonGameScreen.dispose();
        exit.dispose();
        infoGameEnd.dispose();

        infoGameEnd.dispose();
        key.dispose();
        doorOpen.dispose();
        doorClose.dispose();

        fieldDrawer.dispose();
        protagonistDrawer.dispose();
    }

    @Override
    public SizeOfTexture getSizeOfBlock() {
        return fieldDrawer.getSizeOfBlock();
    }

    @Override
    public void lockInput() {
        lockInput = true;
    }

    @Override
    public void unlockInput() {
        lockInput = false;
    }

    @Override
    public void startMovement() {
        isInMotion = true;
    }

    @Override
    public void finishMovement() {
        isInMotion = false;
    }

    private void handelInput(float delta) {

        if (Gdx.input.justTouched() && !lockInput && !typeOfControl) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            presenter.moveProtagonist(touchPos.x, touchPos.y);
        }
        if(typeOfControl && !lockInput)
            hud.handleInput();
    }

    public void setPause() {
        lockInput();
        Gdx.input.setInputProcessor(hud.pauseStage);
        isItPause = true;
    }

    private void update(float delta) {
        handelInput(delta);
        if (isInMotion)
            protagonistDrawer.setPositionOfProtagonist(presenter.getPositionOfMovingProtagonist(delta));
        hud.setTime(delta);
        hud.setKeys(presenter.getNumberOfKeys());
    }

    /**
     * Здесь происходит вся отрисовка
     */
    private void draw(float delta) {
        presenter.drawField();
        update(delta);
        protagonistDrawer.draw();
        if(isGameEnd) {
            batch.draw(infoGameEnd, protagonistDrawer.getPositionOfProtagonist().x - infoGameEnd.getWidth()/2,
                    protagonistDrawer.getPositionOfProtagonist().y - infoGameEnd.getHeight()/2);
            waitingAction();
        }
        camera.position.set(protagonistDrawer.getPositionOfProtagonist());
        camera.update();
    }

    private void waitingAction() {
        if (Gdx.input.justTouched()) {
            this.close();
        }
    }

    @Override
    public void render (float delta) {
        fonGameScreen.stage.draw();
        batch.setProjectionMatrix(camera.combined);
        // Следует вызывать методы отрисовки этого экрана только в пределах begin~end
        batch.begin();
        draw(delta);
        batch.end();

        hud.hudStage.draw();
        if(isItPause)
            hud.pauseStage.draw();
    }

    @Override
    public IProtagonistDrawer getProtagonistDrawer() {
        return protagonistDrawer;
    }

    @Override
    public com.maltsev.labyrinth.presenter.interfaces.IFieldDrawer getFieldDrawer() {
        return fieldDrawer;
    }

    @Override
    public void drawExit(PointOnTheScreen point) {
        batch.draw(exit, point.getX(), point.getY());
    }

    @Override
    public void drawKey(PointOnTheScreen point) {
        batch.draw(key, point.getX(), point.getY());
    }

    @Override
    public void drawCloseDoor(PointOnTheScreen point) {
        batch.draw(doorClose, point.getX(), point.getY());
    }

    @Override
    public void drawOpenDoor(PointOnTheScreen point) {
        batch.draw(doorOpen, point.getX(), point.getY());
    }

    public void close() {
        labyrinth.closeGameScreen();
    }

    @Override
    public void messageOfGameOver() {

        isGameEnd = true;
        hud.stopTimer();

        Gdx.input.setInputProcessor(stage);  //Костыль, чтобы убрать ввыод с Hud, без этого при конце игры нажатие на кнопку выхода в меню ломает приложение
    }

    @Override
    public void resize(int width, int height) {

        fonGameScreen.resize(width,height);
        hud.resize(width, height);

        camera.viewportWidth = Labyrinth.V_WIDTH * (width/defaultWidth);
        camera.viewportHeight = Labyrinth.V_HEIGHT * (height/defaultHeight);

        camera.update();
    }

    public boolean isLockInput() {

        return lockInput;
    }

    @Override
    public void show() {

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
}
