package Fianco.AI.util;

import Fianco.GameLogic.Move;

public class KillerMoves {

    public static final int N_MOVES = 2;
    
    public Move[][] killerMoves;

    public KillerMoves(int maxDepth) {
        this.killerMoves = new Move[maxDepth][N_MOVES];
    }

    public void addKillerMove(int depth, Move move) {
        for (int i = 0; i < N_MOVES; i++) { // first look for match or empty slot
            if (move.equals(this.killerMoves[depth][i])) return;
            if (this.killerMoves[depth][i] == null) {
                this.killerMoves[depth][i] = move;
                return;
            }
        }
        for (int i = N_MOVES - 1; i > 0; i--) { // shift all moves right
            this.killerMoves[depth][i] = this.killerMoves[depth][i - 1];
        }
        this.killerMoves[depth][0] = move;
    }

    public Move[] getKillerMoves(int depth) {
        return this.killerMoves[depth];
    }
}
