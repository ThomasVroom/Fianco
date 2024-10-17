package Fianco.AI.util;

import Fianco.GameLogic.GameState;

public class Eval {

    public static final int MAX_VALUE = 30000;
    public static final int MIN_VALUE = -MAX_VALUE;

    public static final int WIN_P1 = 10000;
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

        // calculate the row sums
        int[] rowSumP1 = new int[8]; byte rowSumsUnder2P1 = 0;
        int[] rowSumP2 = new int[8]; byte rowSumsUnder2P2 = 0;
        for (byte p1 : state.p1Pieces) {
            rowSumP1[8 - p1 / 9]++;
        }
        for (int row : rowSumP1) if (row <= 2) rowSumsUnder2P1++;
        for (byte p2 : state.p2Pieces) {
            rowSumP2[p2 / 9]++;
        }
        for (int row : rowSumP2) if (row <= 2) rowSumsUnder2P2++;

        // calculate the score
        return 100 * material + 2 * (rowSumsUnder2P1 - rowSumsUnder2P2) + (distanceP2 - distanceP1);
    }
}
