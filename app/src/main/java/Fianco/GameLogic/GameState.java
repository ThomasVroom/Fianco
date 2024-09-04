package Fianco.GameLogic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class GameState implements Serializable {
    
    public byte[][] board;
    public short currentTurn;
    public boolean turnIsP1;

    public GameState() {
        this.board = loadDefaultBoard();
        this.currentTurn = 0;
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

    /**
     * Load a game state from a file.
     * Source: https://stackoverflow.com/a/10654435
     * @param filename
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public GameState(String filename) throws IOException {
        FileInputStream fin = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fin);
        GameState state = null;
        try {state = (GameState)ois.readObject();} catch (ClassNotFoundException e) {}
        ois.close();
        this.board = state.board.clone();
        this.currentTurn = state.currentTurn;
        this.turnIsP1 = state.turnIsP1;
    }

    /**
     * Save the game state to a file.
     * Source: https://stackoverflow.com/a/10654435
     * @param filename
     * @throws IOException
     */
    public void saveToFile(String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
    }
}
