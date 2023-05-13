import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class Ghost{
    Game game;
    int[][] cells;
    int cellsWidth;
    int cellsHeight;
    int startX;
    int startY;
    boolean isStartSet=false;
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
                    route= findRoute(game.getCells(),x,y,randomLength);
                    if (!isStartSet){
                        startX=route.get(0)[0];
                        startY=route.get(0)[1];
                        isStartSet=true;
                    }
                    for (int i=0;i<route.size();i++){
                        int[] way=route.get(i);
                        x=way[0];
                        y=way[1];
                        if (game.pacman.getX()==x && game.pacman.getY()==y)
                            game.pacman.catchGhost();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        game.circlePanel.repaint(convertXtoPixels(),convertYtoPixels(),convertXtoPixels()+2*radius,convertYtoPixels()+2*radius);
                    }

                }
            }
        });
        thread.start();
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
    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
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

