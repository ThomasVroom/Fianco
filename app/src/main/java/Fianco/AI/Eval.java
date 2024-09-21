package Fianco.AI;

import Fianco.GameLogic.GameState;

public class Eval {
    
    // calculates the score of the current state from the perspective of p1
    // depth is an optional parameter used to give more weight to faster wins
    public static int getGlobalScore(GameState state, int depth) {
        // check if the game is over
        if (state.p1Win) return 1000000 + depth;
        if (state.p2Win) return -1000000 - depth;

        // calculate the piece advantage
        int material = state.p1Pieces.size() - state.p2Pieces.size();

        // calculate distance to the goal
        int distanceP1 = state.p1Pieces.first() / 9;
        int distanceP2 = 8 - state.p2Pieces.last() / 9;

        // calculate the score
        return material + (distanceP2 - distanceP1);
    }
}
