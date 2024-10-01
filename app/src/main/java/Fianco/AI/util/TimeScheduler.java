package Fianco.AI.util;

public class TimeScheduler {
    
    // target time per move in milliseconds
    public static final int TARGET_TIME = 6000;

    public int nMoves = 0;

    // returns the time limit in milliseconds
    public int timeForMove(int nMoves) {
        return (int)(TARGET_TIME * (2 - Math.min(nMoves, 10) / 10.0f));
    }
}
