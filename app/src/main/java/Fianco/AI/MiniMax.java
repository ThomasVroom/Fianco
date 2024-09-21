package Fianco.AI;

import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public class MiniMax implements Agent {

    public static final int DEPTH = 3;

    @Override
    public Move getMove(GameState state) {
        int[] scoreMovePair = minimax(state, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, state.turnIsP1);
        System.out.println("Selected move: " + state.legalMoves.get(scoreMovePair[1]) + " with score: " + scoreMovePair[0]);
        return state.legalMoves.get(scoreMovePair[1]);
    }

    private int[] minimax(GameState s, int depth, int alpha, int beta, boolean max) {
        if (depth == 0 || s.p1Win || s.p2Win) { // maximum depth or terminal state
            return new int[] {Eval.getGlobalScore(s, depth), 0};
        }

        int[] scoreMovePair, value;
        if (max) { // maximizing player
            scoreMovePair = new int[] {Integer.MIN_VALUE, 0};
            for (int i = 0; i < s.legalMoves.size(); i++) { // for each child
                value = minimax(s.deepStep(s.legalMoves.get(i)), depth - 1, alpha, beta, false);
                if (value[0] > scoreMovePair[0]) {
                    scoreMovePair[0] = value[0];
                    if (depth == DEPTH) { // update the move if we are at the root
                        scoreMovePair[1] = i;
                    }
                }
                alpha = Math.max(alpha, scoreMovePair[0]);
                if (scoreMovePair[0] >= beta) { // beta cutoff
                    break;
                }
            }
        }
        else { // minimizing player
            scoreMovePair = new int[] {Integer.MAX_VALUE, 0};
            for (int i = 0; i < s.legalMoves.size(); i++) { // for each child
                value = minimax(s.deepStep(s.legalMoves.get(i)), depth - 1, alpha, beta, true);
                if (value[0] < scoreMovePair[0]) {
                    scoreMovePair[0] = value[0];
                    if (depth == DEPTH) { // update the move if we are at the root
                        scoreMovePair[1] = i;
                    }
                }
                beta = Math.min(beta, scoreMovePair[0]);
                if (scoreMovePair[0] <= alpha) { // alpha cutoff
                    break;
                }
            }
        }
        return scoreMovePair;
    }
}
