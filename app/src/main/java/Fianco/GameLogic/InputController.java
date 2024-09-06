package Fianco.GameLogic;

import java.util.List;

import Fianco.GUI.GUI;

public class InputController {

    public static GUI gui = null;
    
    public static enum PlayerType {
        HUMAN,
        RANDOM
    }

    public PlayerType playerType;

    public InputController(PlayerType playerType) {
        if (playerType == PlayerType.HUMAN && gui == null) {
            gui = new GUI();
            gui.setVisible(true);
        }
        this.playerType = playerType;
    }

    public Move getMove(GameState state, List<Move> legalMoves) {
        Move move = null;
        if (playerType == PlayerType.HUMAN) {
            while (!legalMoves.contains(move)) {
                gui.showLegalMoves(legalMoves.toArray(new Move[0]));
                move = gui.getMove();
            }
            return move;
        }
        return null;
    }

    public static void refreshGUI(GameState state) {
        if (gui != null) {
            gui.updateGameState(state);
            gui.grid.repaint();
        }
    }
}
