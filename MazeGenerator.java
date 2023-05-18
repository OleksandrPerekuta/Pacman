import java.util.*;

public class MazeGenerator {
    int[][] borders;
    public MazeGenerator(int[][] borders){
        this.borders=borders;
        do {
            this.borders=createSquareArray(borders.length);
            fillCellsVertically(this.borders,5);
            fillCellsHorizontally(this.borders,5);
            replaceRandomIntsWithZeros(this.borders);
        }while (!isReachable(this.borders,this.borders.length/2,this.borders.length/2));
        this.borders=createCorridor(this.borders);
        this.borders=fillWithCherries(this.borders);
        printArray(this.borders);
    }

    public static void printArray(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
    }
    public static void fillCellsVertically(int[][] array, int numLines) {
        Random random = new Random();
        int rows = array.length;
        int cols = array[0].length;

        for (int i = 0; i < numLines; i++) {
            int row = random.nextInt(rows - 3);
            int col = random.nextInt(cols);
            try {
                for (int j=0;j<5;j++){
                    array[row+j][col] = 1;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                i--;
            }
        }
    }
    public static void fillCellsHorizontally(int[][] array, int numLines) {
        Random random = new Random();
        int rows = array.length;
        int cols = array[0].length;
        for (int i = 0; i < numLines; i++) {
            int row = random.nextInt(rows / 2) * 2 + 1;
            int col = random.nextInt(cols);
            try {
                for (int j = 0; j < 5; j++) {
                    array[row][col+j] = 1;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                i--;
            }
        }
    }
    public int[][] getBorders() {
        return borders;
    }
    private static int[][] createSquareArray(int size) {
        int[][] array = new int[size][size];
        int value = 1;
        int startingRow = 0;
        int endingRow = size - 1;
        int startingColumn = 0;
        int endingColumn = size - 1;

        while ((startingRow <= endingRow) && (startingColumn <= endingColumn)) {
            for (int i = startingColumn; i <= endingColumn; i++) {
                array[startingRow][i] = value;
            }
            startingRow++;
            for (int i = startingRow; i <= endingRow; i++) {
                array[i][endingColumn] = value;
            }
            endingColumn--;
            if (startingRow <= endingRow) {
                for (int i = endingColumn; i >= startingColumn; i--) {
                    array[endingRow][i] = value;
                }
                endingRow--;
            }
            if (startingColumn <= endingColumn) {
                for (int i = endingRow; i >= startingRow; i--) {
                    array[i][startingColumn] = value;
                }
                startingColumn++;
            }
            if (value==1)
                value=0;
            else
                value++;
        }
        return array;
    }

    public static void replaceRandomIntsWithZeros(int[][] array) {
        int numToChange = array.length * 3;
        int num = 0;
        Random random = new Random();
        while (num < numToChange) {
            int i = random.nextInt(array.length);
            int j = random.nextInt(array[i].length);
            if (array[i][j] != 0) {
                array[i][j] = 0;
                num++;
            }
        }
    }
    public int[][] createCorridor(int[][] cells){
        int SIZE=cells.length;
        for (int i=0;i<SIZE;i++){
            for (int j=0;j<SIZE;j++){
                if (i==0||i==(SIZE-1)||j==0||j==(SIZE-1))
                    cells[i][j]=1;
            }
        }
        cells[SIZE/2][SIZE/2]=0;
        cells[SIZE/2-1][SIZE/2]=0;
        cells[SIZE/2][SIZE/2-1]=0;
        cells[SIZE/2-1][SIZE/2-1]=0;

        cells[SIZE/2][0]=0;
        cells[SIZE/2-1][0]=0;
        cells[SIZE/2][SIZE-1]=0;
        cells[SIZE/2-1][SIZE-1]=0;
        return cells;
    }
    public int[][] fillWithCherries(int[][] cells){
        for (int i=0;i<cells.length;i++){
            for (int j=0;j<cells.length;j++){
                if (cells[i][j]==0){
                    int random1=new Random().nextInt(40);
                    int random2=new Random().nextInt(40);
                    if (random1==random2){
                        cells[i][j]=3;
                    }else{
                        cells[i][j]=2;
                    }
                }
            }
        }
        return cells;
    }
        public static boolean isReachable(int[][] array, int startRow, int startCol) {
        boolean[][] visited = new boolean[array.length][array[0].length];
        goOneDeeperAndCheckIfPossible(array, visited, startRow, startCol);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] == 0 && !visited[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    private static void goOneDeeperAndCheckIfPossible(int[][] array, boolean[][] visited, int row, int col) {
        if (row < 0 || row >= array.length || col < 0 || col >= array[0].length
                || visited[row][col] || array[row][col] == 1) {
            return;
        }
        visited[row][col] = true;
        int[][] neighbors = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] neighbor : neighbors) {
            int newRow = row + neighbor[0];
            int newCol = col + neighbor[1];
            goOneDeeperAndCheckIfPossible(array, visited, newRow, newCol);
        }
    }
}
