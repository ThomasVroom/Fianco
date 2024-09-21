package Fianco.AI;

import java.util.Comparator;

import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public class NegaMax implements Agent {

    private MoveComparator moveComparator = new MoveComparator();

    @Override
    public Move getMove(GameState state) {
        return null; // TODO
    }

    private int negamax(GameState s, int depth, int alpha, int beta) {
        // base case

        // sort moves
        s.legalMoves.sort(moveComparator);

        //System.out.println(depth + " ]- " + s.legalMoves.get(i) + " value: " + value + " alpha: " + alpha + " beta: " + beta + " score: " + score);
        return 0;
    }

    private class MoveComparator implements Comparator<Move> {
        @Override
        public int compare(Move a, Move b) {
            return 0; // TODO
        }
    }
}
