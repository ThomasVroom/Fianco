package Fianco.AI.util;

import Fianco.GameLogic.Move;

public class KillerMoves {
    
    public Move[][] killerMoves;

    public KillerMoves(int maxDepth) {
        this.killerMoves = new Move[maxDepth + 1][2];
    }

    public void addKillerMove(int depth, Move move) {
        // check first slot
        if (this.killerMoves[depth][0] == null) {
            this.killerMoves[depth][0] = move;
            return;
        }
        if (move.equals(this.killerMoves[depth][0])) return;

        // check second slot
        if (this.killerMoves[depth][1] == null) {
            this.killerMoves[depth][1] = move;
            return;
        }
        if (move.equals(this.killerMoves[depth][1])) return;

        // shift all moves right
        this.killerMoves[depth][1] = this.killerMoves[depth][0];
        this.killerMoves[depth][0] = move;
    }

    public Move[] getKillerMoves(int depth) {
        return this.killerMoves[depth];
    }
}
