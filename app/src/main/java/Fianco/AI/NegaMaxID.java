package Fianco.AI;

import java.util.Comparator;

import Fianco.AI.util.Eval;
import Fianco.AI.util.TranspositionTable;
import Fianco.AI.util.TranspositionTable.Entry;
import Fianco.AI.util.TranspositionTable.Flag;
import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public class NegaMaxID implements Agent {

    public static TranspositionTable tt = new TranspositionTable();

    public static byte DEPTH;
    public static final int DELTA = 6;

    private MoveComparator moveComparator = new MoveComparator();

    @Override
    public Move getMove(GameState state) {
        int guess = 0; // aspiration search
        for (DEPTH = 1; DEPTH <= 9; DEPTH++) {
            int score = negamax(state, DEPTH, guess - DELTA, guess + DELTA);
            if (score >= guess + DELTA) { // fail high
                System.out.println("\u001B[31mFail high (" + score + ">=" + guess + ") at depth " + DEPTH + "\u001B[0m");
                score = negamax(state, DEPTH, score, Eval.MAX_VALUE);
            }
            else if (score <= guess - DELTA) { // fail low
                System.out.println("\u001B[31mFail low (" + score + "<=" + guess + ") at depth " + DEPTH + "\u001B[0m");
                score = negamax(state, DEPTH, Eval.MIN_VALUE, score);
            }
            guess = score;
        }
        Move bestMove = tt.retrieve(state).bestMove;
        System.out.println("Selected move: " + bestMove + " with score: " + guess);
        return bestMove;
    }

    public short negamax(GameState s, byte depth, int alpha, int beta) {
        int oldAlpha = alpha;
        Entry n = tt.retrieve(s);
        if (n != null && n.depth >= depth) {
            switch (n.flag) {
                case EXACT:
                    return n.value;
                case LOWERBOUND:
                    alpha = Math.max(alpha, n.value);
                    break;
                case UPPERBOUND:
                    beta = Math.min(beta, n.value);
                    break;
            }
            if (alpha >= beta) {
                return n.value;
            }
        }

        if (depth == 0 || s.p1Win || s.p2Win) { // maximum depth or terminal state
            return (short)((s.turnIsP1 ? 1 : -1) * Eval.getInfluencedScore(s, depth));
        }

        // sort moves
        s.legalMoves.sort(moveComparator);
        if (n != null && n.bestMove != null) {
            s.legalMoves.add(0, n.bestMove);
        }

        short score = Short.MIN_VALUE, value;
        Move bestMove = null;
        for (Move m : s.legalMoves) {
            value = (short)-negamax(s.deepStep(m), (byte)(depth - 1), -beta, -alpha);
            if (value > score) {
                score = value;
                bestMove = m;
            }
            alpha = Math.max(alpha, score);
            if (score >= beta) { // beta cutoff
                break;
            }
        }

        // store the result in the transposition table
        Flag flag = score <= oldAlpha ? Flag.UPPERBOUND : (score >= beta ? Flag.LOWERBOUND : Flag.EXACT);
        tt.store(s, score, flag, bestMove, depth);
        return score;
    }

    private class MoveComparator implements Comparator<Move> {
        @Override
        public int compare(Move a, Move b) { // sort moves by captures first
            if (a.isCapture && !b.isCapture) {
                return -1;
            } else if (!a.isCapture && b.isCapture) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
