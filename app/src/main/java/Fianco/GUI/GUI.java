package Fianco.GUI;

import java.awt.Dimension;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

/**
 * Main class for the GUI.
 */
public class GUI extends JFrame {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 620;

    public Grid grid;
    
    public GUI() {
        this.setTitle("Fianco");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("Options"));
        menuBar.add(new JMenu("Moves"));
        menuBar.add(new JMenu("Help"));
        this.setJMenuBar(menuBar);

        this.grid = new Grid();
        new UserInput(this.grid);
        this.add(this.grid);
        this.pack();

        this.setLocationRelativeTo(null);
    }

    public void updateGameState(GameState gameState) {
        this.grid.updateGameState(gameState);
        this.grid.repaint();
    }

    public Move getMove() {
        // allow the user to move a piece
        grid.allowMove = true;
        grid.repaint();

        // wait for the user to make a move
        while (grid.target == -1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Move move = new Move((byte)grid.selected, (byte)grid.target, Math.abs(grid.target - grid.selected) > 9);

        // reset util variables
        grid.cursor[0] = -1;
        grid.cursor[1] = -1;
        grid.selected = -1;
        grid.target = -1;
        grid.allowMove = false;
        grid.legalMoves = null;

        return move;
    }

    // shows the legal moves on the gui
    public void showLegalMoves(List<Move> legalMoves) {
        grid.legalMoves = legalMoves;
    }
}
