package Fianco.AI;

import java.util.List;

import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public class RandomAgent implements Agent {

    @Override
    public Move getMove(GameState state, List<Move> legalMoves) {
        return legalMoves.get((int)(Math.random() * legalMoves.size()));
    }
}
