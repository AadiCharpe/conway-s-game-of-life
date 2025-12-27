import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JButton;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.geom.Rectangle2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private boolean[][] cells;
    private Timer timer;
    public lifePanel() {
        cells  = new boolean[20][20];
        cells[0][1] = true;
        cells[1][2] = true;
        cells[2][0] = true;
        cells[2][1] = true;
        cells[2][2] = true;
        timer = new Timer(250, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextGeneration();
                repaint();
            }
        });
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        for(int i = 0; i < cells.length; i++) {
            for(int j = 0; j < cells[0].length; j++) {
                if(cells[i][j])
                    g2.setPaint(java.awt.Color.WHITE);
                else
                    g2.setPaint(java.awt.Color.DARK_GRAY);
                g2.fill(new Rectangle2D.Double(j * 30, i * 30, 30, 30));
            }
        }
    }

    public void nextGeneration() {
        boolean[][] newGen = new boolean[20][20];

        for (int i = 0; i < cells.length; i++) {
            newGen[i] = cells[i].clone();
        }

        for(int i = 0; i < cells.length; i++) {
            for(int j = 0; j < cells[0].length; j++) {
                int count = neighborCount(i, j);
                if(cells[i][j]) {
                    if(count < 2 || count > 3)
                        newGen[i][j] = false;
                } else {
                    if(count == 3)
                        newGen[i][j] = true;
                }
            }
        }
        cells = newGen;
    }

    public int neighborCount(int i, int j) {
        int count = 0;
        if(i > 0) {
            if(j > 0)
                if(cells[i - 1][j - 1]) count++;
            if(j < 19)
                if(cells[i - 1][j + 1]) count++;
            if(cells[i - 1][j]) count++;
        }
        if(i < 19) {
            if(j > 0)
                if(cells[i + 1][j - 1]) count++;
            if(j < 19)
                if(cells[i + 1][j + 1]) count++;
            if(cells[i + 1][j]) count++;
        }
        if(j > 0)
            if(cells[i][j - 1]) count++;
        if(j < 19)
            if(cells[i][j + 1]) count++;
        return count;
    }
}