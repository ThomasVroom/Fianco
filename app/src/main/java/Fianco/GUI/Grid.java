package Fianco.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import Fianco.GameLogic.GameState;

public class Grid extends JPanel {

    /**
     * How much of the cell should the piece take up.
     */
    public static final float PIECE_SCALE = 0.8f;

    public static final Color GRAY_ACCENT = new Color(170, 170, 170);

    // chess positions
    public static final String[] COLS = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
    public static final String[] ROWS = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

    // util for animating piece movement
    int[] selected = new int[] {-1, -1};
    int[] cursor = new int[] {-1, -1};

    // current game state
    private GameState gameState;

    public void updateGameState(GameState gameState) {
        this.gameState = gameState;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        Dimension vis = this.getVisibleRect().getSize();
        float[] delta = {vis.width/9.0f, vis.height/9.0f};

        // background
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, vis.width, vis.height);

        // grid
        g.setColor(Color.BLACK);
        for (int i = 0; i < 9; i++) {
            g.drawLine((int)(i * delta[0]), 0, (int)(i * delta[0]), vis.height);
            g.drawLine(0, (int)(i * delta[1]), vis.width, (int)(i * delta[1]));
        }

        // game state
        if (this.gameState == null) return;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.selected[0] == i && this.selected[1] == j) {
                    g.setColor(GRAY_ACCENT);
                    g.fillOval((int)(j * delta[0] + (1 - 0.5f*PIECE_SCALE)/2 * delta[0]),
                               (int)(i * delta[1] + (1 - 0.5f*PIECE_SCALE)/2 * delta[1]), 
                               (int)(0.5f*PIECE_SCALE * delta[0]), (int)(0.5f*PIECE_SCALE * delta[1]));
                }
                else if (this.gameState.board[i][j] == 1) {
                    g.setColor(Color.WHITE);
                    g.fillOval((int)(j * delta[0] + (1 - PIECE_SCALE)/2 * delta[0]),
                               (int)(i * delta[1] + (1 - PIECE_SCALE)/2 * delta[1]), 
                               (int)(PIECE_SCALE * delta[0]), (int)(PIECE_SCALE * delta[1]));
                } else if (this.gameState.board[i][j] == 2) {
                    g.setColor(Color.BLACK);
                    g.fillOval((int)(j * delta[0] + (1 - PIECE_SCALE)/2 * delta[0]),
                               (int)(i * delta[1] + (1 - PIECE_SCALE)/2 * delta[1]), 
                               (int)(PIECE_SCALE * delta[0]), (int)(PIECE_SCALE * delta[1]));
                }
            }
        }

        // cursor (when dragging a piece)
        if (this.cursor[0] != -1 && this.cursor[1] != -1) {
            g.setColor(Color.RED);
            g.fillOval((int)(this.cursor[0] + (0.5f - PIECE_SCALE) * delta[0]),
                       (int)(this.cursor[1] + (0.5f - PIECE_SCALE) * delta[1]), 
                       (int)(PIECE_SCALE * delta[0]), (int)(PIECE_SCALE * delta[1]));
        }
    }

    /**
     * Moves a piece on the board.
     * @param x_from
     * @param y_from
     * @param to
     */
    public void movePiece(int[] from, int[] to) {
        this.gameState.board[to[0]][to[1]] = this.gameState.board[from[0]][from[1]];
        if (from[0] != to[0] || from[1] != to[1]) this.gameState.board[from[0]][from[1]] = 0;
    }

    public boolean isOccupied(int[] index) {
        if (index[0] == -1) return false;
        return this.gameState.board[index[0]][index[1]] != 0;
    }

    /**
     * Converts a pixel position to an array index.
     * @param x
     * @param y
     * @return
     */
    public int[] getIndex(int x, int y) {
        Dimension vis = this.getVisibleRect().getSize();
        int col = Math.max(Math.min((int)(x / (vis.width/9.0f)), 8), 0);
        int row = Math.max(Math.min((int)(y / (vis.height/9.0f)), 8), 0);
        return new int[]{row, col};
    }

    /**
     * Converts an array index to a chess position.
     * @param index
     * @return
     */
    public String getChessPos(int[] index) {
        return COLS[index[1]] + ROWS[8 - index[0]];
    }
}
