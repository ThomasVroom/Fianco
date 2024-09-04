package Fianco.GUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.event.MouseInputListener;

public class UserInput extends MouseMotionAdapter implements MouseInputListener {

    private Grid grid;

    public UserInput(Grid grid) {
        grid.addMouseListener(this);
        grid.addMouseMotionListener(this);
        this.grid = grid;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // get the index of the cell that was clicked
        int[] index = grid.getIndex(e.getX(), e.getY());
        if (!grid.isOccupied(index)) return;

        // update cursor location
        grid.cursor[0] = e.getX();
        grid.cursor[1] = e.getY();

        // update selected location
        grid.selected[0] = index[0];
        grid.selected[1] = index[1];

        // repaint the grid
        grid.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // get the index of the cell that was released
        int[] index = grid.getIndex(e.getX(), e.getY());
        if (!grid.isOccupied(grid.selected)) return;

        // move the piece from the selected cell to the released cell
        grid.movePiece(grid.selected, index);
        System.out.println("moved from " + grid.getChessPos(grid.selected)
                                + " to " + grid.getChessPos(index)
        );
        
        // reset util variables
        grid.cursor[0] = -1;
        grid.cursor[1] = -1;
        grid.selected[0] = -1;
        grid.selected[1] = -1;

        // repaint the grid
        grid.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!grid.isOccupied(grid.selected)) return;

        // update cursor location
        grid.cursor[0] = e.getX();
        grid.cursor[1] = e.getY();

        // repaint the grid
        grid.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
