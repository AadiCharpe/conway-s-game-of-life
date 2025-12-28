import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.JSlider;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.geom.Rectangle2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

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
        setSize(600, 675);
        setLocationRelativeTo(null);

        lifePanel panel = new lifePanel();
        getContentPane().add(panel);

        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(start.getText().equals("Start")) {
                    panel.setTimer(1);
                    start.setText("Stop");
                } else {
                    panel.setTimer(2);
                    start.setText("Start");
                }
            }
        });

        JButton step = new JButton("Step");
        step.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.setTimer(0);
            }
        });

        JSlider delay = new JSlider(100, 1000);
        delay.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                panel.setDelay(delay.getValue());
            }
        });
        panel.setDelay(delay.getValue());

        JPanel ui = new JPanel();
        ui.add(start);
        ui.add(step);
        ui.add(new JLabel("Speed:"));
        ui.add(delay);

        getContentPane().add(ui, "South");
    }
}

class lifePanel extends JPanel {
    private boolean[][] cells;
    private boolean running = false;
    private Timer timer;
    public lifePanel() {
        cells  = new boolean[20][20];
        timer = new Timer(250, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextGeneration();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(!running) {
                    cells[(int) Math.floor(e.getY() / 30)][(int) Math.floor(e.getX() / 30)] = cells[(int) Math.floor(e.getY() / 30)][(int) Math.floor(e.getX() / 30)] ? false : true;
                    repaint();
                }
            }
        });
    }

    public void setTimer(int i) {
        if(i == 0) {
            nextGeneration();
            repaint();
        } else if(i == 1) {
            timer.start();
            running = true;
        } else {
            timer.stop();
            running = false;
        }
    }

    public void setDelay(int delay) {
        timer.setDelay(delay);
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