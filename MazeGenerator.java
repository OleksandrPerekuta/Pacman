import java.util.*;

public class MazeGenerator {
    boolean[][] figureOne=new boolean[6][3];
    boolean[][] figureTwo=new boolean[4][7];
    boolean[][] figureThree=new boolean[5][10];
    boolean[][] figureFour=new boolean[8][9];
    boolean[][] figureFive=new boolean[4][9];
    boolean[][] figureSix=new boolean[2][5];
    boolean[][] figureSeven=new boolean[2][3];
    boolean[][] figureEight=new boolean[4][7];
    boolean[][] figureNine=new boolean[3][5];
    boolean[][] figureTen=new boolean[5][6];
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




    public static boolean[][] mirrorHorizontally(boolean[][] arr) {
        int rows = arr.length;
        int cols = arr[0].length;
        boolean[][] mirroredArr = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mirroredArr[i][j] = arr[i][cols - 1 - j];
            }
        }
        return mirroredArr;
    }
    public static boolean[][] mirrorVertically(boolean[][] arr) {
        int rows = arr.length;
        int cols = arr[0].length;
        boolean[][] mirroredArr = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mirroredArr[i][j] = arr[rows - 1 - i][j];
            }
        }
        return mirroredArr;
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
            // Randomly choose a cell in the array
            int row = random.nextInt(rows - 3);
            int col = random.nextInt(cols);

            // Fill the three cells below the chosen cell with 1
            try {
                for (int j=0;j<5;j++){
                    array[row+j][col] = 1;

                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // If the chosen cell is at the bottom of the array, catch the exception and try again
                i--;
            }
        }
    }
    public static void fillCellsHorizontally(int[][] array, int numLines) {
        Random random = new Random();
        int rows = array.length;
        int cols = array[0].length;

        for (int i = 0; i < numLines; i++) {
            // Randomly choose a cell in the array with an odd row index
            int row = random.nextInt(rows / 2) * 2 + 1;
            int col = random.nextInt(cols);

            // Fill the five cells to the right of the chosen cell with 1
            try {
                for (int j = 0; j < 5; j++) {
                    array[row][col+j] = 1;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // If the chosen cell is at the right edge of the array, catch the exception and try again
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
        int rowStart = 0, rowEnd = size - 1;
        int colStart = 0, colEnd = size - 1;

        // fill the border with 1
        while (rowStart <= rowEnd && colStart <= colEnd) {
            // fill top row
            for (int i = colStart; i <= colEnd; i++) {
                array[rowStart][i] = value;
            }
            rowStart++;

            // fill right column
            for (int i = rowStart; i <= rowEnd; i++) {
                array[i][colEnd] = value;
            }
            colEnd--;

            // fill bottom row
            if (rowStart <= rowEnd) {
                for (int i = colEnd; i >= colStart; i--) {
                    array[rowEnd][i] = value;
                }
                rowEnd--;
            }

            // fill left column
            if (colStart <= colEnd) {
                for (int i = rowEnd; i >= rowStart; i--) {
                    array[i][colStart] = value;
                }
                colStart++;
            }
            if (value==1)
                value=0;
            else
                value++;
        }

        return array;
    }

    public static int[][] replaceRandomIntsWithZeros(int[][] array) {
        int numIntsToReplace = array.length * 3;
        int numReplaced = 0;
        Random random = new Random();

        while (numReplaced < numIntsToReplace) {
            int i = random.nextInt(array.length);
            int j = random.nextInt(array[i].length);
            if (array[i][j] != 0) {
                array[i][j] = 0;
                numReplaced++;
            }
        }
        return array;
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
                if (cells[i][j]==0)
                    cells[i][j]=2;
            }
        }
        return cells;
    }
        public static boolean isReachable(int[][] matrix, int startRow, int startCol) {
        // Set up a boolean matrix to keep track of visited cells
        boolean[][] visited = new boolean[matrix.length][matrix[0].length];

        // Perform a depth-first search starting from the starting point
        dfs(matrix, visited, startRow, startCol);

        // Check if every 0 in the matrix has been visited
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0 && !visited[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    private static void dfs(int[][] matrix, boolean[][] visited, int row, int col) {
        if (row < 0 || row >= matrix.length || col < 0 || col >= matrix[0].length
                || visited[row][col] || matrix[row][col] == 1) {
            return;
        }

        visited[row][col] = true;

        // Check the four neighboring cells (up, down, left, right)
        int[][] neighbors = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] neighbor : neighbors) {
            int newRow = row + neighbor[0];
            int newCol = col + neighbor[1];
            dfs(matrix, visited, newRow, newCol);
        }
    }





}
