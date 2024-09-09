package Fianco.GameLogic;

public class Move {
    
    public final byte from;
    public final byte to;
    public final boolean isCapture;

    public static final char[] COLS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
    public static final char[] ROWS = {'9', '8', '7', '6', '5', '4', '3', '2', '1'};

    public Move(byte from, byte to, boolean isCapture) {
        this.from = from;
        this.to = to;
        this.isCapture = isCapture;
    }

    public Move inverse() {
        return new Move(to, from, isCapture);
    }

    @Override
    public String toString() {
        return "" + COLS[from % 9] + ROWS[from / 9] + (isCapture ? 'x' : '-') + COLS[to % 9] + ROWS[to / 9];
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
