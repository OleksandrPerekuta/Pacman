import java.awt.*;
import java.io.File;

class Pacman {
    private boolean isStarted=false;
    int scoreCounter=0;
    int[][] cells;
    int cellsWidth;
    int cellsHeight;
    Game game;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int radius;
    private int position=0;
    private boolean isMouthOpened=false;
    public Pacman(Game game){this.game=game;}

    public void setCells(int[][] cells) {
        this.cells = cells;
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
        cellsWidth = cells.length;      // number of rows
        cellsHeight = cells[0].length;  // number of columns in the first row
        int nextX = x + dx;
        int nextY = y + dy;

        // Check if Pacman is at the edge of the map and the next move would cause an index out of bounds exception
        boolean isAtLeftEdge = (x == 0 && dx == -1);
        boolean isAtRightEdge = (x == cellsWidth - 1 && dx == 1);
        boolean isAtTopEdge = (y == 0 && dy == -1);
        boolean isAtBottomEdge = (y == cellsHeight - 1 && dy == 1);

        if (isAtLeftEdge || isAtRightEdge || isAtTopEdge || isAtBottomEdge) {
            // If at the edge, wrap Pacman's position to the opposite side of the map
            if (isAtLeftEdge) {
                x = cellsWidth - 1;
            } else if (isAtRightEdge) {
                x = 0;
            } else if (isAtTopEdge) {
                y = cellsHeight - 1;
            } else if (isAtBottomEdge) {
                y = 0;
            }
        } else {
            // If not at the edge, check if the next move is valid and update Pacman's position
            try {
                if (game.getCellValue(nextY, nextX)!=1){
                    x += dx;
                    y += dy;
                    if (cells[y][x]==2){
                        cells[y][x]=0;
                        scoreCounter+=10;
                        System.out.println(scoreCounter);
                        game.borderTable.repaint();
                    }
                    if (cells[y][x]==3){
                        cells[y][x]=0;
                        scoreCounter+=100;
                        System.out.println("Score "+scoreCounter);
                        game.borderTable.repaint();
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // Do nothing, as we have already handled the edge case above
            }
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