package Fianco.GameLogic;

import java.util.BitSet;

public class GameState {

    // https://stackoverflow.com/a/69290602
    public static BitSet shift(BitSet bitset, int shiftAmount) {
        BitSet b = new BitSet(81);
        bitset.stream().map(bitPos -> bitPos + shiftAmount)
                .dropWhile(bitPos -> bitPos < 0)
                .forEach(bitPos -> b.set(bitPos));
        return b;
    }

    // mask applied when shifting left to prevent wrapping
    public static final BitSet shiftLeftMask = BitSet.valueOf(new long[] {
        0b00000000100000000100000000100000000100000000100000000100000000100000000L,
        0b000000010000000010000000L
    });

    // mask applied when shifting right to prevent wrapping
    public static final BitSet shiftRightMask = BitSet.valueOf(new long[] {
        0b1000000001000000001000000001000000001000000001000000001000000001L,
        0b00000000100000000100000000L
    });

    // mask applied when shifting down to prevent wrapping
    public static final BitSet shiftDownMask = BitSet.valueOf(new long[] {
        0b0000000000000000000000000000000000000000000000000000000000000000L,
        0b000000000000000011111111100000000000000000L
    });

    public BitSet bitBoardP1 = new BitSet(81);
    public BitSet bitBoardP2 = new BitSet(81);
    
    public boolean turnIsP1;

    // returns a new game state with the initial board setup
    public static GameState initialState() {
        GameState s = new GameState();

        s.bitBoardP1.set(80);
        s.bitBoardP1.set(79);
        s.bitBoardP1.set(78);
        s.bitBoardP1.set(77);
        s.bitBoardP1.set(76);
        s.bitBoardP1.set(75);
        s.bitBoardP1.set(74);
        s.bitBoardP1.set(73);
        s.bitBoardP1.set(72);

        s.bitBoardP1.set(70);
        s.bitBoardP1.set(64);

        s.bitBoardP1.set(60);
        s.bitBoardP1.set(56);

        s.bitBoardP1.set(50);
        s.bitBoardP1.set(48);

        s.bitBoardP2.set(0);
        s.bitBoardP2.set(1);
        s.bitBoardP2.set(2);
        s.bitBoardP2.set(3);
        s.bitBoardP2.set(4);
        s.bitBoardP2.set(5);
        s.bitBoardP2.set(6);
        s.bitBoardP2.set(7);
        s.bitBoardP2.set(8);

        s.bitBoardP2.set(10);
        s.bitBoardP2.set(16);

        s.bitBoardP2.set(20);
        s.bitBoardP2.set(24);

        s.bitBoardP2.set(30);
        s.bitBoardP2.set(32);

        s.turnIsP1 = true;

        return s;
    }

    public boolean isGameOver() {
        return this.p1Wins() || this.p2Wins();
        // TODO: check for draw
    }

    public boolean p1Wins() {
        return this.bitBoardP1.nextSetBit(0) < 9;
    }

    public boolean p2Wins() {
        return this.bitBoardP2.nextSetBit(72) > 0;
    }

    public void step(Move move) {
        BitSet from = this.turnIsP1 ? this.bitBoardP1 : this.bitBoardP2;

        from.clear(move.from);
        from.set(move.to);

        if (move.isCapture) {
            BitSet to = this.turnIsP1 ? this.bitBoardP2 : this.bitBoardP1;
            to.clear((move.from + move.to) / 2);
        }

        this.turnIsP1 = !this.turnIsP1;
    }

    // returns a bitset of legal moves for the current player
    public BitSet computeLegalMoves() {
        BitSet legalMoves = new BitSet(81);
        BitSet positions = this.turnIsP1 ? this.bitBoardP1 : this.bitBoardP2;

        // TODO: check if capture is possible

        BitSet up = shift(positions, this.turnIsP1 ? -9 : 9);
        BitSet left = shift(positions, -1);
        BitSet right = shift(positions, 1);

        up.andNot(this.bitBoardP1);
        up.andNot(this.bitBoardP2);
        if (!this.turnIsP1) up.andNot(shiftDownMask);

        left.andNot(this.bitBoardP1);
        left.andNot(this.bitBoardP2);
        left.andNot(shiftLeftMask);

        right.andNot(this.bitBoardP1);
        right.andNot(this.bitBoardP2);
        right.andNot(shiftRightMask);

        legalMoves.or(up);
        legalMoves.or(left);
        legalMoves.or(right);

        return legalMoves;
    }
}
