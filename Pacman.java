import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

class Pacman {
    private boolean isInverted=false;
    private int [] speed={10,20,40,160,240,360};
    private File[][] images={
            {new File("pacmanImages/1left.png"),new File("pacmanImages/2left.png"),new File("pacmanImages/3left.png"),new File("pacmanImages/4left.png"),new File("pacmanImages/5left.png"),new File("pacmanImages/6left.png")},
            {new File("pacmanImages/1up.png"),new File("pacmanImages/2up.png"),new File("pacmanImages/3up.png"),new File("pacmanImages/4up.png"),new File("pacmanImages/5up.png"),new File("pacmanImages/6up.png"),},
            {new File("pacmanImages/1right.png"),new File("pacmanImages/2right.png"),new File("pacmanImages/3right.png"),new File("pacmanImages/4right.png"),new File("pacmanImages/5right.png"),new File("pacmanImages/6right.png"),},
            {new File("pacmanImages/1down.png"),new File("pacmanImages/2down.png"),new File("pacmanImages/3down.png"),new File("pacmanImages/4down.png"),new File("pacmanImages/5down.png"),new File("pacmanImages/6down.png"),}};
    /**
     * left
     * up
     * right
     * down
     */
    private int speedIndex=2;
    private int positionImage=0;
    private int lives=3;
    private boolean isStarted=false;
    private int scoreCounter=-10;
    private int[][] cells;
    private int cellsWidth;
    private int cellsHeight;
    private Game game;
    private ArrayList<Ghost> ghosts=new ArrayList<>();
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int radius;
    private int position=0;
    private boolean isMouthOpened=false;
    private int startX;
    private int startY;
    public Pacman(Game game){this.game=game;}

    public void setCells(int[][] cells) {
        this.cells = cells;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartX() {
        return startX;
    }

    public void setGhosts(ArrayList<Ghost> ghosts) {
        this.ghosts = ghosts;
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

    public void setSpeedIndex(int speedIndex) {
        this.speedIndex = speedIndex;
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
                    game.circlePanel.repaint(convertXtoPixels(),convertYtoPixels(),radius,radius);
                    moveIfPossible();
                    try {
                        for (int j=0;j<2;j++){
                            for (int i=0;i<images[0].length-1;i++){
                                positionImage=i;
                                game.circlePanel.repaint(convertXtoPixels(),convertYtoPixels(),radius,radius);
                                Thread.sleep(speed[speedIndex]/2);
                            }
                            for (int i=images[0].length-1;i>=0;i--){
                                positionImage=i;
                                game.circlePanel.repaint(convertXtoPixels(),convertYtoPixels(),radius,radius);
                                Thread.sleep(speed[speedIndex]/2);
                            }
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
    public synchronized void moveIfPossible(){
        cellsWidth = cells.length;
        cellsHeight = cells[0].length;
        int nextX = x + dx;
        int nextY = y + dy;

        /**
         * IF AT THE EDGE - MOVE TO THE OTHER SIDE OF THE BOARD
         */
        boolean isAtLeftEdge = (x == 0 && dx == -1);
        boolean isAtRightEdge = (x == cellsWidth - 1 && dx == 1);
        boolean isAtTopEdge = (y == 0 && dy == -1);
        boolean isAtBottomEdge = (y == cellsHeight - 1 && dy == 1);

        if (isAtLeftEdge || isAtRightEdge || isAtTopEdge || isAtBottomEdge) {
            //IF ONE OF THEM THEN MOVE
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
            //if not at the edge then move if it is not a block
            try {
                if (game.getCellValue(nextY, nextX)!=1){//if not block then check if there is a ghost
                    boolean isGhost=false;
                    for (Ghost ghost: ghosts){
                        if (ghost.getX() == x + dx && ghost.getY() == y + dy) {
                            isGhost = true;
                            catchGhost();
                        }
                    }
                    if (!isGhost){
                        x += dx;
                        y += dy;
                        if (cells[y][x]==2){// small cherry
                            cells[y][x]=0;
                            scoreCounter+=10;
                            game.scoreLabel.setText(String.valueOf(scoreCounter));
                            System.out.println(scoreCounter);
                            game.borderTable.repaint(convertXtoPixels(),convertYtoPixels(),radius,radius);// HERE
                        }
                        if (cells[y][x]==3){ //big cherry
                            cells[y][x]=0;
                            int random=new Random().nextInt(10);
                            if (random<6){
                                scoreCounter-=100;
                            }
                            else{
                                scoreCounter+=100;
                            }
                            game.causeRandomEffect();
                            game.scoreLabel.setText(String.valueOf(scoreCounter));
                            System.out.println("Score "+scoreCounter);
                            game.borderTable.repaint(convertXtoPixels(),convertYtoPixels(),radius,radius);
                        }
                    }
                    }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
    }
    public File getPacmanImage(){
        return images[position][positionImage];
    }
    private Rectangle getCoordinatesOfTheCell(int row, int column){
        return game.borderTable.getCellRect(row, column, true);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLives() {
        return lives;
    }
    public void catchGhost(){
        lives--;
        game.putPacmanInStart();
        game.downMenu.repaint();

    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScoreCounter() {
        return scoreCounter;
    }
    public void teleportRandomPoint(){
        boolean isPossible=false;
        int randomX;
        int randomY;
        do{
            randomX=new Random().nextInt(game.SIZE);
            randomY=new Random().nextInt(game.SIZE);
            if (cells[randomY][randomX]!=1)
                isPossible=true;
        }while (!isPossible);
        x=randomX;
        y=randomY;
    }

    public void changeInverted() {
        isInverted = !isInverted;
    }
    public boolean getIsInverted(){
        return isInverted;
    }
}