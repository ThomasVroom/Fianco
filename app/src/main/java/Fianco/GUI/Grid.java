package Fianco.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import Fianco.GameLogic.GameState;

public class Grid extends JPanel {

    private static final float PIECE_SCALE = 0.8f;

    private static final String[] COLS = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
    private static final String[] ROWS = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

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
                if (this.gameState.board[i][j] == 1) {
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
    }

    /**
     * Converts a pixel position to a board position.
     * @param x
     * @param y
     * @return the board position given in chess notation.
     */
    public String getPos(int x, int y) {
        Dimension vis = this.getVisibleRect().getSize();
        float[] delta = {vis.width/9.0f, vis.height/9.0f};
        int col = (int)(x / delta[0]);
        int row = 8 - (int)(y / delta[1]);
        return COLS[col] + ROWS[Math.max(row, 0)];
    }
}
