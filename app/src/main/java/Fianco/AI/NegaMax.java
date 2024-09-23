package Fianco.AI;

import java.util.Comparator;

import Fianco.AI.util.Eval;
import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public class NegaMax implements Agent {

    public static final int DEPTH = 8;

    private MoveComparator moveComparator = new MoveComparator();

    @Override
    public Move getMove(GameState state) {
        int score = negamax(state, DEPTH, Eval.MIN_VALUE, Eval.MAX_VALUE);
        System.out.println("Selected move: " + state.legalMoves.get(this.bestMove) + " with score: " + score);
        return state.legalMoves.get(this.bestMove);
    }

    private int bestMove;

    private int negamax(GameState s, int depth, int alpha, int beta) {
        if (depth == 0 || s.p1Win || s.p2Win) { // maximum depth or terminal state
            return (s.turnIsP1 ? 1 : -1) * Eval.getGlobalScore(s, DEPTH - depth);
        }

        // sort moves
        s.legalMoves.sort(moveComparator);

        int score = Integer.MIN_VALUE, value;
        for (int i = 0; i < s.legalMoves.size(); i++) {
            value = -negamax(s.deepStep(s.legalMoves.get(i)), depth - 1, -beta, -alpha);
            if (value > score) {
                score = value;
                if (depth == DEPTH) { // update the move if we are at the root
                    this.bestMove = i;
                }
            }
            alpha = Math.max(alpha, score);
            if (score >= beta) { // beta cutoff
                break;
            }
        }
        return score;
    }

    private class MoveComparator implements Comparator<Move> {
        @Override
        public int compare(Move a, Move b) { // sort moves by captures first
            if (a.isCapture && !b.isCapture) {
                return 1;
            } else if (!a.isCapture && b.isCapture) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
