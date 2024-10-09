package Fianco.GUI;

import java.awt.Dimension;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JEditorPane;
import javax.swing.UIManager;

import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

/**
 * Main class for the GUI.
 */
public class GUI extends JFrame {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 620;

    public Grid grid;
    public MoveMenu movesMenuScroll;

    public boolean restart = false;
    public boolean undo = false;
    
    public GUI() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(e -> {
            this.restart = true;
        });
        optionsMenu.add(newGame);
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        optionsMenu.add(exit);
        menuBar.add(optionsMenu);
        JMenu viewMenu = new JMenu("View");
        JMenuItem showMoveGrid1 = new JMenuItem("Show Grid Numbers");
        showMoveGrid1.addActionListener(e -> {
            grid.showGridNumbers = !grid.showGridNumbers;
            grid.showGridNames = false;
            grid.repaint();
        });
        viewMenu.add(showMoveGrid1);
        JMenuItem showMoveGrid2 = new JMenuItem("Show Grid Names");
        showMoveGrid2.addActionListener(e -> {
            grid.showGridNumbers = false;
            grid.showGridNames = !grid.showGridNames;
            grid.repaint();
        });
        viewMenu.add(showMoveGrid2);
        JMenuItem showLegalMoves = new JMenuItem("Show Legal Moves");
        showLegalMoves.addActionListener(e -> {
            grid.showLegalMoves = !grid.showLegalMoves;
            grid.repaint();
        });
        viewMenu.add(showLegalMoves);
        menuBar.add(viewMenu);
        JMenu movesMenu = new JMenu("Moves");
        JMenuItem undo = new JMenuItem("Undo");
        undo.addActionListener(e -> {
            this.undo = true;
        });
        movesMenu.add(undo);
        movesMenu.addSeparator();
        this.movesMenuScroll = new MoveMenu();
        movesMenu.add(this.movesMenuScroll);
        menuBar.add(movesMenu);
        JMenu helpMenu = new JMenu("Help");
        JMenuItem rules = new JMenuItem("Rules");
        JEditorPane rulesMessage = new JEditorPane("text/html", ""+
        "<center>Players take turns moving their stones.</center>\n"+
        "<center>Each turn a player must move one of their stones. A stone may:</center>\n"+
        "<center> ⚬ Move forwards or sideways to an adjacent empty square.</center>\n"+
        "<center> ⚬ Capture by jumping diagonally forward over an enemy stone, landing on an empty square.</center>\n"+
        "<center>Capturing is mandatory, but limited to one stone per turn.</center>\n"+
        "<center>A player wins by moving one of their stones to the opposite side of the board.</center>");
        rulesMessage.setEditable(false);
        rulesMessage.setBackground(UIManager.getColor("OptionPane.background"));
        rules.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, rulesMessage, "Rules", JOptionPane.PLAIN_MESSAGE);
        });
        helpMenu.add(rules);
        JMenuItem about = new JMenuItem("About");
        JEditorPane aboutMessage = new JEditorPane("text/html", ""+
        "<center>Developed by Thomas Vroom, Maastricht University (©2024).</center>\n"+
        "<center>Fianco is a game by Fred Horn (©1987).</center>\n"+
        "<center><a href=\"http://www.di.fc.ul.pt/~jpn/gv/fianco.htm\">http://www.di.fc.ul.pt/~jpn/gv/fianco.htm</a></center>");
        aboutMessage.setEditable(false);
        aboutMessage.setBackground(UIManager.getColor("OptionPane.background"));
        about.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, aboutMessage, "About", JOptionPane.PLAIN_MESSAGE);
        });
        helpMenu.add(about);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);

        this.grid = new Grid();
        new UserInput(this.grid);
        this.add(this.grid);
        this.pack();

        this.setLocationRelativeTo(null);
    }

    public void updateGameState(GameState gameState) {
        this.setVisible(true);
        this.grid.updateGameState(gameState);
        this.grid.repaint();
    }

    public Move getMove() {
        // allow the user to move a piece
        grid.allowMove = true;
        grid.repaint();

        // wait for the user to make a move
        while (grid.target == -1 && !this.restart && !this.undo) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Move move;
        if (this.restart) move = null;
        else if (this.undo) move = movesMenuScroll.undo();
        else move = Move.getMove((byte)grid.selected, (byte)grid.target, Math.abs(grid.target - grid.selected) > 9);

        // reset util variables
        grid.cursor[0] = -1;
        grid.cursor[1] = -1;
        grid.selected = -1;
        grid.target = -1;
        grid.allowMove = false;
        grid.legalMoves = null;
        this.restart = false;
        // this.undo = false; // handled in InputController

        return move;
    }

    // shows the legal moves on the gui
    public void showLegalMoves(List<Move> legalMoves) {
        grid.legalMoves = legalMoves;
    }

    public void addMove(Move move) {
        this.movesMenuScroll.addMove(move);
    }
}
