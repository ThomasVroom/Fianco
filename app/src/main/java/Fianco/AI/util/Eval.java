package Fianco.AI.util;

import Fianco.GameLogic.GameState;

public class Eval {

    public static final int MAX_VALUE = 10000;
    public static final int MIN_VALUE = -MAX_VALUE;

    public static final int WIN_P1 = 1000;
    public static final int WIN_P2 = -WIN_P1;
    
    // calculates the score of the current state from the perspective of p1
    // depth is an optional parameter used to give more weight to faster wins
    public static int getGlobalScore(GameState state, int depth) {
        // check if the game is over
        if (state.p1Win) return WIN_P1 + depth;
        if (state.p2Win) return WIN_P2 - depth;

        // calculate the piece advantage
        int material = state.p1Pieces.size() - state.p2Pieces.size();

        // calculate distance to the goal
        int distanceP1 = state.p1Pieces.first() / 9;
        int distanceP2 = 8 - state.p2Pieces.last() / 9;

        // calculate the score
        return 3 * material + (distanceP2 - distanceP1);
    }
}
