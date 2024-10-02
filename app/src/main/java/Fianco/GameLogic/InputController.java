package Fianco.GameLogic;

import Fianco.AI.*;
import Fianco.AI.util.Eval;
import Fianco.GUI.GUI;

public class InputController {

    public static GUI gui = null;
    
    public static enum PlayerType {
        HUMAN,
        RANDOM,
        NEGAMAX,
        NEGAMAXID,
        NEGAMAXPLUS
    }

    public static boolean undo = false;
    public static Move undoBuffer = null;

    public PlayerType playerType;

    public Agent agent;

    public InputController(PlayerType playerType) {
        if (gui == null) {
            gui = new GUI();
            gui.setVisible(true);
        }
        switch (playerType) {
            case HUMAN: break;
            case RANDOM: agent = new RandomAgent(); break;
            case NEGAMAX: agent = new NegaMax(); break;
            case NEGAMAXID: agent = new NegaMaxID(); break;
            case NEGAMAXPLUS: agent = new NegaMaxPlus(); break;
        }
        this.playerType = playerType;
    }

    public Move getMove(GameState state) {
        Move move = null;
        long startTime = System.currentTimeMillis();
        if (this.playerType == PlayerType.HUMAN) {
            do {
                gui.showLegalMoves(state.legalMoves);
                move = gui.getMove();
                if (gui.undo) { // undo
                    undo = true;
                    undoBuffer = gui.movesMenuScroll.undo();
                    gui.undo = false;
                    break;
                }
                if (move == null) break; // restart
            } while (!state.legalMoves.contains(move));
            return move;
        }
        move = this.agent.getMove(state);
        System.out.println("Computed move in " + (System.currentTimeMillis() - startTime) + "ms");
        return move;
    }

    public static void refreshGUI(GameState state) {
        if (gui != null) {
            gui.updateGameState(state);
            gui.setTitle("Fianco  |  Turn " + gui.movesMenuScroll.turnCount + ": " +
                        (state.turnIsP1 ? "White" : "Black") + "  |  " +
                        ("Global Score: " + Eval.getGlobalScore(state, 0)));
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
