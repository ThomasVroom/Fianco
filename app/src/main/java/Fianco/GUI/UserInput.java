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
        if (!grid.allowMove) return;

        // get the index of the cell that was clicked
        int index = grid.getIndex(e.getX(), e.getY());
        if (!grid.canMove((byte)index)) return;

        // update cursor location
        grid.cursor[0] = e.getX();
        grid.cursor[1] = e.getY();

        // update selected location
        grid.selected = index;

        // repaint the grid
        grid.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!grid.allowMove || grid.selected == -1) return;

        // get the index of the cell that was released
        int index = grid.getIndex(e.getX(), e.getY());

        // update the target location
        grid.target = index;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!grid.allowMove || grid.selected == -1) return;

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
