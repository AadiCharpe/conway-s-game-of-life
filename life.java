import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JComboBox;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import java.awt.geom.Rectangle2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import java.util.StringTokenizer;

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

        JComboBox sizes = new JComboBox(new String[] {"10x10", "20x20", "50x50"});
        sizes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                start.setText("Start");
                StringTokenizer tokenizer = new StringTokenizer((String) sizes.getSelectedItem(), "x");
                panel.reset(Integer.parseInt(tokenizer.nextToken()));
            }
        });

        JPanel ui = new JPanel();
        ui.add(start);
        ui.add(step);
        ui.add(new JLabel("Speed:"));
        ui.add(delay);
        ui.add(new JLabel("Size:"));
        ui.add(sizes);

        getContentPane().add(ui, "South");
    }
}

class lifePanel extends JPanel {
    private boolean[][] cells;
    private boolean running = false;
    private Timer timer;
    private int SIZE = 10;
    public lifePanel() {
        cells  = new boolean[SIZE][SIZE];
        timer = new Timer(250, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextGeneration();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(!running) {
                    cells[(int) Math.floor(e.getY() / (600 / SIZE))][(int) Math.floor(e.getX() / (600 / SIZE))] = cells[(int) Math.floor(e.getY() / (600 / SIZE))][(int) Math.floor(e.getX() / (600 / SIZE))] ? false : true;
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
                Color color;
                if(cells[i][j])
                    color = Color.WHITE;
                else
                    color = Color.DARK_GRAY;
                g2.setPaint(color);
                g2.fill(new Rectangle2D.Double(j * (600 / SIZE), i * (600 / SIZE), (600 / SIZE), (600 / SIZE)));
                
                g2.setPaint(color.darker());
                g2.draw(new Rectangle2D.Double(j * (600 / SIZE), i * (600 / SIZE), (600 / SIZE), (600 / SIZE)));
            }
        }
    }

    public void reset(int size) {
        SIZE = size;
        timer.stop();
        cells = new boolean[SIZE][SIZE];
        repaint();
    }

    public void nextGeneration() {
        boolean[][] newGen = new boolean[SIZE][SIZE];

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
            if(j < SIZE - 1)
                if(cells[i - 1][j + 1]) count++;
            if(cells[i - 1][j]) count++;
        }
        if(i < SIZE - 1) {
            if(j > 0)
                if(cells[i + 1][j - 1]) count++;
            if(j < SIZE - 1)
                if(cells[i + 1][j + 1]) count++;
            if(cells[i + 1][j]) count++;
        }
        if(j > 0)
            if(cells[i][j - 1]) count++;
        if(j < SIZE - 1)
            if(cells[i][j + 1]) count++;
        return count;
    }
}