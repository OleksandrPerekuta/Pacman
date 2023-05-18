import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

class Ghost{

    /**
     * 1 - blue
     * 2 - green
     * 3 - marmoon
     * 4 - pink
     * 5 - red
     * 6 - yellow
     */
    Game game;
    int[][] cells;
    File[][] images= {
            {new File("Ghosts/blueGhostDown.png"),new File("Ghosts/blueGhostUp.png"),new File("Ghosts/blueGhostRight.png"),new File("Ghosts/blueGhostLeft.png")},
            {new File("Ghosts/greenGhostDown.png"),new File("Ghosts/greenGhostUp.png"),new File("Ghosts/greenGhost.png"),new File("Ghosts/greenGhostLeft.png")},
            {new File("Ghosts/marmoonGhostDown.png"),new File("Ghosts/marmoonGhostUp.png"),new File("Ghosts/marmoonGhost.png"),new File("Ghosts/marmoonGhostLeft.png")},
            {new File("Ghosts/pinkGhostDown.png"),new File("Ghosts/pinkGhostUp.png"),new File("Ghosts/pinkGhostRight.png"),new File("Ghosts/pinkGhostLeft.png")},
            {new File("Ghosts/redGhostDown.png"),new File("Ghosts/redGhostUp.png"),new File("Ghosts/redGhostRight.png"),new File("Ghosts/redGhostLeft.png")},
            {new File("Ghosts/yellowGhostDown.png"),new File("Ghosts/yellowGhostUp.png"),new File("Ghosts/yellowGhostRight.png"),new File("Ghosts/yellowGhostLeft.png")}
    };
    int color;
    int startX;
    int startY;
    boolean isStartSet=false;
    private int x;
    private int y;
    int [] speed={250,500,750,1000,2000,2500};
    int speedIndex=3;
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

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
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
    public void moving(){
        do {
            randomLength=new Random().nextInt(200);
        }while(randomLength%2!=0);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    route= findRoute(game.getCells(),x,y,randomLength);
                    if (!isStartSet){
                        startX=route.get(0)[0];
                        startY=route.get(0)[1];
                        isStartSet=true;
                    }
                    for (int i=0;i<route.size();i++){
                        int[] way=route.get(i);
                        int dx=way[0]-x;
                        int dy=way[1]-y;
                        x=way[0];
                        y=way[1];
                        if (dx==0 && dy==1)
                            position=0;//down
                        else if (dx==0 && dy==-1) {
                            position=1;//up
                        } else if (dx==1 && dy==0) {
                            position=2;//right
                        }else if (dx==-1 && dy==0){
                            position=3;//left
                        }
                        if (game.pacman.getX()==x && game.pacman.getY()==y)
                            game.pacman.catchGhost();
                        try {
                            Thread.sleep(speed[speedIndex]);
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

    public void setColor(int color) {
        this.color = color;
    }
    public File getImage(){
        return images[color][position];
    }
    public void setSpeedIndex(int speedIndex) {
        this.speedIndex = speedIndex;
    }
    public void setCells(int[][] cells) {
        this.cells = cells;
    }

public static ArrayList<int[]> findRoute(int[][] maze, int startX, int startY, int routeLength) {
    ArrayList<int[]> route = new ArrayList<>();
    int[][] moves = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    int currentX = startX;
    int currentY = startY;
    int randomMove;
    int dx;
    int dy;
    int isMovable;
    for (int i = 0; i < routeLength; i++) {
        do {
            randomMove = new Random().nextInt(4);
            dx = moves[randomMove][0];
            dy = moves[randomMove][1];
            int nextX = currentX + dx;
            int nextY = currentY + dy;
            if (nextX >= 0 && nextX < maze[0].length && nextY >= 0 && nextY < maze.length) {
                isMovable = maze[nextY][nextX];
            } else {
                isMovable = 1;
            }
        } while (!(isMovable==0 || isMovable==2 || isMovable==3));
        currentX += dx;
        currentY += dy;
        route.add(new int[]{currentX, currentY});
    }
    return route;
}

}

