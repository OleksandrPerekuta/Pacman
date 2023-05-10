import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class Ghost{
    Game game;
    int[][] cells;
    int cellsWidth;
    int cellsHeight;
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
                    route= findRoute(game.getCells(),x,y,randomLength);
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

    public void setCells(int[][] cells) {
        this.cells = cells;
    }

//    private void moveIfPossible(){
//        cellsWidth = cells.length;      // number of rows
//        cellsHeight = cells[0].length;  // number of columns in the first row
//        int nextX = x + dx;
//        int nextY = y + dy;
//
//        // Check if Pacman is at the edge of the map and the next move would cause an index out of bounds exception
//        boolean isAtLeftEdge = (x == 0 && dx == -1);
//        boolean isAtRightEdge = (x == cellsWidth - 1 && dx == 1);
//        boolean isAtTopEdge = (y == 0 && dy == -1);
//        boolean isAtBottomEdge = (y == cellsHeight - 1 && dy == 1);
//
//        if (isAtLeftEdge || isAtRightEdge || isAtTopEdge || isAtBottomEdge) {
//            // If at the edge, wrap Pacman's position to the opposite side of the map
//            if (isAtLeftEdge) {
//                x = cellsWidth - 1;
//            } else if (isAtRightEdge) {
//                x = 0;
//            } else if (isAtTopEdge) {
//                y = cellsHeight - 1;
//            } else if (isAtBottomEdge) {
//                y = 0;
//            }
//        } else {
//            // If not at the edge, check if the next move is valid and update Pacman's position
//            try {
//                if (game.getCellValue(nextY, nextX)){
//                    x += dx;
//                    y += dy;
//                }
//            } catch (ArrayIndexOutOfBoundsException e) {
//                // Do nothing, as we have already handled the edge case above
//            }
//        }
//    }
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
        } while (!(isMovable==0));
        currentX += dx;
        currentY += dy;
        route.add(new int[]{currentX, currentY});
    }
    return route;
}
}

