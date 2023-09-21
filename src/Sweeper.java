import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import MySweeper.*;

class Sweeper extends JFrame {
    private Game game;
    private JPanel panel, southPanel, specialPanel;
    private JLabel labelBomb, labelSec;
    private final SecLister secLister = new SecLister();
    private JRadioButtonMenuItem easy, normal, hard;
    private JCheckBox sound;
    private JMenu menuGame;
    private JTextField colsField, rowsField, bombsField;

    private int ROWS = 9;
    private int COLS = 9;
    private int BOMBS = 10;
    private final int IMAGE_SIZE = 50;

    private final Font FONT = new Font
            ("Dialog", Font.BOLD, 28);
    private final Font FONTs = new Font
            ("Dialog", Font.BOLD, 15);

    public static void main(String[] args) {
        new Sweeper();
    }

    private Sweeper() {
        game = new Game(COLS, ROWS, BOMBS);
        game.start();
        setImages();
        setUpTool();
        setUpPanel();
        setUpFrame();
        initMetalLookAndFeel();
        secLister.setUpThread();
    }

    private void setUpFrame() {
        setTitle("Sweeper");

        JMenuBar menuBar = new JMenuBar();
        menuGame = new JMenu("Game");
        menuGame.setFont(FONTs);
        menuBar.add(menuGame);

        easy = new JRadioButtonMenuItem("EASY (9x9)");
        easy.setFont(FONTs);
        easy.setSelected(true);
        easy.addActionListener(
                new EasyListener());

        normal = new JRadioButtonMenuItem("NORMAL (16x11)");
        normal.setFont(FONTs);
        normal.addActionListener(
                new NormalListener());

        hard = new JRadioButtonMenuItem("HARD (19x12)");
        hard.setFont(FONTs);
        hard.addActionListener(
                new HardListener());

        JRadioButtonMenuItem special = new JRadioButtonMenuItem
                ("SPACIAL");
        special.setFont(FONTs);
        special.addActionListener(
                new SpecialListener());

        ButtonGroup bg = new ButtonGroup();
        bg.add(easy);
        bg.add(normal);
        bg.add(hard);
        bg.add(special);

        menuGame.add(easy);
        menuGame.add(normal);
        menuGame.add(hard);
        menuGame.add(special);

        JMenu soundMenu = new JMenu("Sound");
        soundMenu.setFont(FONTs);
        menuBar.add(soundMenu);
        sound = new JCheckBox("Sound");
        sound.addActionListener(new SoundListener());
        sound.setFont(FONTs);
        sound.setSelected(true);
        soundMenu.add(sound);

        JMenu tools = new JMenu("Tools");
        tools.setFont(FONTs);
        menuBar.add(tools);

        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(new RestartListener());
        restart.setFont(FONTs);
        tools.add(restart);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ExitListener());
        exit.setFont(FONTs);
        tools.add(exit);

        setJMenuBar(menuBar);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setIconImage(getImage("icon"));
        pack();
        setLocationRelativeTo(null);

        specialPanel = new JPanel();
        Box textFieldBox = new Box(BoxLayout.Y_AXIS);
        Box nameBox = new Box(BoxLayout.Y_AXIS);

        JLabel label = new JLabel("Cols");
        label.setFont(FONTs);
        nameBox.add(label);
        label = new JLabel("Rows");
        label.setFont(FONTs);
        nameBox.add(label);
        label = new JLabel("Bombs");
        label.setFont(FONTs);
        nameBox.add(label);

        colsField = new JTextField("9", 2);
        rowsField = new JTextField("9", 2);
        bombsField  = new JTextField("10", 2);

        colsField.setFont(FONTs);
        rowsField.setFont(FONTs);
        bombsField.setFont(FONTs);

        textFieldBox.add(colsField);
        textFieldBox.add(rowsField);
        textFieldBox.add(bombsField);

        JButton select = new JButton("Save it");
        select.setFont(FONTs);
        select.addActionListener(new SelectListener());

        specialPanel.add(nameBox);
        specialPanel.add(textFieldBox);
        specialPanel.add(select);
    }

    private void initMetalLookAndFeel() {
        try {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Can't use the specified look and feel on this platform.");
        } catch (Exception e) {
            System.err.println("Couldn't get specified look and feel, for some reason.");
        }
    }

    private void setUpPanel() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Coord coord : Ranges.getAllCoords()) {
                    g.drawImage((Image) game.getBox(coord).image,
                            coord.x * IMAGE_SIZE,
                            coord.y * IMAGE_SIZE, this);
                }
            }
        };

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord(x, y);

                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (game.gameOver()) {
                        game.start();
                        secLister.setUpThread();
                        labelBomb.setText("Bombs: "
                                + game.getNowBombs() + "   ");
                    } else game.pressLeftButton(coord);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    game.pressRightButton(coord);
                    labelBomb.setText("Bombs: "
                            + game.getNowBombs() + "   ");
                }
                panel.repaint();
                //new MyMidi().listen("click");
            }
        });

        panel.setPreferredSize(new Dimension(COLS * IMAGE_SIZE,
                ROWS * IMAGE_SIZE));

        southPanel = new JPanel(new FlowLayout());
        southPanel.add(labelBomb);
        southPanel.add(labelSec);

        add(BorderLayout.SOUTH, southPanel);
        add(BorderLayout.CENTER, panel);
    }

    private void setUpTool() {
        labelSec = new JLabel("   Seconds: 0");
        labelSec.setFont(FONT);

        labelBomb = new JLabel("Bombs: " + BOMBS + "   ");
        labelBomb.setFont(FONT);
    }

    private Image getImage(String name) {
        String filename = "img/" + name + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }

    private void setImages() {
        for (MyBox box : MyBox.values())
            box.image = getImage(box.name().toLowerCase());
    }

    private void fixCRB() {
        if (ROWS > 12) ROWS = 12;

        if (ROWS < 9) ROWS = 9;
            rowsField.setText(String.valueOf(ROWS));

        if (COLS > 19) COLS = 19;

        if (COLS < 9) COLS = 9;
            colsField.setText(String.valueOf(COLS));

        if (BOMBS > ROWS*COLS) BOMBS = ROWS*COLS / 3;

        if (BOMBS < 5) BOMBS = 5;
            bombsField.setText(String.valueOf(BOMBS));
    }

    private class SecLister implements Runnable {
        private int sec;

        @Override
        public void run() {
            while (!game.gameOver()) {
                labelSec.setText("   Seconds: " + sec/20);
                game.sleep(50);
                sec++;
            }
        }

        private void setUpThread() {
            sec = 0;
            new Thread(secLister).start();
        }

        private void setSec() {
            sec = 0;
        }
    }

    private class EasyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            repaintSweeper(9, 9, 10);
            easy.setSelected(true);
            menuGame.remove(specialPanel);
        }
    }

    private class NormalListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            repaintSweeper(16, 11, 21);
            normal.setSelected(true);
            menuGame.remove(specialPanel);
        }
    }

    private class HardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            repaintSweeper(19, 12, 28);
            hard.setSelected(true);
            menuGame.remove(specialPanel);
        }
    }

    private class SpecialListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            menuGame.add(specialPanel);
        }
    }

    private class SoundListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.setSound(sound.isSelected());
        }
    }

    private class RestartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            repaintSweeper(COLS, ROWS, BOMBS);
        }
    }

    private static class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private void repaintSweeper(int cols, int rows, int bombs) {
        remove(southPanel);
        remove(panel);
        repaint();
        COLS = cols;
        ROWS = rows;
        BOMBS = bombs;
        fixCRB();
        game = new Game(COLS, ROWS, BOMBS);
        game.start();
        setImages();
        setUpTool();
        setUpPanel();
        initMetalLookAndFeel();
        pack();
        setLocationRelativeTo(null);
        secLister.setSec();
    }

    private class SelectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                repaintSweeper(Integer.parseInt(colsField.getText()),
                    Integer.parseInt(rowsField.getText()),
                    Integer.parseInt(bombsField.getText()));
            } catch (Exception ex) {
                repaintSweeper(9, 9, 10);
            }
        }
    }
}
