package Fianco.GameLogic;

public class Move {
    
    public final byte fromX;
    public final byte fromY;
    public final byte toX;
    public final byte toY;
    public final boolean isCapture;

    public static final char[] COLS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
    public static final char[] ROWS = {'9', '8', '7', '6', '5', '4', '3', '2', '1'};

    public Move(byte fromX, byte fromY, byte toX, byte toY, boolean isCapture) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.isCapture = isCapture;
    }

    public Move inverse() {
        return new Move(toX, toY, fromX, fromY, isCapture);
    }

    @Override
    public String toString() {
        return "" + COLS[fromY] + ROWS[fromX] + (isCapture ? 'x' : '-') + COLS[toY] + ROWS[toX];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Move)) {
            return false;
        }
        Move other = (Move)obj;
        return this.fromX == other.fromX && this.fromY == other.fromY && this.toX == other.toX && this.toY == other.toY;
    }
}
