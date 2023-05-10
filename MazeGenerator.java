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
    boolean[][] borders;
    int length;

    public MazeGenerator(boolean[][] borders){
        this.borders=borders;
        int length;
        if (this.borders.length%2==0)
            length= this.borders.length/2-1;
        else
            length= this.borders.length/2;

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

    public static void printArray(boolean[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j] ? "T " : "F ");
            }
            System.out.println();
        }
    }

    public boolean[][] getBorders() {
        return borders;
    }
}
