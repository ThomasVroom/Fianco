package Fianco;

import Fianco.GUI.GUI;
import Fianco.GameLogic.GameState;

public class Main {

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.updateGameState(new GameState());
        gui.setVisible(true);
    }
}
