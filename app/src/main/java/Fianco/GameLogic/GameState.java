package Fianco.GameLogic;

public class GameState {
    
    public byte[][] board;
    public boolean turnIsP1;

    public GameState() {
        this.board = loadDefaultBoard();
        this.turnIsP1 = true;
    }

    private static byte[][] loadDefaultBoard() {
        return new byte[][] {{2, 2, 2, 2, 2, 2, 2, 2, 2},
                             {0, 2, 0, 0, 0, 0, 0, 2, 0},
                             {0, 0, 2, 0, 0, 0, 2, 0, 0},
                             {0, 0, 0, 2, 0, 2, 0, 0, 0},
                             {0, 0, 0, 0, 0, 0, 0, 0, 0},
                             {0, 0, 0, 1, 0, 1, 0, 0, 0},
                             {0, 0, 1, 0, 0, 0, 1, 0, 0},
                             {0, 1, 0, 0, 0, 0, 0, 1, 0},
                             {1, 1, 1, 1, 1, 1, 1, 1, 1}};
    }

    public void step(Move move) {
        this.board[move.toX][move.toY] = this.board[move.fromX][move.fromY];
        this.board[move.fromX][move.fromY] = 0;
        if (move.isCapture) this.board[(move.fromX + move.toX) / 2][(move.fromY + move.toY) / 2] = 0;
        this.turnIsP1 = !this.turnIsP1;
    }
}
