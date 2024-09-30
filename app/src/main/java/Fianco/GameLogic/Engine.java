package Fianco.GameLogic;

import Fianco.GameLogic.InputController.PlayerType;

public class Engine implements Runnable {
    
    public GameState state;
    public InputController p1;
    public InputController p2;

    public Engine(PlayerType p1Type, PlayerType p2Type) {
        state = GameState.initialState();
        p1 = new InputController(p1Type);
        p2 = new InputController(p2Type);
    }

    public Engine(GameState state, PlayerType p1Type, PlayerType p2Type) {
        this.state = state;
        p1 = new InputController(p1Type);
        p2 = new InputController(p2Type);
    }

    @Override
    public void run() {
        Move move;

        // main game loop
        while (!(this.state.p1Win || this.state.p2Win)) {
            InputController.refreshGUI(this.state);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            move = (this.state.turnIsP1 ? p1 : p2).getMove(this.state);
            if (InputController.undo) { // undo
                this.state.undo(move);
                this.state.undo(InputController.undoBuffer);
                InputController.undo = false;
            }
            else if (move == null) { // restart
                this.state = GameState.initialState();
                InputController.resetGUI();
            }
            else this.step(move);
        }

        // declare winner
        InputController.refreshGUI(this.state);
        if(this.state.p1Win) System.out.println("White wins!");
        else System.out.println("Black wins!");

        // wait for restart
        while (!InputController.gui.restart) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        InputController.gui.restart = false;
        InputController.resetGUI();
        Thread game = new Thread(new Engine(p1.playerType, p2.playerType));
        game.start();
    }

    public void step(Move move) {
        this.state.step(move);
        InputController.addMoveToGUI(move);
    }
}
