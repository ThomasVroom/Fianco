package Fianco.GameLogic;

import java.util.TreeSet;
import java.util.SortedSet;
import java.util.List;
import java.util.ArrayList;

public class GameState {

    public SortedSet<Byte> p1Pieces;
    public SortedSet<Byte> p2Pieces;
    
    public boolean turnIsP1;

    public boolean p1Win;
    public boolean p2Win;

    public List<Move> legalMoves;

    // returns a new game state with the initial board setup
    public static GameState initialState() {
        GameState s = new GameState();

        s.p1Pieces = new TreeSet<Byte>();
        s.p1Pieces.add((byte)72);
        s.p1Pieces.add((byte)73);
        s.p1Pieces.add((byte)74);
        s.p1Pieces.add((byte)75);
        s.p1Pieces.add((byte)76);
        s.p1Pieces.add((byte)77);
        s.p1Pieces.add((byte)78);
        s.p1Pieces.add((byte)79);
        s.p1Pieces.add((byte)80);
        s.p1Pieces.add((byte)64);
        s.p1Pieces.add((byte)70);
        s.p1Pieces.add((byte)56);
        s.p1Pieces.add((byte)60);
        s.p1Pieces.add((byte)48);
        s.p1Pieces.add((byte)50);

        s.p2Pieces = new TreeSet<Byte>();
        s.p2Pieces.add((byte)0);
        s.p2Pieces.add((byte)1);
        s.p2Pieces.add((byte)2);
        s.p2Pieces.add((byte)3);
        s.p2Pieces.add((byte)4);
        s.p2Pieces.add((byte)5);
        s.p2Pieces.add((byte)6);
        s.p2Pieces.add((byte)7);
        s.p2Pieces.add((byte)8);
        s.p2Pieces.add((byte)10);
        s.p2Pieces.add((byte)16);
        s.p2Pieces.add((byte)20);
        s.p2Pieces.add((byte)24);
        s.p2Pieces.add((byte)30);
        s.p2Pieces.add((byte)32);

        s.turnIsP1 = true;

        return s;
    }

    public void step(Move move) {
        SortedSet<Byte> pieces = this.turnIsP1 ? this.p1Pieces : this.p2Pieces;

        pieces.remove(move.from);
        pieces.add(move.to);

        if (move.isCapture) {
            SortedSet<Byte> opponent = this.turnIsP1 ? this.p2Pieces : this.p1Pieces;
            opponent.remove((byte)((move.from + move.to) / 2));
        }

        this.turnIsP1 = !this.turnIsP1;

        if (!this.p1Pieces.isEmpty() && (this.p1Pieces.first() < 9 || this.p2Pieces.isEmpty())) {
            this.p1Win = true;
        }
        else if (this.p2Pieces.last() >= 72 || this.p1Pieces.isEmpty()) {
            this.p2Win = true;
        }

        this.legalMoves = null;
    }

    // performs a step and returns a new state
    public GameState deepStep(Move move) {
        // copy state
        GameState newState = new GameState();
        newState.p1Pieces = new TreeSet<Byte>();
        newState.p2Pieces = new TreeSet<Byte>();
        this.p1Pieces.forEach((piece) -> newState.p1Pieces.add(piece));
        this.p2Pieces.forEach((piece) -> newState.p2Pieces.add(piece));
        newState.turnIsP1 = this.turnIsP1;

        // perform step
        newState.step(move);
        return newState;
    }

    public void undo(Move move) {
        if (move == null) return;
        SortedSet<Byte> pieces = this.turnIsP1 ? this.p2Pieces : this.p1Pieces;

        pieces.remove(move.to);
        pieces.add(move.from);

        if (move.isCapture) {
            SortedSet<Byte> opponent = this.turnIsP1 ? this.p1Pieces : this.p2Pieces;
            byte target = (byte)((move.from + move.to) / 2);
            opponent.add(target);
        }

        this.turnIsP1 = !this.turnIsP1;
        this.p1Win = false;
        this.p2Win = false;
        this.legalMoves = null;
    }

    // computes a list of legal moves for the current player
    public void computeLegalMoves() {
        this.legalMoves = new ArrayList<Move>(20);
        SortedSet<Byte> positions = this.turnIsP1 ? this.p1Pieces : this.p2Pieces;
        SortedSet<Byte> opponent = this.turnIsP1 ? this.p2Pieces : this.p1Pieces;
        byte target, capture_target;

        // check if capture is possible
        for (byte position : positions) {
            if (this.turnIsP1) {
                // check left
                if (position % 9 > 1 && position >= 18) {
                    target = (byte)(position - 20);
                    capture_target = (byte)(position - 10);
                    if (opponent.contains(capture_target) && !positions.contains(target) && !opponent.contains(target)) {
                        this.legalMoves.add(Move.getMove(position, target, true));
                    }
                }

                // check right
                if (position % 9 < 7 && position >= 18) {
                    target = (byte)(position - 16);
                    capture_target = (byte)(position - 8);
                    if (opponent.contains(capture_target) && !positions.contains(target) && !opponent.contains(target)) {
                        this.legalMoves.add(Move.getMove(position, target, true));
                    }
                }
            }
            else {
                // check left
                if (position % 9 < 7 && position < 63) {
                    target = (byte)(position + 20);
                    capture_target = (byte)(position + 10);
                    if (opponent.contains(capture_target) && !positions.contains(target) && !opponent.contains(target)) {
                        this.legalMoves.add(Move.getMove(position, target, true));
                    }
                }

                // check right
                if (position % 9 > 1 && position < 63) {
                    target = (byte)(position + 16);
                    capture_target = (byte)(position + 8);
                    if (opponent.contains(capture_target) && !positions.contains(target) && !opponent.contains(target)) {
                        this.legalMoves.add(Move.getMove(position, target, true));
                    }
                }
            }
        }

        // capture is mandatory
        if (!this.legalMoves.isEmpty()) return;

        for (byte position : positions) {
            // check up
            if (this.turnIsP1) {
                if (position >= 9) {
                    target = (byte)(position - 9);
                    if (!positions.contains(target) && !opponent.contains(target)) {
                        this.legalMoves.add(Move.getMove(position, target, false));
                    }
                }
            }
            else {
                if (position < 72) {
                    target = (byte)(position + 9);
                    if (!positions.contains(target) && !opponent.contains(target)) {
                        this.legalMoves.add(Move.getMove(position, target, false));
                    }
                }
            }

            // check left
            if (position % 9 > 0) {
                target = (byte)(position - 1);
                if (!positions.contains(target) && !opponent.contains(target)) {
                    this.legalMoves.add(Move.getMove(position, target, false));
                }
            }

            // check right
            if (position % 9 < 8) {
                target = (byte)(position + 1);
                if (!positions.contains(target) && !opponent.contains(target)) {
                    this.legalMoves.add(Move.getMove(position, target, false));
                }
            }
        }
    }
}
