package Fianco.AI.util;

import java.util.Comparator;

import Fianco.GameLogic.Move;

public class HistoryHeuristic {
    
    public int[][] hhScore;

    public HistoryHeuristic() {
        this.hhScore = new int[81][81];
    }

    public void updateHistoryScore(Move move) {
        this.hhScore[move.from][move.to]++;
    }

    public final Comparator<Move> COMPARATOR = (m1, m2) -> {
        return this.hhScore[m2.from][m2.to] - this.hhScore[m1.from][m1.to];
    };
}
