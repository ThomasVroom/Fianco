package Fianco.AI;

import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public interface Agent {
    public Move getMove(GameState state, int timeLimit);
}
