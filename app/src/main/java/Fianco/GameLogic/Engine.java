package Fianco.GameLogic;

import java.util.List;

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

    @Override
    public void run() {
        List<Move> legalMoves;
        Move move;

        // main game loop
        while (!this.state.isGameOver()) {
            InputController.refreshGUI(this.state);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            legalMoves = this.state.computeLegalMoves();
            System.out.print("\033\143"); // clear terminal
            System.out.println("Turn: " + (this.state.turnIsP1 ? "White" : "Black"));
            move = (this.state.turnIsP1 ? p1 : p2).getMove(this.state, legalMoves);
            this.step(move);
        }

        // declare winner
        InputController.refreshGUI(this.state);
        if (this.state.p1Wins()) {
            System.out.println("White wins!");
            return;
        }
        if (this.state.p2Wins()) {
            System.out.println("Black wins!");
            return;
        }
    }

    public void step(Move move) {
        this.state.step(move);
    }
}
