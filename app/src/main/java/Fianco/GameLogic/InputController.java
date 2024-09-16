package Fianco.GameLogic;

import java.util.List;

import Fianco.AI.*;
import Fianco.GUI.GUI;

public class InputController {

    public static GUI gui = null;
    
    public static enum PlayerType {
        HUMAN,
        RANDOM
    }

    public static boolean undo = false;
    public static Move undoBuffer = null;

    public PlayerType playerType;

    public Agent agent;

    public InputController(PlayerType playerType) {
        switch (playerType) {
            case HUMAN: {
                if (gui == null) {
                    gui = new GUI();
                    gui.setVisible(true);
                } break;
            }
            case RANDOM: agent = new RandomAgent(); break;
        }
        this.playerType = playerType;
    }

    public Move getMove(GameState state, List<Move> legalMoves) {
        Move move = null;
        if (this.playerType == PlayerType.HUMAN) {
            do {
                gui.showLegalMoves(legalMoves);
                move = gui.getMove();
                if (gui.undo) { // undo
                    undo = true;
                    undoBuffer = gui.movesMenuScroll.undo();
                    gui.undo = false;
                    break;
                }
                if (move == null) break; // restart
            } while (!legalMoves.contains(move));
            return move;
        }
        return this.agent.getMove(state, legalMoves);
    }

    public static void refreshGUI(GameState state) {
        if (gui != null) {
            gui.updateGameState(state);
            gui.setTitle("Fianco | Turn " + gui.movesMenuScroll.turnCount + ": " + (state.turnIsP1 ? "White" : "Black"));
            gui.grid.repaint();
        }
    }

    public static void addMoveToGUI(Move move) {
        if (gui != null) {
            gui.addMove(move);
        }
    }

    public static void resetGUI() {
        gui.movesMenuScroll.clear();
    }
}
