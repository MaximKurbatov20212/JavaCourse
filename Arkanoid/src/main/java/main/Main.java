package main;

import Controller.GameController;

public class Main {
    public static void main(String[] args) {
        GameController controller = GameController.INSTANCE;
        controller.initGame();
    }
}

