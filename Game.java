import javax.imageio.ImageIO;
import javax.swing.*;
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
    public final int SIZE = 30;
    public int cellSize;
    private boolean[][] cells;
     boolean[][] cherriesArray;
    JLayeredPane pane;
    JPanel circlePanel;
    JPanel downMenu;
    JPanel cherryPanel;
    Pacman pacman=new Pacman(this);
    Ghost ghost;
    Game game=this;
    int ghostQuantity;
    ArrayList<Ghost> ghosts=new ArrayList<>();
    BufferedImage pacmanImage;
    BufferedImage ghostImage;
    int scoreCounter;
    public Game(){
        ghostQuantity=(int)SIZE/10;
        for (int i=0;i<ghostQuantity;i++){
            ghost=new Ghost(game);
            ghost.setX((SIZE/2)-(ghostQuantity/2)+i);
            ghost.setY(2);
            ghost.setRadius(cellSize);
            ghosts.add(ghost);
        }


        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode mode = gd.getDisplayMode();
        pane = new JLayeredPane();
        pane.setLayout(new BorderLayout());
        cells = new boolean[SIZE][SIZE];
//        for (int i=0;i<SIZE;i++)
//            for (int j=0;j<SIZE;j++){
//                cells[i][j]=new Random().nextBoolean();
//            }
        for (int i=0;i<SIZE;i++){
            for (int j=0;j<SIZE;j++){
                cells[i][j]=true;
            }
        }
        cells[SIZE/2][SIZE/2]=true;
        cells[SIZE/2-1][SIZE/2]=true;
        cells[SIZE/2][SIZE/2-1]=true;
        cells[SIZE/2-1][SIZE/2-1]=true;
        for (int i=0;i<SIZE;i++){
            for (int j=0;j<SIZE;j++){
                if (i==0||i==(SIZE-1)||j==0||j==(SIZE-1))
                    cells[i][j]=false;
            }
        }

        DefaultTableModel modelForBorders = new DefaultTableModel(SIZE, SIZE);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                modelForBorders.setValueAt(cells[i][j], i, j);
            }
        }
        borderTable = new JTable(modelForBorders);
        borderTable.setGridColor(Color.WHITE);
        borderTable.setBackground(Color.black);
        borderTable.setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JPanel cell = new JPanel(new BorderLayout());
                if (!((boolean) value)){
                    JPanel insidePanel=new JPanel(){
                        BufferedImage image = null;
                        {
                            try {
                                image = ImageIO.read(new File("borderimage.png"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            if (image != null) {
                                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
                            }
                        }
                    };
                    cell.add(insidePanel,BorderLayout.CENTER);
                }else{
                    cell.setBackground(Color.black);
                }
                return cell;
            }
        });
        cherriesArray = new boolean[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!cells[i][j] && new Random().nextDouble() < 0.05) {
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
    }
    private Rectangle getCoordinatesOfTheCell(int row, int column){
        return borderTable.getCellRect(row, column, true);
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


    public boolean getCellValue(int row, int col) {
        return cells[row][col];
    }
    public static ArrayList<int[]> findRoute(boolean[][] maze, int startX, int startY, int routeLength) {
        ArrayList<int[]> route = new ArrayList<>();
        int[][] moves={{0,1},{0,-1},{1,0},{-1,0}};
        int currentX=startX;
        int currentY=startY;
        int randomMove;
        int Dx;
        int Dy;
        boolean isMovable;
        for (int i=0;i<routeLength;i++){
            do{
                randomMove=new Random().nextInt(4);
                Dx=moves[randomMove][0];
                Dy=moves[randomMove][1];
                if(!maze[currentY+Dy][currentX+Dx])
                    isMovable=false;
                else
                    isMovable=true;
            }while (!isMovable);
            currentX+=Dx;
            currentY+=Dy;
            route.add(new int[]{currentX, currentY});
        }
        return route;
    }


    public boolean[][] getCells() {
        return cells;
    }
}
class Pacman {
    private boolean isStarted=false;
    Game game;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int radius;
    private int position=0;
    private boolean isMouthOpened=false;
    public Pacman(Game game){
        this.game=game;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setMouthOpened(boolean mouthOpened) {
        isMouthOpened = mouthOpened;
    }
    public boolean getMouthOpened(){
        return isMouthOpened;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }
    public boolean getIsStarted(){return this.isStarted;};
    public void moving(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    moveIfPossible();
                    game.circlePanel.repaint();
                    try {

                        for (int i=0;i<3;i++){
                            setMouthOpened(true);
                            game.circlePanel.repaint();
                            Thread.sleep(40);
                            setMouthOpened(false);
                            game.circlePanel.repaint();
                            Thread.sleep(40);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        thread.start();
    }
    public int convertXtoPixels(){
        Rectangle rectangle=getCoordinatesOfTheCell(this.y,this.x);
        return rectangle.x;
    }
    public int convertYtoPixels(){
        Rectangle rectangle=getCoordinatesOfTheCell(this.y,this.x);
        return rectangle.y;
    }
    private void moveIfPossible(){
        int nextX=x+dx;
        int nextY=y+dy;
        if (game.getCellValue(nextY,nextX)){
            x+=dx;
            y+=dy;

        }
    }
    public File getPacmanImage(int number){
        switch (number){
            case 0:
                return new File("pacmanfull.png");
            case 1:
                return new File("pacmanLeft.png");
            case 2:
                return new File("pacmanUp.png");
            case 3:
                return new File("pacmanRight.png");
            case 4:
                return new File("pacmanDown.png");
        };
        return null;
    }

    private Rectangle getCoordinatesOfTheCell(int row, int column){
        return game.borderTable.getCellRect(row, column, true);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
class Ghost{
    Game game;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int radius;
    int randomLength=0;
    private int position=0;
    ArrayList<int[]> route;
    public Ghost(Game game){
        this.game=game;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }
    private Rectangle getCoordinatesOfTheCell(int row, int column){
        return game.borderTable.getCellRect(row, column, true);
    }
    public int convertXtoPixels(){
        Rectangle rectangle=getCoordinatesOfTheCell(this.y,this.x);
        return rectangle.x;
    }
    public int convertYtoPixels(){
        Rectangle rectangle=getCoordinatesOfTheCell(this.y,this.x);
        return rectangle.y;
    }

    public ArrayList<int[]> getRoute() {
        return route;
    }

    public void moving(){
        do {
            randomLength=new Random().nextInt(200);
        }while(randomLength%2!=0);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    route= Game.findRoute(game.getCells(),x,y,randomLength);
                    for (int i=0;i<route.size();i++){
                        int[] way=route.get(i);
                        x=way[0];
                        y=way[1];
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        game.circlePanel.repaint();
                    }

                }
            }
        });
        thread.start();
    }
    private boolean moveIfPossible(){
        int nextX=x+dx;
        int nextY=y+dy;
        if (game.getCellValue(nextY,nextX)){
            x+=dx;
            y+=dy;
            return true;
        }else
            return false;
    }
}

