package Fianco.AI;

import java.util.List;

import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public interface Agent {
    public Move getMove(GameState state, List<Move> legalMoves);
}
