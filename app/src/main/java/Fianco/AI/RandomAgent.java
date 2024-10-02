package Fianco.AI;

import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public class RandomAgent implements Agent {

    @Override
    public Move getMove(GameState state) {
        return state.legalMoves.get((int)(Math.random() * state.legalMoves.size()));
    }
}
