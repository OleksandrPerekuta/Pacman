import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Game extends JFrame implements KeyListener {
    public JTable borderTable;
    public final int SIZE = 20;
    public int cellSize;
    private int[][] cells;
    boolean[][] cherriesArray;
    JLayeredPane pane;
    JPanel circlePanel;
    JPanel downMenu;
    JPanel cherryPanel;
    Game game = this;
    Pacman pacman = new Pacman(game);
    MyCustomCellRenderer myTableCellRenderer;
    Ghost ghost;
    int ghostQuantity;
    ArrayList<Ghost> ghosts = new ArrayList<>();
    BufferedImage pacmanImage;
    BufferedImage ghostImage;
    BufferedImage ThreeHearts;
    BufferedImage TwoHearts;
    BufferedImage OneHearts;

    int scoreCounter;
    boolean isInGame = false;
    Stopwatch stopwatch = new Stopwatch(game);
    DefaultTableModel modelForBorders;
    JLabel timerLabel = new JLabel("00:00");
    Thread gameThread = new Thread(new Runnable() {
        @Override
        public void run() {
            for (Ghost ghost1 : ghosts)
                ghost1.moving();
            while (true) {
                System.out.println(isFieldEaten(cells));
                if (isFieldEaten(cells)) {
                    game.dispose();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    });


    public Game() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode mode = gd.getDisplayMode();
        pane = new JLayeredPane();
        pane.setLayout(new BorderLayout());
        cells = new int[SIZE][SIZE];
        MazeGenerator mazeGenerator = new MazeGenerator(cells);
        cells = mazeGenerator.getBorders();
        modelForBorders = new DefaultTableModel(SIZE, SIZE);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                modelForBorders.setValueAt(cells[i][j], i, j);
            }
        }
        borderTable = new JTable(modelForBorders);
        borderTable.setGridColor(Color.black);
        borderTable.setBackground(Color.black);
        myTableCellRenderer = new MyCustomCellRenderer(cells);
        borderTable.setDefaultRenderer(Object.class, myTableCellRenderer);
        cherriesArray = new boolean[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!(cells[i][j] == 0) && new Random().nextDouble() < 0.05) {
                    cherriesArray[i][j] = true;
                }
            }
        }
        circlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                {
                    if (pacman.getMouthOpened()) {
                        try {
                            pacmanImage = ImageIO.read(pacman.getPacmanImage(pacman.getPosition()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            pacmanImage = ImageIO.read(pacman.getPacmanImage(0));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        ghostImage = ImageIO.read(new File("redGhost.png"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                for (Ghost value : ghosts) {
                    g.drawImage(ghostImage, value.convertXtoPixels(), value.convertYtoPixels(), value.getRadius(), value.getRadius(), null);
                }
                g.drawImage(pacmanImage, pacman.convertXtoPixels(), pacman.convertYtoPixels(), pacman.getRadius(), pacman.getRadius(), null);
            }
        };
        circlePanel.setFocusable(true);
        circlePanel.requestFocusInWindow();
        downMenu = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ThreeHearts = ImageIO.read(new File("3Hearts.png"));
                    TwoHearts = ImageIO.read(new File("2Hearts.png"));
                    OneHearts = ImageIO.read(new File("1Hearts.png"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                int imageHeight = getHeight();
                int imageWidth = (int) (imageHeight * ((double) ThreeHearts.getWidth() / ThreeHearts.getHeight()));
                if (pacman.getLives() == 3) {
                    g.drawImage(ThreeHearts, 0, 0, imageWidth, imageHeight, null);
                } else if (pacman.getLives() == 2) {
                    g.drawImage(TwoHearts, 0, 0, imageWidth, imageHeight, null);
                } else if (pacman.getLives() == 1) {
                    g.drawImage(OneHearts, 0, 0, imageWidth, imageHeight, null);
                }
            }

        };
        downMenu.add(timerLabel); // add the timer label to the JPanel


        pane.setLayout(null);
        pane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelHeight = pane.getHeight();
                int panelWidth = pane.getWidth();
                circlePanel.setBounds(0, 0, panelWidth, panelHeight / 100 * 95);
                borderTable.setBounds(0, 0, panelWidth, panelHeight / 100 * 95);
                downMenu.setBounds(0, panelHeight * 90 / 100, panelWidth, panelHeight / 100 * 10);
                for (Ghost ghost1 : ghosts)
                    ghost1.setRadius(cellSize);
                pacman.setRadius(cellSize);
            }
        });

        circlePanel.setOpaque(false);
        circlePanel.setFocusable(true);


        pane.add(borderTable, BorderLayout.CENTER, 3);
        pane.add(circlePanel, 0);
        pane.add(downMenu, 2);
        add(pane);
        addKeyListener(this);
        pane.addKeyListener(this);
        borderTable.addKeyListener(this);
        circlePanel.addKeyListener(this);
        setSize(mode.getWidth(), mode.getHeight());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int width = getSize().width;
                int height = getSize().height;
                if (width != height) {
                    int size = Math.min(width, height);
                    setSize(new Dimension(size, size));
                    borderTable.setRowHeight((pane.getHeight() - (getHeight() / SIZE)) / SIZE);
                    cellSize = size / 100 * 95 / SIZE;

                }
            }
        });
        createWhat();
        startGame();
        stopwatch.start();
    }

    public void putPacmanInStart() {
        pacman.setX(pacman.getStartX());
        pacman.setY(pacman.getStartY());
    }

    public static boolean isFieldEaten(int[][] matrix) {
        for (int[] row : matrix) {
            for (int cell : row) {
                if (cell != 0 && cell != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public void startGame() {
        if (!pacman.getIsStarted()) {
            pacman.setStartX(SIZE / 2);
            pacman.setStartY(SIZE / 2);
            pacman.setX(pacman.getStartX());
            pacman.setY(pacman.getStartY());
            circlePanel.repaint();
        } else {
            for (Ghost ghost1 : ghosts)
                ghost1.moving();
        }
    }

    private Rectangle getCoordinatesOfTheCell(int row, int column) {
        return borderTable.getCellRect(row, column, true);
    }

    public int getCellSize() {
        return cellSize;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            pacman.setDx(-1);
            pacman.setDy(0);
            pacman.setStarted(true);
            pacman.setPosition(1);
            if (gameThread.getState() == Thread.State.NEW)
                gameThread.start();
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            pacman.setDx(1);
            pacman.setDy(0);
            pacman.setStarted(true);
            pacman.setPosition(3);
            if (gameThread.getState() == Thread.State.NEW)
                gameThread.start();
        } else if (keyCode == KeyEvent.VK_UP) {
            pacman.setDx(0);
            pacman.setDy(-1);
            pacman.setStarted(true);
            pacman.setPosition(2);
            if (gameThread.getState() == Thread.State.NEW)
                gameThread.start();
        } else if (keyCode == KeyEvent.VK_DOWN) {
            pacman.setDx(0);
            pacman.setDy(1);
            pacman.setStarted(true);
            pacman.setPosition(4);
            if (gameThread.getState() == Thread.State.NEW)
                gameThread.start();
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


    public int getCellValue(int row, int col) {
        return cells[row][col];
    }


    public int[][] getCells() {
        return cells;
    }

    public DefaultTableModel getTableModel() {
        return modelForBorders;
    }

    public JTable getBorderTable() {
        return borderTable;
    }

    private void fillWithCherries() {
    }

    public Thread getGameThread() {
        return gameThread;
    }

    public void endGame() {
        gameThread.interrupt();
        System.out.println(gameThread.getState());
    }

    public void createWhat() {
        ghostQuantity = 2 * (int) SIZE / 10;
        for (int i = 0; i < ghostQuantity; i++) {
            ghost = new Ghost(game);
            int randomX;
            int randomY;
            boolean isPossibleToPut = false;
            do {
                randomX = new Random().nextInt(SIZE);
                randomY = new Random().nextInt(SIZE);
                if (cells[randomY][randomX] != 1)
                    isPossibleToPut = true;
            } while (!isPossibleToPut);
            ghost.setX(randomX);
            ghost.setY(randomY);
            ghost.setRadius(cellSize);
            ghosts.add(ghost);
            ghost.setCells(cells);
        }
        pacman.setCells(cells);
        pacman.setGhosts(ghosts);
        pacman.moving();

    }
}