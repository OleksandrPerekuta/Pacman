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
    String username;
    public JTable borderTable;
    int score;
    public  int SIZE;
    public int cellSize;
    private int[][] cells;
    boolean[][] cherriesArray;
    JLayeredPane pane;
    JPanel circlePanel;
    JPanel downMenu;
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
    Stopwatch stopwatch = new Stopwatch(game);
    DefaultTableModel modelForBorders;
    JLabel timerLabel = new JLabel("00:00");
    JLabel scoreLabel=new JLabel("0");
    JPanel timerPanel;
    JPanel scorePanel;
    JPanel heartsPanel;
    Font font=null;

    Thread gameThread = new Thread(new Runnable() {
        @Override
        public void run() {
            for (Ghost ghost1 : ghosts)
                ghost1.moving();
            while (true) {
                if (isFieldEaten(cells)) {
                    SwingUtilities.invokeLater(()->new Winning(getUsername(),pacman.getScoreCounter()));
                    game.dispose();
                    gameThread.stop();
                }
                if(pacman.getLives()==0){
                    SwingUtilities.invokeLater(()->new Lost());
                    game.dispose();
                    gameThread.stop();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    });


    public Game(int SIZE,String username) {
        this.SIZE=SIZE;
        this.username=username;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("04B_30__.TTF"));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        Font workFont = font.deriveFont(36f);
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
                    try {
                        pacmanImage = ImageIO.read(pacman.getPacmanImage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                for (Ghost ghost : ghosts) {
                    try {
                        ghostImage=ImageIO.read(ghost.getImage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    g.drawImage(ghostImage, ghost.convertXtoPixels(), ghost.convertYtoPixels(), ghost.getRadius(), ghost.getRadius(), null);
                }
                g.drawImage(pacmanImage, pacman.convertXtoPixels(), pacman.convertYtoPixels(), pacman.getRadius(), pacman.getRadius(), null);
            }
        };
        circlePanel.setFocusable(true);
        circlePanel.requestFocusInWindow();
        downMenu = new JPanel(new GridLayout(1,3)) {
        };
        heartsPanel=new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ThreeHearts = ImageIO.read(new File("hearts3.png"));
                    TwoHearts = ImageIO.read(new File("hearts2.png"));
                    OneHearts = ImageIO.read(new File("hearts1.png"));
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        timerPanel=new JPanel(new GridBagLayout());
        timerPanel.setBackground(Color.black);
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        timerLabel.setFont(workFont);
        timerLabel.setForeground(Color.yellow);
        timerPanel.add(timerLabel,gbc);

        scorePanel=new JPanel(new GridBagLayout());
        scorePanel.setBackground(Color.black);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setFont(workFont);
        scoreLabel.setForeground(Color.yellow);
        scorePanel.add(scoreLabel,gbc);

        downMenu.add(heartsPanel);
        downMenu.add(timerPanel);
        downMenu.add(scorePanel);


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
        JPanel blackPanel=new JPanel();
        blackPanel.setBackground(Color.black);
        blackPanel.setSize(new Dimension(mode.getWidth(),mode.getHeight()));
        circlePanel.setOpaque(false);
        circlePanel.setFocusable(true);


        pane.add(borderTable, BorderLayout.CENTER, 3);
        pane.add(circlePanel, 0);
        for (Component component : downMenu.getComponents()) {
            component.setBackground(Color.black);
        }
        pane.add(downMenu, 2);
        pane.add(blackPanel,4);
        add(pane);
        addKeyListener(this);
        pane.addKeyListener(this);
        borderTable.addKeyListener(this);
        circlePanel.addKeyListener(this);
        setTitle(username+"'s game");
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
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            pacman.setStarted(true);
            if (!pacman.getIsInverted()) {
                pacman.setPosition(0);
                pacman.setDx(-1);
                pacman.setDy(0);
            }else {
                pacman.setPosition(2);
                pacman.setDx(1);
                pacman.setDy(0);
            }
            if (gameThread.getState() == Thread.State.NEW)
                gameThread.start();
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            pacman.setStarted(true);
            if (!pacman.getIsInverted()) {
                pacman.setPosition(2);
                pacman.setDx(1);
                pacman.setDy(0);
            }else {
                pacman.setPosition(0);
                pacman.setDx(-1);
                pacman.setDy(0);
            }
            if (gameThread.getState() == Thread.State.NEW)
                gameThread.start();
        } else if (keyCode == KeyEvent.VK_UP) {

            pacman.setStarted(true);
            if (!pacman.getIsInverted()) {
                pacman.setPosition(1);
                pacman.setDx(0);
                pacman.setDy(-1);
            }else {
                pacman.setPosition(3);
                pacman.setDx(0);
                pacman.setDy(1);
            }
            if (gameThread.getState() == Thread.State.NEW)
                gameThread.start();
        } else if (keyCode == KeyEvent.VK_DOWN) {
            pacman.setStarted(true);
            if (!pacman.getIsInverted()) {
                pacman.setPosition(3);
                pacman.setDx(0);
                pacman.setDy(1);
            }else {
                pacman.setPosition(1);
                pacman.setDx(0);
                pacman.setDy(-1);
            }
            if (gameThread.getState() == Thread.State.NEW)
                gameThread.start();
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }else if (e.isShiftDown() && e.isControlDown() && e.getKeyCode()==KeyEvent.VK_Q) {
            SwingUtilities.invokeLater(()->new Menu());
            gameThread.stop();
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

    public void createWhat() {
        ghostQuantity = 2 * (int) SIZE / 10;
        for (int i = 0; i < ghostQuantity; i++) {
            ghost = new Ghost(game);
            int randomColor;
            int randomX;
            int randomY;
            boolean isPossibleToPut = false;
            do {
                randomColor=new Random().nextInt(6);
                ghost.setColor(randomColor);
                randomX = new Random().nextInt(SIZE);
                randomY = new Random().nextInt(SIZE);

                if (cells[randomY][randomX] != 1){
                    if (((randomX!=SIZE/2 && randomY!=SIZE/2) || (randomX!=SIZE/2 && randomY!=SIZE/2-1) || (randomX!=SIZE/2-1 && randomY!=SIZE/2) || (randomX!=SIZE/2-1 && randomY!=SIZE/2-1))){
                        isPossibleToPut = true;
                    }
                }
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

    public String getUsername() {
        return username;
    }
    public void causeRandomEffect(){
        int randomEffect=new Random().nextInt(5);
        int randomSpeed=new Random().nextInt(6);
        switch (randomEffect){
            case 0://change speed of ghost
            {
                for (Ghost ghost:ghosts)
                    ghost.setSpeedIndex(randomSpeed);
                System.out.println("ghost speed changed");
            }
            break;
            case 1:{
                pacman.setSpeedIndex(randomSpeed);
                System.out.println("pacman speed changed");
                break;
            }
            case 2:{
                for (Ghost ghost:ghosts){
                    int randomColor=new Random().nextInt(6);
                    ghost.setColor(randomColor);
                }
                System.out.println("ghost speed changed");
            }
            break;
            case 3:{
                System.out.println("inversed keys");
                pacman.changeInverted();
                break;
            }
            case 4:{
                System.out.println("teleportation happened");
                pacman.teleportRandomPoint();
                break;
            }
            default:
                System.out.println("else");
                break;
        }
    }
}