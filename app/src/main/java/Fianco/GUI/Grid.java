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

    // colors for moving pieces
    public static final Color GRAY_ACCENT = new Color(170, 170, 170);
    public static final Color GREEN_ACCENT = new Color(160, 180, 160);

    // util for piece movement
    boolean allowMove = false;
    int selected = -1;
    int[] cursor = new int[] {-1, -1};
    int target = -1;
    int[] legalMoves = new int[0];

    // current game state
    private GameState gameState;

    public void updateGameState(GameState gameState) {
        this.gameState = gameState;
    }

    // returns true if the index is a piece that can be moved
    public boolean canMove(int index) {
        return (this.gameState.turnIsP1 ? this.gameState.bitBoardP1 : this.gameState.bitBoardP2).get(index);
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

        // legal moves
        // TODO: optimize repeated calculations
        for (int i = 0; i < this.legalMoves.length; i++) {
            g.setColor(GREEN_ACCENT);
            if (this.selected != -1) {
                if (this.selected == this.legalMoves[i]) { // TODO bug: need to be able to get a "from" index
                    g.fillOval((int)(this.legalMoves[i] % 9 * delta[0] + (1 - 0.5f*PIECE_SCALE)/2 * delta[0]), 
                       (int)(this.legalMoves[i] / 9 * delta[1] + (1 - 0.5f*PIECE_SCALE)/2 * delta[1]), 
                       (int)(0.5f*PIECE_SCALE * delta[0]), (int)(0.5f*PIECE_SCALE * delta[1]));
                }
            }
            else {
                g.fillOval((int)(this.legalMoves[i] % 9 * delta[0] + (1 - 0.5f*PIECE_SCALE)/2 * delta[0]), 
                       (int)(this.legalMoves[i] / 9 * delta[1] + (1 - 0.5f*PIECE_SCALE)/2 * delta[1]), 
                       (int)(0.5f*PIECE_SCALE * delta[0]), (int)(0.5f*PIECE_SCALE * delta[1]));
            }
        }

        // game state
        for (int i = this.gameState.bitBoardP1.nextSetBit(0); i != -1; i = this.gameState.bitBoardP1.nextSetBit(i + 1)) {
            if (this.selected == i) {
                g.setColor(GRAY_ACCENT);
                g.fillOval((int)(i % 9 * delta[0] + (1 - 0.5f*PIECE_SCALE)/2 * delta[0]),
                           (int)(i / 9 * delta[1] + (1 - 0.5f*PIECE_SCALE)/2 * delta[1]), 
                           (int)(0.5f*PIECE_SCALE * delta[0]), (int)(0.5f*PIECE_SCALE * delta[1]));
            }
            else {
                g.setColor(Color.WHITE);
                g.fillOval((int)(i % 9 * delta[0] + (1 - PIECE_SCALE)/2 * delta[0]),
                           (int)(i / 9 * delta[1] + (1 - PIECE_SCALE)/2 * delta[1]), 
                           (int)(PIECE_SCALE * delta[0]), (int)(PIECE_SCALE * delta[1]));
            }
        }
        for (int i = this.gameState.bitBoardP2.nextSetBit(0); i != -1; i = this.gameState.bitBoardP2.nextSetBit(i + 1)) {
            if (this.selected == i) {
                g.setColor(GRAY_ACCENT); // TODO: duplicate code
                g.fillOval((int)(i % 9 * delta[0] + (1 - 0.5f*PIECE_SCALE)/2 * delta[0]),
                           (int)(i / 9 * delta[1] + (1 - 0.5f*PIECE_SCALE)/2 * delta[1]), 
                           (int)(0.5f*PIECE_SCALE * delta[0]), (int)(0.5f*PIECE_SCALE * delta[1]));
            } else {
                g.setColor(Color.BLACK);
                g.fillOval((int)(i % 9 * delta[0] + (1 - PIECE_SCALE)/2 * delta[0]),
                           (int)(i / 9 * delta[1] + (1 - PIECE_SCALE)/2 * delta[1]), 
                           (int)(PIECE_SCALE * delta[0]), (int)(PIECE_SCALE * delta[1]));
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

    // converts a pixel position to a board index
    public int getIndex(int x, int y) {
        Dimension vis = this.getVisibleRect().getSize();
        int col = Math.max(Math.min((int)(x / (vis.width/9.0f)), 8), 0);
        int row = Math.max(Math.min((int)(y / (vis.height/9.0f)), 8), 0);
        return row * 9 + col;
    }
}
