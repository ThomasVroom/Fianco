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
    public int turnCount = 1;
    public Dimension size = new Dimension(140, 0);
    
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
        MoveLabel lbl = new MoveLabel(" Turn " + turnCount++ + ": " + move.toString() +
                                      " (" + (turnCount % 2 == 0 ? "W" : "B") +")", move);
        box.add(lbl);
        size.height = Math.min(box.getComponentCount() / 2 * (lbl.getPreferredSize().height + 2), 200);
    }

    public void clear() {
        box.removeAll();
        turnCount = 1;
        size.height = 0;
    }

    public Move undo() {
        int count = box.getComponentCount();
        if (count == 0) return null;
        MoveLabel lbl = (MoveLabel)box.getComponent(count - 1);
        box.remove(lbl);
        box.remove(count - 2);
        turnCount--;
        size.height = Math.min(box.getComponentCount() / 2 * (lbl.getPreferredSize().height + 2), 200);
        return lbl.move;
    }

    private static class MoveLabel extends JLabel {
        public Move move;

        public MoveLabel(String text, Move move) {
            super(text);
            this.move = move;
        }
    }
}
