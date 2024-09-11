package Fianco.GUI;

import javax.swing.JScrollPane;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.UIManager;

import Fianco.GameLogic.Move;

// source: https://stackoverflow.com/a/70826107
public class MoveMenu extends JScrollPane {

    public Box box = new Box(BoxLayout.Y_AXIS);
    private int turnCount = 1;
    private Dimension size = new Dimension(130, 0);
    
    public MoveMenu() {
        super();
        this.setViewportView(box);
        this.getVerticalScrollBar().setUnitIncrement(20);
        this.setPreferredSize(size);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.getViewport().setBackground(UIManager.getColor("Menu.background"));
    }

    public void addMove(Move move) {
        box.add(Box.createRigidArea(new Dimension(0, 2))); // space between items
        JLabel lbl = new JLabel(" Turn " + turnCount++ + ": " + move.toString() + " (" + (turnCount % 2 == 0 ? "W" : "B") +")");
        box.add(lbl);
        size.height = Math.min(box.getComponentCount() / 2 * (lbl.getPreferredSize().height + 2), 200);
    }
}
