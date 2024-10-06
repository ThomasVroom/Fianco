package Fianco.GameLogic;

public class Move {
    
    public final byte from;
    public final byte to;
    public final boolean isCapture;

    public static final char[] COLS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
    public static final char[] ROWS = {'9', '8', '7', '6', '5', '4', '3', '2', '1'};

    public static final Move[][] allMoves = new Move[81][81];

    public static String toAlgebraic(byte index) {
        return "" + COLS[index % 9] + ROWS[index / 9];
    }

    public static Move getMove(byte from, byte to, boolean isCapture) {
        if (allMoves[from][to] == null) {
            allMoves[from][to] = new Move(from, to, isCapture);
        }
        return allMoves[from][to];
    }

    private Move(byte from, byte to, boolean isCapture) {
        this.from = from;
        this.to = to;
        this.isCapture = isCapture;
    }

    @Override
    public String toString() {
        return "" + toAlgebraic(from) + (isCapture ? 'x' : '-') + toAlgebraic(to);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Move)) {
            return false;
        }
        Move other = (Move)obj;
        return this.from == other.from && this.to == other.to;
    }
}
