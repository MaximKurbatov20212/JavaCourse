package model;

import view.Viewer;

import static model.Condition.*;

public class ArkanoidFrame {
    public static GameField gameField;
    public static final RecordManager recordManager = RecordManager.INSTANCE;
    public final Registrator registrator = new Registrator();
    public static final Menu menu = new Menu();

    public static Condition condition = IN_REGISTRATION;

    public ArkanoidFrame(Condition condition) {
        ArkanoidFrame.condition = condition;
        gameField = new GameField();
    }

    public static void update() {
        switch (condition) {
            case IN_GAME, END_GAME -> Viewer.addGameCondition(new GameCondition(condition, gameField.getGameObjects()));
            case IN_MAIN_MENU -> Viewer.addGameCondition(new GameCondition(condition, menu.getGameObjects()));
            case IN_HIGH_SCORES -> Viewer.addGameCondition(new GameCondition(condition, recordManager.getGameObjects()));
            case IN_ABOUT_MENU, IN_REGISTRATION -> Viewer.addGameCondition(new GameCondition(condition, null));
            default -> throw new RuntimeException("Unexpected ArkanoidFiled condition");
        }
    }

    public Condition getCondition() {
        return condition;
    }
}
