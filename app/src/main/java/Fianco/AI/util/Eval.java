package Fianco.AI.util;

import Fianco.GameLogic.GameState;

public class Eval {

    public static final int MAX_VALUE = 10000;
    public static final int MIN_VALUE = -10000;
    
    // calculates the score of the current state from the perspective of p1
    // depth is an optional parameter used to give more weight to faster wins
    public static int getGlobalScore(GameState state, int depth) {
        // check if the game is over
        if (state.p1Win) return 1000 + depth;
        if (state.p2Win) return -1000 - depth;

        // calculate the piece advantage
        int material = state.p1Pieces.size() - state.p2Pieces.size();

        // calculate distance to the goal
        int distanceP1 = state.p1Pieces.first() / 9;
        int distanceP2 = 8 - state.p2Pieces.last() / 9;

        // calculate the score
        return material*material + (distanceP2 - distanceP1);
    }

    // returns true if the score is associated with a win
    public static boolean scoreIsWin(int score, boolean turnIsP1) {
        return (score > 1000 && turnIsP1) || (score < -1000 && !turnIsP1);
    }
}
