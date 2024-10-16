package Fianco.AI;

import Fianco.AI.util.Eval;
import Fianco.AI.util.TranspositionTable;
import Fianco.AI.util.TranspositionTable.Entry;
import Fianco.AI.util.TranspositionTable.Flag;
import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public class NegaMaxID implements Agent {

    public static final int DELTA = 10;
    public static final int TARGET_TIME = 5000;
    public static final int MAX_DEPTH = 64;
    public static final int MIN_DEPTH = 8;

    public TranspositionTable tt = new TranspositionTable();

    public byte DEPTH;
    public int moveCounter;

    // util for time management
    public long startTime;
    public int timeLimit;
    public boolean timeOut;
    public int nMoves;
    public int timeCheckDepth;

    @Override
    public Move getMove(GameState state) {
        // if there is only one legal move, return it
        if (state.legalMoves.size() == 1) return state.legalMoves.get(0);

        // set time limit
        this.timeLimit = (int)(TARGET_TIME * (2 - Math.min(nMoves++, 10) / 10.0f));
        this.startTime = System.currentTimeMillis();
        this.timeOut = false;

        // aspiration search
        int guess = (state.turnIsP1 ? 1 : -1) * Eval.getGlobalScore(state, 0);
        for (DEPTH = 1; DEPTH < MAX_DEPTH; DEPTH++) {
            this.timeCheckDepth = (int)(0.9f * DEPTH);
            this.moveCounter = 0;
            int score = negamax(state, DEPTH, guess - DELTA, guess + DELTA);
            if (score >= guess + DELTA) { // fail high
                this.moveCounter = 0; this.timeOut = false;
                System.out.println("\u001B[31mFail high (" + score + ">=" + guess + ") at depth " + DEPTH + "\u001B[0m");
                score = negamax(state, DEPTH, score, Eval.MAX_VALUE);
            }
            else if (score <= guess - DELTA) { // fail low
                this.moveCounter = 0; this.timeOut = false;
                System.out.println("\u001B[31mFail low (" + score + "<=" + guess + ") at depth " + DEPTH + "\u001B[0m");
                score = negamax(state, DEPTH, Eval.MIN_VALUE, score);
            }
            guess = score;

            // early exit if we have a winning move
            if (score >= Eval.WIN_P1) {
                System.out.println("\u001B[32mEarly Termination: win in "+(DEPTH - Math.abs(score) + Eval.WIN_P1)+" moves\u001B[0m");
                break;
            }

            // check for a time out
            if (this.timeOut) break;
        }

        Move bestMove = tt.retrieve(state).bestMove;
        System.out.println("(" + (state.turnIsP1 ? "W" : "B") + ") Selected move: " + bestMove + " with score: " + guess +
                            " at depth " + DEPTH + " (" + moveCounter + "/" + state.legalMoves.size() + ")");
        return bestMove;
    }

    public short negamax(GameState s, byte depth, int alpha, int beta) {
        // retreive transposition table information
        int oldAlpha = alpha;
        Entry n = this.tt.retrieve(s);
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

        // maximum depth or terminal state
        if (depth == 0 || s.p1Win || s.p2Win) {
            return (short)((s.turnIsP1 ? 1 : -1) * Eval.getGlobalScore(s, depth));
        }

        // try the tt move first
        if (s.legalMoves == null) s.computeLegalMoves();
        if (n != null && n.bestMove != null) {
            s.legalMoves.add(0, n.bestMove);
        }

        short score = Short.MIN_VALUE, value;
        Move bestMove = null;
        for (Move m : s.legalMoves) {
            if (depth == this.DEPTH) this.moveCounter++; // for printing
            value = (short)-negamax(s.deepStep(m), (byte)(depth - 1), -beta, -alpha);
            if (value > score) {
                score = value;
                bestMove = m;
            }
            alpha = Math.max(alpha, score);
            if (score >= beta) {
                break; // beta cutoff
            }
            if (DEPTH >= MIN_DEPTH && depth >= this.timeCheckDepth
                    && System.currentTimeMillis() - this.startTime > this.timeLimit) {
                this.timeOut = true; // time is up
            }
        }

        // store the result in the transposition table
        Flag flag = score <= oldAlpha ? Flag.UPPERBOUND : (score >= beta ? Flag.LOWERBOUND : Flag.EXACT);
        if (!this.timeOut || depth == this.DEPTH) this.tt.store(s, score, flag, bestMove, depth);
        return score;
    }
}
