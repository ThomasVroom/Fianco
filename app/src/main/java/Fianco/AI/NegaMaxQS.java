package Fianco.AI;

import Fianco.AI.util.Eval;
import Fianco.AI.util.HistoryHeuristic;
import Fianco.AI.util.KillerMoves;
import Fianco.AI.util.TranspositionTable;
import Fianco.AI.util.TranspositionTable.Entry;
import Fianco.AI.util.TranspositionTable.Flag;
import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public class NegaMaxQS implements Agent {

    public static final int DELTA = 100;
    public static final int TARGET_TIME = 8000;
    public static final int MAX_DEPTH = 64;
    public static final int MIN_DEPTH = 8;

    public TranspositionTable tt = new TranspositionTable();
    public KillerMoves killerMoves = new KillerMoves(MAX_DEPTH);
    public HistoryHeuristic hh = new HistoryHeuristic();

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
                this.timeOut = false;
                System.out.println("\u001B[31mFail high (" + score + ">=" + guess + ") at depth " + DEPTH + "\u001B[0m");
                score = negamax(state, DEPTH, score, Eval.MAX_VALUE);
            }
            else if (score <= guess - DELTA) { // fail low
                this.timeOut = false;
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
                            " at depth " + DEPTH + " (~" + moveCounter + "/" + (state.legalMoves.size() + 1) + ")");
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
            return quiescenceSearch(s, alpha, beta, depth);
        }

        // negamax search
        short score = Short.MIN_VALUE, value;
        Move bestMove = null;

        // try the tt move first
        if (n != null && n.bestMove != null) {
            if (n.bestMove.isCapture) {
                value = (short)-negamax(s.deepStep(n.bestMove), depth, -beta, -alpha);
            }
            else {
                value = (short)-negamax(s.deepStep(n.bestMove), (byte)(depth - 1), -beta, -alpha);
            }
            if (value > score) {
                score = value;
                bestMove = n.bestMove;
            }
            alpha = Math.max(alpha, score);
            if (score >= beta) { // beta cutoff
                if (!n.bestMove.isCapture) this.hh.updateHistoryScore(n.bestMove, depth, s.turnIsP1);
            }
            if (DEPTH >= MIN_DEPTH && depth >= this.timeCheckDepth
                    && System.currentTimeMillis() - this.startTime > this.timeLimit) {
                this.timeOut = true; // time is up
            }
        }

        // try the killer moves
        if (score < beta && !this.timeOut) {
            s.computeLegalMoves();
            if (n != null && n.bestMove != null) s.legalMoves.remove(n.bestMove);
            for (Move km : this.killerMoves.getKillerMoves(depth)) {
                if (km == null) break; // no more killer moves
                if (!s.legalMoves.remove(km)) continue; // not a legal move
                value = (short)-negamax(s.deepStep(km), (byte)(depth - 1), -beta, -alpha);
                if (value > score) {
                    score = value;
                    bestMove = km;
                }
                alpha = Math.max(alpha, score);
                if (score >= beta) { // beta cutoff
                    this.hh.updateHistoryScore(km, depth, s.turnIsP1);
                    break;
                }
                if (DEPTH >= MIN_DEPTH && depth >= this.timeCheckDepth
                        && System.currentTimeMillis() - this.startTime > this.timeLimit) {
                    this.timeOut = true;
                    break; // time is up
                }
            }
        }

        // try all other moves
        if (score < beta && !this.timeOut) {
            // sort by history heuristic
            s.legalMoves.sort(s.turnIsP1 ? this.hh.P1_COMPARATOR : this.hh.P2_COMPARATOR);

            for (Move m : s.legalMoves) {
                if (depth == this.DEPTH) this.moveCounter++; // for printing
                if (m.isCapture) {
                    value = (short)-negamax(s.deepStep(m), depth, -beta, -alpha);
                }
                else {
                    value = (short)-negamax(s.deepStep(m), (byte)(depth - 1), -beta, -alpha);
                }
                if (value > score) {
                    score = value;
                    bestMove = m;
                }
                alpha = Math.max(alpha, score);
                if (score >= beta) { // beta cutoff
                    // update heuristics
                    if (!m.isCapture) {
                        this.killerMoves.addKillerMove(depth, m);
                        this.hh.updateHistoryScore(m, depth, s.turnIsP1);
                    }
                    break;
                }
                if (DEPTH >= MIN_DEPTH && depth >= this.timeCheckDepth
                        && System.currentTimeMillis() - this.startTime > this.timeLimit) {
                    this.timeOut = true;
                    break; // time is up
                }
            }
        }

        // store the result in the transposition table
        Flag flag = score <= oldAlpha ? Flag.UPPERBOUND : (score >= beta ? Flag.LOWERBOUND : Flag.EXACT);
        if (!this.timeOut || depth == this.DEPTH) this.tt.store(s, score, flag, bestMove, depth);
        return score;
    }

    public short quiescenceSearch(GameState s, int alpha, int beta, byte depth) {
        short score = (short)((s.turnIsP1 ? 1 : -1) * Eval.getGlobalScore(s, depth));
        if (score >= beta || s.p1Win || s.p2Win) return score;
        if (score > alpha) alpha = score;
        s.computeLegalMoves();
        for (Move m : s.legalMoves) {
            if (!m.isCapture) break; // only consider captures
            score = (short)-quiescenceSearch(s.deepStep(m), -beta, -alpha, (byte)(depth - 1));
            if (score >= beta) break;
            if (score > alpha) alpha = score;
        }
        return score;
    }
}
