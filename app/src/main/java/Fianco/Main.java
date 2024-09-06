package Fianco;

import Fianco.GameLogic.Engine;

public class Main {

    public static void main(String[] args) {
        Thread game = new Thread(new Engine());
        game.start();
    }
}
