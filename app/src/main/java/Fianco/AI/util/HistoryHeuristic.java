package Fianco.AI.util;

import java.util.Comparator;

import Fianco.GameLogic.Move;

public class HistoryHeuristic {
    
    public int[][][] hhScore;

    public HistoryHeuristic() {
        this.hhScore = new int[81][81][2];
    }

    public void updateHistoryScore(Move move, byte depth, boolean turnIsP1) {
        this.hhScore[move.from][move.to][turnIsP1 ? 0 : 1] += depth * depth;
    }

    public final Comparator<Move> P1_COMPARATOR = (m1, m2) -> {
        return this.hhScore[m2.from][m2.to][0] - this.hhScore[m1.from][m1.to][0];
    };

    public final Comparator<Move> P2_COMPARATOR = (m1, m2) -> {
        return this.hhScore[m2.from][m2.to][1] - this.hhScore[m1.from][m1.to][1];
    };
}
