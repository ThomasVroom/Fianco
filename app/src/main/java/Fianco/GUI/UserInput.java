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
        System.out.println("pressed at " + grid.getPos(e.getX(), e.getY()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("released at " + grid.getPos(e.getX(), e.getY()));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("moved to " + e.getX() + " " + e.getY());
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
