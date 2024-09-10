package Fianco;

import Fianco.GameLogic.Engine;
import Fianco.GameLogic.InputController.PlayerType;

public class Main {

    public static void main(String[] args) {
        Thread game = new Thread(new Engine(PlayerType.HUMAN, PlayerType.RANDOM));
        game.start();
    }
}
