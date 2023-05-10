import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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
    Game game=this;
    Pacman pacman=new Pacman(game);
    MyCustomCellRenderer myTableCellRenderer;
    Ghost ghost;
    int ghostQuantity;
    ArrayList<Ghost> ghosts=new ArrayList<>();
    BufferedImage pacmanImage;
    BufferedImage ghostImage;
    int scoreCounter;
    DefaultTableModel modelForBorders;
    public Game(){
        ghostQuantity=(int)SIZE/10;



        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode mode = gd.getDisplayMode();
        pane = new JLayeredPane();
        pane.setLayout(new BorderLayout());
        cells = new int[SIZE][SIZE];

        for (int i=0;i<SIZE;i++){
            for (int j=0;j<SIZE;j++){
                cells[i][j]=0;
            }
        }
        cells[SIZE/2][SIZE/2]=0;
        cells[SIZE/2-1][SIZE/2]=0;
        cells[SIZE/2][SIZE/2-1]=0;
        cells[SIZE/2-1][SIZE/2-1]=0;
        for (int i=0;i<SIZE;i++){
            for (int j=0;j<SIZE;j++){
                if (i==0||i==(SIZE-1)||j==0||j==(SIZE-1))
                    cells[i][j]=1;
            }
        }
        cells[2][2]=2;
        for (int i=0;i<ghostQuantity;i++){
            ghost=new Ghost(game);
            ghost.setX((SIZE/2)-(ghostQuantity/2)+i);
            ghost.setY(2);
            ghost.setRadius(cellSize);
            ghosts.add(ghost);
            ghost.setCells(cells);
        }
        pacman.setCells(cells);
        modelForBorders = new DefaultTableModel(SIZE, SIZE);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                modelForBorders.setValueAt(cells[i][j], i, j);
            }
        }
        borderTable = new JTable(modelForBorders);
        borderTable.setGridColor(Color.black);
        borderTable.setBackground(Color.black);
        myTableCellRenderer=new MyCustomCellRenderer(cells);
        borderTable.setDefaultRenderer(Object.class,myTableCellRenderer);
        cherriesArray = new boolean[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!(cells[i][j]==0) && new Random().nextDouble() < 0.05) {
                    cherriesArray[i][j] = true;
                }
            }
        }
        circlePanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);{
                    if (pacman.getMouthOpened()){
                        try {
                            pacmanImage = ImageIO.read(pacman.getPacmanImage(pacman.getPosition()));
                        } catch (IOException e) {e.printStackTrace();}
                    }else{
                        try {
                            pacmanImage = ImageIO.read(pacman.getPacmanImage(0));

                        } catch (IOException e) {e.printStackTrace();}
                    }
                    try {
                        ghostImage=ImageIO.read(new File("redGhost.png"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                for (Ghost value : ghosts) {
                    g.drawImage(ghostImage, value.convertXtoPixels(), value.convertYtoPixels(), value.getRadius(), value.getRadius(), null);
                }
                g.drawImage(pacmanImage,pacman.convertXtoPixels(),pacman.convertYtoPixels(),pacman.getRadius(),pacman.getRadius(),null);
            }
        };
        circlePanel.setFocusable(true);
        circlePanel.requestFocusInWindow();
        downMenu =new JPanel();
        downMenu.setBackground(Color.yellow);
        pane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelHeight= pane.getHeight();
                int panelWidth = pane.getWidth();
                circlePanel.setBounds(0, 0, panelWidth, panelHeight/100*95);
                borderTable.setBounds(0, 0, panelWidth, panelHeight/100*95);
                downMenu.setBounds(0,panelHeight/100*95,panelWidth,panelHeight/20);
                for (Ghost ghost1: ghosts)
                    ghost1.setRadius(cellSize);
                pacman.setRadius(cellSize);
            }
        });
        circlePanel.setOpaque(false);
        circlePanel.setFocusable(true);







        pane.add(borderTable, BorderLayout.CENTER,3);
        pane.add(circlePanel, 0);
        pane.add(downMenu,2);
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
                    cellSize=size/100*95/SIZE;

                    if (!pacman.getIsStarted()){
                        pacman.setX(SIZE/2);
                        pacman.setY(SIZE-2);
                    }
                }
            }
        });
        pacman.moving();
        for(Ghost ghost1: ghosts)
            ghost1.moving();
        myTableCellRenderer.drawCherry(borderTable,1,1);
    }
    private Rectangle getCoordinatesOfTheCell(int row, int column){
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
            circlePanel.repaint();

            //interrupt
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            pacman.setDx(1);
            pacman.setDy(0);
            pacman.setStarted(true);
            pacman.setPosition(3);
            circlePanel.repaint();

        } else if (keyCode == KeyEvent.VK_UP) {
            pacman.setDx(0);
            pacman.setDy(-1);
            pacman.setStarted(true);
            pacman.setPosition(2);
            circlePanel.repaint();

        } else if (keyCode == KeyEvent.VK_DOWN) {
            pacman.setDx(0);
            pacman.setDy(1);
            pacman.setStarted(true);
            pacman.setPosition(4);
            circlePanel.repaint();

        } else if (keyCode==KeyEvent.VK_ESCAPE) {
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
    public DefaultTableModel getTableModel(){
        return modelForBorders;
    }

    public JTable getBorderTable() {
        return borderTable;
    }
}


