package Fianco.GameLogic;

import java.util.BitSet;

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

    public Move getMove(GameState state, BitSet legalMoves) {
        Move move = null;
        if (playerType == PlayerType.HUMAN) {
            do {
                gui.showLegalMoves(legalMoves);
                move = gui.getMove();
            } while (!legalMoves.get(move.to)); // TODO bug: move.from is not checked
            return move;
        }
        // TODO: implement AI interface
        return null;
    }

    public static void refreshGUI(GameState state) {
        if (gui != null) {
            gui.updateGameState(state);
            gui.grid.repaint();
        }
    }
}
