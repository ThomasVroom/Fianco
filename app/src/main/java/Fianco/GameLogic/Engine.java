package Fianco.GameLogic;

import java.util.BitSet;

public class Engine implements Runnable {
    
    public GameState state;
    public InputController p1;
    public InputController p2;

    public Engine() {
        state = GameState.initialState();
        p1 = new InputController(InputController.PlayerType.HUMAN);
        p2 = new InputController(InputController.PlayerType.HUMAN);
    }

    @Override
    public void run() {
        BitSet legalMoves;
        Move move;

        // main game loop
        while (!this.state.isGameOver()) {
            InputController.refreshGUI(this.state);
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
        System.out.println("It's a draw!");
    }

    public void step(Move move) {
        this.state.step(move);
    }
}
