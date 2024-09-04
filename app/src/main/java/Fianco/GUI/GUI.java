package Fianco.GUI;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import Fianco.GameLogic.GameState;

/**
 * Main class for the GUI.
 */
public class GUI extends JFrame {

    public static final int Width = 600;
    public static final int Height = 620;

    private Grid grid;
    
    public GUI() {
        this.setTitle("Fianco");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setPreferredSize(new Dimension(Width, Height));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("Options"));
        menuBar.add(new JMenu("Moves"));
        menuBar.add(new JMenu("Help"));
        this.setJMenuBar(menuBar);

        this.grid = new Grid();
        new UserInput(grid);
        this.add(this.grid);
        this.pack();

        this.setLocationRelativeTo(null);
    }

    public void updateGameState(GameState gameState) {
        this.grid.updateGameState(gameState);
    }
}
