package Fianco.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import Fianco.GameLogic.GameState;
import Fianco.GameLogic.Move;

public class Grid extends JPanel {

    /**
     * How much of the cell should the piece take up.
     */
    public static final float PIECE_SCALE = 0.8f;
    public static final float PIECE_SCALE_SMALL = 0.4f;
    public static final float PIECE_SCALE_OFF = 0.3f;

    // colors for moving pieces
    public static final Color GRAY_ACCENT = new Color(170, 170, 170);
    public static final Color GREEN_ACCENT = new Color(160, 180, 160);

    // util for piece movement
    boolean allowMove = false;
    int selected = -1;
    int[] cursor = new int[] {-1, -1};
    int target = -1;
    List<Move> legalMoves = null;

    // util for display
    boolean showGridNumbers = false;
    boolean showGridNames = true;
    boolean showLegalMoves = true;

    // current game state
    private GameState gameState;

    public void updateGameState(GameState gameState) {
        this.gameState = gameState;
    }

    // returns true if the index is a piece that can be moved
    public boolean canMove(byte index) {
        return (this.gameState.turnIsP1 ? this.gameState.p1Pieces : this.gameState.p2Pieces).contains(index);
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
        if (this.showLegalMoves && this.legalMoves != null) {
            for (Move move : this.legalMoves) {
                g.setColor(GREEN_ACCENT);
                if (this.selected != -1) {
                    if (this.selected == move.from) {
                        g.fillOval((int)(move.to % 9 * delta[0] + PIECE_SCALE_OFF * delta[0]), 
                                   (int)(move.to / 9 * delta[1] + PIECE_SCALE_OFF * delta[1]), 
                                   (int)(PIECE_SCALE_SMALL * delta[0]), (int)(PIECE_SCALE_SMALL * delta[1]));
                    }
                }
                else {
                    g.fillOval((int)(move.to % 9 * delta[0] + PIECE_SCALE_OFF * delta[0]), 
                               (int)(move.to / 9 * delta[1] + PIECE_SCALE_OFF * delta[1]), 
                               (int)(PIECE_SCALE_SMALL * delta[0]), (int)(PIECE_SCALE_SMALL * delta[1]));
                }
            }
        }

        // game state
        for (byte b : this.gameState.p1Pieces) {
            if (this.selected == b) {
                g.setColor(GRAY_ACCENT);
                g.fillOval((int)(b % 9 * delta[0] + PIECE_SCALE_OFF * delta[0]),
                           (int)(b / 9 * delta[1] + PIECE_SCALE_OFF * delta[1]), 
                           (int)(PIECE_SCALE_SMALL * delta[0]), (int)(PIECE_SCALE_SMALL * delta[1]));
            }
            else {
                g.setColor(Color.WHITE);
                g.fillOval((int)(b % 9 * delta[0] + (1 - PIECE_SCALE)/2 * delta[0]),
                           (int)(b / 9 * delta[1] + (1 - PIECE_SCALE)/2 * delta[1]), 
                           (int)(PIECE_SCALE * delta[0]), (int)(PIECE_SCALE * delta[1]));
            }
        }
        for (byte b : this.gameState.p2Pieces) {
            if (this.selected == b) {
                g.setColor(GRAY_ACCENT);
                g.fillOval((int)(b % 9 * delta[0] + PIECE_SCALE_OFF * delta[0]),
                           (int)(b / 9 * delta[1] + PIECE_SCALE_OFF * delta[1]), 
                           (int)(PIECE_SCALE_SMALL * delta[0]), (int)(PIECE_SCALE_SMALL * delta[1]));
            } else {
                g.setColor(Color.BLACK);
                g.fillOval((int)(b % 9 * delta[0] + (1 - PIECE_SCALE)/2 * delta[0]),
                           (int)(b / 9 * delta[1] + (1 - PIECE_SCALE)/2 * delta[1]), 
                           (int)(PIECE_SCALE * delta[0]), (int)(PIECE_SCALE * delta[1]));
            }
        }

        // grid overlays
        if (this.showGridNumbers) {
            g.setColor(Color.BLACK);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    g.drawString(String.valueOf(j * 9 + i), (int)((i + 0.02f) * delta[0]), (int)((j + 0.98f) * delta[1]));
                }
            }
        }
        else if (this.showGridNames) {
            g.setColor(Color.BLACK);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    g.drawString(Move.toAlgebraic((byte)(j * 9 + i)), (int)((i + 0.02f) * delta[0]), (int)((j + 0.98f) * delta[1]));
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

    // converts a pixel position to a board index
    public int getIndex(int x, int y) {
        Dimension vis = this.getVisibleRect().getSize();
        int col = Math.max(Math.min((int)(x / (vis.width/9.0f)), 8), 0);
        int row = Math.max(Math.min((int)(y / (vis.height/9.0f)), 8), 0);
        return row * 9 + col;
    }
}
