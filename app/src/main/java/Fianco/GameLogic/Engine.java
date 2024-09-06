package Fianco.GameLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Engine implements Runnable {
    
    public GameState state;
    public Stack<Move> moveStack;
    public InputController p1;
    public InputController p2;

    public Engine() {
        state = new GameState();
        moveStack = new Stack<Move>();
        p1 = new InputController(InputController.PlayerType.HUMAN);
        p2 = new InputController(InputController.PlayerType.HUMAN);
    }

    @Override
    public void run() {
        List<Move> legalMoves;
        Move move;

        // main game loop
        while (!isGameOver()) {
            InputController.refreshGUI(this.state);
            legalMoves = computeLegalMoves();
            System.out.print("\033\143"); // clear terminal
            System.out.println("Turn: " + (this.state.turnIsP1 ? "White" : "Black"));
            move = (this.state.turnIsP1 ? p1 : p2).getMove(this.state, legalMoves);
            this.step(move);
        }

        // declare winner
        InputController.refreshGUI(this.state);
        for (int i = 0; i < 9; i++) {
            if (state.board[0][i] == 1) {
                System.out.println("White wins!");
                return;
            }
            if (state.board[8][i] == 2) {
                System.out.println("Black wins!");
                return;
            }
        }
        System.out.println("It's a draw!");
    }

    public void step(Move move) {
        state.step(move);
        moveStack.push(move);
    }

    public boolean isGameOver() {
        // check if either player has reached the last row
        for (int i = 0; i < 9; i++) {
            if (state.board[0][i] == 1 || state.board[8][i] == 2) {
                return true;
            }
        }

        // check if draw
        if (moveStack.size() >= 5) {
            Move lastMove = moveStack.peek();
            if (lastMove.equals(moveStack.get(moveStack.size() - 3).inverse())) {
                if (lastMove.equals(moveStack.get(moveStack.size() - 5))) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<Move> computeLegalMoves() {
        List<Move> legalMoves = new ArrayList<Move>(10);
        List<byte[]> positions = new ArrayList<byte[]>(15);

        // find all pieces of current player
        for (byte i = 0; i < 9; i++) {
            for (byte j = 0; j < 9; j++) {
                if (this.state.turnIsP1 && this.state.board[i][j] == 1) {
                    positions.add(new byte[] {i, j});
                    // check if capture is possible
                    if (i > 1) {
                        if (j > 1 && this.state.board[i-1][j-1] == 2 && this.state.board[i-2][j-2] == 0) {
                            legalMoves.add(new Move(i, j, (byte)(i-2), (byte)(j-2), true));
                        }
                        if (j < 7 && this.state.board[i-1][j+1] == 2 && this.state.board[i-2][j+2] == 0) {
                            legalMoves.add(new Move(i, j, (byte)(i-2), (byte)(j+2), true));
                        }

                    }
                }
                else if (!this.state.turnIsP1 && this.state.board[i][j] == 2) {
                    positions.add(new byte[] {i, j});
                    // check if capture is possible
                    if (i < 7) {
                        if (j > 1 && this.state.board[i+1][j-1] == 1 && this.state.board[i+2][j-2] == 0) {
                            legalMoves.add(new Move(i, j, (byte)(i+2), (byte)(j-2), true));
                        }
                        if (j < 7 && this.state.board[i+1][j+1] == 1 && this.state.board[i+2][j+2] == 0) {
                            legalMoves.add(new Move(i, j, (byte)(i+2), (byte)(j+2), true));
                        }
                    }
                }
            }
        }

        // return if captures are possible
        if (legalMoves.size() > 0) {
            return legalMoves;
        }

        // no captures possible, add all possible moves
        for (byte[] pos : positions) {
            if (this.state.turnIsP1) {
                    if (pos[0] > 0 && this.state.board[pos[0]-1][pos[1]] == 0) {
                        legalMoves.add(new Move(pos[0], pos[1], (byte)(pos[0]-1), pos[1], false));
                    }
                    if (pos[1] > 0 && this.state.board[pos[0]][pos[1]-1] == 0) {
                        legalMoves.add(new Move(pos[0], pos[1], pos[0], (byte)(pos[1]-1), false));
                    }
                    if (pos[1] < 8 && this.state.board[pos[0]][pos[1]+1] == 0) {
                        legalMoves.add(new Move(pos[0], pos[1], pos[0], (byte)(pos[1]+1), false));
                    }
            }
            else {
                if (pos[0] < 8 && this.state.board[pos[0]+1][pos[1]] == 0) {
                    legalMoves.add(new Move(pos[0], pos[1], (byte)(pos[0]+1), pos[1], false));
                }
                if (pos[1] > 0 && this.state.board[pos[0]][pos[1]-1] == 0) {
                    legalMoves.add(new Move(pos[0], pos[1], pos[0], (byte)(pos[1]-1), false));
                }
                if (pos[1] < 8 && this.state.board[pos[0]][pos[1]+1] == 0) {
                    legalMoves.add(new Move(pos[0], pos[1], pos[0], (byte)(pos[1]+1), false));
                }
            }
        }

        return legalMoves;
    }
}
