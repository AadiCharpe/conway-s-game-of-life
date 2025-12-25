import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

public class life {
    public static void main(String[] args) {
        lifeFrame frame = new lifeFrame();
        frame.show();
    }
}

class lifeFrame extends JFrame {
    public lifeFrame() {
        setDefaultCloseOperation(3);
        setTitle("Conway's Game of Life");
        setSize(600, 800);

        lifePanel panel = new lifePanel();

        getContentPane().add(panel);
    }
}

class lifePanel extends JPanel {
    private boolean[][] cells = new boolean[20][20];
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        for(int i = 0; i < cells.length; i++) {
            for(int j = 0; j < cells[0].length; j++) {
                if(cells[i][j])
                    g2.setPaint(java.awt.Color.WHITE);
                else
                    g2.setPaint(java.awt.Color.DARK_GRAY);
                g2.fill(new Rectangle2D.Double(i * 30, j * 30, 30, 30));
            }
        }
    }
}