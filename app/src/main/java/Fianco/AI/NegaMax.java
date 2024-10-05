package Fianco.AI;

import Fianco.AI.util.Eval;
import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public class NegaMax implements Agent {

    public static final int DEPTH = 8;

    @Override
    public Move getMove(GameState state) {
        if (state.legalMoves.size() == 1) return state.legalMoves.get(0);
        int score = negamax(state, DEPTH, Eval.MIN_VALUE, Eval.MAX_VALUE);
        System.out.println("Selected move: " + this.bestMove + " with score: " + score);
        return this.bestMove;
    }

    private Move bestMove;

    private int negamax(GameState s, int depth, int alpha, int beta) {
        if (depth == 0 || s.p1Win || s.p2Win) { // maximum depth or terminal state
            return (s.turnIsP1 ? 1 : -1) * Eval.getGlobalScore(s, depth);
        }

        int score = Integer.MIN_VALUE, value;
        if (s.legalMoves == null) s.computeLegalMoves();
        for (Move m : s.legalMoves) {
            value = -negamax(s.deepStep(m), depth - 1, -beta, -alpha);
            if (value > score) {
                score = value;
                if (depth == DEPTH) { // update the move if we are at the root
                    this.bestMove = m;
                }
            }
            alpha = Math.max(alpha, score);
            if (score >= beta) { // beta cutoff
                break;
            }
        }
        return score;
    }
}
